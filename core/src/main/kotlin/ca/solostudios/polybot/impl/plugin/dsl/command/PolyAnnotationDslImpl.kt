/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyAnnotationDslImpl.kt is part of PolyBot
 * Last modified on 23-11-2022 12:17 p.m.
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

import ca.solostudios.polybot.api.commands.PolyCommand
import ca.solostudios.polybot.api.plugin.dsl.command.AnnotationMapper
import ca.solostudios.polybot.api.plugin.dsl.command.BuilderModifier
import ca.solostudios.polybot.api.plugin.dsl.command.ExecutionMethodFactory
import ca.solostudios.polybot.api.plugin.dsl.command.PolyAnnotationDsl
import ca.solostudios.polybot.api.plugin.dsl.command.PreprocessorMapper
import java.lang.reflect.Method
import kotlin.reflect.KClass

internal class PolyAnnotationDslImpl : PolyAnnotationDsl {
    val cmdExecMethodFactories = mutableListOf<CommandExecutionMethod>()
    val builderModifierHolders = mutableListOf<BuilderModifierHolder<*>>()
    val annotationMapperHolders = mutableListOf<AnnotationMapperHolder<*>>()
    val preprocessorMapperHolders = mutableListOf<PreprocessorMapperHolder<*>>()
    val scanPackages = mutableListOf<String>()
    val commands = mutableListOf<PolyCommand>()
    val commandInstantiationMethods = mutableListOf<CommandInstantiationMethod<*>>()
    
    override fun commandExecutionMethod(shouldUse: (Method) -> Boolean, executionMethod: ExecutionMethodFactory) {
        cmdExecMethodFactories += CommandExecutionMethod(shouldUse, executionMethod)
        // annotationParser.registerCommandExecutionMethodFactory(shouldUse, executionMethod)
    }
    
    override fun <A : Annotation> builderModifier(annotationType: KClass<A>, modifier: BuilderModifier<A>) {
        builderModifierHolders += BuilderModifierHolder(annotationType, modifier)
        // annotationParser.registerBuilderModifier(clazz.java) { annotation, commandBuilder ->
        //     commandBuilder.toMutable(cloud)
        //             .also { mutableCommandBuilder ->
        //                 modifier(mutableCommandBuilder, annotation)
        //             }
        //             .commandBuilder
        // }
    }
    
    override fun <A : Annotation> annotationMapper(annotationType: KClass<A>, mapper: AnnotationMapper<A>) {
        annotationMapperHolders += AnnotationMapperHolder(annotationType, mapper)
        // annotationParser.registerAnnotationMapper(clazz.java, mapper)
    }
    
    override fun <A : Annotation> preprocessorMapper(annotationType: KClass<A>, mapper: PreprocessorMapper<A>) {
        preprocessorMapperHolders += PreprocessorMapperHolder(annotationType, mapper)
        // annotationParser.registerPreprocessorMapper(annotationType.java) { annotation -> BiFunction(mapper(annotation)) }
    }
    
    override fun <T : PolyCommand> commandInstantiationMethod(
            commandType: KClass<T>,
            shouldUse: (KClass<T>) -> Boolean,
            instantiationMethod: (KClass<T>) -> T,
                                                             ) {
        commandInstantiationMethods += CommandInstantiationMethod(commandType, shouldUse, instantiationMethod)
    }
    
    override fun scanPackage(pkg: String) {
        scanPackages += pkg
    }
    
    override fun scanPackages(pkgs: List<String>) {
        scanPackages += pkgs
    }
    
    override fun command(command: PolyCommand) {
        this.commands += command
    }
    
    override fun commands(commands: List<PolyCommand>) {
        this.commands += commands
    }
    
    data class CommandExecutionMethod(
            val shouldUse: (Method) -> Boolean,
            val executionMethod: ExecutionMethodFactory
                                     )
    
    data class BuilderModifierHolder<A : Annotation>(
            val annotationType: KClass<A>,
            val modifier: BuilderModifier<A>,
                                                    )
    
    data class AnnotationMapperHolder<A : Annotation>(
            val annotationType: KClass<A>,
            val mapper: AnnotationMapper<A>,
                                                     )
    
    data class PreprocessorMapperHolder<A : Annotation>(
            val annotationType: KClass<A>,
            val mapper: PreprocessorMapper<A>,
                                                       )
    
    data class CommandInstantiationMethod<T : Any>(
            val commandType: KClass<T>,
            val shouldUse: (KClass<T>) -> Boolean,
            val instantiationMethod: (KClass<T>) -> T
                                                  )
}