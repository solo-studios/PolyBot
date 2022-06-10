/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file TypeAliases.kt is part of PolyBot
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

import ca.solostudios.polybot.api.cloud.ArgumentParser
import ca.solostudios.polybot.api.cloud.CommandContext
import ca.solostudios.polybot.api.cloud.CommandMethodContext
import ca.solostudios.polybot.api.cloud.MethodCommandExecutionHandler
import ca.solostudios.polybot.api.cloud.MutableCommandBuilder
import ca.solostudios.polybot.api.cloud.event.MessageEvent
import cloud.commandframework.arguments.parser.ArgumentParseResult
import cloud.commandframework.arguments.parser.ParserParameters
import io.leangen.geantyref.TypeToken
import java.util.Queue

public typealias ExceptionHandler<E> = (event: MessageEvent, exception: E) -> Unit

public typealias PreprocessorMapper<A> = (annotation: A) -> (CommandContext, Queue<String>) -> ArgumentParseResult<Boolean>

public typealias AnnotationMapper<A> = (annotation: A) -> ParserParameters

public typealias TypedAnnotationMapper<A> = (annotation: A, type: TypeToken<*>) -> ParserParameters

public typealias BuilderModifier<A> = (MutableCommandBuilder).(annotation: A) -> Unit

public typealias ExecutionMethodFactory = (CommandMethodContext) -> MethodCommandExecutionHandler

public typealias ParserSupplier<T> = (parameters: ParserParameters) -> ArgumentParser<T>

public typealias SuggestionProvider = (context: CommandContext, input: String) -> List<String>