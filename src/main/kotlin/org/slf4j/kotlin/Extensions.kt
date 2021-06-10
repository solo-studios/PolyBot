/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Extensions.kt is part of PolyhedralBot
 * Last modified on 14-04-2021 04:16 p.m.
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

package org.slf4j.kotlin

import org.slf4j.Logger
import org.slf4j.Marker

/**
 * Log a message at the TRACE level.
 *
 * @param message the message string to be logged
 *
 */
@JvmSynthetic
inline fun Logger.trace(crossinline message: () -> String) {
    if (isTraceEnabled)
        trace(message())
}

/**
 * Log a message at the TRACE level according to the specified format
 * and argument.
 *
 *
 *
 * This form avoids superfluous object creation when the logger
 * is disabled for the TRACE level.
 *
 * @param message the format string
 * @param arg    the argument
 *
 */
@JvmSynthetic
inline fun Logger.trace(arg: Any?, crossinline message: () -> String) {
    if (isTraceEnabled)
        trace(message(), arg)
}

/**
 * Log a message at the TRACE level according to the specified format
 * and arguments.
 *
 *
 *
 * This form avoids superfluous object creation when the logger
 * is disabled for the TRACE level.
 *
 * @param message the format string
 * @param arg1   the first argument
 * @param arg2   the second argument
 *
 */
@JvmSynthetic
inline fun Logger.trace(arg1: Any?, arg2: Any?, crossinline message: () -> String) {
    if (isTraceEnabled)
        trace(message(), arg1, arg2)
}

/**
 * Log a message at the TRACE level according to the specified format
 * and arguments.
 *
 *
 *
 * This form avoids superfluous string concatenation when the logger
 * is disabled for the TRACE level. However, this variant incurs the hidden
 * (and relatively small) cost of creating an `Object[]` before invoking the method,
 * even if this logger is disabled for TRACE. The variants taking [one][.trace] and
 * [two][.trace] arguments exist solely in order to avoid this hidden cost.
 *
 * @param message    the format string
 * @param arguments a list of 3 or more arguments
 *
 */
@JvmSynthetic
inline fun Logger.trace(vararg arguments: Any?, crossinline message: () -> String) {
    if (isTraceEnabled)
        trace(message(), arguments)
}

/**
 * Log an exception (throwable) at the TRACE level with an
 * accompanying message.
 *
 * @param message the message accompanying the exception
 * @param throwable   the exception (throwable) to log
 *
 */
@JvmSynthetic
inline fun Logger.trace(throwable: Throwable?, crossinline message: () -> String) {
    if (isTraceEnabled)
        trace(message(), throwable)
}

/**
 * Log a message with the specific Marker at the TRACE level.
 *
 * @param marker the marker data specific to this log statement
 * @param message    the message string to be logged
 *
 */
@JvmSynthetic
inline fun Logger.trace(marker: Marker?, crossinline message: () -> String) {
    if (isTraceEnabled)
        trace(marker, message())
}

/**
 * This method is similar to [.trace] method except that the
 * marker data is also taken into consideration.
 *
 * @param marker the marker data specific to this log statement
 * @param message the format string
 * @param arg    the argument
 *
 */
@JvmSynthetic
inline fun Logger.trace(marker: Marker?, arg: Any?, crossinline message: () -> String) {
    if (isTraceEnabled)
        trace(marker, message(), arg)
}

/**
 * This method is similar to [.trace]
 * method except that the marker data is also taken into
 * consideration.
 *
 * @param marker the marker data specific to this log statement
 * @param message the format string
 * @param arg1   the first argument
 * @param arg2   the second argument
 *
 */
@JvmSynthetic
inline fun Logger.trace(marker: Marker?, arg1: Any?, arg2: Any?, crossinline message: () -> String) {
    if (isTraceEnabled)
        trace(marker, message(), arg1, arg2)
}

/**
 * This method is similar to [.trace]
 * method except that the marker data is also taken into
 * consideration.
 *
 * @param marker   the marker data specific to this log statement
 * @param message   the format string
 * @param argArray an array of arguments
 *
 */
@JvmSynthetic
inline fun Logger.trace(marker: Marker?, vararg argArray: Any?, crossinline message: () -> String) {
    if (isTraceEnabled)
        trace(marker, message(), argArray)
}

/**
 * This method is similar to [.trace] method except that the
 * marker data is also taken into consideration.
 *
 * @param marker the marker data specific to this log statement
 * @param message    the message accompanying the exception
 * @param throwable      the exception (throwable) to log
 *
 */
@JvmSynthetic
inline fun Logger.trace(marker: Marker?, throwable: Throwable?, crossinline message: () -> String) {
    if (isTraceEnabled)
        trace(marker, message(), throwable)
}

/**
 * Log a message at the DEBUG level.
 *
 * @param message the message string to be logged
 */
@JvmSynthetic
inline fun Logger.debug(crossinline message: () -> String) {
    if (isDebugEnabled)
        debug(message())
}

/**
 * Log a message at the DEBUG level according to the specified format
 * and argument.
 *
 *
 *
 * This form avoids superfluous object creation when the logger
 * is disabled for the DEBUG level.
 *
 * @param message the format string
 * @param arg    the argument
 */
@JvmSynthetic
inline fun Logger.debug(arg: Any?, crossinline message: () -> String) {
    if (isDebugEnabled)
        debug(message(), arg)
}

/**
 * Log a message at the DEBUG level according to the specified format
 * and arguments.
 *
 *
 *
 * This form avoids superfluous object creation when the logger
 * is disabled for the DEBUG level.
 *
 * @param message the format string
 * @param arg1   the first argument
 * @param arg2   the second argument
 */
@JvmSynthetic
inline fun Logger.debug(arg1: Any?, arg2: Any?, crossinline message: () -> String) {
    if (isDebugEnabled)
        debug(message(), arg1, arg2)
}

/**
 * Log a message at the DEBUG level according to the specified format
 * and arguments.
 *
 *
 *
 * This form avoids superfluous string concatenation when the logger
 * is disabled for the DEBUG level. However, this variant incurs the hidden
 * (and relatively small) cost of creating an `Object[]` before invoking the method,
 * even if this logger is disabled for DEBUG. The variants taking
 * [one][.debug] and [two][.debug]
 * arguments exist solely in order to avoid this hidden cost.
 *
 * @param message    the format string
 * @param arguments a list of 3 or more arguments
 */
@JvmSynthetic
inline fun Logger.debug(vararg arguments: Any?, crossinline message: () -> String) {
    if (isDebugEnabled)
        debug(message(), arguments)
}

/**
 * Log an exception (throwable) at the DEBUG level with an
 * accompanying message.
 *
 * @param message the message accompanying the exception
 * @param throwable   the exception (throwable) to log
 */
@JvmSynthetic
inline fun Logger.debug(throwable: Throwable?, crossinline message: () -> String) {
    if (isDebugEnabled)
        debug(message(), throwable)
}

/**
 * Log a message with the specific Marker at the DEBUG level.
 *
 * @param marker the marker data specific to this log statement
 * @param message    the message string to be logged
 */
@JvmSynthetic
inline fun Logger.debug(marker: Marker?, crossinline message: () -> String) {
    if (isDebugEnabled)
        debug(marker, message())
}

/**
 * This method is similar to [.debug] method except that the
 * marker data is also taken into consideration.
 *
 * @param marker the marker data specific to this log statement
 * @param message the format string
 * @param arg    the argument
 */
@JvmSynthetic
inline fun Logger.debug(marker: Marker?, arg: Any?, crossinline message: () -> String) {
    if (isDebugEnabled)
        debug(marker, message(), arg)
}

/**
 * This method is similar to [.debug]
 * method except that the marker data is also taken into
 * consideration.
 *
 * @param marker the marker data specific to this log statement
 * @param message the format string
 * @param arg1   the first argument
 * @param arg2   the second argument
 */
@JvmSynthetic
inline fun Logger.debug(marker: Marker?, arg1: Any?, arg2: Any?, crossinline message: () -> String) {
    if (isDebugEnabled)
        debug(marker, message(), arg1, arg2)
}

/**
 * This method is similar to [.debug]
 * method except that the marker data is also taken into
 * consideration.
 *
 * @param marker    the marker data specific to this log statement
 * @param message    the format string
 * @param arguments a list of 3 or more arguments
 */
@JvmSynthetic
inline fun Logger.debug(marker: Marker?, vararg arguments: Any?, crossinline message: () -> String) {
    if (isDebugEnabled)
        debug(marker, message(), arguments)
}

/**
 * This method is similar to [.debug] method except that the
 * marker data is also taken into consideration.
 *
 * @param marker the marker data specific to this log statement
 * @param message    the message accompanying the exception
 * @param throwable      the exception (throwable) to log
 */
@JvmSynthetic
inline fun Logger.debug(marker: Marker?, throwable: Throwable?, crossinline message: () -> String) {
    if (isDebugEnabled)
        debug(marker, message(), throwable)
}

/**
 * Log a message at the INFO level.
 *
 * @param message the message string to be logged
 */
@JvmSynthetic
inline fun Logger.info(crossinline message: () -> String) {
    if (isInfoEnabled)
        info(message())
}

/**
 * Log a message at the INFO level according to the specified format
 * and argument.
 *
 *
 *
 * This form avoids superfluous object creation when the logger
 * is disabled for the INFO level.
 *
 * @param message the format string
 * @param arg    the argument
 */
@JvmSynthetic
inline fun Logger.info(arg: Any?, crossinline message: () -> String) {
    if (isInfoEnabled)
        info(message(), arg)
}

/**
 * Log a message at the INFO level according to the specified format
 * and arguments.
 *
 *
 *
 * This form avoids superfluous object creation when the logger
 * is disabled for the INFO level.
 *
 * @param message the format string
 * @param arg1   the first argument
 * @param arg2   the second argument
 */
@JvmSynthetic
inline fun Logger.info(arg1: Any?, arg2: Any?, crossinline message: () -> String) {
    if (isInfoEnabled)
        info(message(), arg1, arg2)
}

/**
 * Log a message at the INFO level according to the specified format
 * and arguments.
 *
 *
 *
 * This form avoids superfluous string concatenation when the logger
 * is disabled for the INFO level. However, this variant incurs the hidden
 * (and relatively small) cost of creating an `Object[]` before invoking the method,
 * even if this logger is disabled for INFO. The variants taking
 * [one][.info] and [two][.info]
 * arguments exist solely in order to avoid this hidden cost.
 *
 * @param message    the format string
 * @param arguments a list of 3 or more arguments
 */
@JvmSynthetic
inline fun Logger.info(vararg arguments: Any?, crossinline message: () -> String) {
    if (isInfoEnabled)
        info(message(), arguments)
}

/**
 * Log an exception (throwable) at the INFO level with an
 * accompanying message.
 *
 * @param message the message accompanying the exception
 * @param throwable   the exception (throwable) to log
 */
@JvmSynthetic
inline fun Logger.info(throwable: Throwable?, crossinline message: () -> String) {
    if (isInfoEnabled)
        info(message(), throwable)
}

/**
 * Log a message with the specific Marker at the INFO level.
 *
 * @param marker The marker specific to this log statement
 * @param message    the message string to be logged
 */
@JvmSynthetic
inline fun Logger.info(marker: Marker?, crossinline message: () -> String) {
    if (isInfoEnabled)
        info(marker, message())
}

/**
 * This method is similar to [.info] method except that the
 * marker data is also taken into consideration.
 *
 * @param marker the marker data specific to this log statement
 * @param message the format string
 * @param arg    the argument
 */
@JvmSynthetic
inline fun Logger.info(marker: Marker?, arg: Any?, crossinline message: () -> String) {
    if (isInfoEnabled)
        info(marker, message(), arg)
}

/**
 * This method is similar to [.info]
 * method except that the marker data is also taken into
 * consideration.
 *
 * @param marker the marker data specific to this log statement
 * @param message the format string
 * @param arg1   the first argument
 * @param arg2   the second argument
 */
@JvmSynthetic
inline fun Logger.info(marker: Marker?, arg1: Any?, arg2: Any?, crossinline message: () -> String) {
    if (isInfoEnabled)
        info(marker, message(), arg1, arg2)
}

/**
 * This method is similar to [.info]
 * method except that the marker data is also taken into
 * consideration.
 *
 * @param marker    the marker data specific to this log statement
 * @param message    the format string
 * @param arguments a list of 3 or more arguments
 */
@JvmSynthetic
inline fun Logger.info(marker: Marker?, vararg arguments: Any?, crossinline message: () -> String) {
    if (isInfoEnabled)
        info(marker, message(), arguments)
}

/**
 * This method is similar to [.info] method
 * except that the marker data is also taken into consideration.
 *
 * @param marker the marker data for this log statement
 * @param message    the message accompanying the exception
 * @param throwable      the exception (throwable) to log
 */
@JvmSynthetic
inline fun Logger.info(marker: Marker?, throwable: Throwable?, crossinline message: () -> String) {
    if (isInfoEnabled)
        info(marker, message(), throwable)
}

/**
 * Log a message at the WARN level.
 *
 * @param message the message string to be logged
 */
@JvmSynthetic
inline fun Logger.warn(crossinline message: () -> String) {
    if (isWarnEnabled)
        warn(message())
}

/**
 * Log a message at the WARN level according to the specified format
 * and argument.
 *
 *
 *
 * This form avoids superfluous object creation when the logger
 * is disabled for the WARN level.
 *
 * @param message the format string
 * @param arg    the argument
 */
@JvmSynthetic
inline fun Logger.warn(arg: Any?, crossinline message: () -> String) {
    if (isWarnEnabled)
        warn(message(), arg)
}

/**
 * Log a message at the WARN level according to the specified format
 * and arguments.
 *
 *
 *
 * This form avoids superfluous string concatenation when the logger
 * is disabled for the WARN level. However, this variant incurs the hidden
 * (and relatively small) cost of creating an `Object[]` before invoking the method,
 * even if this logger is disabled for WARN. The variants taking
 * [one][.warn] and [two][.warn]
 * arguments exist solely in order to avoid this hidden cost.
 *
 * @param message    the format string
 * @param arguments a list of 3 or more arguments
 */
@JvmSynthetic
inline fun Logger.warn(vararg arguments: Any?, crossinline message: () -> String) {
    if (isWarnEnabled)
        warn(message(), arguments)
}

/**
 * Log a message at the WARN level according to the specified format
 * and arguments.
 *
 *
 *
 * This form avoids superfluous object creation when the logger
 * is disabled for the WARN level.
 *
 * @param message the format string
 * @param arg1   the first argument
 * @param arg2   the second argument
 */
@JvmSynthetic
inline fun Logger.warn(arg1: Any?, arg2: Any?, crossinline message: () -> String) {
    if (isWarnEnabled)
        warn(message(), arg1, arg2)
}

/**
 * Log an exception (throwable) at the WARN level with an
 * accompanying message.
 *
 * @param message the message accompanying the exception
 * @param throwable   the exception (throwable) to log
 */
@JvmSynthetic
inline fun Logger.warn(throwable: Throwable?, crossinline message: () -> String) {
    if (isWarnEnabled)
        warn(message(), throwable)
}

/**
 * Log a message with the specific Marker at the WARN level.
 *
 * @param marker The marker specific to this log statement
 * @param message    the message string to be logged
 */
@JvmSynthetic
inline fun Logger.warn(marker: Marker?, crossinline message: () -> String) {
    if (isWarnEnabled)
        warn(marker, message())
}

/**
 * This method is similar to [.warn] method except that the
 * marker data is also taken into consideration.
 *
 * @param marker the marker data specific to this log statement
 * @param message the format string
 * @param arg    the argument
 */
@JvmSynthetic
inline fun Logger.warn(marker: Marker?, arg: Any?, crossinline message: () -> String) {
    if (isWarnEnabled)
        warn(marker, message(), arg)
}

/**
 * This method is similar to [.warn]
 * method except that the marker data is also taken into
 * consideration.
 *
 * @param marker the marker data specific to this log statement
 * @param message the format string
 * @param arg1   the first argument
 * @param arg2   the second argument
 */
@JvmSynthetic
inline fun Logger.warn(marker: Marker?, arg1: Any?, arg2: Any?, crossinline message: () -> String) {
    if (isWarnEnabled)
        warn(marker, message(), arg1, arg2)
}

/**
 * This method is similar to [.warn]
 * method except that the marker data is also taken into
 * consideration.
 *
 * @param marker    the marker data specific to this log statement
 * @param message    the format string
 * @param arguments a list of 3 or more arguments
 */
@JvmSynthetic
inline fun Logger.warn(marker: Marker?, vararg arguments: Any?, crossinline message: () -> String) {
    if (isWarnEnabled)
        warn(marker, message(), arguments)
}

/**
 * This method is similar to [.warn] method
 * except that the marker data is also taken into consideration.
 *
 * @param marker the marker data for this log statement
 * @param message    the message accompanying the exception
 * @param throwable      the exception (throwable) to log
 */
@JvmSynthetic
inline fun Logger.warn(marker: Marker?, throwable: Throwable?, crossinline message: () -> String) {
    if (isWarnEnabled)
        warn(marker, message(), throwable)
}

/**
 * Log a message at the ERROR level.
 *
 * @param message the message string to be logged
 */
@JvmSynthetic
inline fun Logger.error(crossinline message: () -> String) {
    if (isErrorEnabled)
        error(message())
}

/**
 * Log a message at the ERROR level according to the specified format
 * and argument.
 *
 *
 *
 * This form avoids superfluous object creation when the logger
 * is disabled for the ERROR level.
 *
 * @param message the format string
 * @param arg    the argument
 */
@JvmSynthetic
inline fun Logger.error(arg: Any?, crossinline message: () -> String) {
    if (isErrorEnabled)
        error(message(), arg)
}

/**
 * Log a message at the ERROR level according to the specified format
 * and arguments.
 *
 *
 *
 * This form avoids superfluous object creation when the logger
 * is disabled for the ERROR level.
 *
 * @param message the format string
 * @param arg1   the first argument
 * @param arg2   the second argument
 */
@JvmSynthetic
inline fun Logger.error(arg1: Any?, arg2: Any?, crossinline message: () -> String) {
    if (isErrorEnabled)
        error(message(), arg1, arg2)
}

/**
 * Log a message at the ERROR level according to the specified format
 * and arguments.
 *
 *
 *
 * This form avoids superfluous string concatenation when the logger
 * is disabled for the ERROR level. However, this variant incurs the hidden
 * (and relatively small) cost of creating an `Object[]` before invoking the method,
 * even if this logger is disabled for ERROR. The variants taking
 * [one][.error] and [two][.error]
 * arguments exist solely in order to avoid this hidden cost.
 *
 * @param message    the format string
 * @param arguments a list of 3 or more arguments
 */
@JvmSynthetic
inline fun Logger.error(vararg arguments: Any?, crossinline message: () -> String) {
    if (isErrorEnabled)
        error(message(), arguments)
}

/**
 * Log an exception (throwable) at the ERROR level with an
 * accompanying message.
 *
 * @param message the message accompanying the exception
 * @param throwable   the exception (throwable) to log
 */
@JvmSynthetic
inline fun Logger.error(throwable: Throwable?, crossinline message: () -> String) {
    if (isErrorEnabled)
        error(message(), throwable)
}

/**
 * Log a message with the specific Marker at the ERROR level.
 *
 * @param marker The marker specific to this log statement
 * @param message    the message string to be logged
 */
@JvmSynthetic
inline fun Logger.error(marker: Marker?, crossinline message: () -> String) {
    if (isErrorEnabled)
        error(marker, message())
}

/**
 * This method is similar to [.error] method except that the
 * marker data is also taken into consideration.
 *
 * @param marker the marker data specific to this log statement
 * @param message the format string
 * @param arg    the argument
 */
@JvmSynthetic
inline fun Logger.error(marker: Marker?, arg: Any?, crossinline message: () -> String) {
    if (isErrorEnabled)
        error(marker, message(), arg)
}

/**
 * This method is similar to [.error]
 * method except that the marker data is also taken into
 * consideration.
 *
 * @param marker the marker data specific to this log statement
 * @param message the format string
 * @param arg1   the first argument
 * @param arg2   the second argument
 */
@JvmSynthetic
inline fun Logger.error(marker: Marker?, arg1: Any?, arg2: Any?, crossinline message: () -> String) {
    if (isErrorEnabled)
        error(marker, message(), arg1, arg2)
}

/**
 * This method is similar to [.error]
 * method except that the marker data is also taken into
 * consideration.
 *
 * @param marker    the marker data specific to this log statement
 * @param message    the format string
 * @param arguments a list of 3 or more arguments
 */
@JvmSynthetic
inline fun Logger.error(marker: Marker?, vararg arguments: Any?, crossinline message: () -> String) {
    if (isErrorEnabled)
        error(marker, message(), arguments)
}

/**
 * This method is similar to [.error]
 * method except that the marker data is also taken into
 * consideration.
 *
 * @param marker the marker data specific to this log statement
 * @param message    the message accompanying the exception
 * @param throwable      the exception (throwable) to log
 */
@JvmSynthetic
inline fun Logger.error(marker: Marker?, throwable: Throwable?, crossinline message: () -> String) {
    if (isErrorEnabled)
        error(marker, message(), throwable)
}


