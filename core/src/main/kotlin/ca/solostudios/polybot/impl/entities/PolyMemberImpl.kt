/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyMemberImpl.kt is part of PolyBot
 * Last modified on 10-06-2022 11:32 a.m.
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
 * POLYBOT IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ca.solostudios.polybot.impl.entities

import ca.solostudios.polybot.api.PolyBot
import ca.solostudios.polybot.api.entities.PolyGuild
import ca.solostudios.polybot.api.entities.PolyGuildChannel
import ca.solostudios.polybot.api.entities.PolyMember
import ca.solostudios.polybot.api.entities.PolyPrivateChannel
import ca.solostudios.polybot.api.entities.PolyRole
import ca.solostudios.polybot.api.entities.Snowflake
import ca.solostudios.polybot.api.entities.Snowflake.Companion.snowflake
import ca.solostudios.polybot.api.util.ext.poly
import dev.minn.jda.ktx.await
import java.awt.Color
import java.util.EnumSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.IPermissionHolder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User

internal class PolyMemberImpl(
        override val bot: PolyBot,
        override val jdaMember: Member,
                             ) : PolyMember {
    override val snowflake: Snowflake by lazy { jdaMember.idLong.snowflake() }
    
    override val name: String
        get() = jdaUser.name
    override val discriminator: Int
        get() = jdaUser.discriminator.toInt()
    override val discriminatorString: String
        get() = jdaUser.discriminator
    override val asTag: String
        get() = jdaUser.asTag
    override val isOwner: Boolean
        get() = TODO("Not yet implemented")
    override val isCoOwner: Boolean
        get() = TODO("Not yet implemented")
    override val mutualGuilds: Flow<PolyGuild>
        get() = jdaUser.mutualGuilds.asFlow().map { it.poly(bot) }
    override val avatarUrl: String
        get() = jdaUser.effectiveAvatarUrl
    override val defaultAvatarId: String
        get() = jdaUser.defaultAvatarId
    override val defaultAvatarUrl: String
        get() = jdaUser.defaultAvatarUrl
    override val customAvatarId: String?
        get() = jdaUser.avatarId
    override val customAvatarUrl: String?
        get() = jdaUser.avatarUrl
    override val isAvatarAnimated: Boolean
        get() = jdaUser.avatarUrl?.endsWith(".gif") ?: false
    override val isAvatarCustom: Boolean
        get() = jdaUser.avatarUrl != null
    override val isBot: Boolean
        get() = jdaUser.isBot
    override val isSystem: Boolean
        get() = jdaUser.isSystem
    override val isDiscordStaff: Boolean
        get() = User.UserFlag.STAFF in jdaUser.flags
    override val isDiscordPartner: Boolean
        get() = User.UserFlag.PARTNER in jdaUser.flags
    override val isDiscordVerifiedBot: Boolean
        get() = User.UserFlag.VERIFIED_BOT in jdaUser.flags
    override val isDiscordVerifiedDeveloper: Boolean
        get() = User.UserFlag.VERIFIED_DEVELOPER in jdaUser.flags
    override val isDiscordCertifiedModerator: Boolean
        get() = User.UserFlag.CERTIFIED_MODERATOR in jdaUser.flags
    
    override val jdaUser: User
        get() = jdaMember.user
    
    override val guild: PolyGuild
        get() = jdaMember.guild.poly(bot)
    override val guildId: ULong
        get() = jdaMember.guild.idLong.toULong()
    override val timeJoined: Instant
        get() = jdaMember.timeJoined.toInstant().toKotlinInstant()
    override val timeBoosted: Instant?
        get() = jdaMember.timeBoosted?.run { toInstant().toKotlinInstant() }
    override val nickname: String?
        get() = jdaMember.nickname
    override val hasNickname: Boolean
        get() = jdaMember.nickname != null
    override val effectiveName: String
        get() = jdaMember.effectiveName
    override val roles: Flow<PolyRole>
        get() = jdaMember.roles.asFlow().map { it.poly(bot) }
    override val color: Color?
        get() = jdaMember.color
    override val isGuildOwner: Boolean
        get() = jdaMember.isOwner
    override val jdaPermissionHolder: IPermissionHolder
        get() = jdaMember
    override val permissions: EnumSet<Permission>
        get() = jdaMember.permissions
    override val explicitPermissions: EnumSet<Permission>
        get() = jdaMember.permissionsExplicit
    
    override suspend fun changeNickname(nickname: String?) {
        jdaMember.modifyNickname(nickname)
                .await()
    }
    
    override fun getPermissions(channel: PolyGuildChannel): EnumSet<Permission> {
        return jdaMember.getPermissions(channel.jdaChannel)
    }
    
    override fun getExplicitPermissions(channel: PolyGuildChannel): EnumSet<Permission> {
        return jdaMember.getPermissionsExplicit(channel.jdaChannel)
    }
    
    override fun contains(permission: Permission): Boolean {
        return jdaMember.hasPermission(permission)
    }
    
    override fun containsAll(permissions: Collection<Permission>): Boolean {
        return jdaMember.hasPermission(permissions)
    }
    
    override fun containsAll(channel: PolyGuildChannel, permissions: Collection<Permission>): Boolean {
        return jdaMember.hasPermission(channel.jdaChannel, permissions)
    }
    
    override fun hasAccess(channel: PolyGuildChannel): Boolean {
        return jdaMember.hasAccess(channel.jdaChannel)
    }
    
    override suspend fun privateChannel(): PolyPrivateChannel {
        return jdaUser.openPrivateChannel()
                .await()
                .poly(bot)
    }
    
    override val asMention: String
        get() = jdaMember.asMention
    
    override fun toString(): String = asMention
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PolyMemberImpl) return false
        
        if (jdaMember != other.jdaMember) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        return jdaMember.hashCode()
    }
}
