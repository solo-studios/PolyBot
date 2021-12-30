/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyCloudCommandListener.kt is part of PolyhedralBot
 * Last modified on 22-12-2021 11:41 p.m.
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

package ca.solostudios.polybot.cloud.manager

import cloud.commandframework.exceptions.ArgumentParseException
import cloud.commandframework.exceptions.CommandExecutionException
import cloud.commandframework.exceptions.InvalidCommandSenderException
import cloud.commandframework.exceptions.InvalidSyntaxException
import cloud.commandframework.exceptions.NoPermissionException
import cloud.commandframework.exceptions.NoSuchCommandException
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class PolyCloudCommandListener(
        private val commandManager: PolyCloudCommandManager
                              ) : ListenerAdapter() {
    
    private val prefixes = commandManager.bot.botConfig.prefixes
    private val botId: Long
        get() = commandManager.botId
    
    override fun onMessageReceived(event: MessageReceivedEvent) {
        val message = event.message
        val sender = commandManager.eventMapper.jdaEventToPlatformEvent(event)
        
        if (botId == event.author.idLong) // Self
            return
        if (event.author.isBot || event.author.isSystem || event.isWebhookMessage) // Bot
            return
        
        val contentRaw = message.contentRaw
        val prefix = detectPrefix(contentRaw) ?: return
        
        val commandContent = contentRaw.substring(prefix.length).trim()
        
        commandManager.executeCommand(sender, commandContent)
                .whenComplete { _, throwable ->
                    if (throwable == null) {
                        return@whenComplete
                    }
                    
                    when (throwable) {
                        is InvalidSyntaxException        -> {
                            commandManager.handleException(sender, InvalidSyntaxException::class.java, throwable) { _, _ ->
                                sendMessage(event, "Invalid Command Syntax. Correct command syntax is: $prefix${throwable.correctSyntax}")
                            }
                        }
                        
                        is InvalidCommandSenderException -> {
                            commandManager.handleException(sender, InvalidCommandSenderException::class.java, throwable) { _, _ ->
                                sendMessage(event, throwable.message ?: "An unknown error occurred.")
                            }
                        }
                        
                        is NoPermissionException         -> {
                            commandManager.handleException(sender, NoPermissionException::class.java, throwable) { _, _ ->
                                sendMessage(event, ("I'm sorry, but you do not have permission to perform this command. "
                                        + "Please contact the server administrators if you believe that this is in error."))
                            }
                        }
                        
                        is NoSuchCommandException        -> {
                            commandManager.handleException(sender, NoSuchCommandException::class.java, throwable) { _, _ ->
                                sendMessage(event, "Unknown command")
                            }
                        }
                        
                        is ArgumentParseException        -> {
                            commandManager.handleException(sender, ArgumentParseException::class.java, throwable) { _, _ ->
                                sendMessage(event, "Invalid Command Argument: ${throwable.cause.message}")
                            }
                        }
                        
                        is CommandExecutionException     -> {
                            commandManager.handleException(sender, CommandExecutionException::class.java, throwable) { _, _ ->
                                sendMessage(event, "An internal error occurred while attempting to perform this command.")
                                throwable.cause!!.printStackTrace()
                            }
                        }
                        
                        else                             -> {
                            sendMessage(event, throwable.message ?: "An unknown error occurred")
                        }
                    }
                }
    }
    
    private fun sendMessage(event: MessageReceivedEvent, message: String) {
        event.channel.sendMessage(message).queue()
    }
    
    private fun detectPrefix(content: String): String? {
        for (prefix in prefixes) {
            if (content.startsWith(prefix))
                return prefix
        }
        
        return when {
            content.startsWith("<@$botId>")  -> "<@$botId>"
            content.startsWith("<@!$botId>") -> "<@!$botId>"
            else                             -> null
        }
    }
}