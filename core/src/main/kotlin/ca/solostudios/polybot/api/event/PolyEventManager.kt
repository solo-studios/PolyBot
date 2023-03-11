/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyEventManager.kt is part of PolyBot
 * Last modified on 10-03-2023 03:23 p.m.
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

package ca.solostudios.polybot.api.event

import ca.solostudios.polybot.api.PolyObject
import kotlin.reflect.KClass

/**
 * An event manager
 *
 * Event managers are used to register listeners to events and dispatch events to the listeners
 */
public interface PolyEventManager : PolyObject {
    /**
     * A list of all currently registered listeners
     */
    public val listeners: Map<KClass<*>, List<PolyEventListener<*>>>
    
    /**
     * Register a listener for a specific event
     *
     * @param T The type event to listen for
     * @param listener The listener to be invoked when the event is dispatched
     * @param clazz The event to listen for
     * @see dispatch
     */
    public fun <T : PolyEvent> register(listener: PolyEventListener<T>, clazz: KClass<out T>)
    
    /**
     * Unregisters a listener for a specific event
     *
     * @param T The event type the listener is listening to
     * @param listener The listener to be removed
     * @param clazz The event type the listener is listening to
     * @see register
     */
    public fun <T : PolyEvent> unregister(listener: PolyEventListener<T>, clazz: KClass<out T>)
    
    /**
     * Dispatches an event and alerts all registered listeners
     *
     * @param T The event type to dispatch
     * @param event The event to be dispatched
     * @param clazz The event type to dispatch
     * @see listeners
     */
    public suspend fun <T : PolyEvent> dispatch(event: T, clazz: KClass<out T>)
}