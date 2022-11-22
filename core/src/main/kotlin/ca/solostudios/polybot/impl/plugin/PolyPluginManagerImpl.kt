/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyPluginManagerImpl.kt is part of PolyBot
 * Last modified on 22-11-2022 03:06 p.m.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * POLYBOT IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ca.solostudios.polybot.impl.plugin

import ca.solostudios.polybot.api.PolyBot
import ca.solostudios.polybot.api.plugin.PolyPlugin
import ca.solostudios.polybot.api.plugin.PolyPluginContainer
import ca.solostudios.polybot.api.plugin.PolyPluginManager
import ca.solostudios.polybot.api.plugin.PolyPluginManager.State
import ca.solostudios.polybot.api.plugin.dsl.PolyPluginDsl
import ca.solostudios.polybot.api.plugin.finder.PluginCandidateFinder
import ca.solostudios.polybot.api.plugin.info.PluginInfo
import ca.solostudios.polybot.api.util.StringPair
import ca.solostudios.polybot.api.util.error
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.Path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.slf4j.kotlin.*
import kotlin.io.path.extension
import kotlin.io.path.inputStream
import kotlin.io.path.notExists
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor

public class PolyPluginManagerImpl(
        override val polybot: PolyBot,
        override val candidateFinders: List<PluginCandidateFinder>
                                  ) : PolyPluginManager, DIAware {
    private val logger by getLogger()
    
    override val di: DI = polybot.di
    
    override val plugins: List<PolyPluginContainer<*>>
        get() = pluginMap.entries.map { it.value }
    
    override var state: State = State.NEW
        private set
    
    public val pluginMap: MutableMap<StringPair, PolyPluginContainer<*>> = mutableMapOf()
    
    override suspend fun startPlugins(polyPluginDsl: PolyPluginDsl) {
        try {
            ensureState(State.LOADED_PLUGINS, "State must be set to ${State.LOADED_PLUGINS} to start plugins. Currently: $state.")
            updateState(State.STARTING)
            for (plugin in plugins) {
                for (entrypoint in plugin.entrypoints) {
                    logger.debug { "Starting entrypoint ${entrypoint::class} for plugin ${plugin.info.id}" }
                    with(entrypoint) {
                        polyPluginDsl.init()
                    }
                }
            }
            updateState(State.RUNNING)
        } catch (e: Exception) {
            updateState(State.FAILED)
            error(cause = e, "Could not start plugins due to exception while starting.")
        }
    }
    
    override suspend fun loadPlugins() {
        try {
            ensureState(State.NEW, "State must be set to ${State.NEW} to load plugins. Currently: $state")
            updateState(State.LOADING_PLUGINS)
            
            coroutineScope {
                val candidates = resolveCandidates()
                
                for (candidate in candidates) {
                    for (path in candidate.paths) {
                        logger.debug { "Adding path $path to classloader." }
                        polybot.classLoader.addURL(path.toUri().toURL())
                    }
                }
                
                candidates.map { candidate ->
                    async {
                        val entrypoints = mutableListOf<PolyPlugin>()
                        for (entrypoint in candidate.info.entrypoints) {
                            logger.debug { "Loading entrypoint class $entrypoint for plugin ${candidate.info.group}:${candidate.info.id}:${candidate.info.version}" }
                            try {
                                val kClass = polybot.classLoader.loadClass(entrypoint).kotlin
                                
                                if (!kClass.isSubclassOf(PolyPlugin::class)) {
                                    logger.error { "Error loading plugin entrypoint; class $kClass is not an instance of ${PolyPlugin::class}" }
                                    error("Class $kClass is not an instance of ${PolyPlugin::class}")
                                }
                                
                                @Suppress("UNCHECKED_CAST") /* We already checked if kClass is a subtype of PolyPlugin. */
                                val pluginInstance = instantiatePlugin(kClass as KClass<PolyPlugin>)
                                
                                entrypoints.add(pluginInstance)
                            } catch (e: Exception) {
                                logger.error(e) { "Error loading entrypoint class $entrypoint for plugin ${candidate.info.group}:${candidate.info.id}:${candidate.info.version}" }
                                error(cause = e, "Could not properly load plugin entrypoint classes due to class loading errors...")
                            }
                        }
                        val plugin = PolyPluginContainerImpl(entrypoints, candidate.info, candidate.paths, candidate.filesystem)
                        pluginMap[candidate.info.group, candidate.info.id] = plugin
                    }
                }.awaitAll()
            }
            
            updateState(State.LOADING_PLUGINS)
        } catch (e: Exception) {
            updateState(State.FAILED)
            error(cause = e, "Could not load plugins due to exception while loading.")
        }
    }
    
    override suspend fun shutdownPlugins() {
        TODO("Not yet implemented")
    }
    
    private fun instantiatePlugin(kClass: KClass<PolyPlugin>): PolyPlugin {
        if (kClass.visibility != null && kClass.visibility != KVisibility.PUBLIC)
            error("Error while loading plugin $kClass, the visibility is not public.")
        
        if (kClass.objectInstance != null) { // Is object
            return kClass.objectInstance!!
        }
        
        val constructor = kClass.primaryConstructor ?: error("Could not locate primary constructor in plugin class $kClass.")
        
        return when {
            constructor.parameters.isNotEmpty()          -> {
                error("Could not instantiate $kClass, as the primary constructor takes parameters ${constructor.parameters}, when it should take none.")
            }
            
            constructor.visibility != KVisibility.PUBLIC -> {
                error("Could not instantiate $kClass, as the primary constructor is not public.")
            }
            
            else                                         -> {
                kClass.createInstance()
            }
        }
    }
    
    private suspend fun resolveCandidates(): List<PolyPluginCandidate> {
        return coroutineScope {
            val candidatePaths = candidateFinders.flatMap { it.findCandidates() }
            
            logger.debug { "Found the following candidate paths: $candidatePaths" }
            
            val pluginCandidates = candidatePaths.map { path ->
                async {
                    if (path.extension == "jar")
                        resolveJarCandidate(path)
                    else
                        resolveFolderCandidate(path) // TODO: 2022-08-16 Resolve Jar Candidate
                }
            }.awaitAll().filterNotNull()
            
            logger.debug { "Found the following plugin candidates: $pluginCandidates" }
            
            return@coroutineScope pluginCandidates
        }
    }
    
    private suspend fun resolveFolderCandidate(candidatePath: Path): PolyPluginCandidate? {
        val folderFs = withContext(Dispatchers.IO) { FileSystems.newFileSystem(candidatePath, null) }
        val pluginInfo = loadPluginJson(folderFs) ?: return null
        
        return PolyPluginCandidate(pluginInfo, listOf(candidatePath), folderFs)
    }
    
    private suspend fun resolveJarCandidate(candidatePath: Path): PolyPluginCandidate? {
        val jarFs = withContext(Dispatchers.IO) { FileSystems.newFileSystem(candidatePath, null) }
        val pluginInfo = loadPluginJson(jarFs) ?: return null
        
        return PolyPluginCandidate(pluginInfo, listOf(candidatePath), jarFs)
    }
    
    @OptIn(ExperimentalSerializationApi::class)
    private suspend fun loadPluginJson(fs: FileSystem): PluginInfo? {
        return withContext(Dispatchers.IO) {
            val pluginJsonPath = fs.getPath("polybot.plugin.json")
            if (pluginJsonPath.notExists())
                return@withContext null
    
            Json.decodeFromStream(pluginJsonPath.inputStream())
        }
    }
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : PolyPlugin> getPluginContainer(group: String, id: String): PolyPluginContainer<T> {
        return pluginMap[group, id] as PolyPluginContainer<T>
    }
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : PolyPlugin> getPlugin(kClass: KClass<T>, group: String, id: String): T {
        val pluginContainer = pluginMap[group, id] ?: error("Could not find plugin container for plugin ${group}:${id}}")
        val pluginEntrypoint = pluginContainer.entrypoints.find {
            kClass.isInstance(it)
        }
            ?: error("Could not find entrypoint to plugin ${group}:${id} that is an instance of $kClass")
    
        return pluginEntrypoint as T
    }
    
    private operator fun <T, U, V> Map<Pair<T, U>, V>.get(key1: T, key2: U): V? {
        return this[key1 to key2]
    }
    
    private operator fun <T, U, V> MutableMap<Pair<T, U>, V>.set(key1: T, key2: U, value: V) {
        this[key1 to key2] = value
    }
    
    private fun ensureState(state: State, message: String) {
        if (this.state != state)
            error(message)
    }
    
    private fun updateState(newState: State) {
        this.state = newState
    }
}