/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Service.kt is part of PolyBot
 * Last modified on 20-08-2022 05:43 p.m.
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
 * This represents a service, which can be started and stopped.
 *
 * This class provides a common interface for services, as well as managing them.
 *
 * All services can be started, stopped, and then started again.
 */
public interface Service {
    /**
     * The state the service is currently in
     *
     * @see State
     */
    public val state: State
    
    /**
     * True if this service has been shutdown successfully. False otherwise.
     *
     * Corresponds to when `state` is `SHUTDOWN` or `FAILED`.
     *
     * @see state
     * @see State.SHUTDOWN
     * @see State.FAILED
     */
    public val shutdown: Boolean
    
    /**
     * True if this service is running. False otherwise.
     *
     * Corresponds to when `state` is `RUNNING`
     *
     * @see state
     * @see State.RUNNING
     */
    public val running: Boolean
    
    /**
     * True if this service is active. False otherwise.
     *
     * Corresponds to any state with [State.active]
     *
     * When a service is in an active state, this indicates that it is capable of performing actions outside its own class namespace.
     *
     * This includes, but is not limited to:
     * - reading/writing to any files
     * - sending HTTP requests
     * - manipulating objects outside its own namespace
     *
     * @see state
     * @see State
     */
    public val active: Boolean
    
    /**
     * Shutdown the running service and blocks until it is fully shutdown.
     *
     * Exceptions may be thrown during shutdown.
     */
    @Throws(Exception::class)
    public suspend fun shutdown()
    
    /**
     * Starts this service and blocks until it is fully started.
     *
     * Exceptions may be thrown during startup.
     *
     * Note: services may be restarted several times.
     */
    @Throws(Exception::class)
    public suspend fun start()
    
    public enum class State(
            /**
             * Any state where this is true indicates that the service is capable of performing actions outside its own class namespace.
             *
             * This includes, but is not limited to:
             * - reading/writing to any files
             * - sending HTTP requests
             * - manipulating objects outside its own namespace
             */
            public val active: Boolean,
                           ) {
        /**
         * The service is setting up supporting *internal* systems like thread pools, etc.
         *
         * It is **not** starting up the service.
         */
        INITIALIZING(false),
        
        /**
         * The service is starting up and is connecting to all required systems.
         */
        STARTING(true),
        
        /**
         * The service is actively running
         */
        RUNNING(true),
        
        /**
         * The service has begun the shutdown proces and is cleaning up all remaining processes/tasks.
         */
        SHUTTING_DOWN(true),
        
        /**
         * The service has shutdown successfully.
         *
         * It is dormant in this state.
         */
        SHUTDOWN(false),
        
        /**
         * The service failed to start successfully.
         *
         * It is dormant during this state.
         */
        FAILED(false)
    }
}