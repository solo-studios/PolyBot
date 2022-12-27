/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyServiceDslImpl.kt is part of PolyBot
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

package ca.solostudios.polybot.impl.plugin.dsl.service

import ca.solostudios.guava.kotlin.collect.mutableListMultimapOf
import ca.solostudios.polybot.api.plugin.dsl.service.PolyServiceDsl
import ca.solostudios.polybot.api.service.PolyService
import ca.solostudios.polybot.api.service.config.ServiceConfig
import ca.solostudios.polybot.api.service.config.ServiceConfigHolder
import ca.solostudios.polybot.impl.service.PolyServiceManagerImpl
import org.kodein.di.DI
import org.slf4j.kotlin.*
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

internal class PolyServiceDslImpl : PolyServiceDsl {
    private val logger by getLogger()
    
    /**
     * List of service providers
     */
    val serviceInitializers = mutableListOf<ServiceInitializer<*, *>>()
    
    /**
     * Service configurers map, by service class -> list of service configurers
     */
    val serviceConfigurers = mutableListMultimapOf<KClass<*>, ServiceConfigurer<*, *>>()
    
    /**
     * Service config initializers map, by config class -> config initializer
     */
    val serviceConfigInitializers = mutableMapOf<KClass<*>, ServiceConfigInitializer<*>>()
    
    
    override fun <T : PolyService<C>, C : ServiceConfig> register(
            serviceClass: KClass<T>,
            configClass: KClass<C>,
            serviceProvider: (C, DI) -> T,
                                                                 ) {
        serviceInitializers += ServiceInitializer(serviceClass, configClass, serviceProvider)
    }
    
    override fun <T : PolyService<C>, C : ServiceConfig> configure(
            serviceClass: KClass<T>,
            configClass: KClass<C>,
            configBlock: C.() -> Unit,
                                                                  ) {
        serviceConfigurers[serviceClass] += ServiceConfigurer(serviceClass, configClass, configBlock)
    }
    
    override fun <C : ServiceConfig> configInitializer(configClass: KClass<C>, initializer: (configHolder: ServiceConfigHolder) -> C) {
        serviceConfigInitializers[configClass] = ServiceConfigInitializer(configClass, initializer)
    }
    
    fun applyServiceDsl(serviceManager: PolyServiceManagerImpl, di: DI) {
        for (serviceInitializer in serviceInitializers) {
            val serviceClass = serviceInitializer.serviceClass
            val configClass = serviceInitializer.configClass
            val configInitializer = serviceConfigInitializers[configClass]
            val config = configInitializer?.run { this.configInitializer(ServiceConfigHolder()) }
                ?: configClass.objectInstance
                ?: error("Could not instantiate config instance for type $configClass due to not being an object and not or not being found in the config initializers.")
            val initializer = serviceInitializer.initializer
            
            val service = initializer(config, di)
            
            serviceConfigurers[serviceClass].forEach {
                if (configClass == it.configClass || configClass.isSubclassOf(it.configClass)) {
                
                } else {
                    logger.warn { "Could not invoke an initializer for $serviceClass, as the initializer specifies" }
                }
            }
        }
    }
}