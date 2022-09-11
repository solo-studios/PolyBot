/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file AbstractServiceManager.kt is part of PolyBot
 * Last modified on 11-09-2022 06:12 p.m.
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

import kotlin.reflect.KClass
import kotlin.reflect.cast
import kotlin.time.Duration

public abstract class AbstractServiceManager<S : Service> : AbstractService(), ServiceManager<S> {
    final override val healthy: Boolean
        get() = serviceHealth.values.all { it.healthy }
    
    final override val startupTimes: MutableMap<S, Duration> = mutableMapOf()
    
    final override val serviceHealth: MutableMap<KClass<S>, ServiceManager.ServiceHealth> = mutableMapOf()
    
    override fun <T : S> getService(clazz: KClass<T>): T {
        for (service in services) {
            if (clazz.isInstance(service))
                return clazz.cast(service)
        }
        throw NullPointerException("Could not find plugin that is an instance of $clazz")
    }
    
    override fun <T : S> getServices(clazz: KClass<T>): List<T> {
        return services.filter { clazz.isInstance(it) }.map { clazz.cast(it) }
    }
    
    override fun addException(serviceClass: KClass<S>, exception: Exception) {
        val health = (serviceHealth[serviceClass] ?: ServiceHealthImpl()) as ServiceHealthImpl
        health.exceptions.add(exception)
        
        serviceHealth[serviceClass] = health
    }
    
    internal class ServiceHealthImpl : ServiceManager.ServiceHealth {
        override val healthy: Boolean
            get() = exceptions.isEmpty()
        override val exceptions: MutableList<Exception> = mutableListOf()
        
    }
}