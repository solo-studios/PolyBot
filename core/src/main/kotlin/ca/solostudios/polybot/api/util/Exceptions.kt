/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Exceptions.kt is part of PolyBot
 * Last modified on 03-02-2023 01:38 p.m.
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

@file:Suppress("NOTHING_TO_INLINE")

package ca.solostudios.polybot.api.util


/**
 * Throws an [IllegalStateException] with the given [message] and [cause].
 *
 * @throws IllegalStateException
 */
public inline fun error(cause: Throwable, message: String): Nothing {
    throw IllegalStateException(message, cause)
}

/**
 * Throws an [IllegalStateException] with the given [cause].
 *
 * @throws IllegalStateException
 */
public inline fun error(cause: Throwable? = null): Nothing {
    throw IllegalStateException("Error", cause)
}

/**
 * Throws an [IllegalStateException] with the given [message].
 *
 * @throws IllegalStateException
 */
public inline fun error(message: String): Nothing {
    throw IllegalStateException(message)
}

/**
 * Throws an [IllegalArgumentException] if the [value] is false.
 *
 * Use the `require*` methods for verifying arguments.
 *
 * @see check
 *
 * @throws IllegalArgumentException
 */
public inline fun require(value: Boolean) {
    require(value) { "Failed requirement." }
}

/**
 * Throws an [IllegalArgumentException] with the result of calling [lazyMessage] if the [value] is false.
 *
 * Use the `require*` methods for verifying arguments.
 *
 * @see check
 *
 * @throws IllegalArgumentException
 */
public inline fun require(value: Boolean, lazyMessage: () -> String) {
    if (!value)
        throw IllegalArgumentException(lazyMessage())
}

/**
 * Throws an [NullPointerException] if the [value] is null.
 * Otherwise, returns the not null value.
 *
 * Use the `require*` methods for verifying arguments.
 *
 * @see checkNotNull
 *
 * @throws NullPointerException
 */
public inline fun <T : Any> requireNotNull(value: T?): T {
    return requireNotNull(value) { "Required value was null." }
}

/**
 * Throws an [NullPointerException] with the result of calling [lazyMessage] if the [value] is null.
 * Otherwise, returns the not null value.
 *
 * Use the `require*` methods for verifying arguments.
 *
 * @see checkNotNull
 *
 * @throws NullPointerException
 */
public inline fun <T : Any> requireNotNull(value: T?, lazyMessage: () -> String): T {
    return value ?: throw NullPointerException(lazyMessage())
}

/**
 * Throws an [IllegalStateException] if the [value] is false.
 *
 * Use the `check*` methods for verifying internal state.
 *
 * @see require
 *
 * @throws IllegalStateException
 */
public inline fun check(value: Boolean) {
    check(value) { "Check failed." }
}

/**
 * Throws an [IllegalStateException] with the result of calling [lazyMessage] if the [value] is false.
 *
 * Use the `check*` methods for verifying internal state.
 *
 * @see require
 *
 * @throws IllegalStateException
 */
public inline fun check(value: Boolean, lazyMessage: () -> String) {
    if (!value)
        throw IllegalStateException(lazyMessage())
}

/**
 * Throws an [IllegalStateException] if the [value] is null.
 * Otherwise, returns the not null value.
 *
 * Use the `check*` methods for verifying internal state.
 *
 * @see requireNotNull
 *
 * @throws IllegalStateException
 */
public inline fun <T : Any> checkNotNull(value: T?): T {
    return checkNotNull(value) { "Checked value was null." }
}

/**
 * Throws an [IllegalStateException] with the result of calling [lazyMessage] if the [value] is null.
 * Otherwise, returns the not null value.
 *
 * Use the `check*` methods for verifying internal state.
 *
 * @see requireNotNull
 *
 * @throws IllegalStateException
 */
public inline fun <T : Any> checkNotNull(value: T?, lazyMessage: () -> String): T {
    return value ?: throw IllegalStateException(lazyMessage())
}

