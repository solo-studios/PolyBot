/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyMember.kt is part of PolyhedralBot
 * Last modified on 31-12-2021 01:29 p.m.
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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package ca.solostudios.polybot.entities

import ca.solostudios.polybot.PolyBot
import ca.solostudios.polybot.util.jda.poly
import dev.minn.jda.ktx.await
import java.awt.Color
import java.time.LocalDateTime
import java.time.OffsetDateTime
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role

class PolyMember(override val bot: PolyBot, val jdaMember: Member) : PolyUser(bot, jdaMember.user) {
    val effectiveName: String
        get() = jdaMember.effectiveName
    
    val nickname: String?
        get() = jdaMember.nickname
    
    val hasNickname: Boolean
        get() = jdaMember.nickname != null
    
    val user: PolyUser
        get() = PolyUser(bot, jdaMember.user)
    
    val guild: PolyGuild
        get() = PolyGuild(bot, jdaMember.guild)
    
    val guildId: Long
        get() = jdaMember.guild.idLong
    
    val guildPermissions: List<Permission>
        get() = jdaMember.permissions.toList()
    
    val timeJoined: OffsetDateTime
        get() = jdaMember.timeJoined
    
    val roles: List<PolyRole>
        get() = jdaMember.roles.map { it.poly(bot) }
    
    val color: Color?
        get() = jdaMember.color
    
    val colorRaw: Int?
        get() = if (jdaMember.colorRaw == Role.DEFAULT_COLOR_RAW) null else jdaMember.colorRaw
    
    val isGuildOwner: Boolean
        get() = jdaMember.isOwner
    
    val data by lazy { bot.entityManager.getMember(this) }
    
    val warns by lazy { bot.entityManager.getWarns(this) }
    
    suspend fun ban(reason: String, daysToDelete: Int, moderator: PolyMember, replyAction: suspend (String) -> Unit) {
        bot.moderationManager.banMember(this, moderator, reason, daysToDelete, replyAction)
    }
    
    suspend fun kick(reason: String, moderator: PolyMember, replyAction: suspend (String) -> Unit) {
        bot.moderationManager.kickMember(this, moderator, reason, replyAction)
    }
    
    suspend fun warn(reason: String,
                     moderator: PolyMember,
                     time: LocalDateTime = LocalDateTime.now(),
                     replyAction: suspend (String) -> Unit) {
        bot.moderationManager.warnMember(guild, this, moderator, reason, time, replyAction)
    }
    
    suspend fun changeNickname(nickname: String?) {
        jdaMember.modifyNickname(nickname).await()
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
    
    override fun toString(): String = jdaMember.asMention
}