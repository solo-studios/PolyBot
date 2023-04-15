/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyServiceManagerImpl.kt is part of PolyBot
 * Last modified on 15-04-2023 01:08 p.m.
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

package ca.solostudios.polybot.impl.service

import ca.solostudios.guava.kotlin.collect.mutableListMultimapOf
import ca.solostudios.polybot.api.PolyBot
import ca.solostudios.polybot.api.service.AbstractPolyService
import ca.solostudios.polybot.api.service.PolyService
import ca.solostudios.polybot.api.service.PolyServiceManager
import ca.solostudios.polybot.api.service.config.EmptyServiceConfig
import ca.solostudios.polybot.common.service.Service
import ca.solostudios.polybot.common.service.ServiceManager
import ca.solostudios.polybot.common.service.exceptions.ServiceManagerShutdownException
import ca.solostudios.polybot.common.service.exceptions.ServiceManagerStartupException
import org.slf4j.kotlin.*
import kotlin.reflect.KClass
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource.Monotonic
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

internal class PolyServiceManagerImpl(
        override val config: EmptyServiceConfig,
        override val bot: PolyBot,
                                     ) : AbstractPolyService<EmptyServiceConfig>(config, bot),
                                         PolyServiceManager<EmptyServiceConfig, PolyService<*>> {
    private val logger by getLogger()
    
    override var services = mutableListMultimapOf<KClass<out PolyService<*>>, PolyService<*>>()
        private set
    
    override var startupTimes: Map<PolyService<*>, Duration> = emptyMap()
        private set
    
    override val serviceHealth = mutableMapOf<KClass<PolyService<*>>, ServiceHealthImpl>()
    
    override val healthy: Boolean
        get() = serviceHealth.all { it.value.healthy }
    
    override fun addException(serviceClass: KClass<PolyService<*>>, exception: Exception) {
        serviceHealth.getOrPut(serviceClass) { ServiceHealthImpl() }.exceptions.add(exception)
    }
    
    override fun <T : PolyService<*>> getServices(clazz: KClass<T>): List<T> {
        @Suppress("UNCHECKED_CAST")
        return services[clazz] as List<T>
    }
    
    override fun <T : PolyService<*>> getService(clazz: KClass<T>): T {
        @Suppress("UNCHECKED_CAST")
        return services[clazz].first() as T
    }
    
    override fun <T : PolyService<*>> addService(service: T, clazz: KClass<T>) {
        ensureState(
                Service.State.INITIALIZING,
                "You can only add new services to the service manager before the start() method has been called."
                   )
        services[clazz] += service
    }
    
    @OptIn(ExperimentalTime::class)
    override suspend fun serviceStart() {
        logger.info { "Starting ${services.size} services..." }
        
        val startupExceptions = mutableListOf<Exception>()
        
        val (serviceStartupTimes, totalStartupTime) = Monotonic.measureTimedValue {
            buildMap {
                for ((serviceClass, service) in services) {
                    if (!service.active) {
                        try {
                            
                            logger.debug { "Starting service $serviceClass with implementation ${service::class}" }
                            
                            val startupTime = Monotonic.measureTime {
                                service.start()
                            }
                            this@buildMap[service] = startupTime
                            
                            logger.debug { "Successfully started $serviceClass service in $startupTime" }
                        } catch (e: Exception) {
                            logger.error(e) { "Caught exception while starting service $serviceClass with implementation ${service::class}" }
                            
                            startupExceptions += e
                        }
                    } else {
                        logger.debug { "Service $serviceClass with implementation ${service::class} is active. Skipping startup." }
                    }
                }
            }
        }
        startupTimes = serviceStartupTimes
        
        if (startupExceptions.isNotEmpty()) {
            val exceptionSize = startupExceptions.size
            
            logger.error { "$exceptionSize errors occurred in services while starting the service manager." }
            
            throw ServiceManagerStartupException(
                    "Could not start service manager due to service $exceptionSize exceptions",
                    startupExceptions
                                                )
        }
        
        logger.info { "Successfully started ${services.size} services in $totalStartupTime" }
    }
    
    @OptIn(ExperimentalTime::class)
    override suspend fun serviceShutdown() {
        logger.info { "Shutting down ${services.size} services..." }
        
        val shutdownExceptions = mutableListOf<Exception>()
        
        val totalShutdownTime = Monotonic.measureTime {
            for ((serviceClass, service) in services) {
                val serviceImplClass = service::class
                
                if (service.active) {
                    try {
                        logger.debug { "Shutting down service $serviceClass with implementation $serviceImplClass" }
                        
                        val startupTime = Monotonic.measureTime {
                            service.shutdown()
                        }
                        
                        logger.debug { "Successfully shut down $serviceClass service in $startupTime" }
                    } catch (e: Exception) {
                        logger.error(e) { "Caught exception while shutting down service $serviceClass with implementation $serviceImplClass" }
                        
                        shutdownExceptions += e
                    }
                } else {
                    logger.debug { "Service $serviceClass with implementation $serviceImplClass is not active. Skipping shutdown." }
                }
            }
        }
        
        if (shutdownExceptions.isNotEmpty()) {
            val exceptionSize = shutdownExceptions.size
            
            logger.error { "$exceptionSize errors occurred in services while shutting down the service manager." }
            
            throw ServiceManagerShutdownException(
                    "Could not shut down service manager due to service $exceptionSize exceptions",
                    shutdownExceptions
                                                 )
        }
        
        logger.info { "Successfully shut down ${services.size} services in $totalShutdownTime" }
    }
    
    internal data class ServiceHealthImpl(
            override val exceptions: MutableList<Exception> = mutableListOf(),
                                         ) : ServiceManager.ServiceHealth {
        override val healthy: Boolean
            get() = exceptions.isEmpty()
    }
}