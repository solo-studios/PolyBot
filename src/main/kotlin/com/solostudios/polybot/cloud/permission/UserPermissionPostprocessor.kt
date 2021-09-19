/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file UserPermissionPostprocessor.kt is part of PolyhedralBot
 * Last modified on 19-09-2021 06:31 p.m.
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

package com.solostudios.polybot.cloud.permission

import cloud.commandframework.execution.postprocessor.CommandPostprocessingContext
import cloud.commandframework.execution.postprocessor.CommandPostprocessor
import cloud.commandframework.services.types.ConsumerService
import com.solostudios.polybot.PolyBot
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.slf4j.kotlin.*

class UserPermissionPostprocessor<T>(val bot: PolyBot) : CommandPostprocessor<T> {
    private val logger by getLogger()
    
    @Suppress("DuplicatedCode")
    override fun accept(postprocessingContext: CommandPostprocessingContext<T>) {
        logger.info { "Ya" }
    
        val context = postprocessingContext.commandContext
        val commandMeta = postprocessingContext.command.commandMeta
        val event = context.get<MessageReceivedEvent>("MessageReceivedEvent")
    
        if (commandMeta.getOrDefault(CO_OWNER_ONLY, NotBoolean(false)).value) {
            if (event.author.idLong !in bot.botConfig.ownerIds && event.author.idLong !in bot.botConfig.coOwnerIds) {
                event.message.replyFormat("This command can only be performed by co-owners and owners of the bot.")
                        .mentionRepliedUser(false)
                        .queue()
                ConsumerService.interrupt()
            }
        } else if (commandMeta.getOrDefault(OWNER_ONLY, NotBoolean(false)).value) {
            if (event.author.idLong !in bot.botConfig.ownerIds) {
                event.message.replyFormat("This command can only be performed by owners of the bot.")
                        .mentionRepliedUser(false)
                        .queue()
                ConsumerService.interrupt()
            }
        }
        
        if (event.author.idLong in bot.botConfig.ownerIds)
            return
        
        if (context.contains("Guild")) {
            val user = event.member ?: return
            
            val permissions =
                    if (context.contains("TextChannel"))
                        context.get<TextChannel>("TextChannel").getPermissionOverride(user)?.allowed ?: user.permissions
                    else
                        user.permissions
            
            val neededPermissions = commandMeta.getOrDefault(USER_PERMISSIONS, emptyList())
            if (!permissions.containsAll(neededPermissions)) {
                event.message.replyFormat("Cannot execute command due to insufficient permission. You require the following permission(s) to do execute this command: %s.",
                                          neededPermissions.subtract(permissions).joinToString(separator = ", ") { it.getName() })
                        .mentionRepliedUser(false)
                        .queue()
                ConsumerService.interrupt()
            }
        }
    }
}