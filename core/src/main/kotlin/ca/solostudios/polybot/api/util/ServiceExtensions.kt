/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file ServiceExtensions.kt is part of PolyhedralBot
 * Last modified on 08-02-2022 03:38 p.m.
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

package ca.solostudios.polybot.api.util

import ca.solostudios.polybot.api.service.DuplicateServiceException
import ca.solostudios.polybot.api.service.PolyService
import ca.solostudios.polybot.api.service.PolyServiceManager
import ca.solostudios.polybot.api.service.ServiceAlreadyStartedException
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Returns a service from the manager.
 *
 * @param T The type of the service to return
 * @param clazz The class of the service
 * @return The service
 * @throws NullPointerException if no service of the specified type can be found
 */
public operator fun <T : PolyService> PolyServiceManager.get(clazz: KClass<T>): T = getService(clazz)

/**
 * Returns a service from the manager.
 *
 * @param T The type of the service to return
 * @return The service
 * @throws NullPointerException if no service of the specified type can be found
 */
public inline fun <reified T : PolyService> PolyServiceManager.service(): T = getService(T::class)

/**
 * Add service to the manager
 *
 * @param T The type of service to be added
 * @param service The service to be added
 * @throws DuplicateServiceException if a service is added more than once
 * @throws ServiceAlreadyStartedException if a service has already been started
 * @throws IllegalArgumentException if the service being added is a [PolyServiceManager]
 */
@Throws(DuplicateServiceException::class, ServiceAlreadyStartedException::class, IllegalArgumentException::class)
public operator fun <T : PolyService> PolyServiceManager.set(clazz: KClass<T>, service: T): Unit = addService(service, clazz)

/**
 * Add service to the manager
 *
 * @param T The type of service to be added
 * @param service The service to be added
 * @throws DuplicateServiceException if a service is added more than once
 * @throws ServiceAlreadyStartedException if a service has already been started
 * @throws IllegalArgumentException if the service being added is a [PolyServiceManager]
 */
@Throws(DuplicateServiceException::class, ServiceAlreadyStartedException::class, IllegalArgumentException::class)
public inline fun <reified T : PolyService> PolyServiceManager.addService(service: T): Unit = addService(service, T::class)

/**
 * Property delegate for a service, from the service manager.
 *
 * @param T The type of the service to return
 * @return The service
 * @throws NullPointerException if no service of the specified type can be found
 */
public inline operator fun <reified T : PolyService> PolyServiceManager.getValue(thisRef: Any?, property: KProperty<*>): T {
    return getService(T::class)
}