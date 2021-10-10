/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file jvm.kt is part of PolyhedralBot
 * Last modified on 09-10-2021 10:58 p.m.
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

package ca.solostudios.polybot.util

import java.lang.management.ManagementFactory
import java.lang.management.RuntimeMXBean
import kotlin.concurrent.thread

fun onJvmShutdown(block: () -> Unit) = onJvmShutdown("JVM-Shutdown-Thread", block)


fun onJvmShutdown(name: String, block: () -> Unit) {
    Runtime.getRuntime().addShutdownHook(thread(start = false, isDaemon = false, name = name, block = block))
}

val runtime: Runtime
    get() = Runtime.getRuntime()

/**
 * [Runtime.freeMemory]
 */
val Runtime.freeMemory: Long
    get() = freeMemory()

/**
 * [Runtime.totalMemory]
 */
val Runtime.totalMemory: Long
    get() = totalMemory()

/**
 * [Runtime.maxMemory]
 */
val Runtime.maxMemory: Long
    get() = maxMemory()

val Runtime.processors: Int
    get() = availableProcessors()

val Runtime.availableProcessors: Int
    get() = availableProcessors()

val runtimeMXBean: RuntimeMXBean
    get() = ManagementFactory.getRuntimeMXBean()