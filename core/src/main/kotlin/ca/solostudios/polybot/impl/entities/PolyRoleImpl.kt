/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyRoleImpl.kt is part of PolyBot
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
import ca.solostudios.polybot.api.entities.PolyRole
import ca.solostudios.polybot.api.entities.Snowflake
import ca.solostudios.polybot.api.entities.Snowflake.Companion.snowflake
import ca.solostudios.polybot.api.util.ext.poly
import dev.minn.jda.ktx.await
import java.awt.Color
import java.util.EnumSet
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.IPermissionHolder
import net.dv8tion.jda.api.entities.Role

internal class PolyRoleImpl(
        override val bot: PolyBot,
        override val jdaRole: Role,
                           ) : PolyRole {
    override val snowflake: Snowflake by lazy { jdaRole.idLong.snowflake() }
    
    override val name: String
        get() = jdaRole.name
    override val isManaged: Boolean
        get() = jdaRole.isManaged
    override val isHoisted: Boolean
        get() = jdaRole.isHoisted
    override val isMentionable: Boolean
        get() = jdaRole.isMentionable
    override val color: Color?
        get() = jdaRole.color
    override val isPublicRole: Boolean
        get() = jdaRole.isPublicRole
    override val guild: PolyGuild
        get() = jdaRole.guild.poly(bot)
    override val guildId: ULong
        get() = jdaRole.guild.idLong.toULong()
    override val isBot: Boolean
        get() = jdaRole.tags.isBot
    override val botId: ULong
        get() = jdaRole.tags.botIdLong.toULong()
    override val isBoost: Boolean
        get() = jdaRole.tags.isBoost
    override val isIntegration: Boolean
        get() = jdaRole.tags.isIntegration
    override val integrationId: ULong
        get() = jdaRole.tags.integrationIdLong.toULong()
    override val jdaPermissionHolder: IPermissionHolder
        get() = jdaRole
    override val permissions: EnumSet<Permission>
        get() = jdaRole.permissions
    override val explicitPermissions: EnumSet<Permission>
        get() = jdaRole.permissionsExplicit
    
    override fun getPermissions(channel: PolyGuildChannel): EnumSet<Permission> {
        return jdaRole.getPermissions(channel.jdaChannel)
    }
    
    override fun getExplicitPermissions(channel: PolyGuildChannel): EnumSet<Permission> {
        return jdaRole.getPermissionsExplicit(channel.jdaChannel)
    }
    
    override fun contains(permission: Permission): Boolean {
        return jdaRole.hasPermission(permission)
    }
    
    override fun containsAll(permissions: Collection<Permission>): Boolean {
        return jdaRole.hasPermission(permissions)
    }
    
    override fun containsAll(channel: PolyGuildChannel, permissions: Collection<Permission>): Boolean {
        return jdaRole.hasPermission(channel.jdaChannel, permissions)
    }
    
    override fun hasAccess(channel: PolyGuildChannel): Boolean {
        return jdaRole.hasAccess(channel.jdaChannel)
    }
    
    override fun canInteract(role: Role): Boolean {
        return jdaRole.canInteract(role)
    }
    
    override suspend fun delete(reason: String?) {
        jdaRole.delete()
                .reason(reason)
                .await()
    }
    
    override val asMention: String
        get() = jdaRole.asMention
    
    override fun toString(): String = asMention
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PolyRoleImpl) return false
        
        if (jdaRole != other.jdaRole) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        return jdaRole.hashCode()
    }
}