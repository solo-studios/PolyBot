/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyParserRegistryDslImpl.kt is part of PolyBot
 * Last modified on 23-11-2022 12:26 p.m.
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

import ca.solostudios.polybot.api.plugin.dsl.command.ParserSupplier
import ca.solostudios.polybot.api.plugin.dsl.command.PolyParserRegistryDsl
import ca.solostudios.polybot.api.plugin.dsl.command.SuggestionProvider
import ca.solostudios.polybot.api.plugin.dsl.command.TypedAnnotationMapper
import io.leangen.geantyref.TypeToken
import kotlin.reflect.KClass

internal class PolyParserRegistryDslImpl : PolyParserRegistryDsl {
    val parserSuppliers = mutableListOf<ParserSupplierHolder<*>>()
    val namedParserSuppliers = mutableListOf<NamedParserSupplierHolder>()
    val annotationMappers = mutableListOf<AnnotationMapperHolder<*>>()
    val suggestionProviders = mutableListOf<SuggestionProviderHolder>()
    
    override fun <T> parserSupplier(type: TypeToken<T>, supplier: ParserSupplier<T>) {
        parserSuppliers += ParserSupplierHolder(type, supplier)
        // parserRegistry.registerParserSupplier(type, supplier)
    }
    
    override fun namedParserSupplier(name: String, supplier: ParserSupplier<*>) {
        namedParserSuppliers += NamedParserSupplierHolder(name, supplier)
        // parserRegistry.registerNamedParserSupplier(name, supplier)
    }
    
    override fun <A : Annotation> annotationMapper(annotationType: KClass<A>, mapper: TypedAnnotationMapper<A>) {
        annotationMappers += AnnotationMapperHolder(annotationType, mapper)
        // parserRegistry.registerAnnotationMapper<A, Any>(annotationType.java, mapper)
    }
    
    override fun suggestionProvider(name: String, suggestionsProvider: SuggestionProvider) {
        suggestionProviders += SuggestionProviderHolder(name, suggestionsProvider)
        // parserRegistry.registerSuggestionProvider(name, suggestionsProvider)
    }
    
    data class ParserSupplierHolder<T>(
            val type: TypeToken<T>,
            val supplier: ParserSupplier<T>,
                                      )
    
    data class NamedParserSupplierHolder(
            val name: String,
            val supplier: ParserSupplier<*>,
                                        )
    
    data class AnnotationMapperHolder<A : Annotation>(
            val annotationType: KClass<A>,
            val mapper: TypedAnnotationMapper<A>,
                                                     )
    
    data class SuggestionProviderHolder(
            val name: String,
            val suggestionProvider: SuggestionProvider,
                                       )
}