/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file ServiceManager.kt is part of PolyBot
 * Last modified on 15-04-2023 01:59 p.m.
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

import ca.solostudios.guava.kotlin.collect.ListMultimap
import ca.solostudios.polybot.common.service.exceptions.DuplicateServiceException
import ca.solostudios.polybot.common.service.exceptions.ServiceAlreadyStartedException
import kotlin.reflect.KClass
import kotlin.time.Duration

/**
 * Service manager
 */
public interface ServiceManager<S : Service> : Service {
    /**
     * All the services that have been registered to this service manager.
     */
    public val services: ListMultimap<KClass<out S>, S>
    
    /**
     * The amount of time it took for each service to start up
     */
    public val startupTimes: Map<S, Duration>
    
    /**
     * The health of each service.
     *
     * @see ServiceHealth
     */
    public val serviceHealth: Map<KClass<S>, ServiceHealth>
    
    /**
     * The state the service manager is currently in
     *
     * @see State
     */
    override val state: Service.State
    
    /**
     * True if this service manager has been shutdown successfully. False otherwise.
     *
     * Corresponds to when `state` is `SHUTDOWN` or `FAILED`.
     *
     * @see state
     * @see Service.State.SHUTDOWN
     * @see Service.State.FAILED
     */
    override val shutdown: Boolean
    
    /**
     * True if this service manager is running. False otherwise.
     *
     * Corresponds to when `state` is `RUNNING`
     *
     * @see state
     * @see Service.State.RUNNING
     */
    override val running: Boolean
    
    /**
     * True if this service manager is active. False otherwise.
     *
     * This indicates that one, or many, services that are controlled by this manager have a state with [State.active].
     *
     * When a service is in an active state, this indicates that it is capable of performing actions outside its own class namespace.
     *
     * This includes, but is not limited to:
     * - reading/writing to any files
     * - sending HTTP requests
     * - manipulating objects outside its own namespace
     *
     * @see state
     * @see Service.State
     */
    override val active: Boolean
    
    /**
     * Whether this service manager is healthy.
     *
     * This is true when all services had no errors during startup & have not reported any errors
     */
    public val healthy: Boolean
    
    /**
     * Shutdown the service manager and all services attached to it.
     *
     * This method blocks until it every service is completely shutdown.
     *
     * This will never throw any exceptions.
     */
    override suspend fun shutdown()
    
    /**
     * Starts the service manager and all services attached to it.
     *
     * This method blocks until every service has started successfully.
     *
     * This will never thrown any exceptions.
     */
    override suspend fun start()
    
    /**
     * Add a service to the manager
     *
     * @param T The type of service to be added
     * @param service The service to be added
     * @param clazz The class of the service to be added
     * @throws DuplicateServiceException if a service is added more than once
     * @throws ServiceAlreadyStartedException if a service has already been started
     * @throws IllegalArgumentException if the service being added is a [ServiceManager]
     * @throws IllegalStateException if the service manager has already been started
     */
    @Throws(DuplicateServiceException::class, ServiceAlreadyStartedException::class, IllegalArgumentException::class, IllegalStateException::class)
    public fun <T : S> addService(service: T, clazz: KClass<out T>)
    
    /**
     * Returns a service from the manager.
     *
     * @param T The type of the service to return
     * @param clazz The class of the service
     * @return The service
     * @throws NullPointerException If no service of the specified type can be found
     */
    @Throws(NullPointerException::class)
    public fun <T : S> getService(clazz: KClass<T>): T
    
    /**
     * Returns a list of services from the manager.
     *
     * @param T The type of the services to return
     * @param clazz The class of the services
     * @return The list of services. Empty if none are found
     */
    public fun <T : S> getServices(clazz: KClass<T>): List<T>
    
    /**
     * Adds an exception to the specified service.
     * This will mark the service as being unhealthy.
     *
     * This method should only be called from *within* the service, or by the service manager itself.
     *
     * @param serviceClass The service class
     * @param exception The exception to be added
     */
    public fun addException(serviceClass: KClass<S>, exception: Exception)
    
    public interface ServiceHealth {
        /**
         * Whether this service is healthy.
         *
         * The service is healthy if it has not thrown any exceptions.
         *
         * @see suppressedExceptions
         */
        public val healthy: Boolean
        
        /**
         * A list of exceptions thrown by the service.
         *
         * If the service is healthy, the list will have a size of 0.
         */
        public val exceptions: List<Exception>
    }
}