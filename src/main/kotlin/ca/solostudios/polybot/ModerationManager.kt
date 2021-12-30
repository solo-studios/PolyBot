/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file ModerationManager.kt is part of PolyhedralBot
 * Last modified on 30-12-2021 03:58 p.m.
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

package ca.solostudios.polybot

import ca.solostudios.polybot.entities.PolyGuild
import ca.solostudios.polybot.entities.PolyMember
import ca.solostudios.polybot.entities.data.PolyWarnData
import ca.solostudios.polybot.event.moderation.PolyBanEvent
import ca.solostudios.polybot.event.moderation.PolyKickEvent
import dev.minn.jda.ktx.await
import java.time.LocalDateTime
import kotlinx.uuid.UUID
import kotlinx.uuid.generateUUID
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException
import org.slf4j.kotlin.*

class ModerationManager(val bot: PolyBot) {
    private val logger by getLogger()
    
    suspend fun banMember(member: PolyMember,
                          moderator: PolyMember,
                          reason: String,
                          daysToDelete: Int,
                          replyAction: suspend (String) -> Unit) {
        when (member.id) {
            moderator.id -> {
                replyAction("You cannot ban yourself!")
            }
            
            bot.id       -> {
                replyAction("You cannot ban the bot!")
            }
            
            else         -> {
                val event = PolyBanEvent(member, reason, moderator)
    
                bot.eventManager.dispatch(event)
    
                member.jdaMember.ban(daysToDelete, reason)
                        .await()
    
                replyAction("User ${member.name}#${member.discriminator} has been banned from the server, " +
                                    "and $daysToDelete days of messages have been deleted, $reason")
    
                logger.debug {
                    "User ${member.name}#${member.discriminator} has been banned from the server ${member.guild.name}," +
                            " and $daysToDelete days of messages have been deleted. $reason"
                }
            }
        }
    }
    
    suspend fun kickMember(member: PolyMember,
                           moderator: PolyMember,
                           reason: String,
                           replyAction: suspend (String) -> Unit) {
        when (member.id) {
            moderator.id -> {
                replyAction("You cannot kick yourself!")
            }
            
            bot.id       -> {
                replyAction("You cannot kick the bot!")
            }
            
            else         -> {
                member.jdaMember.kick(reason)
                        .submit()
                        .await()
    
                val event = PolyKickEvent(member, reason, moderator)
    
                bot.eventManager.dispatch(event)
    
                replyAction("User ${member.name}#${member.discriminator} has been kicked from the server, $reason")
    
                // log to console
                logger.debug {
                    "User ${member.name}#${member.discriminator} <@${member.id}> has been kicked from the server ${member.guild.name}, $reason."
                }
            }
        }
    }
    
    suspend fun warnMember(
            guild: PolyGuild,
            member: PolyMember,
            moderator: PolyMember,
            reason: String,
            time: LocalDateTime,
            replyAction: suspend (String) -> Unit,
                          ) {
        when {
            member.id == moderator.id -> {
                replyAction("You cannot warn yourself!")
            }
            
            member.id == bot.id       -> {
                replyAction("You cannot warn the bot!")
            }
            
            member.isBot              -> {
                replyAction("You cannot warn another bot!")
            }
            
            else                      -> {
                val warnFailed = try {
                    val channel = member.user.privateChannel()
                    
                    channel.sendMessage("You have been warned. blah blah blah")
                    
                    false
                } catch (e: InsufficientPermissionException) {
                    true
                } catch (e: UnsupportedOperationException) {
                    true
                }
                val warn = PolyWarnData(bot, UUID.generateUUID(bot.globalRandom), guild.id, member.id, moderator.id, time, reason)
    
                bot.entityManager.saveWarn(warn)
    
                replyAction("${member.mention} has been warned for $reason.")
    
                if (warnFailed)
                    replyAction("Member ${member.name}#${member.discriminator} could not be messaged because they either don't share a server with this bot, or have dms off.")
    
    
                logger.debug {
                    "User ${member.name}#${member.discriminator} <@${member.id}> has been warned in the server ${member.guild.name}, $reason."
                }
            }
        }
    }
}
