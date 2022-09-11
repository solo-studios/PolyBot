/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyCommandDslImpl.kt is part of PolyBot
 * Last modified on 11-09-2022 07:03 p.m.
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
import ca.solostudios.polybot.api.cloud.CommandManager
import ca.solostudios.polybot.api.cloud.CommandPostprocessor
import ca.solostudios.polybot.api.cloud.CommandPreprocessor
import ca.solostudios.polybot.api.cloud.CommandSyntaxFormatter
import ca.solostudios.polybot.api.cloud.InjectionService
import ca.solostudios.polybot.api.cloud.ParameterInjector
import ca.solostudios.polybot.api.cloud.event.MessageEvent
import ca.solostudios.polybot.api.plugin.dsl.command.ExceptionHandler
import ca.solostudios.polybot.api.plugin.dsl.command.PolyAnnotationDsl
import ca.solostudios.polybot.api.plugin.dsl.command.PolyCommandDsl
import ca.solostudios.polybot.api.plugin.dsl.command.PolyParserRegistryDsl
import cloud.commandframework.annotations.AnnotationParser
import kotlin.reflect.KClass

internal class PolyCommandDslImpl(
        val cloud: CommandManager,
        annotationParser: AnnotationParser<MessageEvent>,
                                 ) : PolyCommandDsl {
    @PolyPluginDelicateApi
    override var commandSyntaxFormatter: CommandSyntaxFormatter
        get() = cloud.commandSyntaxFormatter()
        set(value) = cloud.commandSyntaxFormatter(value)
    
    @PolyPluginDelicateApi
    override var captionRegistry: CaptionRegistry
        get() = cloud.captionRegistry()
        set(value) = cloud.captionRegistry(value)
    
    val annotationDsl: PolyAnnotationDslImpl = PolyAnnotationDslImpl(cloud, annotationParser)
    
    val parserRegistryDslImpl: PolyParserRegistryDslImpl = PolyParserRegistryDslImpl(cloud)
    
    override fun annotations(block: PolyAnnotationDsl.() -> Unit) {
        annotationDsl.block()
    }
    
    override fun parserRegistry(block: PolyParserRegistryDsl.() -> Unit) {
        parserRegistryDslImpl.block()
    }
    
    override fun commandPreProcessor(vararg preProcessors: CommandPreprocessor) {
        for (preProcessor in preProcessors) {
            cloud.registerCommandPreProcessor(preProcessor)
        }
    }
    
    override fun commandPostProcessor(vararg postProcessors: CommandPostprocessor) {
        for (postProcessor in postProcessors) {
            cloud.registerCommandPostProcessor(postProcessor)
        }
    }
    
    override fun <E : Exception> exceptionHandler(clazz: KClass<E>, handler: ExceptionHandler<E>) {
        cloud.registerExceptionHandler(clazz.java, handler)
    }
    
    override fun <T : Any> injector(clazz: KClass<T>, injector: ParameterInjector<T>) {
        cloud.parameterInjectorRegistry().registerInjector(clazz.java, injector)
    }
    
    override fun injectionService(service: InjectionService) {
        cloud.parameterInjectorRegistry().registerInjectionService(service)
    }
}