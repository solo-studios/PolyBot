/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyExceptionHandler.kt is part of PolyhedralBot
 * Last modified on 30-06-2021 11:43 p.m.
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
 * POLYHEDRALBOT IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.solostudios.polybot

import cloud.commandframework.CommandManager
import cloud.commandframework.exceptions.ArgumentParseException
import cloud.commandframework.exceptions.CommandExecutionException
import cloud.commandframework.exceptions.CommandParseException
import cloud.commandframework.exceptions.InvalidCommandSenderException
import cloud.commandframework.exceptions.InvalidSyntaxException
import cloud.commandframework.exceptions.NoCommandInLeafException
import cloud.commandframework.exceptions.NoPermissionException
import cloud.commandframework.exceptions.NoSuchCommandException
import com.solostudios.polybot.event.MessageEvent
import java.util.function.BiConsumer
import org.slf4j.kotlin.getLogger
import org.slf4j.kotlin.warn
import kotlin.reflect.KClass

class PolyExceptionHandler(val manager: CommandManager<MessageEvent>) {
    private val logger by getLogger()
    
    init {
        manager.registerExceptionHandler(InvalidCommandSenderException::class, this::handleInvalidCommandSender)
        manager.registerExceptionHandler(InvalidSyntaxException::class, this::handleInvalidSyntax)
        manager.registerExceptionHandler(NoPermissionException::class, this::handleNoPermission)
        manager.registerExceptionHandler(ArgumentParseException::class, this::handleArgumentParse)
        manager.registerExceptionHandler(CommandExecutionException::class, this::handleCommandExecution)
        manager.registerExceptionHandler(NoCommandInLeafException::class, this::handleNoCommandInLeaf)
        manager.registerExceptionHandler(CommandParseException::class, this::handleCommandParse)
        manager.registerExceptionHandler(NoSuchCommandException::class, this::handleNoSuchCommand)
    }
    
    private fun handleInvalidCommandSender(event: MessageEvent, exception: InvalidCommandSenderException) {
        event.event.message.replyFormat("Invalid command syntax. Correct command syntax is: `%s`.", exception.requiredSender).queue()
    }
    
    private fun handleInvalidSyntax(event: MessageEvent, exception: InvalidSyntaxException) {
        event.event.message.replyFormat("Invalid command syntax. Correct command syntax is: `%s`.", exception.correctSyntax).queue()
    }
    
    private fun handleNoPermission(event: MessageEvent, exception: NoPermissionException) {
        event.event.message.replyFormat("I'm sorry, but you have insufficient permissions to execute this command.\n" +
                                                "You are missing the \"%s\" permission.", exception.missingPermission).queue()
    }
    
    private fun handleArgumentParse(event: MessageEvent, exception: ArgumentParseException) {
        event.event.message.replyFormat("Invalid command argument.%n%s", exception.cause.message).queue()
    }
    
    private fun handleCommandParse(event: MessageEvent, exception: CommandParseException) {
        event.event.message.replyFormat("Invalid command argument.%n%s", exception.message).queue()
    }
    
    private fun handleNoCommandInLeaf(event: MessageEvent, exception: NoCommandInLeafException) {
        event.event.message.replyFormat("%s.%nYou should never see this message. Ever. If you see this message, then something went very, *very* wrong.",
                                        exception.message).queue()
    }
    
    private fun handleCommandExecution(event: MessageEvent, exception: CommandExecutionException) {
        // TODO: 2021-06-08 Add flag to show stack trace
        event.event.message.replyFormat("An internal error occurred while attempting to perform this command.%n%s",
                                        if (exception.cause != null) exception.cause!!.message else exception.message).queue()
        logger.warn(exception) { "Error occurred while attempting to execute command." }
    }
    
    private fun handleNoSuchCommand(event: MessageEvent, exception: NoSuchCommandException) {
        event.event.message.replyFormat("No such command \"%s\" found.", exception.suppliedCommand).queue()
    }
}

fun <C, E : Exception> CommandManager<C>.registerExceptionHandler(clazz: KClass<E>, handler: BiConsumer<C, E>) {
    registerExceptionHandler(clazz.java, handler)
}
