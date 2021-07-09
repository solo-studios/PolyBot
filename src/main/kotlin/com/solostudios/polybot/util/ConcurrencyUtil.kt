/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file ConcurrencyUtil.kt is part of PolyhedralBot
 * Last modified on 09-07-2021 05:17 p.m.
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

package com.solostudios.polybot.util

import java.util.concurrent.Callable
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.until

fun ScheduledExecutorService.schedule(delay: Long,
                                      unit: TimeUnit = TimeUnit.MILLISECONDS,
                                      command: () -> Unit): ScheduledFuture<*> = schedule(command, delay, unit)

fun ScheduledExecutorService.schedule(delay: Instant, command: () -> Unit): ScheduledFuture<*> =
    schedule(Clock.System.now().until(delay, DateTimeUnit.MILLISECOND), TimeUnit.MILLISECONDS, command)

fun <V : Any> ScheduledExecutorService.schedule(delay: Long,
                                                unit: TimeUnit = TimeUnit.MILLISECONDS,
                                                callable: Callable<V>): ScheduledFuture<V> = schedule(callable, delay, unit)

fun <V : Any> ScheduledExecutorService.schedule(delay: Instant, callable: Callable<V>): ScheduledFuture<*> =
    schedule(Clock.System.now().until(delay, DateTimeUnit.MILLISECOND), TimeUnit.MILLISECONDS, callable)

fun ScheduledExecutorService.scheduleAtFixedRate(initialDelay: Long,
                                                 period: Long,
                                                 unit: TimeUnit = TimeUnit.MILLISECONDS,
                                                 command: () -> Unit): ScheduledFuture<*> =
    scheduleAtFixedRate(command, initialDelay, period, unit)

fun ScheduledExecutorService.scheduleAtFixedRate(initialDelay: Instant, period: Instant, command: () -> Unit): ScheduledFuture<*> {
    val now = Clock.System.now()
    return scheduleAtFixedRate(now.until(initialDelay, DateTimeUnit.MILLISECOND),
                               now.until(period, DateTimeUnit.MILLISECOND),
                               TimeUnit.MILLISECONDS, command)
}

fun ScheduledExecutorService.fixedRate(initialDelay: Long,
                                       period: Long,
                                       unit: TimeUnit = TimeUnit.MILLISECONDS,
                                       command: () -> Unit) = scheduleAtFixedRate(initialDelay, period, unit, command)

fun ScheduledExecutorService.fixedRate(initialDelay: Instant, period: Instant, command: () -> Unit): ScheduledFuture<*> {
    val now = Clock.System.now()
    return fixedRate(now.until(initialDelay, DateTimeUnit.MILLISECOND),
                     now.until(period, DateTimeUnit.MILLISECOND),
                     TimeUnit.MILLISECONDS,
                     command)
}

fun ScheduledExecutorService.scheduleWithFixedDelay(initialDelay: Long,
                                                    delay: Long,
                                                    unit: TimeUnit = TimeUnit.MILLISECONDS,
                                                    command: () -> Unit): ScheduledFuture<*> =
    scheduleWithFixedDelay(command, initialDelay, delay, unit)

fun ScheduledExecutorService.scheduleWithFixedDelay(initialDelay: Instant, delay: Instant, command: () -> Unit): ScheduledFuture<*> {
    val now = Clock.System.now()
    return scheduleWithFixedDelay(now.until(initialDelay, DateTimeUnit.MILLISECOND),
                                  now.until(delay, DateTimeUnit.MILLISECOND),
                                  TimeUnit.MILLISECONDS,
                                  command)
}

fun ScheduledExecutorService.fixedDelay(initialDelay: Long,
                                        delay: Long,
                                        unit: TimeUnit = TimeUnit.MILLISECONDS,
                                        command: () -> Unit) = scheduleWithFixedDelay(initialDelay, delay, unit, command)

fun ScheduledExecutorService.fixedDelay(initialDelay: Instant, delay: Instant, command: () -> Unit): ScheduledFuture<*> {
    val now = Clock.System.now()
    return fixedDelay(now.until(initialDelay, DateTimeUnit.MILLISECOND),
                      now.until(delay, DateTimeUnit.MILLISECOND),
                      TimeUnit.MILLISECONDS,
                      command)
}