/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2023-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyEventManagerImpl.kt is part of PolyBot
 * Last modified on 10-03-2023 03:26 p.m.
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

package ca.solostudios.polybot.impl.event

import ca.solostudios.polybot.api.PolyBot
import ca.solostudios.polybot.api.event.PolyEvent
import ca.solostudios.polybot.api.event.PolyEventListener
import ca.solostudios.polybot.api.event.PolyEventManager
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

internal class PolyEventManagerImpl(override val bot: PolyBot) : PolyEventManager {
    override val listeners: MutableMap<KClass<*>, MutableList<PolyEventListener<*>>> = mutableMapOf()
    
    override fun <T : PolyEvent> register(listener: PolyEventListener<T>, clazz: KClass<out T>) {
        listeners.getOrPut(clazz) { mutableListOf() }.add(listener)
    }
    
    override fun <T : PolyEvent> unregister(listener: PolyEventListener<T>, clazz: KClass<out T>) {
        listeners[clazz]?.remove(listener)
    }
    
    override suspend fun <T : PolyEvent> dispatch(event: T, clazz: KClass<out T>): Unit = coroutineScope {
        listeners[clazz]?.forEach {
            @Suppress("UNCHECKED_CAST")
            val listener = (it as? PolyEventListener<T>)
            
            if (listener != null) {
                launch {
                    listener(event)
                }
            }
        }
    }
}