/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyUser.kt is part of PolyhedralBot
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
import net.dv8tion.jda.api.entities.User

open class PolyUser(open val bot: PolyBot, val jdaUser: User) {
    val id: Long
        get() = jdaUser.idLong
    
    val isBot: Boolean
        get() = jdaUser.isBot
    
    val isSystem: Boolean
        get() = jdaUser.isSystem
    
    val isStaff: Boolean
        get() = ((jdaUser.flagsRaw shr (User.UserFlag.STAFF.offset)) and 1) == 1 // bit magic
    
    val isPartner: Boolean
        get() = ((jdaUser.flagsRaw shr (User.UserFlag.PARTNER.offset)) and 1) == 1 // bit magic
    
    val isVerifiedBot: Boolean
        get() = ((jdaUser.flagsRaw shr (User.UserFlag.VERIFIED_BOT.offset)) and 1) == 1 // bit magic
    
    val isVerifiedDeveloper: Boolean
        get() = ((jdaUser.flagsRaw shr (User.UserFlag.VERIFIED_DEVELOPER.offset)) and 1) == 1 // bit magic
    
    val isCertifiedModerator: Boolean
        get() = ((jdaUser.flagsRaw shr (User.UserFlag.CERTIFIED_MODERATOR.offset)) and 1) == 1 // bit magic
    
    val name: String
        get() = jdaUser.name
    
    val discriminator: String
        get() = jdaUser.discriminator
    
    val tag: String
        get() = jdaUser.asTag
    
    val mention: String
        get() = jdaUser.asMention
    
    val avatarUrl: String?
        get() = jdaUser.avatarUrl
    
    val effectiveAvatarUrl: String
        get() = jdaUser.effectiveAvatarUrl
    
    val isOwner: Boolean
        get() = id in bot.config.polybotConfig.ownerIds
    
    val isCoOwner: Boolean
        get() = id in bot.config.polybotConfig.ownerIds || id in bot.config.polybotConfig.coOwnerIds
    
    suspend fun privateChannel(): PolyMessageChannel {
        return jdaUser.openPrivateChannel().await().poly(bot)
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
    
        other as PolyUser
    
        return jdaUser != other.jdaUser
    }
    
    override fun hashCode(): Int {
        return jdaUser.hashCode()
    }
    
    override fun toString(): String = jdaUser.asMention
    
    // val data by lazy { bot.databaseManager }
    
}