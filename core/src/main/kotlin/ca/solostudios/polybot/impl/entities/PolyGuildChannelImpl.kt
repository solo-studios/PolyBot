/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyGuildChannelImpl.kt is part of PolyBot
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
import ca.solostudios.polybot.api.entities.PolyCategory
import ca.solostudios.polybot.api.entities.PolyGuild
import ca.solostudios.polybot.api.entities.PolyGuildChannel
import ca.solostudios.polybot.api.entities.PolyMember
import ca.solostudios.polybot.api.entities.PolyPermissionHolder
import ca.solostudios.polybot.api.entities.Snowflake
import ca.solostudios.polybot.api.entities.Snowflake.Companion.snowflake
import ca.solostudios.polybot.api.util.ext.poly
import dev.minn.jda.ktx.await
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import net.dv8tion.jda.api.entities.GuildChannel
import net.dv8tion.jda.api.entities.Invite
import net.dv8tion.jda.api.entities.PermissionOverride
import kotlin.time.Duration

internal open class PolyGuildChannelImpl(
        override val bot: PolyBot,
        override val jdaChannel: GuildChannel,
                                        ) : PolyGuildChannel {
    override val snowflake: Snowflake by lazy { jdaChannel.idLong.snowflake() }
    
    override val name: String
        get() = jdaChannel.name
    
    override val guild: PolyGuild
        get() = jdaChannel.guild.poly(bot)
    override val parent: PolyCategory?
        get() = jdaChannel.parent?.poly(bot)
    override val members: Flow<PolyMember>
        get() = jdaChannel.members.asFlow().map { it.poly(bot) }
    override val position: Int
        get() = jdaChannel.position
    override val positionRaw: Int
        get() = jdaChannel.positionRaw
    override val permissionOverrides: Flow<PermissionOverride>
        get() = jdaChannel.permissionOverrides.asFlow()
    override val memberPermissionOverrides: Flow<PermissionOverride>
        get() = jdaChannel.memberPermissionOverrides.asFlow()
    override val rolePermissionOverrides: Flow<PermissionOverride>
        get() = jdaChannel.rolePermissionOverrides.asFlow()
    override val isSynced: Boolean
        get() = jdaChannel.isSynced
    override val invites: Flow<Invite>
        get() = flow {
            val invites = jdaChannel.retrieveInvites().await()
            
            for (invite in invites)
                emit(invite)
        }.flowOn(Dispatchers.IO)
    
    override fun getPermissionOverride(permissionHolder: PolyPermissionHolder): PermissionOverride? {
        return jdaChannel.getPermissionOverride(permissionHolder.jdaPermissionHolder)
    }
    
    override suspend fun delete(reason: String?) {
        jdaChannel.delete()
                .await()
    }
    
    override suspend fun setName(name: String, reason: String?) {
        jdaChannel.manager.setName(name)
                .reason(reason)
                .await()
    }
    
    override suspend fun setCategory(parent: PolyCategory?, reason: String?) {
        jdaChannel.manager.setParent(parent?.jdaCategory)
                .reason(reason)
                .await()
    }
    
    override suspend fun setPosition(position: Int, reason: String?) {
        jdaChannel.manager.setPosition(position)
                .reason(reason)
                .await()
    }
    
    override suspend fun createInvite(maxAge: Duration?, maxUses: Int?, temporary: Boolean?, unique: Boolean?): Invite {
        return jdaChannel.createInvite()
                .setMaxAge(maxAge?.inWholeMilliseconds, TimeUnit.MILLISECONDS)
                .setMaxUses(maxUses)
                .setTemporary(temporary)
                .setUnique(unique)
                .await()
    }
    
    override val asMention: String
        get() = jdaChannel.asMention
    
    override fun toString(): String = asMention
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PolyGuildChannelImpl) return false
        
        if (jdaChannel != other.jdaChannel) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        return jdaChannel.hashCode()
    }
}