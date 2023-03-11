/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyCloudCommandListener.kt is part of PolyBot
 * Last modified on 03-03-2023 12:45 p.m.
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

package ca.solostudios.polybot.impl.cloud

import ca.solostudios.polybot.api.PolyBot
import ca.solostudios.polybot.api.PolyObject
import ca.solostudios.polybot.api.cloud.event.EventMapper
import ca.solostudios.polybot.config.PolyConfigSpec
import cloud.commandframework.exceptions.ArgumentParseException
import cloud.commandframework.exceptions.CommandExecutionException
import cloud.commandframework.exceptions.InvalidCommandSenderException
import cloud.commandframework.exceptions.InvalidSyntaxException
import cloud.commandframework.exceptions.NoPermissionException
import cloud.commandframework.exceptions.NoSuchCommandException
import com.uchuhimo.konf.Config
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import org.slf4j.kotlin.*

internal class PolyCloudCommandListener(
        private val commandManager: PolyCloudCommandManager,
        override val bot: PolyBot,
        override val di: DI
                                       ) : ListenerAdapter(), DIAware, PolyObject {
    private val logger by getLogger()
    private val config: Config by instance()
    // private val eventMapper: EventMapper by instance()
    
    private val prefixes: List<String> = config[PolyConfigSpec.prefixes]
    private val botId: Long = commandManager.botId
    
    override fun onMessageReceived(event: MessageReceivedEvent) {
        val message = event.message
        val sender = EventMapper.jdaEventToPlatformEvent(bot, event)
        
        if (botId == event.author.idLong) // Self
            return
        if (event.author.isBot || event.author.isSystem || event.isWebhookMessage) // Bot
            return
        
        val contentRaw = message.contentRaw
        val prefix = detectPrefix(contentRaw) ?: return
        
        val commandContent = contentRaw.substring(prefix.length).trimStart()
        
        commandManager.executeCommand(sender, commandContent).whenComplete { _, throwable ->
            if (throwable == null) {
                return@whenComplete
            }
            
            when (throwable) {
                is InvalidSyntaxException        -> commandManager.handleException(sender, InvalidSyntaxException::class.java, throwable) { _, _ ->
                    sendMessage(event, "$invalidCommandSyntax $prefix${throwable.correctSyntax}")
                }
                
                is InvalidCommandSenderException -> commandManager.handleException(sender, InvalidCommandSenderException::class.java, throwable) { _, _ ->
                    sendMessage(event, throwable.message ?: unknownError)
                    logger.warn(throwable) { "Invalid Command Sender Exception" }
                }
                
                is NoPermissionException         -> commandManager.handleException(sender, NoPermissionException::class.java, throwable) { _, _ ->
                    sendMessage(event, noPermissionMessage)
                }
                
                is NoSuchCommandException        -> commandManager.handleException(sender, NoSuchCommandException::class.java, throwable) { _, _ ->
                    sendMessage(event, unknownCommand)
                }
                
                is ArgumentParseException        -> commandManager.handleException(sender, ArgumentParseException::class.java, throwable) { _, _ ->
                    sendMessage(event, "$invalidArgument ${throwable.cause.message}")
                }
                
                is CommandExecutionException     -> commandManager.handleException(sender, CommandExecutionException::class.java, throwable) { _, _ ->
                    sendMessage(event, internalError)
                    logger.warn(throwable) { "Command Execution Exception" }
                }
                
                else                             -> {
                    sendMessage(event, throwable.message ?: unknownError)
                    logger.warn(throwable) { "Unknown Exception" }
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
    
    private companion object {
        const val invalidCommandSyntax = "Invalid Command Syntax. The correct syntax is:"
        const val unknownCommand = "Unknown command."
        const val invalidArgument = "Invalid Command Argument:"
        val noPermissionMessage = """
            |I'm sorry, but you do not have permission to perform this command.
            |Please contact the server administrators if you believe that this is in error.
        """.trimMargin("|")
        val unknownError = """
            |An unknown error occurred.
            |Please contact the bot developers.
        """.trimMargin("|")
        val internalError = """
            |An internal error occurred while attempting to perform this command.
            |Please contact the bot developers.
        """.trimMargin("|")
    }
}