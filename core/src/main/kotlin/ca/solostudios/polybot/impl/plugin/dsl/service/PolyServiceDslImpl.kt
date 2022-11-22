/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyServiceDslImpl.kt is part of PolyBot
 * Last modified on 22-11-2022 03:06 p.m.
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
import kotlin.reflect.KClass

internal class PolyServiceDslImpl : PolyServiceDsl {
    /**
     * List of service providers
     */
    val serviceProviders = mutableListOf<ServiceInitializer<*, *>>()
    
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
            serviceProvider: (config: C) -> T,
                                                                 ) {
        serviceProviders += ServiceInitializer(serviceClass, configClass, serviceProvider)
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
}