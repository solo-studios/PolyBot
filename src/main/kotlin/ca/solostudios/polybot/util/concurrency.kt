/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file concurrency.kt is part of PolyhedralBot
 * Last modified on 29-11-2021 12:52 p.m.
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

@file:Suppress("unused")

package ca.solostudios.polybot.util

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ThreadFactory
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

@Suppress("FunctionName")
fun ScheduledThreadPool(
        corePoolSize: Int,
                       ): ScheduledExecutorService {
    return Executors.newScheduledThreadPool(corePoolSize)
}

@Suppress("FunctionName")
fun ScheduledThreadPool(
        corePoolSize: Int,
        threadFactory: ThreadFactory,
                       ): ScheduledExecutorService {
    return Executors.newScheduledThreadPool(corePoolSize, threadFactory)
}

fun ScheduledExecutorService.schedule(
        delay: Long,
        unit: TimeUnit = TimeUnit.MILLISECONDS,
        command: () -> Unit,
                                     ): ScheduledFuture<*> {
    return schedule(command, delay, unit)
}

fun ScheduledExecutorService.schedule(
        delay: Duration,
        command: () -> Unit,
                                     ): ScheduledFuture<*> {
    return schedule(delay.inWholeMilliseconds, TimeUnit.MILLISECONDS, command)
}

fun <V : Any> ScheduledExecutorService.schedule(
        delay: Long,
        unit: TimeUnit = TimeUnit.MILLISECONDS,
        callable: Callable<V>,
                                               ): ScheduledFuture<V> {
    return schedule(callable, delay, unit)
}

fun <V : Any> ScheduledExecutorService.schedule(
        delay: Duration,
        callable: Callable<V>,
                                               ): ScheduledFuture<V> {
    return schedule(delay.inWholeMilliseconds, TimeUnit.MILLISECONDS, callable)
}

fun ScheduledExecutorService.scheduleAtFixedRate(
        initialDelay: Long,
        period: Long,
        unit: TimeUnit = TimeUnit.MILLISECONDS,
        command: () -> Unit,
                                                ): ScheduledFuture<*> {
    return scheduleAtFixedRate(command, initialDelay, period, unit)
}

fun ScheduledExecutorService.scheduleAtFixedRate(
        initialDelay: Duration,
        period: Duration,
        command: () -> Unit,
                                                ): ScheduledFuture<*> {
    return scheduleAtFixedRate(initialDelay.inWholeMilliseconds, period.inWholeMilliseconds, TimeUnit.MILLISECONDS, command)
}

fun ScheduledExecutorService.fixedRate(
        initialDelay: Long,
        period: Long,
        unit: TimeUnit = TimeUnit.MILLISECONDS,
        command: () -> Unit,
                                      ): ScheduledFuture<*> {
    return scheduleAtFixedRate(initialDelay, period, unit, command)
}

fun ScheduledExecutorService.fixedRate(
        initialDelay: Duration,
        period: Duration,
        command: () -> Unit,
                                      ): ScheduledFuture<*> {
    return fixedRate(initialDelay.inWholeMilliseconds, period.inWholeMilliseconds, TimeUnit.MILLISECONDS, command)
}

fun ScheduledExecutorService.scheduleWithFixedDelay(
        initialDelay: Long,
        delay: Long,
        unit: TimeUnit = TimeUnit.MILLISECONDS,
        command: () -> Unit,
                                                   ): ScheduledFuture<*> {
    return scheduleWithFixedDelay(command, initialDelay, delay, unit)
}

fun ScheduledExecutorService.scheduleWithFixedDelay(
        initialDelay: Duration,
        delay: Duration,
        command: () -> Unit,
                                                   ): ScheduledFuture<*> {
    return scheduleWithFixedDelay(initialDelay.inWholeMilliseconds, delay.inWholeMilliseconds, TimeUnit.MILLISECONDS, command)
}

fun ScheduledExecutorService.fixedDelay(
        initialDelay: Long,
        delay: Long,
        unit: TimeUnit = TimeUnit.MILLISECONDS,
        command: () -> Unit,
                                       ): ScheduledFuture<*> {
    return scheduleWithFixedDelay(initialDelay, delay, unit, command)
}

fun ScheduledExecutorService.fixedDelay(
        initialDelay: Duration,
        delay: Duration,
        command: () -> Unit,
                                       ): ScheduledFuture<*> {
    return fixedDelay(initialDelay.inWholeMilliseconds, delay.inWholeMilliseconds, TimeUnit.MILLISECONDS, command)
}

operator fun Runnable.invoke(): Unit = run()

inline val currentThread: Thread
    get() = Thread.currentThread()

// val mainThread: Thread
//     get() {
//         ClassLoader.getSystemClassLoader()
//         var rootGroup = Thread.currentThread().threadGroup
//         var parentGroup: ThreadGroup
//         while (rootGroup.parent.also { parentGroup = it } != null) {
//             rootGroup = parentGroup
//         }
//
//         rootGroup
//     }