/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyCommandDslImpl.kt is part of PolyBot
 * Last modified on 23-11-2022 12:32 p.m.
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

import ca.solostudios.polybot.api.annotations.PolyPluginDelicateApi
import ca.solostudios.polybot.api.cloud.CaptionRegistry
import ca.solostudios.polybot.api.cloud.CommandPostprocessor
import ca.solostudios.polybot.api.cloud.CommandPreprocessor
import ca.solostudios.polybot.api.cloud.CommandSyntaxFormatter
import ca.solostudios.polybot.api.cloud.InjectionService
import ca.solostudios.polybot.api.cloud.ParameterInjector
import ca.solostudios.polybot.api.plugin.dsl.command.ExceptionHandler
import ca.solostudios.polybot.api.plugin.dsl.command.PolyAnnotationDsl
import ca.solostudios.polybot.api.plugin.dsl.command.PolyCommandDsl
import ca.solostudios.polybot.api.plugin.dsl.command.PolyParserRegistryDsl
import kotlin.reflect.KClass

internal class PolyCommandDslImpl : PolyCommandDsl {
    val annotationDsl = PolyAnnotationDslImpl()
    val parserRegistryDslImpl = PolyParserRegistryDslImpl()
    val commandPreprocessors = mutableListOf<CommandPreprocessor>()
    val commandPostProcessors = mutableListOf<CommandPostprocessor>()
    val exceptionHandlers = mutableListOf<ExceptionHandlerHolder<*>>()
    val parameterInjectors = mutableListOf<ParameterInjectorHolder<*>>()
    val parameterInjectionServices = mutableListOf<InjectionService>()
    
    @PolyPluginDelicateApi
    override var commandSyntaxFormatter: CommandSyntaxFormatter? = null
    
    @PolyPluginDelicateApi
    override var captionRegistry: CaptionRegistry? = null
    
    override fun annotations(block: PolyAnnotationDsl.() -> Unit) {
        annotationDsl.block()
    }
    
    override fun parserRegistry(block: PolyParserRegistryDsl.() -> Unit) {
        parserRegistryDslImpl.block()
    }
    
    override fun commandPreProcessor(preProcessor: CommandPreprocessor) {
        commandPreprocessors += preProcessor
    }
    
    override fun commandPostProcessor(postProcessor: CommandPostprocessor) {
        commandPostProcessors += commandPostProcessors
    }
    
    override fun commandPreProcessors(preProcessors: List<CommandPreprocessor>) {
        commandPreprocessors += preProcessors
    }
    
    override fun commandPostProcessors(postProcessors: List<CommandPostprocessor>) {
        commandPostProcessors += commandPostProcessors
    }
    
    override fun <E : Exception> exceptionHandler(clazz: KClass<E>, handler: ExceptionHandler<E>) {
        exceptionHandlers += ExceptionHandlerHolder(clazz, handler)
    }
    
    override fun <T : Any> injector(clazz: KClass<T>, injector: ParameterInjector<T>) {
        parameterInjectors += ParameterInjectorHolder(clazz, injector)
        // cloud.parameterInjectorRegistry().registerInjector(clazz.java, injector)
    }
    
    override fun injectionService(service: InjectionService) {
        parameterInjectionServices += service
        // cloud.parameterInjectorRegistry().registerInjectionService(service)
    }
    
    data class ExceptionHandlerHolder<E : Exception>(
            val exceptionType: KClass<E>,
            val handler: ExceptionHandler<E>,
                                                    )
    
    data class ParameterInjectorHolder<T : Any>(
            val parameterType: KClass<T>,
            val injector: ParameterInjector<T>,
                                               )
}