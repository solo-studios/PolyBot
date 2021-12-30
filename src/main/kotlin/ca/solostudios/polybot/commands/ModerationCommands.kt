/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file ModerationCommands.kt is part of PolyhedralBot
 * Last modified on 30-12-2021 03:19 p.m.
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
import ca.solostudios.polybot.cloud.commands.annotations.CommandLongDescription
import ca.solostudios.polybot.cloud.commands.annotations.CommandName
import ca.solostudios.polybot.cloud.commands.annotations.CurrentGuild
import ca.solostudios.polybot.cloud.commands.annotations.JDABotPermission
import ca.solostudios.polybot.cloud.commands.annotations.JDAGuildCommand
import ca.solostudios.polybot.cloud.commands.annotations.JDAUserPermission
import ca.solostudios.polybot.cloud.commands.annotations.PolyCategory
import ca.solostudios.polybot.cloud.commands.annotations.SourceMessage
import ca.solostudios.polybot.entities.PolyGuild
import ca.solostudios.polybot.entities.PolyMember
import ca.solostudios.polybot.entities.PolyMessage
import ca.solostudios.polybot.entities.PolyTextChannel
import ca.solostudios.polybot.event.moderation.PolyClearEvent
import ca.solostudios.polybot.util.poly
import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
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
    
    @CommandName("Set Logging Channel")
    @JDAGuildCommand
    @JDAUserPermission(Permission.MANAGE_SERVER)
    @CommandMethod("logging|logs [channel]")
    @CommandDescription("Sets the channel to use for logging in this server.")
    @CommandLongDescription("Sets the channel used for the mod log in this server.\nWhatever channel this is set to will have any moderation events sent to it, such as:\n- edited messages\n- deleted messages\n- banned members\n- member joins/leaves\netc.\n")
    suspend fun loggingChannel(
            @SourceMessage
            message: PolyMessage,
            @CurrentGuild
            guild: PolyGuild,
            @Argument(value = "channel", description = "The channel to send logs to.")
            loggingChannel: PolyTextChannel? = null,
                              ) {
        val oldLoggingChannel = guild.data.loggingChannel
        
        guild.data.loggingChannelId = loggingChannel?.id ?: -1L
        
        
        message.reply("Updated the logging channel from $oldLoggingChannel to $loggingChannel")
    }
    
    @JDAGuildCommand
    @CommandName("Ban")
    @JDABotPermission(Permission.BAN_MEMBERS)
    @JDAUserPermission(Permission.BAN_MEMBERS)
    @CommandMethod("ban|banish|begone <member> [reason]")
    @CommandDescription("Bans a member from this server deleting the last few days of messages.")
    @CommandLongDescription("Permanently bans the mentioned user from this server with a reason, if provided.\nThis will also delete the last specified days of messages, defaulting to 3.\n\nThis action will also be reported to the moderation log channel, if any exists.")
    suspend fun banUser(
            @SourceMessage
            message: PolyMessage,
            @Argument(value = "member", description = "The member to ban from the server.")
            member: PolyMember,
            @Argument(value = "reason", description = "The reason this member was banned.")
            @Greedy
            reason: String?,
            @Flag("days", aliases = ["d"], description = "The amount of days to delete when banning the user. Defaults to 3.")
            days: Int?,
                       ) {
        val realReason = if (reason != null) "for \"${reason.removeSuffix(".")}\"." else "with no reason provided."
        
        member.ban(realReason, days ?: 3, message.member) {
            message.reply(it)
        }
    }
    
    @JDAGuildCommand
    @CommandName("Kick")
    @JDABotPermission(Permission.KICK_MEMBERS)
    @JDAUserPermission(Permission.KICK_MEMBERS)
    @CommandMethod("kick|yeet <member> [reason]")
    @CommandDescription("Kicks a member from this server.")
    @CommandLongDescription("Kicks the mentioned user from this server with a reason, if provided.\n\nThis action will also be reported to the moderation log channel, if any exists.")
    suspend fun kickMember(
            @SourceMessage
            message: PolyMessage,
            @Argument(value = "member", description = "The member to kick from the server.")
            member: PolyMember,
            @Argument("reason", description = "The reason the member was kicked.")
            reason: String?,
                          ) {
        val realReason = if (reason != null) "for \"${reason.removeSuffix(".")}\"." else "with no reason provided."
        
        member.kick(realReason, message.member) {
            message.reply(it)
        }
    }
    
    @JDAGuildCommand
    @CommandName("Clear")
    @JDABotPermission(Permission.MESSAGE_MANAGE)
    @JDAUserPermission(Permission.MESSAGE_MANAGE)
    @CommandMethod("clear|clean|purge <amount>")
    @CommandDescription("Clears a number of messages from the chat.")
    @CommandLongDescription("Removes up to 100 messages from the chat. You can provide a user if you only want to delete messages from a certain user.\n\nThis action will also be reported in the moderation log channel, if any exists.")
    suspend fun purgeMessages(
            @SourceMessage
            message: PolyMessage,
            @Range(min = "1", max = "100")
            @Argument("amount", description = "Amount of messages to filter through and attempt to delete.")
            amount: Int,
            @Flag("user", aliases = ["u"], description = "Delete only messages from this user.")
            member: PolyMember?,
            @Flag("message-regex", aliases = ["e"], description = "Delete only messages that match this regex.")
            messageRegex: String?,
            @Flag("starts-with", description = "Delete only messages that start with this string.")
            startsWith: String?,
            @Flag("ends-with", description = "Delete only messages that end with this string.")
            endsWith: String?,
            @Flag("bot-only", aliases = ["b"], description = "Delete only messages from bots.")
            botOnly: Boolean = false,
            @Flag("ignore-case", aliases = ["i"], description = "Case-insensitive matching.")
            caseInsensitive: Boolean = false,
                             ) {
        logger.info { "Running clear command" }
        
        val event = PolyClearEvent(message.textChannel, message.member)
        bot.eventManager.dispatch(event)
        
        logger.info { "Dispatched clear event" }
        
        val regex =
                if (caseInsensitive)
                    messageRegex?.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE))
                else
                    messageRegex?.toRegex(RegexOption.MULTILINE)
        
        
        val messages = PastMessageSequence(message.textChannel).take(500).filter {
            it.id != message.id
        }.filter {
            if (member != null) it.member == member else true
        }.filter {
            if (regex != null) it.matches(regex) else true
        }.filter {
            if (startsWith != null) it.startsWith(startsWith, caseInsensitive) else true
        }.filter {
            if (endsWith != null) it.endsWith(endsWith, caseInsensitive) else true
        }.filter {
            if (botOnly) it.isFromBot else true
        }.take(amount).toList()
        
        message.delete()
        
        if (messages.size == 1)
            messages.first().delete()
        else
            message.textChannel.deleteMessages(messages)
        
        message.channel.sendMessage("Deleted ${messages.size} messages.")
    }
    
    @JDAGuildCommand
    @CommandName("Warn")
    @JDAUserPermission(Permission.MESSAGE_MANAGE)
    @CommandMethod("warn|warning <member> <reason>")
    @CommandDescription("Warns a member in the server with a given reason.")
    @CommandLongDescription("Warns a user in this server with a reason, if provided.\n\nThis action will also be reported to the moderation log channel, if any exists.")
    suspend fun warnMember(
            @SourceMessage
            message: PolyMessage,
            @Argument(value = "member", description = "The member to warn.")
            member: PolyMember,
            @Argument(value = "reason", description = "The reason the member was warned.")
            reason: String?,
                          ) {
        val realReason = if (reason != null) "for \"${reason.removeSuffix(".")}\"." else "with no reason provided."
        
        member.warn(realReason, message.member) {
            message.reply(it)
        }
    }
    
    private class PastMessageSequence(
            val channel: PolyTextChannel,
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
