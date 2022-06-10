/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file JVMExtensions.kt is part of PolyBot
 * Last modified on 10-06-2022 11:33 a.m.
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

import java.lang.management.ManagementFactory
import java.lang.management.RuntimeMXBean
import kotlin.concurrent.thread


public fun onJvmShutdown(block: () -> Unit): Thread = onJvmShutdown("JVM-Shutdown-Thread", block)


public fun onJvmShutdown(name: String, block: () -> Unit): Thread {
    return thread(start = false, isDaemon = false, name = name, block = block).also {
        Runtime.getRuntime().addShutdownHook(it)
    }
}

public fun removeJvmShutdownThread(thread: Thread): Boolean = Runtime.getRuntime().removeShutdownHook(thread)

public val runtime: Runtime
    get() = Runtime.getRuntime()

/**
 * [Runtime.freeMemory]
 */
public val Runtime.freeMemory: Long
    get() = freeMemory()

/**
 * [Runtime.totalMemory]
 */
public val Runtime.totalMemory: Long
    get() = totalMemory()

/**
 * [Runtime.maxMemory]
 */
public val Runtime.maxMemory: Long
    get() = maxMemory()

public val Runtime.processors: Int
    get() = availableProcessors()

public val Runtime.availableProcessors: Int
    get() = availableProcessors()

public val runtimeMXBean: RuntimeMXBean
    get() = ManagementFactory.getRuntimeMXBean()