/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file ModerationCommands.kt is part of PolyhedralBot
 * Last modified on 16-07-2021 02:37 a.m.
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

package com.solostudios.polybot.commands

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.Flag
import cloud.commandframework.annotations.specifier.Greedy
import com.solostudios.polybot.PolyBot
import com.solostudios.polybot.annotations.permission.JDABotPermission
import com.solostudios.polybot.annotations.permission.JDAUserPermission
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.User
import org.slf4j.kotlin.debug
import org.slf4j.kotlin.getLogger
import org.slf4j.kotlin.info

class ModerationCommands(val bot: PolyBot) {
    private val logger by getLogger()
    
    @CommandMethod("ban|banish|begone <member> [reason]")
    @JDABotPermission(Permission.BAN_MEMBERS)
    @JDAUserPermission(Permission.BAN_MEMBERS)
    fun banUser(message: Message,
                @Argument("member")
                member: Member,
                @Argument("reason")
                @Greedy
                reason: String?,
                @Flag("days", aliases = ["d"], description = "The amount of days to delete")
                days: Int?) {
        val realReason = if (reason != null) "for \"$reason\"." else "no reason provided."
        
        member.ban(days ?: 3)
                .reason(realReason)
                .queue()
        
        message.reply("User ${member.user.name}#${member.user.discriminator} has been banned from the server, and ${days ?: 3} days of messages have been deleted, $realReason")
                .queue()
        
        // log to console
        logger.debug(member.user.name,
                     member.user.discriminator,
                     member.guild.name,
                     days ?: 3,
                     realReason) { "User {}#{} has been banned from the server {}, and {} days of messages have been deleted. {}" }
        // TODO: 2021-07-11 dispatch ban event
    }
    
    @CommandMethod("kick|yeet <member> [reason]")
    @JDABotPermission(Permission.KICK_MEMBERS)
    @JDAUserPermission(Permission.KICK_MEMBERS)
    fun kickMember(message: Message,
                   @Argument("member")
                   member: Member,
                   @Argument("reason")
                   reason: String?) {
        val realReason = if (reason != null) "for \"$reason\"." else "no reason provided."
        
        member.kick(realReason)
                .queue()
        
        message.reply("User ${member.user.name}#${member.user.discriminator} has been kicked from the server, $realReason")
                .queue()
        
        // log to console
        logger.debug(member.user.name,
                     member.user.discriminator,
                     member.guild.name,
                     realReason) { "User {}#{} has been kick from the server {}, {}" }
        // TODO: 2021-07-11 dispatch kick event
    }
    
    @CommandMethod("purge|clear|clean <amount>")
    @JDABotPermission(Permission.MESSAGE_MANAGE)
    @JDAUserPermission(Permission.MESSAGE_MANAGE)
    fun purgeMessages(message: Message,
                      @Argument("amount", description = "Amount of messages to filter through and attempt to delete.")
                      amount: Int,
                      @Flag("user", aliases = ["u"], description = "Delete only messages from this user.")
                      user: User?,
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
        logger.info(message.member?.effectiveName, amount) { "User {} ran command to clear {} messages" }
        message.replyFormat("User %s ran command to clear %d messages", message.member?.effectiveName, amount).queue()
        logger.info(amount, user, messageRegex, startsWith, endsWith, botOnly, caseInsensitive) {
            "amount = [{}], user = [{}], messageRegex = [{}], startsWith = [{}], endsWith = [{}], botOnly = [{}], caseInsensitive = [{}]"
        }
    }
    
    @CommandMethod("warn|warning <member> <reason>")
    @JDAUserPermission(Permission.MESSAGE_MANAGE)
    fun warnMember(message: Message,
                   @Argument("member")
                   member: Member,
                   @Argument("reason")
                   reason: String?) {
        logger.info(message.member?.effectiveName, member.effectiveName, reason) { "User {} ran command to warn {} for '{}'" }
        message.replyFormat("User %s ran command to warn %s for '%s'", message.member?.effectiveName, member.effectiveName, reason).queue()
    }
}