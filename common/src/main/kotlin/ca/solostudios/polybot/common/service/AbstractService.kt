/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file AbstractService.kt is part of PolyBot
 * Last modified on 27-12-2022 01:32 p.m.
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

package ca.solostudios.polybot.common.service

import ca.solostudios.polybot.common.service.Service.State

/**
 * Abstract service to make creating services easier.
 */
public abstract class AbstractService : Service {
    final override var state: State = State.INITIALIZING
        protected set
    
    final override val shutdown: Boolean
        get() = state == State.SHUTDOWN
    
    final override val running: Boolean
        get() = state == State.RUNNING
    
    final override val active: Boolean
        get() = state.active
    
    @Throws(Exception::class)
    final override suspend fun shutdown() {
        if (!running)
            return
    
        updateState(State.SHUTTING_DOWN)
    
        serviceShutdown() // Throws an exception on failure
    
        updateState(State.SHUTDOWN)
    }
    
    @Throws(Exception::class)
    final override suspend fun start() {
        if (state.active) // If this service is active, just return
            return
    
        updateState(State.STARTING)
    
        serviceStart() // Throws an exception on failure
    
        updateState(State.RUNNING)
    }
    
    @Throws(Exception::class)
    protected abstract suspend fun serviceStart()
    
    @Throws(Exception::class)
    protected abstract suspend fun serviceShutdown()
    
    protected fun ensureState(state: State, message: String) {
        if (this.state != state)
            error(message)
    }
    
    protected fun updateState(newState: State) {
        this.state = newState
    }
}