/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyPluginExtensions.kt is part of PolyBot
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

package ca.solostudios.polybot.api.util.ext


import ca.solostudios.polybot.api.plugin.PolyPlugin
import ca.solostudios.polybot.api.plugin.PolyPluginContainer
import ca.solostudios.polybot.api.plugin.PolyPluginManager


/**
 * Returns a plugin from the manager.
 *
 * @param T The type of the plugin to return
 * @param group The group of the plugin
 * @param id The id of the plugin
 *
 * @return The plugin
 * @throws NullPointerException if no plugin of the specified type can be found
 */
public inline operator fun <reified T : PolyPlugin> PolyPluginManager.get(group: String, id: String): T = getPlugin(T::class, group, id)

/**
 * Returns a plugin from the manager.
 *
 * @param T The type of the plugin to return
 * @param group The group of the plugin
 * @param id The id of the plugin
 *
 * @return The plugin
 * @throws NullPointerException if no plugin of the specified type can be found
 */
public inline fun <reified T : PolyPlugin> PolyPluginManager.plugin(group: String, id: String): T = getPlugin(T::class, group, id)

/**
 * Returns a plugin container from the manager.
 *
 * @param T The type of the plugin to return
 * @param group The group of the plugin
 * @param id The id of the plugin
 *
 * @return The plugin
 * @throws NullPointerException if no plugin of the specified type can be found
 */
public inline fun <reified T : PolyPlugin> PolyPluginManager.pluginContainer(group: String, id: String): PolyPluginContainer<T> {
    return getPluginContainer(group, id)
}