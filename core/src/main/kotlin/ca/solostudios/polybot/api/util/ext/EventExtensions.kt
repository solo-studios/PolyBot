/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file EventExtensions.kt is part of PolyBot
 * Last modified on 10-06-2022 11:33 a.m.
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

import ca.solostudios.polybot.api.event.PolyEvent
import ca.solostudios.polybot.api.event.PolyEventListener
import ca.solostudios.polybot.api.event.PolyEventManager

/**
 * Register a listener for a specific event
 *
 * @param T The type event to listen for
 * @param listener The listener to be invoked when the event is dispatched
 * @see dispatch
 */
public inline fun <reified T : PolyEvent> PolyEventManager.register(listener: PolyEventListener<T>) {
    register(listener, T::class)
}

/**
 * Unregisters a listener for a specific event
 *
 * @param T The event type the listener is listening to
 * @param listener The listener to be removed
 * @see register
 */
public inline fun <reified T : PolyEvent> PolyEventManager.unregister(listener: PolyEventListener<T>) {
    unregister(listener, T::class)
}

/**
 * Dispatches an event and alerts all registered listeners
 *
 * @param T The event type to dispatch
 * @param event The event to be dispatched
 * @see PolyEventManager.listeners
 */
public inline fun <reified T : PolyEvent> PolyEventManager.dispatch(event: T) {
    dispatch(event, event::class)
}