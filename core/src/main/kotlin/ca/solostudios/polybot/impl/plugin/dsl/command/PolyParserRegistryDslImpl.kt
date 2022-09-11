/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyParserRegistryDslImpl.kt is part of PolyBot
 * Last modified on 11-09-2022 06:49 p.m.
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

package ca.solostudios.polybot.impl.plugin.dsl.command

import ca.solostudios.polybot.api.cloud.CommandManager
import ca.solostudios.polybot.api.cloud.event.MessageEvent
import ca.solostudios.polybot.api.plugin.dsl.command.ParserSupplier
import ca.solostudios.polybot.api.plugin.dsl.command.PolyParserRegistryDsl
import ca.solostudios.polybot.api.plugin.dsl.command.SuggestionProvider
import ca.solostudios.polybot.api.plugin.dsl.command.TypedAnnotationMapper
import cloud.commandframework.arguments.parser.ParserRegistry
import io.leangen.geantyref.TypeToken
import kotlin.reflect.KClass

internal class PolyParserRegistryDslImpl(
        val cloud: CommandManager
                                        ) : PolyParserRegistryDsl {
    val parserRegistry: ParserRegistry<MessageEvent>
        get() = cloud.parserRegistry()
    
    override fun <T> parserSupplier(type: TypeToken<T>, supplier: ParserSupplier<T>) {
        parserRegistry.registerParserSupplier(type, supplier)
    }
    
    override fun namedParserSupplier(name: String, supplier: ParserSupplier<*>) {
        parserRegistry.registerNamedParserSupplier(name, supplier)
    }
    
    override fun <A : Annotation, T> annotationMapper(clazz: KClass<A>, mapper: TypedAnnotationMapper<A>) {
        parserRegistry.registerAnnotationMapper<A, T>(clazz.java, mapper)
    }
    
    override fun suggestionProvider(name: String, suggestionsProvider: SuggestionProvider) {
        parserRegistry.registerSuggestionProvider(name, suggestionsProvider)
    }
}