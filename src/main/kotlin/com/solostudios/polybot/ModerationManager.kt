/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file ModerationManager.kt is part of PolyhedralBot
 * Last modified on 24-07-2021 08:15 p.m.
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

import com.solostudios.polybot.event.moderation.PolyBanEvent
import com.solostudios.polybot.event.moderation.PolyKickEvent
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import org.slf4j.kotlin.debug
import org.slf4j.kotlin.getLogger

class ModerationManager(val bot: PolyBot) {
    private val logger by getLogger()
    
    fun banMember(guild: Guild,
                  member: Member,
                  moderator: Member = guild.selfMember,
                  reason: String,
                  daysToDelete: Int,
                  replyAction: (String) -> Unit) {
        val event = PolyBanEvent(member, reason, moderator)
        
        bot.eventManager.dispatch(event)
        
        val user = member.user
        
        when (member.idLong) {
            moderator.idLong        -> {
                replyAction("You cannot ban yourself!")
            }
            
            guild.selfMember.idLong -> {
                replyAction("You cannot ban the bot!")
            }
            
            else                    -> {
                replyAction("User ${user.name}#${user.discriminator} has been banned from the server, and $daysToDelete days of messages have been deleted, $reason")
                
                guild.ban(user, daysToDelete)
                        .reason(reason)
                        .queue()
                
                logger.debug(user.name, user.discriminator, guild.name, daysToDelete, reason) {
                    "User {}#{} has been banned from the server {}, and {} days of messages have been deleted. {}"
                }
            }
        }
    }
    
    fun kickMember(guild: Guild,
                   member: Member,
                   moderator: Member = guild.selfMember,
                   reason: String,
                   replyAction: (String) -> Unit) {
        val event = PolyKickEvent(member, reason, moderator)
        
        bot.eventManager.dispatch(event)
        
        val user = member.user
        
        when (member.idLong) {
            moderator.idLong        -> {
                replyAction("You cannot kick yourself!")
            }
            
            guild.selfMember.idLong -> {
                replyAction("You cannot kick the bot!")
            }
            
            else                    -> {
                member.kick(reason)
                        .queue()
                
                replyAction("User ${user.name}#${user.discriminator} has been kicked from the server, $reason")
                
                // log to console
                logger.debug(user.name, user.discriminator, member.guild.name, reason) {
                    "User {}#{} has been kicked from the server {}, {}."
                }
            }
        }
    }
}
