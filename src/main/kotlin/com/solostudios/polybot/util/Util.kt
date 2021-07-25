/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Util.kt is part of PolyhedralBot
 * Last modified on 24-07-2021 08:24 p.m.
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

import cloud.commandframework.CommandManager
import cloud.commandframework.annotations.AnnotationAccessor
import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.annotations.injection.ParameterInjectorRegistry
import cloud.commandframework.arguments.parser.ArgumentParser
import cloud.commandframework.arguments.parser.ParserParameters
import cloud.commandframework.arguments.parser.ParserRegistry
import cloud.commandframework.context.CommandContext
import cloud.commandframework.meta.CommandMeta
import io.leangen.geantyref.TypeToken
import java.util.EnumSet
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.managers.Presence
import net.dv8tion.jda.api.utils.MemberCachePolicy
import kotlin.concurrent.thread
import kotlin.text.padStart as ktPadStart

inline fun <T> stringIfNotNull(fromObject: T?, transform: (T) -> String): String = if (fromObject != null) transform(fromObject) else ""

fun Any.padStart(length: Int, padChar: Char): String = this.toString().ktPadStart(length, padChar)

inline fun <reified T : Enum<T>> enumSetOf(vararg elems: T): EnumSet<T> = EnumSet.noneOf(T::class.java).apply { addAll(elems) }

infix fun MemberCachePolicy.or(policy: MemberCachePolicy): MemberCachePolicy {
    return policy.or(policy)
}

infix fun MemberCachePolicy.and(policy: MemberCachePolicy): MemberCachePolicy {
    return policy.and(policy)
}

fun <C> AnnotationParser<C>.parse(vararg instances: Any) {
    for (instance in instances)
        parse(instance)
}

var Presence.onlineStatus: OnlineStatus
    set(value) = setStatus(value)
    get() = status

inline fun <reified T, C> ParserRegistry<C>.registerParserSupplier(noinline supplier: (ParserParameters) -> ArgumentParser<C, T>) {
    registerParserSupplier(TypeToken.get(T::class.java), supplier)
}

inline fun <reified T, C> ParserRegistry<C>.registerParserSupplier(supplier: ArgumentParser<C, T>) {
    registerParserSupplier(TypeToken.get(T::class.java)) { supplier }
}

inline fun <reified T, C> ParameterInjectorRegistry<C>.registerInjector(noinline function: (context: CommandContext<C>, annotationAccessor: AnnotationAccessor) -> T) {
    registerInjector(T::class.java, function)
}

inline fun <reified C> AnnotationParser(commandManager: CommandManager<C>, noinline metaMapper: (ParserParameters) -> CommandMeta) =
        AnnotationParser(commandManager, C::class.java, metaMapper)

fun onJvmShutdown(name: String, block: () -> Unit) {
    Runtime.getRuntime().addShutdownHook(thread(start = false, isDaemon = true, name = name, block = block))
}