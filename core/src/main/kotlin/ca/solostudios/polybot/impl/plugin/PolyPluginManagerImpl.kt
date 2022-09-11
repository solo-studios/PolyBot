/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyPluginManagerImpl.kt is part of PolyBot
 * Last modified on 10-09-2022 04:57 p.m.
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
import ca.solostudios.polybot.api.plugin.dsl.PolyPluginDsl
import ca.solostudios.polybot.api.plugin.finder.PluginCandidateFinder
import ca.solostudios.polybot.api.plugin.info.PluginInfo
import ca.solostudios.polybot.api.plugin.loader.PolyClassLoader
import ca.solostudios.polybot.common.service.Service
import ca.solostudios.polybot.common.service.ServiceManager
import java.nio.file.FileSystems
import java.nio.file.Path
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.isSubclassOf
import kotlin.time.Duration

public class PolyPluginManagerImpl(
        override val polybot: PolyBot,
        override val candidateFinders: List<PluginCandidateFinder>
                                  ) : PolyPluginManager, DIAware {
    private val logger by getLogger()
    
    override val di: DI = polybot.di
    
    override val plugins: MutableList<PolyPluginContainer<*>> = mutableListOf()
    override val pluginMap: MutableMap<String, PolyPluginContainer<*>> = mutableMapOf()
    
    override suspend fun initPlugins() {
        val candidates = resolveCandidates()
        
        val classLoader = polybot.classLoader as PolyClassLoader
        
        for (candidate in candidates) {
            for (path in candidate.paths) {
                classLoader.addURL(path.toUri().toURL())
            }
        }
        
        for (candidate in candidates) {
            val entrypoints = mutableListOf<PolyPlugin>()
            for (entrypoint in candidate.info.entrypoints) {
                logger.debug { "Loading entrypoint class $entrypoint for plugin ${candidate.info.id}" }
                try {
                    val kClass = classLoader.loadClass(entrypoint).kotlin
                    
                    if (!kClass.isSubclassOf(PolyPlugin::class)) {
                        logger.error { "Error loading plugin entrypoint; class $kClass is not an instance of ${PolyPlugin::class}" }
                        error("Class $kClass is not an instance of ${PolyPlugin::class}")
                    }
                    entrypoints.add(kClass.createInstance() as PolyPlugin)
                } catch (e: Exception) {
                    logger.error(e) { "Error loading entrypoint class $entrypoint for plugin ${candidate.info.id}" }
                    error("Could not properly load plugin entrypoint classes due to class loading errors...")
                }
            }
            val plugin = PolyPluginContainerImpl(entrypoints, candidate.info, candidate.paths, candidate.filesystem)
            plugins.add(plugin)
            pluginMap[candidate.info.id] = plugin
        }
    }
    
    @Suppress("UNREACHABLE_CODE")
    override suspend fun start() {
        for (entrypoint in plugins.flatMap { it.entrypoints }) {
            val polyPluginDsl: PolyPluginDsl = TODO("Initialize PolyPluginDsl")
            with(entrypoint) {
                polyPluginDsl.init()
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
                        resolveFileCandidate(path) // TODO: 2022-08-16 Resolve Jar Candidate
                }
            }.awaitAll().filterNotNull()
            
            logger.debug { "Found the following plugin candidates: $pluginCandidates" }
            
            return@coroutineScope pluginCandidates
        }
    }
    
    @OptIn(ExperimentalSerializationApi::class)
    private fun resolveFileCandidate(candidatePath: Path): PolyPluginCandidate? {
        val pluginJsonPath = candidatePath.resolve("polybot.plugin.json")
        
        if (pluginJsonPath.notExists())
            return null
        
        val pluginInfo = Json.decodeFromStream<PluginInfo>(pluginJsonPath.inputStream())
        
        return PolyPluginCandidate(pluginInfo, listOf(candidatePath), FileSystems.newFileSystem(candidatePath, null))
    }
    
    @OptIn(ExperimentalSerializationApi::class)
    private fun resolveJarCandidate(candidatePath: Path): PolyPluginCandidate? {
        val jarFs = FileSystems.newFileSystem(candidatePath, null)
        
        val pluginJsonPath = jarFs.getPath("polybot.plugin.json")
        if (pluginJsonPath.notExists())
            return null
        
        val pluginInfo = Json.decodeFromStream<PluginInfo>(pluginJsonPath.inputStream())
        
        return PolyPluginCandidate(pluginInfo, listOf(candidatePath), jarFs)
    }
    
    override val services: List<PolyPlugin>
        get() = plugins.flatMap { it.entrypoints }
    override val startupTimes: List<Pair<PolyPlugin, Duration>>
        get() = TODO("Not yet implemented")
    override val serviceHealth: Map<KClass<PolyPlugin>, ServiceManager.ServiceHealth> = mutableMapOf()
    override var state: Service.State = Service.State.INITIALIZING
        private set
    override val shutdown: Boolean
        get() = state == Service.State.SHUTDOWN || state == Service.State.FAILED
    override val running: Boolean
        get() = state == Service.State.RUNNING
    override val active: Boolean
        get() = state.active
    
    override val healthy: Boolean
        get() = serviceHealth.values.all { it.healthy }
    
    override fun <T : PolyPlugin> get(group: String, id: String): PolyPluginContainer<T> {
        TODO("Not yet implemented")
    }
    
    @Deprecated("Plugins cannot be added to the manager", level = DeprecationLevel.HIDDEN)
    override fun <T : PolyPlugin> addService(service: T, clazz: KClass<T>) {
        throw UnsupportedOperationException("Plugins cannot be added at runtime.")
    }
    
    override suspend fun shutdown() {
        TODO("Not yet implemented")
    }
    
    override fun <T : PolyPlugin> getService(clazz: KClass<T>): T {
        TODO("Not yet implemented")
    }
    
    override fun addException(serviceClass: KClass<PolyPlugin>, exception: Exception) {
        TODO("Not yet implemented")
    }
}