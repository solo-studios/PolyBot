/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyParserRegistryDsl.kt is part of PolyBot
 * Last modified on 10-06-2022 11:33 a.m.
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

package ca.solostudios.polybot.api.plugin.dsl.command

import ca.solostudios.polybot.api.annotations.PolyPluginDslMarker
import cloud.commandframework.arguments.parser.ParserRegistry
import cloud.commandframework.arguments.parser.StandardParameters
import io.leangen.geantyref.TypeToken
import org.kodein.di.DIAware
import kotlin.reflect.KClass

@PolyPluginDslMarker
public interface PolyParserRegistryDsl : DIAware {
    /**
     * Registers a parser supplier.
     *
     * @param T The type that is parsed by the parser.
     * @param type The type instance that is parsed by the parser.
     * @param supplier The function that generates the parser.
     * The map supplied may contain parameters used to configure the parser, many of which are documented in [StandardParameters]
     *
     * @see ParserRegistry.registerParserSupplier
     */
    public fun <T : Any?> parserSupplier(type: TypeToken<T>, supplier: ParserSupplier<T>)
    
    /**
     * Registers a named parser supplier.
     *
     * @param name The parser name.
     * @param supplier The function that generates the parser.
     * The map supplied may contain parameters used to configure the parser, many of which are documented in [StandardParameters]
     *
     * @see ParserRegistry.registerNamedParserSupplier
     */
    public fun namedParserSupplier(name: String, supplier: ParserSupplier<*>)
    
    /**
     * Registers a mapper that maps annotation instances to a map of parameter-object pairs.
     *
     * @param A The annotation type.
     * @param clazz The annotation class.
     * @param mapper The ampper that maps the annotations and the type to be parsed to a map of parameter-object pairs.
     *
     * @see ParserRegistry.registerAnnotationMapper
     */
    public fun <A : Annotation> annotationMapper(clazz: KClass<A>, mapper: TypedAnnotationMapper<A>)
    
    /**
     * Registers a new named suggestion provider.
     *
     * @param name The name of the suggestions provider. The name is case-insensitive.
     * @param suggestionsProvider The suggestions provider.
     *
     * @see ParserRegistry.registerSuggestionProvider
     */
    public fun suggestionProvider(name: String, suggestionsProvider: SuggestionProvider)
}