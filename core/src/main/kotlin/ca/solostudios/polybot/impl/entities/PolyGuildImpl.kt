/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyGuildImpl.kt is part of PolyBot
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
import ca.solostudios.polybot.api.entities.PolyEmote
import ca.solostudios.polybot.api.entities.PolyGuild
import ca.solostudios.polybot.api.entities.PolyGuildChannel
import ca.solostudios.polybot.api.entities.PolyMember
import ca.solostudios.polybot.api.entities.PolyRole
import ca.solostudios.polybot.api.entities.PolyTextChannel
import ca.solostudios.polybot.api.entities.PolyUser
import ca.solostudios.polybot.api.entities.PolyVoiceChannel
import ca.solostudios.polybot.api.entities.Snowflake
import ca.solostudios.polybot.api.entities.Snowflake.Companion.snowflake
import ca.solostudios.polybot.api.util.ext.poly
import dev.minn.jda.ktx.await
import java.awt.Color
import java.util.Locale
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Icon
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import org.slf4j.kotlin.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal class PolyGuildImpl(
        override val bot: PolyBot,
        override val jdaGuild: Guild,
                            ) : PolyGuild {
    override val snowflake: Snowflake by lazy { jdaGuild.idLong.snowflake() }
    
    private val logger by getLogger()
    
    override val isLoaded: Boolean
        get() = jdaGuild.isLoaded
    override val isAvailable: Boolean
        get() = jdaGuild.jda.isUnavailable(idLong)
    override val memberCount: Int
        get() = jdaGuild.memberCount
    override val name: String
        get() = jdaGuild.name
    override val iconId: String?
        get() = jdaGuild.iconId
    override val iconUrl: String?
        get() = jdaGuild.iconUrl
    override val features: Set<String>
        get() = jdaGuild.features
    override val splashId: String?
        get() = jdaGuild.splashId
    override val splashUrl: String?
        get() = jdaGuild.splashUrl
    override val vanityCode: String?
        get() = jdaGuild.vanityCode
    override val vanityUrl: String?
        get() = jdaGuild.vanityUrl
    override val description: String?
        get() = jdaGuild.description
    override val locale: Locale
        get() = jdaGuild.locale
    override val bannerId: String?
        get() = jdaGuild.bannerId
    override val bannerUrl: String?
        get() = jdaGuild.bannerUrl
    override val boostTier: Guild.BoostTier
        get() = jdaGuild.boostTier
    override val boosts: Int
        get() = jdaGuild.boostCount
    override val boostRole: PolyRole?
        get() = jdaGuild.boostRole?.poly(bot)
    override val boosters: Flow<PolyMember>
        get() = jdaGuild.boosters.asFlow().map { it.poly(bot) }
    override val verificationLevel: Guild.VerificationLevel
        get() = jdaGuild.verificationLevel
    override val notificationLevel: Guild.NotificationLevel
        get() = jdaGuild.defaultNotificationLevel
    override val requiredMFALevel: Guild.MFALevel
        get() = jdaGuild.requiredMFALevel
    override val explicitContentLevel: Guild.ExplicitContentLevel
        get() = jdaGuild.explicitContentLevel
    override val maxBitrate: Int
        get() = jdaGuild.maxBitrate
    override val maxFileSize: Long
        get() = jdaGuild.maxFileSize
    override val maxEmotes: Int
        get() = jdaGuild.maxEmotes
    override val maxMembers: Int
        get() = jdaGuild.maxMembers
    override val maxPresences: Int
        get() = jdaGuild.maxPresences
    override val afkChannel: PolyVoiceChannel?
        get() = jdaGuild.afkChannel?.poly(bot)
    override val systemChannel: PolyTextChannel?
        get() = jdaGuild.systemChannel?.poly(bot)
    override val rulesChannel: PolyTextChannel?
        get() = jdaGuild.rulesChannel?.poly(bot)
    override val communityUpdatesChannel: PolyTextChannel?
        get() = jdaGuild.communityUpdatesChannel?.poly(bot)
    override val owner: PolyMember?
        get() = jdaGuild.owner?.poly(bot)
    override val afkTimeout: Duration
        get() = jdaGuild.afkTimeout.seconds.seconds
    override val selfMember: PolyMember
        get() = jdaGuild.selfMember.poly(bot)
    override val botRole: PolyRole?
        get() = jdaGuild.botRole?.poly(bot)
    override val channels: Flow<PolyGuildChannel>
        get() = jdaGuild.channels.asFlow().map { it.poly(bot) }
    override val textChannels: Flow<PolyTextChannel>
        get() = jdaGuild.textChannelCache.asFlow().map { it.poly(bot) }
    override val voiceChannels: Flow<PolyVoiceChannel>
        get() = jdaGuild.voiceChannelCache.asFlow().map { it.poly(bot) }
    override val categories: Flow<PolyCategory>
        get() = jdaGuild.categoryCache.asFlow().map { it.poly(bot) }
    override val roles: Flow<PolyRole>
        get() = jdaGuild.roleCache.asFlow().map { it.poly(bot) }
    override val emotes: Flow<PolyEmote>
        get() = jdaGuild.emoteCache.asFlow().map { it.poly(bot) }
    
    override suspend fun getMetadata(): Guild.MetaData {
        return jdaGuild.retrieveMetaData().await()
    }
    
    override suspend fun memberById(id: ULong): PolyMember? {
        return try {
            jdaGuild.retrieveMemberById(id.toLong()).await().poly(bot)
        } catch (e: ErrorResponseException) {
            null
        }
    }
    
    override suspend fun memberByTag(tag: String): PolyMember? {
        return jdaGuild.getMemberByTag(tag)?.poly(bot)
    }
    
    override suspend fun memberByUser(user: PolyUser): PolyMember? {
        return jdaGuild.getMember(user.jdaUser)?.poly(bot)
    }
    
    override suspend fun findMembers(filter: (PolyMember) -> Boolean): Flow<PolyMember> {
        return callbackFlow {
            loadMembers {
                if (filter(it))
                    trySendBlocking(it).onFailure {
                        logger.debug { "Attempted to send member to channel, but channel was closed. Guild $name, id $id" }
                    }
            }
            
            awaitClose()
        }.buffer()
    }
    
    override suspend fun findMembersWithRoles(roles: Collection<PolyRole>): Flow<PolyMember> {
        return findMembers { true }.filter { it.roles.toList().containsAll(roles) }
    }
    
    override suspend fun loadMembers(callback: (PolyMember) -> Unit) {
        logger.debug { "Loading all members from guild $name, id $id" }
        jdaGuild.loadMembers {
            callback(it.poly(bot))
        }
    }
    
    override suspend fun prune(days: Int, wait: Boolean, vararg roles: PolyRole) {
        jdaGuild.prune(days, wait, *roles.map { it.jdaRole }.toTypedArray()).await()
    }
    
    override suspend fun kick(member: PolyMember, reason: String?) {
        jdaGuild.kick(member.jdaMember)
                .reason(reason)
                .await()
    }
    
    override suspend fun kick(member: ULong, reason: String?) {
        jdaGuild.kick(member.toLong().toString())
                .reason(reason)
                .await()
    }
    
    override suspend fun ban(user: PolyUser, delDays: Int, reason: String?) {
        jdaGuild.ban(user.jdaUser, delDays)
                .reason(reason)
                .await()
    }
    
    override suspend fun ban(user: ULong, delDays: Int, reason: String?) {
        jdaGuild.ban(user.toLong().toString(), delDays)
                .reason(reason)
                .await()
    }
    
    override suspend fun unban(user: PolyUser, reason: String?) {
        jdaGuild.unban(user.jdaUser)
                .reason(reason)
                .await()
    }
    
    override suspend fun unban(user: ULong, reason: String?) {
        jdaGuild.unban(user.toLong().toString())
                .reason(reason)
                .await()
    }
    
    override suspend fun deafen(member: PolyMember, deafen: Boolean, reason: String?) {
        jdaGuild.deafen(member.jdaMember, deafen)
                .reason(reason)
                .await()
    }
    
    override suspend fun mute(member: PolyMember, mute: Boolean, reason: String?) {
        jdaGuild.mute(member.jdaMember, mute)
                .reason(reason)
                .await()
    }
    
    override suspend fun addRole(member: PolyMember, role: PolyRole, reason: String?) {
        jdaGuild.addRoleToMember(member.jdaMember, role.jdaRole)
                .reason(reason)
                .await()
    }
    
    override suspend fun removeRole(member: PolyMember, role: PolyRole, reason: String?) {
        jdaGuild.removeRoleFromMember(member.jdaMember, role.jdaRole)
                .reason(reason)
                .await()
    }
    
    override suspend fun modifyRoles(member: PolyMember, rolesToAdd: List<PolyRole>?, rolesToRemove: List<PolyRole>?, reason: String?) {
        // jdaGuild.modifyMemberRoles(member.jdaMember, rolesToAdd)
        val roles = member.roles
        
        val newRoles = mutableListOf<Role>()
        
        if (rolesToAdd != null)
            newRoles.addAll(rolesToAdd.map { it.jdaRole })
        
        if (rolesToRemove != null)
            roles.collectLatest {
                if (!it.isPublicRole && it !in rolesToRemove)
                    newRoles.add(it.jdaRole)
            }
        
        jdaGuild.modifyMemberRoles(member.jdaMember, newRoles)
                .reason(reason)
                .await()
    }
    
    override suspend fun createTextChannel(name: String,
                                           parent: PolyCategory?,
                                           position: Int?,
                                           topic: String?,
                                           slowmode: Int?,
                                           news: Boolean?): PolyTextChannel {
        val channelAction = jdaGuild.createTextChannel(name)
                .setParent(parent?.jdaCategory)
                .setPosition(position)
                .setTopic(topic)
        
        if (slowmode != null)
            channelAction.setSlowmode(slowmode)
        if (news != null)
            channelAction.setNews(news)
        
        return channelAction.await().poly(bot)
    }
    
    override suspend fun createVoiceChannel(name: String,
                                            parent: PolyCategory?,
                                            position: Int?,
                                            bitrate: Int?,
                                            userLimit: Int?): PolyVoiceChannel {
        return jdaGuild.createVoiceChannel(name)
                .setParent(parent?.jdaCategory)
                .setPosition(position)
                .setBitrate(bitrate)
                .setUserlimit(userLimit)
                .await()
                .poly(bot)
    }
    
    override suspend fun createCategory(name: String): PolyCategory {
        return jdaGuild.createCategory(name)
                .await()
                .poly(bot)
    }
    
    override suspend fun createRole(name: String,
                                    hoisted: Boolean?,
                                    mentionable: Boolean?,
                                    color: Color?,
                                    permissions: List<Permission>?): PolyRole {
        return jdaGuild.createRole()
                .setName(name)
                .setHoisted(hoisted)
                .setMentionable(mentionable)
                .setColor(color)
                .setPermissions(permissions)
                .await()
                .poly(bot)
    }
    
    override suspend fun createEmote(name: String, icon: Icon): PolyEmote {
        return jdaGuild.createEmote(name, icon, null, null)
                .await()
                .poly(bot)
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PolyGuildImpl) return false
        
        if (jdaGuild != other.jdaGuild) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        return jdaGuild.hashCode()
    }
}