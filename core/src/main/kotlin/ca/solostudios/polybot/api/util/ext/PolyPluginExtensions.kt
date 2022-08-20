/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyPluginExtensions.kt is part of PolyBot
 * Last modified on 02-08-2022 04:46 p.m.
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
import ca.solostudios.polybot.api.plugin.PolyPluginCompanionObject
import ca.solostudios.polybot.api.plugin.PolyPluginManager
import kotlin.reflect.KClass
import kotlin.reflect.KProperty


/**
 * Returns a Plugin from the manager.
 *
 * @param T The type of the Plugin to return
 * @param clazz The class of the Plugin
 * @return The Plugin
 * @throws NullPointerException if no Plugin of the specified type can be found
 */
public operator fun <T : PolyPlugin> PolyPluginManager.get(clazz: KClass<T>): T = getService(clazz)

/**
 * Returns a Plugin from the manager.
 *
 * @param T The type of the Plugin to return
 * @param clazz The class of the Plugin
 * @return The Plugin
 * @throws NullPointerException if no Plugin of the specified type can be found
 */
public operator fun <T : PolyPlugin> PolyPluginManager.get(clazz: PolyPluginCompanionObject<T>): T = getService(clazz.serviceClass)

/**
 * Returns a Plugin from the manager.
 *
 * @param T The type of the Plugin to return
 * @return The Plugin
 * @throws NullPointerException if no Plugin of the specified type can be found
 */
public inline fun <reified T : PolyPlugin> PolyPluginManager.plugin(): T = getService(T::class)

/**
 * Property delegate for a Plugin, from the Plugin manager.
 *
 * @param T The type of the Plugin to return
 * @return The Plugin
 * @throws NullPointerException if no Plugin of the specified type can be found
 */
public inline operator fun <reified T : PolyPlugin> PolyPluginManager.getValue(thisRef: Any?, property: KProperty<*>): T {
    return getService(T::class)
}