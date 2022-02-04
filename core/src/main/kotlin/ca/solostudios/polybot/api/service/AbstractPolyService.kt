/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file AbstractPolyService.kt is part of PolyhedralBot
 * Last modified on 03-02-2022 08:00 p.m.
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

package ca.solostudios.polybot.api.service

/**
 * Abstract service to make creating services easier.
 *
 * [initialized] **must** be called when the service is finished initialization,
 * whether this is in the constructor or off-thread.
 */
public abstract class AbstractPolyService : PolyService {
    final override var state: PolyService.State = PolyService.State.INITIALIZING
        protected set
    
    final override val shutdown: Boolean
        get() = state == PolyService.State.SHUTDOWN || state == PolyService.State.FAILED
    
    final override val running: Boolean
        get() = state == PolyService.State.RUNNING
    
    final override val active: Boolean
        get() = state.active
    
    @Throws(Exception::class)
    final override suspend fun shutdown() {
        if (!running)
            return
        
        state = PolyService.State.SHUTTING_DOWN
        
        serviceShutdown() // Throws an exception on failure
        
        state = PolyService.State.SHUTDOWN
    }
    
    @Throws(Exception::class)
    final override suspend fun start() {
        if (state.active) // If this service is active, just return
            return
        
        state = PolyService.State.STARTING
        
        serviceStart() // Throws an exception on failure
        
        state = PolyService.State.RUNNING
    }
    
    /**
     * This method must be invoked when the service is finished being initialized.
     */
    protected fun initialized() {
        state = PolyService.State.INITIALIZED
    }
    
    @Throws(Exception::class)
    protected abstract fun serviceStart()
    
    @Throws(Exception::class)
    protected abstract fun serviceShutdown()
}