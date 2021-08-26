/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file date-time.kt is part of PolyhedralBot
 * Last modified on 25-08-2021 10:42 p.m.
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

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.time.Duration

val dayMonthYearFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME

fun footerDate(millis: Long, separator: String = " • ") = footerDate(Instant.ofEpochMilli(millis), separator)

fun footerDate(instant: Instant, separator: String = " • ") = footerDate(instant.atOffset(ZoneOffset.UTC), separator)

fun footerDate(time: OffsetDateTime, separator: String = " • ") = " $separator ${time.format(dayMonthYearFormatter)}"

fun StringBuilder.footerDate(millis: Long, separator: String = " • "): StringBuilder = footerDate(Instant.ofEpochMilli(millis), separator)

fun StringBuilder.footerDate(instant: Instant, separator: String = " • "): StringBuilder =
        footerDate(instant.atOffset(ZoneOffset.UTC), separator)

fun StringBuilder.footerDate(time: OffsetDateTime, separator: String = " • "): StringBuilder =
        append(separator).append(time.format(dayMonthYearFormatter))


private const val nanosPerMicro = 1000
private const val microsPerMillis = 1000
private const val millisPerSecond = 1000
private const val secondsPerMinute = 60
private const val minutesPerHour = 60
private const val hoursPerDay = 24

fun Duration.shortFormat(): String {
    return when {
        days > 0    -> "${days}d${hours}h${minutes}m${seconds}s"
        hours > 0   -> "${hours}h${minutes}m${seconds}s${millis}ms"
        minutes > 0 -> "${minutes}m${seconds}s${millis}ms${micros}μs"
        seconds > 0 -> "${seconds}s${millis}ms${micros}μs${nanos}ns"
        millis > 0  -> "${millis}ms${micros}μs${nanos}ns"
        micros > 0  -> "${micros}μs${nanos}ns"
        else        -> "${nanos}ns"
    }
}

fun Duration.longFormat(): String {
    return when {
        days > 0    -> "$days days, $hours hours, $minutes minutes"
        hours > 0   -> "$hours hours, $minutes minutes, $seconds seconds"
        minutes > 0 -> "$minutes minutes, $seconds seconds, $millis milliseconds"
        seconds > 0 -> "$seconds seconds, $millis milliseconds, $micros microseconds"
        millis > 0  -> "$millis milliseconds, $micros microseconds, $nanos nanoseconds"
        micros > 0  -> "$micros microseconds, $nanos nanoseconds"
        else        -> "$nanos nanoseconds"
    }
}

val Duration.days
    get() = inWholeDays

val Duration.hours
    get() = inWholeHours % hoursPerDay

val Duration.minutes
    get() = inWholeMinutes % minutesPerHour

val Duration.seconds
    get() = inWholeSeconds % secondsPerMinute

val Duration.millis
    get() = inWholeMilliseconds % millisPerSecond

val Duration.micros
    get() = inWholeMicroseconds % microsPerMillis

val Duration.nanos
    get() = inWholeNanoseconds % nanosPerMicro