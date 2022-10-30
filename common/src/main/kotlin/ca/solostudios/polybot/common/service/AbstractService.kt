/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file AbstractService.kt is part of PolyBot
 * Last modified on 30-10-2022 02:06 p.m.
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

/**
 * Abstract service to make creating services easier.
 */
public abstract class AbstractService : Service {
    final override var state: Service.State = Service.State.INITIALIZING
        protected set
    
    final override val shutdown: Boolean
        get() = state == Service.State.SHUTDOWN
    
    final override val running: Boolean
        get() = state == Service.State.RUNNING
    
    final override val active: Boolean
        get() = state.active
    
    @Throws(Exception::class)
    final override suspend fun shutdown() {
        if (!running)
            return
        
        state = Service.State.SHUTTING_DOWN
        
        serviceShutdown() // Throws an exception on failure
        
        state = Service.State.SHUTDOWN
    }
    
    @Throws(Exception::class)
    final override suspend fun start() {
        if (state.active) // If this service is active, just return
            return
        
        state = Service.State.STARTING
        
        serviceStart() // Throws an exception on failure
        
        state = Service.State.RUNNING
    }
    
    @Throws(Exception::class)
    protected abstract fun serviceStart()
    
    @Throws(Exception::class)
    protected abstract fun serviceShutdown()
}