/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyMember.kt is part of PolyhedralBot
 * Last modified on 27-09-2021 06:27 p.m.
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

package com.solostudios.polybot.entities

import com.solostudios.polybot.PolyBot
import java.time.LocalDateTime
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member

class PolyMember(val bot: PolyBot, val jdaMember: Member) {
    val id: Long
        get() = jdaMember.idLong
    
    val isBot: Boolean
        get() = jdaMember.user.isBot
    
    val name: String
        get() = jdaMember.user.name
    
    val discriminator: String
        get() = jdaMember.user.discriminator
    
    val effectiveName: String
        get() = jdaMember.effectiveName
    
    val mention: String
        get() = jdaMember.asMention
    
    val user: PolyUser
        get() = PolyUser(bot, jdaMember.user)
    
    val guild: PolyGuild
        get() = PolyGuild(bot, jdaMember.guild)
    
    val guildId: Long
        get() = jdaMember.guild.idLong
    
    val guildPermissions: List<Permission>
        get() = jdaMember.permissions.toList()
    
    val data by lazy { bot.entityManager.getMember(this) }
    
    val warns by lazy { bot.entityManager.getWarns(this) }
    
    val isOwner: Boolean
        get() = id in bot.botConfig.ownerIds
    
    val isCoOwner: Boolean
        get() = id in bot.botConfig.ownerIds || id in bot.botConfig.coOwnerIds
    
    fun ban(reason: String, daysToDelete: Int, moderator: PolyMember, replyAction: suspend (String) -> Unit) {
        bot.moderationManager.banMember(this, moderator, reason, daysToDelete, replyAction)
    }
    
    fun kick(reason: String, moderator: PolyMember, replyAction: suspend (String) -> Unit) {
        bot.moderationManager.kickMember(this, moderator, reason, replyAction)
    }
    
    fun warn(reason: String, moderator: PolyMember, time: LocalDateTime = LocalDateTime.now(), replyAction: suspend (String) -> Unit) {
        bot.moderationManager.warnMember(guild, this, moderator, reason, time, replyAction)
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        
        other as PolyMember
        
        return jdaMember != other.jdaMember
    }
    
    override fun hashCode(): Int {
        return jdaMember.hashCode()
    }
}