/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file cloud.kt is part of PolyhedralBot
 * Last modified on 31-07-2021 01:11 a.m.
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

fun <C> AnnotationParser<C>.parse(vararg instances: Any) {
    for (instance in instances)
        parse(instance)
}

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