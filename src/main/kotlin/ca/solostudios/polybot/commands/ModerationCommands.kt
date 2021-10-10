/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file ModerationCommands.kt is part of PolyhedralBot
 * Last modified on 09-10-2021 11:19 p.m.
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

package ca.solostudios.polybot.commands

import ca.solostudios.polybot.PolyBot
import ca.solostudios.polybot.cloud.commands.PolyCommandContainer
import ca.solostudios.polybot.cloud.commands.PolyCommands
import ca.solostudios.polybot.cloud.commands.annotations.JDABotPermission
import ca.solostudios.polybot.cloud.commands.annotations.JDAGuildCommand
import ca.solostudios.polybot.cloud.commands.annotations.JDAUserPermission
import ca.solostudios.polybot.cloud.commands.annotations.PolyCategory
import ca.solostudios.polybot.entities.PolyMember
import ca.solostudios.polybot.entities.PolyMessage
import ca.solostudios.polybot.entities.PolyMessageChannel
import ca.solostudios.polybot.entities.PolyUser
import ca.solostudios.polybot.event.moderation.PolyClearEvent
import ca.solostudios.polybot.util.poly
import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.Flag
import cloud.commandframework.annotations.specifier.Greedy
import cloud.commandframework.annotations.specifier.Range
import net.dv8tion.jda.api.Permission
import org.slf4j.kotlin.*

@PolyCategory(MOD_CATEGORY)
@PolyCommandContainer
class ModerationCommands(bot: PolyBot) : PolyCommands(bot) {
    private val logger by getLogger()
    
    @JDAGuildCommand
    @JDABotPermission(Permission.BAN_MEMBERS)
    @JDAUserPermission(Permission.BAN_MEMBERS)
    @CommandMethod("ban|banish|begone <member> [reason]")
    suspend fun banUser(message: PolyMessage,
                        @Argument("member")
                        member: PolyMember,
                        @Argument("reason")
                        @Greedy
                        reason: String?,
                        @Flag("days", aliases = ["d"], description = "The amount of days to delete when banning the user. Defaults to 3.")
                        days: Int?) {
        val realReason = if (reason != null) "for \"${reason.removeSuffix(".")}\"." else "with no reason provided."
        
        member.ban(realReason, days ?: 3, message.member) {
            message.reply(it)
        }
    }
    
    @JDAGuildCommand
    @JDABotPermission(Permission.KICK_MEMBERS)
    @JDAUserPermission(Permission.KICK_MEMBERS)
    @CommandMethod("kick|yeet <member> [reason]")
    suspend fun kickMember(message: PolyMessage,
                           @Argument("member")
                           member: PolyMember,
                           @Argument("reason", description = "The reason the member was kicked.")
                           reason: String?) {
        val realReason = if (reason != null) "for \"${reason.removeSuffix(".")}\"." else "with no reason provided."
        
        member.kick(realReason, message.member) {
            message.reply(it)
        }
    }
    
    @JDAGuildCommand
    @JDABotPermission(Permission.MESSAGE_MANAGE)
    @JDAUserPermission(Permission.MESSAGE_MANAGE)
    @CommandMethod("purge|clear|clean <amount>")
    suspend fun purgeMessages(message: PolyMessage,
                              @Range(min = "1", max = "100")
                              @Argument("amount", description = "Amount of messages to filter through and attempt to delete.")
                              amount: Int,
                              @Flag("user", aliases = ["u"], description = "Delete only messages from this user.")
                              user: PolyUser?,
                              @Flag("message-regex", aliases = ["e"], description = "Delete only messages that match this regex.")
                              messageRegex: String?,
                              @Flag("starts-with", description = "Delete only messages that start with this string.")
                              startsWith: String?,
                              @Flag("ends-with", description = "Delete only messages that end with this string.")
                              endsWith: String?,
                              @Flag("bot-only", aliases = ["b"], description = "Delete only messages from bots.")
                              botOnly: Boolean = false,
                              @Flag("ignore-case", aliases = ["i"], description = "Case insensitive matching.")
                              caseInsensitive: Boolean = false) {
        logger.info { "Running clear command" }
        
        val event = PolyClearEvent(message.textChannel, message.member)
        bot.eventManager.dispatch(event)
        
        logger.info { "Dispatched clear event" }
        
        val regex =
                if (caseInsensitive)
                    messageRegex?.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE))
                else
                    messageRegex?.toRegex(RegexOption.MULTILINE)
        
        
        val messages = PastMessageSequence(message.channel).take(500).filter {
            it.id != message.id
        }.filter {
            if (user != null) it.author == user else true
        }.filter {
            if (regex != null) it.matches(regex) else true
        }.filter {
            if (startsWith != null) it.startsWith(startsWith, caseInsensitive) else true
        }.filter {
            if (endsWith != null) it.endsWith(endsWith, caseInsensitive) else true
        }.filter {
            if (botOnly) it.fromBot else true
        }.take(amount).toList()
        
        logger.info { "clearing: ${messages.map { it.contentRaw }}" }
        
        message.delete()
        
        message.textChannel.deleteMessages(messages)
        
        message.channel.sendMessage("Deleted ${messages.size} messages.")
    }
    
    @JDAGuildCommand
    @JDAUserPermission(Permission.MESSAGE_MANAGE)
    @CommandMethod("warn|warning <member> <reason>")
    suspend fun warnMember(message: PolyMessage,
                           @Argument("member")
                           member: PolyMember,
                           @Argument("reason")
                           reason: String?) {
        val realReason = if (reason != null) "for \"${reason.removeSuffix(".")}\"." else "with no reason provided."
        
        member.warn(realReason, message.member) {
            message.reply(it)
        }
    }
    
    private class PastMessageSequence(
            val channel: PolyMessageChannel,
            private val bulkQuery: Int = 24,
                                     ) : Sequence<PolyMessage> {
        private val history = channel.jdaChannel.history
        
        override fun iterator(): Iterator<PolyMessage> = object : Iterator<PolyMessage> {
            var stack: MutableList<PolyMessage> = mutableListOf()
            
            override fun next(): PolyMessage {
                if (hasNext())
                    return stack.removeFirst()
                else
                    throw NoSuchElementException()
            }
            
            override fun hasNext(): Boolean {
                if (stack.isEmpty())
                    stack.addAll(history.retrievePast(bulkQuery).complete().map { it.poly(channel.bot) })
                
                return stack.isNotEmpty()
            }
        }
    }
}
