/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file EventManager.kt is part of PolyhedralBot
 * Last modified on 24-07-2021 08:33 p.m.
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
 * POLYHEDRALBOT IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.solostudios.polybot.event

import com.solostudios.polybot.PolyBot
import kotlin.reflect.KClass

open class Event

interface EventListener<T : Event> {
    operator fun invoke(event: T)
}

/**
 * Event manager
 *
 * Negative priority = higher
 *
 * @property bot
 * @constructor Create empty Event manager
 */
class EventManager(val bot: PolyBot) {
    val listeners: MutableList<InternalEventListener<out Event>> = mutableListOf()
    
    inline fun <reified T : Event> register(listener: EventListener<T>) {
        listeners.add(InternalEventListener(listener, T::class))
    }
    
    inline fun <reified T : Event> register(crossinline listener: (T) -> Unit) {
        listeners.add(InternalEventListener(object : EventListener<T> {
            override fun invoke(event: T) {
                listener(event)
            }
        }, T::class))
    }
    
    fun <T : Event> dispatch(event: T) {
        listeners.forEach {
            if (it.clazz.java.isAssignableFrom(event::class.java)) {
                @Suppress("UNCHECKED_CAST")
                val listener = it.listener as EventListener<T>
                listener(event)
            }
        }
    }
    
    data class InternalEventListener<T : Event>(val listener: EventListener<T>, val clazz: KClass<T>)
}