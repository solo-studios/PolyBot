/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file BotPermissionPostprocessor.kt is part of PolyhedralBot
 * Last modified on 17-11-2021 02:59 p.m.
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

package ca.solostudios.polybot.cloud.commands.permission

import ca.solostudios.polybot.cloud.commands.PolyMeta
import cloud.commandframework.execution.postprocessor.CommandPostprocessingContext
import cloud.commandframework.execution.postprocessor.CommandPostprocessor
import cloud.commandframework.services.types.ConsumerService
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.kodein.di.DI

class BotPermissionPostprocessor<T>(di: DI) : CommandPostprocessor<T> {
    @Suppress("DuplicatedCode")
    override fun accept(postprocessingContext: CommandPostprocessingContext<T>) {
        val context = postprocessingContext.commandContext
        val commandMeta = postprocessingContext.command.commandMeta
        val event = context.get<MessageReceivedEvent>("MessageReceivedEvent")
        
        if (context.contains("Guild")) {
            val guild = context.get<Guild>("Guild")
            val bot = guild.selfMember
            
            val permissions =
                    if (context.contains("TextChannel"))
                        context.get<TextChannel>("TextChannel").getPermissionOverride(bot)?.allowed ?: bot.permissions
                    else
                        bot.permissions
    
            val neededPermissions = commandMeta.getOrDefault(PolyMeta.BOT_PERMISSIONS, emptyList())
            if (!permissions.containsAll(neededPermissions)) {
                event.message.replyFormat("Cannot execute command due to insufficient permission. The bot requires the following permission(s) to execute this command: %s.",
                                          neededPermissions.subtract(permissions).joinToString(separator = ", ") { it.getName() })
                        .mentionRepliedUser(false)
                        .queue()
                ConsumerService.interrupt()
            }
        }
    }
}