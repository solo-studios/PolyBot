/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyPluginManager.kt is part of PolyBot
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

package ca.solostudios.polybot.api.plugin

import ca.solostudios.polybot.api.PolyObject
import ca.solostudios.polybot.api.plugin.dsl.PolyPluginDsl
import ca.solostudios.polybot.api.plugin.finder.PluginCandidateFinder
import kotlin.reflect.KClass

public interface PolyPluginManager : PolyObject {
    public val plugins: List<PolyPluginContainer<*>>
    
    public val candidateFinders: List<PluginCandidateFinder>
    
    /**
     * The state the plugin manager is currently in.
     *
     * @see State
     */
    public val state: State
    
    public suspend fun loadPlugins()
    
    public suspend fun startPlugins(polyPluginDsl: PolyPluginDsl)
    
    public suspend fun shutdownPlugins()
    
    /**
     * Returns the container for a specified group and id.
     *
     * @param group The group of the container to return
     * @param id The id of the container to return
     */
    public fun <T : PolyPlugin> getPluginContainer(group: String, id: String): PolyPluginContainer<T>
    
    public fun <T : PolyPlugin> getPlugin(kClass: KClass<T>, group: String, id: String): T
    
    public enum class State {
        /**
         * The service is setting up supporting *internal* systems like thread pools, etc.
         *
         * It is **not** starting up the service.
         */
        NEW,
    
        /**
         * The plugin manager is loading plugins.
         */
        LOADING_PLUGINS,
    
        /**
         * The plugin manager has finished loading plugins.
         */
        LOADED_PLUGINS,
    
        /**
         * The plugin manager is starting all the plugins.
         */
        STARTING,
    
        /**
         * The plugin manager has started all plugins successfully and is currently running.
         */
        RUNNING,
        
        /**
         * The plugin manager is shutting down.
         */
        SHUTTING_DOWN,
        
        /**
         * The plugin manager has shutdown successfully.
         */
        SHUTDOWN,
        
        /**
         * The plugin manager failed to start successfully due to errors in a provided plugin.
         */
        FAILED
    }
}