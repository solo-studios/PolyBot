/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file LazyLogger.kt is part of PolyhedralBot
 * Last modified on 10-06-2021 04:07 p.m.
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
@file:Suppress("NOTHING_TO_INLINE")

package org.slf4j.kotlin

import java.lang.invoke.MethodHandles
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass


@JvmSynthetic
inline fun getLazyLogger(): Lazy<KLogger> = getLazyLogger(MethodHandles.lookup().lookupClass())

@JvmSynthetic
inline fun getLazyLogger(clazz: KClass<*>): Lazy<KLogger> = getLazyLogger(clazz.java)

@JvmSynthetic
inline fun getLazyLogger(clazz: Class<*>): Lazy<KLogger> = lazy {
    val name = clazz.name
    val sliced = when {
        name.endsWith("Kt") -> name.substringBeforeLast("Kt")
        //        name.contains("Kt") -> name.substringBefore("Kt")
        //        name.contains("$")   -> name.substringBefore("$")
        else                -> name
    }
    KLogger(LoggerFactory.getLogger(sliced))
}

@JvmSynthetic
inline fun getLazyLogger(name: String): Lazy<KLogger> = lazy { KLogger(LoggerFactory.getLogger(name)) }


@JvmSynthetic
inline fun getLogger(): Lazy<KLogger> = getLazyLogger(MethodHandles.lookup().lookupClass())

@JvmSynthetic
inline fun getLogger(clazz: KClass<*>): Lazy<KLogger> = getLazyLogger(clazz.java)

@JvmSynthetic
inline fun getLogger(clazz: Class<*>): Lazy<KLogger> = getLazyLogger(clazz)

@JvmSynthetic
inline fun getLogger(name: String): Lazy<KLogger> = getLazyLogger(name)