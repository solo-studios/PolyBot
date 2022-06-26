/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyPluginManager.kt is part of PolyBot
 * Last modified on 26-06-2022 04:41 p.m.
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

package ca.solostudios.polybot.api.plugin

import ca.solostudios.polybot.api.PolyObject
import ca.solostudios.polybot.api.plugin.info.PolyPluginInfo
import ca.solostudios.polybot.common.service.DuplicateServiceException
import ca.solostudios.polybot.common.service.Service
import ca.solostudios.polybot.common.service.ServiceAlreadyStartedException
import ca.solostudios.polybot.common.service.ServiceManager
import kotlin.reflect.KClass

public interface PolyPluginManager : ServiceManager, PolyObject {
    public val plugins: Map<PolyPluginInfo, PolyPlugin>
    
    public suspend fun loadPlugins()
    
    /**
     * Add service to the manager
     *
     * @param T The type of service to be added
     * @param service The service to be added
     * @param clazz The class of the service to be added
     * @throws DuplicateServiceException if a service is added more than once
     * @throws ServiceAlreadyStartedException if a service has already been started
     * @throws IllegalArgumentException if the service being added is a [ServiceManager]
     */
    @Throws(DuplicateServiceException::class, ServiceAlreadyStartedException::class, IllegalArgumentException::class)
    public override fun <T : Service> addService(service: T, clazz: KClass<T>)
    
    /**
     * Returns a service from the manager.
     *
     * @param T The type of the service to return
     * @param clazz The class of the service
     * @return The service
     * @throws NullPointerException if no service of the specified type can be found
     */
    @Deprecated(message = "Avoid using any service methods with the plugin manager, use the plugin methods instead.")
    public override fun <T : Service> getService(clazz: KClass<T>): T
    
    /**
     * Adds an exception to the specified service.
     * This will mark the service as being unhealthy.
     *
     * This method should only be called from *within* the service, or by the service manager itself.
     *
     * @param T The type of the service to add an exception to
     * @param serviceClass The service class
     * @param exception The exception to be added
     */
    public override fun <T : Service> addException(serviceClass: KClass<T>, exception: Exception)
}