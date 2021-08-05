/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file date-time.kt is part of PolyhedralBot
 * Last modified on 04-08-2021 11:21 p.m.
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

val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME

fun footerDate(millis: Long, separator: String = " • ") = footerDate(Instant.ofEpochMilli(millis), separator)

fun footerDate(instant: Instant, separator: String = " • ") = footerDate(instant.atOffset(ZoneOffset.UTC), separator)

fun footerDate(time: OffsetDateTime, separator: String = " • ") = " $separator ${time.format(dateFormatter)}"

fun StringBuilder.footerDate(millis: Long, separator: String = " • "): StringBuilder = footerDate(Instant.ofEpochMilli(millis), separator)

fun StringBuilder.footerDate(instant: Instant, separator: String = " • "): StringBuilder =
        footerDate(instant.atOffset(ZoneOffset.UTC), separator)

fun StringBuilder.footerDate(time: OffsetDateTime, separator: String = " • "): StringBuilder =
        append(separator).append(time.format(dateFormatter))


fun Duration.shortFormat(): String {
    return when {
        inWholeDays > 0         -> "${inWholeDays}d${inWholeHours}h${inWholeMinutes}m${inWholeSeconds}s"
        inWholeHours > 0        -> "${inWholeHours}h${inWholeMinutes}m${inWholeSeconds}s${inWholeMilliseconds}ms"
        inWholeMinutes > 0      -> "${inWholeMinutes}m${inWholeSeconds}s${inWholeMilliseconds}ms${inWholeMicroseconds}μs"
        inWholeSeconds > 0      -> "${inWholeSeconds}s${inWholeMilliseconds}ms${inWholeMicroseconds}μs${inWholeNanoseconds}ns"
        inWholeMilliseconds > 0 -> "${inWholeMilliseconds}ms${inWholeMicroseconds}μs${inWholeNanoseconds}ns"
        inWholeMicroseconds > 0 -> "${inWholeMicroseconds}μs${inWholeNanoseconds}ns"
        else                    -> "${inWholeNanoseconds}ns"
    }
}
