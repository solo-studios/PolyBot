/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file cloud.kt is part of PolyhedralBot
 * Last modified on 09-10-2021 10:30 p.m.
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

import cloud.commandframework.CommandManager
import cloud.commandframework.CommandTree
import cloud.commandframework.annotations.AnnotationAccessor
import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.annotations.injection.ParameterInjectorRegistry
import cloud.commandframework.arguments.parser.ArgumentParser
import cloud.commandframework.arguments.parser.ParserParameters
import cloud.commandframework.arguments.parser.ParserRegistry
import cloud.commandframework.context.CommandContext
import cloud.commandframework.execution.postprocessor.CommandPostprocessor
import cloud.commandframework.execution.preprocessor.CommandPreprocessor
import cloud.commandframework.meta.CommandMeta
import cloud.commandframework.types.tuples.Triplet
import io.leangen.geantyref.TypeToken

fun <C> AnnotationParser<C>.parseCommands(vararg instances: Any) {
    for (instance in instances)
        parse(instance)
}

fun <C> AnnotationParser<C>.parseCommands(instances: List<Any>) {
    for (instance in instances)
        parse(instance)
}

fun <C> CommandManager<C>.registerCommandPreProcessors(vararg preProcessors: CommandPreprocessor<C>) {
    for (preProcessor in preProcessors)
        registerCommandPreProcessor(preProcessor)
}

fun <C> CommandManager<C>.registerCommandPostProcessors(vararg postProcessors: CommandPostprocessor<C>) {
    for (postProcessor in postProcessors)
        registerCommandPostProcessor(postProcessor)
}

// inline fun <reified T, C> ParserRegistry<C>.registerParserSupplier(noinline supplier: (ParserParameters) -> ArgumentParser<C, T>) {
//     registerParserSupplier(TypeToken.get(T::class.java), supplier)
// }

inline fun <reified T, C> ParserRegistry<C>.registerParserSupplier(supplier: ArgumentParser<C, T>) {
    registerParserSupplier(TypeToken.get(T::class.java)) { supplier }
}

inline fun <reified T, C> ParameterInjectorRegistry<C>.registerInjector(noinline function: (context: CommandContext<C>, annotationAccessor: AnnotationAccessor) -> T) {
    registerInjector(T::class.java, function)
}

inline fun <reified C> AnnotationParser(commandManager: CommandManager<C>, noinline metaMapper: (ParserParameters) -> CommandMeta) =
        AnnotationParser(commandManager, C::class.java, metaMapper)

val CommandManager<*>.commandCount: Int
    get() = commandHelpHandler.allCommands.size


private fun CommandTree.Node<*>.count(): Int {
    return children.size + children.sumOf {
        it.count()
    }
}

operator fun <U, V, W> Triplet<U, V, W>.component3(): W = this.third

operator fun <U, V, W> Triplet<U, V, W>.component2(): V = this.second

operator fun <U, V, W> Triplet<U, V, W>.component1(): U = this.first