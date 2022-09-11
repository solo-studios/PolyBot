/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyAnnotationDslImpl.kt is part of PolyBot
 * Last modified on 11-09-2022 07:01 p.m.
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

import ca.solostudios.polybot.api.cloud.CommandContext
import ca.solostudios.polybot.api.cloud.CommandManager
import ca.solostudios.polybot.api.commands.PolyCommand
import ca.solostudios.polybot.api.plugin.dsl.command.AnnotationMapper
import ca.solostudios.polybot.api.plugin.dsl.command.AnnotationParser
import ca.solostudios.polybot.api.plugin.dsl.command.BuilderModifier
import ca.solostudios.polybot.api.plugin.dsl.command.ExecutionMethodFactory
import ca.solostudios.polybot.api.plugin.dsl.command.PolyAnnotationDsl
import ca.solostudios.polybot.api.plugin.dsl.command.PreprocessorMapper
import cloud.commandframework.kotlin.extension.toMutable
import java.lang.reflect.Method
import java.util.Queue
import java.util.function.BiFunction
import kotlin.reflect.KClass

internal class PolyAnnotationDslImpl(
        val cloud: CommandManager,
        val annotationParser: AnnotationParser
                                    ) : PolyAnnotationDsl {
    val scanPackages: MutableList<String> = mutableListOf()
    val commands: MutableList<PolyCommand> = mutableListOf()
    val commandInstantiationMethods: MutableList<CommandInstantiationMethod<*>> = mutableListOf()
    
    override fun commandExecutionMethod(shouldUse: (Method) -> Boolean, executionMethod: ExecutionMethodFactory) {
        annotationParser.registerCommandExecutionMethodFactory(shouldUse, executionMethod)
    }
    
    override fun <A : Annotation> builderModifier(clazz: KClass<A>, modifier: BuilderModifier<A>) {
        annotationParser.registerBuilderModifier(clazz.java) { annotation, commandBuilder ->
            val mutableCommandBuilder = commandBuilder.toMutable(cloud)
            modifier(mutableCommandBuilder, annotation)
            mutableCommandBuilder.commandBuilder
        }
    }
    
    override fun <A : Annotation> annotationMapper(clazz: KClass<A>, mapper: AnnotationMapper<A>) {
        annotationParser.registerAnnotationMapper(clazz.java, mapper)
    }
    
    override fun <A : Annotation> preprocessorMapper(clazz: KClass<A>, mapper: PreprocessorMapper<A>) {
        annotationParser.registerPreprocessorMapper(clazz.java) { annotation ->
            val preprocessor = mapper(annotation)
            
            BiFunction { context: CommandContext, queue: Queue<String> ->
                preprocessor(context, queue)
            }
        }
    }
    
    override fun <T : PolyCommand> commandInstantiationMethod(shouldUse: (KClass<T>) -> Boolean, instantiationMethod: (KClass<T>) -> T) {
        commandInstantiationMethods.add(CommandInstantiationMethod(shouldUse, instantiationMethod))
    }
    
    override fun scanPackage(vararg packages: String) {
        scanPackages.addAll(packages)
    }
    
    override fun command(vararg commands: PolyCommand) {
        this.commands.addAll(commands)
    }
    
    internal data class CommandInstantiationMethod<T : Any>(
            val shouldUse: (KClass<T>) -> Boolean,
            val instantiationMethod: (KClass<T>) -> T
                                                           )
}