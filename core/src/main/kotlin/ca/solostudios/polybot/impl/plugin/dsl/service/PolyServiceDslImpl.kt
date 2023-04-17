/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyServiceDslImpl.kt is part of PolyBot
 * Last modified on 17-04-2023 12:14 p.m.
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

import ca.solostudios.polybot.api.plugin.dsl.service.PolyServiceDsl
import ca.solostudios.polybot.api.service.PolyService
import ca.solostudios.polybot.api.service.config.ServiceConfig
import ca.solostudios.polybot.api.service.config.ServiceConfigHolder
import ca.solostudios.polybot.impl.service.PolyServiceManagerImpl
import org.checkerframework.checker.units.qual.C
import org.kodein.di.DI
import org.slf4j.kotlin.*
import kotlin.reflect.KClass

internal class PolyServiceDslImpl : PolyServiceDsl {
    private val logger by getLogger()
    
    /**
     * List of service providers
     */
    val serviceInitializers = mutableListOf<ServiceInitializer<*, *>>()
    
    /**
     * Service configurers map, by service class -> list of service configurers
     */
    val serviceConfigurers = mutableListOf<ServiceConfigurer<*>>()
    
    /**
     * Service config initializers map, by config class -> config initializer
     */
    val serviceConfigInitializers = mutableListOf<ServiceConfigInitializer<*>>()
    
    
    override fun <T : PolyService<C>, C : ServiceConfig> register(
            serviceClass: KClass<T>,
            configClass: KClass<C>,
            serviceProvider: (C, DI) -> T,
                                                                 ) {
        serviceInitializers += ServiceInitializer(serviceClass, configClass, serviceProvider)
    }
    
    override fun <C : ServiceConfig> configure(
            configClass: KClass<C>,
            configBlock: C.() -> Unit,
                                              ) {
        serviceConfigurers += ServiceConfigurer(configClass, configBlock)
    }
    
    override fun <C : ServiceConfig> configInitializer(configClass: KClass<C>, initializer: (configHolder: ServiceConfigHolder) -> C) {
        serviceConfigInitializers += ServiceConfigInitializer(configClass, initializer)
    }
    
    fun applyServiceDsl(serviceManager: PolyServiceManagerImpl, di: DI) {
        for ((configClass, configInitializer) in serviceConfigInitializers) {
            val configHolder = ServiceConfigHolder()
            val config = configInitializer(configHolder)
        
            serviceManager.addServiceConfig(config, configClass)
        }
    
        for ((configClass, configBlock) in serviceConfigurers) {
            val config = serviceManager.getServiceConfig(configClass)
        
            configBlock(config)
        }
    
        for ((serviceClass, configClass, initializer) in serviceInitializers) {
            val config = serviceManager.getServiceConfig(configClass)
        
            val service = initializer(config, di)
        
            serviceManager.addService(service, serviceClass)
        }
    }
}