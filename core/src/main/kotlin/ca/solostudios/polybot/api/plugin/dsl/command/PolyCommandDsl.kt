/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyCommandDsl.kt is part of PolyBot
 * Last modified on 23-11-2022 12:29 p.m.
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

import ca.solostudios.polybot.api.annotations.PolyPluginDelicateApi
import ca.solostudios.polybot.api.annotations.PolyPluginDslMarker
import ca.solostudios.polybot.api.cloud.CaptionRegistry
import ca.solostudios.polybot.api.cloud.CommandPostprocessor
import ca.solostudios.polybot.api.cloud.CommandPreprocessor
import ca.solostudios.polybot.api.cloud.CommandSyntaxFormatter
import ca.solostudios.polybot.api.cloud.InjectionService
import ca.solostudios.polybot.api.cloud.ParameterInjector
import cloud.commandframework.CommandManager
import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.annotations.injection.ParameterInjectorRegistry
import cloud.commandframework.arguments.parser.ParserRegistry
import kotlin.reflect.KClass


@PolyPluginDslMarker
public interface PolyCommandDsl {
    /**
     * The command syntax formatter.
     */
    @PolyPluginDelicateApi
    public var commandSyntaxFormatter: CommandSyntaxFormatter?
    
    /**
     * Caption registry
     */
    @PolyPluginDelicateApi
    public var captionRegistry: CaptionRegistry?
    
    /**
     * Configures the [AnnotationParser] using the DSL.
     *
     * @param block The configuration block for the annotation parser.
     */
    public fun annotations(block: PolyAnnotationDsl.() -> Unit)
    
    /**
     * Configures the [ParserRegistry] using the DSL.
     *
     * @param block The configuration block for the parser registry.
     */
    public fun parserRegistry(block: PolyParserRegistryDsl.() -> Unit)
    
    /**
     * Registers a new command preprocessor. The order they are registered in is respected, and they are called in LIFO order.
     *
     * @param preProcessor The preprocessor to register.
     *
     * @see CommandManager.registerCommandPreProcessor
     */
    public fun commandPreProcessor(preProcessor: CommandPreprocessor)
    
    /**
     * Registers a several new command preprocessors. The order they are registered in is respected, and they are called in LIFO order.
     *
     * @param preProcessors The preprocessors to register.
     *
     * @see CommandManager.registerCommandPreProcessor
     */
    public fun commandPreProcessors(preProcessors: List<CommandPreprocessor>)
    
    /**
     * Registers a new command postprocessor. The order they are registered in is respected, and they are called in LIFO order.
     *
     * @param postProcessor The postprocessor to register.
     *
     * @see CommandManager.registerCommandPostProcessor
     */
    public fun commandPostProcessor(postProcessor: CommandPostprocessor)
    
    /**
     * Registers a several new command postprocessors. The order they are registered in is respected, and they are called in LIFO order.
     *
     * @param postProcessors The postprocessor(s) to register.
     *
     * @see CommandManager.registerCommandPostProcessor
     */
    public fun commandPostProcessors(postProcessors: List<CommandPostprocessor>)
    
    /**
     *Registers an exception handler for an exception type.
     * This will then be used when [CommandManager.handleException] is called for the particular exception type.
     *
     * @param E The exception type.
     * @param clazz The exception class.
     * @param handler The exception handler.
     *
     * @see CommandManager.registerExceptionHandler
     */
    public fun <E : Exception> exceptionHandler(clazz: KClass<E>, handler: ExceptionHandler<E>)
    
    /**
     * Registers an injector for a particular type.
     *
     * @param T The type that the injector should inject for.
     * @param clazz The type clazz that the injector will inject for. This type will be matched using [Class.isAssignableFrom].
     * @param injector The injector that should inject the value into the command method.
     *
     * @see ParameterInjectorRegistry.registerInjector
     */
    public fun <T : Any> injector(clazz: KClass<T>, injector: ParameterInjector<T>)
    
    /**
     * Registers an injection service that will be able to provide injections using [ParameterInjectorRegistry.getInjectable].
     *
     * @param service The service implementation.
     *
     * @see ParameterInjectorRegistry.registerInjectionService
     */
    public fun injectionService(service: InjectionService)
}