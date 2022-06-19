/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyAnnotationDsl.kt is part of PolyBot
 * Last modified on 10-06-2022 12:11 p.m.
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
import ca.solostudios.polybot.api.commands.PolyCommand
import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.kotlin.MutableCommandBuilder
import java.lang.reflect.Method
import kotlin.reflect.KClass

@PolyPluginDslMarker
public interface PolyAnnotationDsl : PolyParameterInjectorDsl {
    /**
     * Registers a new command execution method factory. This allows for the registration of custom command method execution strategies
     * (eg. suspending functions, etc.)
     *
     * @param shouldUse Whether to apply the custom execution handler to a given method.
     * @param executionMethod The function that produces the command execution handler.
     *
     * @see AnnotationParser.registerCommandExecutionMethodFactory
     */
    public fun commandExecutionMethod(shouldUse: (Method) -> Boolean, executionMethod: ExecutionMethodFactory)
    
    /**
     * Registers a builder modifier for the specified annotation.
     * Builder modifiers mutate [MutableCommandBuilder] after all the arguments have been added to the builder.
     * This allows for modifications of the builder instance before the command is registered to the command handler.
     *
     * @param A The annotation type to target.
     * @param clazz The annotation class.
     * @param modifier The modifier method that acts on the given annotation and the incoming builder.
     *
     * @see AnnotationParser.registerBuilderModifier
     */
    public fun <A : Annotation> builderModifier(clazz: KClass<A>, modifier: BuilderModifier<A>)
    
    /**
     * Registers an annotation mapper.
     *
     * @param A The annotation type to target.
     * @param clazz The annotation class.
     * @param mapper The annotation mapping function.
     *
     * @see AnnotationParser.registerAnnotationMapper
     */
    public fun <A : Annotation> annotationMapper(clazz: KClass<A>, mapper: AnnotationMapper<A>)
    
    /**
     * Registers a preprocessor mapper.
     *
     * @param A The annotation type to target.
     * @param clazz The annotation class.
     * @param preprocessorMapper The preprocessor mapping function.
     *
     * @see AnnotationParser.registerPreprocessorMapper
     */
    public fun <A : Annotation> preprocessorMapper(clazz: KClass<A>, preprocessorMapper: PreprocessorMapper<A>)
    
    /**
     * Marks certain packages to be scanned for commands, which are then parsed by the annotation processor.
     *
     * @param packages The package(s) to be scanned for commands.
     */
    public fun scanPackage(vararg packages: String)
    
    /**
     * Adds a certain command to the list of commands to be parsed by the annotation processor.
     *
     * @param commands The command(s) to be parsed by the annotation processor.
     */
    public fun command(vararg commands: PolyCommand)
    
    /**
     * Registers a new command instantiation method. This allows for the registration of custom command instantiation strategies
     * (eg. adding a dependency injection object to the command constructor, etc.)
     *
     * @param shouldUse Whether to apply the custom command instigator to a given method.
     * @param instantiationMethod The function that produces the instance of the command object.
     *
     * @see scanPackage
     */
    public fun <T : PolyCommand> commandInstantiationMethod(shouldUse: (KClass<T>) -> Boolean, instantiationMethod: (KClass<T>) -> T)
}
