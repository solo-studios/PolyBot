/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file TypeAliases.kt is part of PolyBot
 * Last modified on 10-06-2022 11:32 a.m.
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

package ca.solostudios.polybot.api.cloud

import ca.solostudios.polybot.api.cloud.event.MessageEvent
import cloud.commandframework.CommandManager
import cloud.commandframework.CommandTree
import cloud.commandframework.annotations.MethodCommandExecutionHandler
import cloud.commandframework.annotations.injection.InjectionService
import cloud.commandframework.annotations.injection.ParameterInjector
import cloud.commandframework.arguments.CommandSyntaxFormatter
import cloud.commandframework.arguments.parser.ArgumentParser
import cloud.commandframework.captions.CaptionRegistry
import cloud.commandframework.context.CommandContext
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.execution.postprocessor.CommandPostprocessor
import cloud.commandframework.execution.preprocessor.CommandPreprocessor
import cloud.commandframework.kotlin.MutableCommandBuilder

public typealias CommandManager = CommandManager<MessageEvent>

public typealias CommandTree = CommandTree<MessageEvent>

public typealias CommandExecutionCoordinator = CommandExecutionCoordinator<MessageEvent>

public typealias CommandExecutionCoordinatorSupplier = (CommandTree<MessageEvent>) -> CommandExecutionCoordinator<MessageEvent>

public typealias CommandContext = CommandContext<MessageEvent>

public typealias CommandMethodContext = MethodCommandExecutionHandler.CommandMethodContext<MessageEvent>

public typealias MethodCommandExecutionHandler = MethodCommandExecutionHandler<MessageEvent>

public typealias ArgumentParser<T> = ArgumentParser<MessageEvent, T>

public typealias MutableCommandBuilder = MutableCommandBuilder<MessageEvent>

public typealias CommandPreprocessor = CommandPreprocessor<MessageEvent>

public typealias CommandPostprocessor = CommandPostprocessor<MessageEvent>

public typealias CommandSyntaxFormatter = CommandSyntaxFormatter<MessageEvent>

public typealias CaptionRegistry = CaptionRegistry<MessageEvent>

public typealias ParameterInjector<T> = ParameterInjector<MessageEvent, T>

public typealias InjectionService = InjectionService<MessageEvent>