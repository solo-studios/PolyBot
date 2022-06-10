/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file ConcurrencyExtensions.kt is part of PolyBot
 * Last modified on 10-06-2022 11:32 a.m.
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

package ca.solostudios.polybot.api.util.ext

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ThreadFactory
import java.util.concurrent.TimeUnit
import kotlin.time.Duration


@Suppress("FunctionName")
public fun ScheduledThreadPool(
        corePoolSize: Int,
                              ): ScheduledExecutorService {
    return Executors.newScheduledThreadPool(corePoolSize)
}

@Suppress("FunctionName")
public fun ScheduledThreadPool(
        corePoolSize: Int,
        threadFactory: ThreadFactory,
                              ): ScheduledExecutorService {
    return Executors.newScheduledThreadPool(corePoolSize, threadFactory)
}

public fun ScheduledExecutorService.schedule(
        delay: Long,
        unit: TimeUnit = TimeUnit.MILLISECONDS,
        command: () -> Unit,
                                            ): ScheduledFuture<*> {
    return schedule(command, delay, unit)
}

public fun ScheduledExecutorService.schedule(
        delay: Duration,
        command: () -> Unit,
                                            ): ScheduledFuture<*> {
    return schedule(delay.inWholeMilliseconds, TimeUnit.MILLISECONDS, command)
}

public fun <V : Any> ScheduledExecutorService.schedule(
        delay: Long,
        unit: TimeUnit = TimeUnit.MILLISECONDS,
        callable: Callable<V>,
                                                      ): ScheduledFuture<V> {
    return schedule(callable, delay, unit)
}

public fun <V : Any> ScheduledExecutorService.schedule(
        delay: Duration,
        callable: Callable<V>,
                                                      ): ScheduledFuture<V> {
    return schedule(delay.inWholeMilliseconds, TimeUnit.MILLISECONDS, callable)
}

public fun ScheduledExecutorService.scheduleAtFixedRate(
        initialDelay: Long,
        period: Long,
        unit: TimeUnit = TimeUnit.MILLISECONDS,
        command: () -> Unit,
                                                       ): ScheduledFuture<*> {
    return scheduleAtFixedRate(command, initialDelay, period, unit)
}

public fun ScheduledExecutorService.scheduleAtFixedRate(
        initialDelay: Duration,
        period: Duration,
        command: () -> Unit,
                                                       ): ScheduledFuture<*> {
    return scheduleAtFixedRate(initialDelay.inWholeMilliseconds, period.inWholeMilliseconds, TimeUnit.MILLISECONDS, command)
}

public fun ScheduledExecutorService.fixedRate(
        initialDelay: Long,
        period: Long,
        unit: TimeUnit = TimeUnit.MILLISECONDS,
        command: () -> Unit,
                                             ): ScheduledFuture<*> {
    return scheduleAtFixedRate(initialDelay, period, unit, command)
}

public fun ScheduledExecutorService.fixedRate(
        initialDelay: Duration,
        period: Duration,
        command: () -> Unit,
                                             ): ScheduledFuture<*> {
    return fixedRate(initialDelay.inWholeMilliseconds, period.inWholeMilliseconds, TimeUnit.MILLISECONDS, command)
}

public fun ScheduledExecutorService.scheduleWithFixedDelay(
        initialDelay: Long,
        delay: Long,
        unit: TimeUnit = TimeUnit.MILLISECONDS,
        command: () -> Unit,
                                                          ): ScheduledFuture<*> {
    return scheduleWithFixedDelay(command, initialDelay, delay, unit)
}

public fun ScheduledExecutorService.scheduleWithFixedDelay(
        initialDelay: Duration,
        delay: Duration,
        command: () -> Unit,
                                                          ): ScheduledFuture<*> {
    return scheduleWithFixedDelay(initialDelay.inWholeMilliseconds, delay.inWholeMilliseconds, TimeUnit.MILLISECONDS, command)
}

public fun ScheduledExecutorService.fixedDelay(
        initialDelay: Long,
        delay: Long,
        unit: TimeUnit = TimeUnit.MILLISECONDS,
        command: () -> Unit,
                                              ): ScheduledFuture<*> {
    return scheduleWithFixedDelay(initialDelay, delay, unit, command)
}

public fun ScheduledExecutorService.fixedDelay(
        initialDelay: Duration,
        delay: Duration,
        command: () -> Unit,
                                              ): ScheduledFuture<*> {
    return fixedDelay(initialDelay.inWholeMilliseconds, delay.inWholeMilliseconds, TimeUnit.MILLISECONDS, command)
}

public operator fun Runnable.invoke(): Unit = run()

public inline val currentThread: Thread
    get() = Thread.currentThread()