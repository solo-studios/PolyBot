/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyServiceManager.kt is part of PolyBot
 * Last modified on 15-04-2023 01:41 p.m.
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

package ca.solostudios.polybot.api.service

import ca.solostudios.polybot.api.PolyObject
import ca.solostudios.polybot.api.service.config.ServiceConfig
import ca.solostudios.polybot.api.service.exception.DuplicateServiceConfigException
import ca.solostudios.polybot.common.service.ServiceManager
import kotlin.reflect.KClass

/**
 * Poly service manager
 *
 * @see ServiceManager
 */
public interface PolyServiceManager<C : ServiceConfig, S : PolyService<*>> : ServiceManager<S>,
                                                                             PolyService<C>,
                                                                             PolyObject {
    /**
     * All the service configs that have been registered to this service manager.
     */
    public val serviceConfigs: Map<KClass<out ServiceConfig>, ServiceConfig>
    
    /**
     * Add a service config to the manager
     *
     * @param T The type of service config to be added
     * @param config The service to be added
     * @param clazz The class of the service config to be added
     * @throws DuplicateServiceConfigException if a service config is added more than once
     * @throws IllegalStateException if the service manager has already been started
     */
    @Throws(DuplicateServiceConfigException::class, IllegalStateException::class)
    public fun <T : ServiceConfig> addServiceConfig(config: T, clazz: KClass<out T>)
    
    /**
     * Returns a service config from the manager.
     *
     * @param T The type of the service config to return
     * @param clazz The class of the service config
     * @return The service config
     * @throws NullPointerException If no service config of the specified type can be found
     */
    @Throws(NullPointerException::class)
    public fun <T : ServiceConfig> getServiceConfig(clazz: KClass<T>): T
}