/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyGuild.kt is part of PolyhedralBot
 * Last modified on 06-02-2022 04:05 p.m.
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

package ca.solostudios.polybot.api.entities

import java.awt.Color
import java.util.Locale
import kotlinx.coroutines.flow.Flow
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Icon
import kotlin.time.Duration

public interface PolyGuild : PolySnowflakeEntity {
    /**
     * The JDA guild that is being wrapped by this entity
     */
    public val jdaGuild: Guild
    
    public val loaded: Boolean
    
    public val available: Boolean
    
    public val memberCount: Int
    
    public val name: String
    
    public val iconId: String?
    
    public val iconUrl: String?
    
    public val features: Set<String>
    
    public val splashId: String?
    
    public val splashUrl: String?
    
    public val vanityCode: String?
    
    public val vanityUrl: String?
    
    public val description: String?
    
    public val bannerId: String?
    
    public val bannerUrl: String?
    
    public val locale: Locale
    
    public val boostTier: Guild.BoostTier
    
    public val boosts: Int
    
    public val boostRole: PolyRole?
    
    public val boosters: Flow<PolyMember>
    
    public val verificationLevel: Guild.VerificationLevel
    
    public val notificationLevel: Guild.NotificationLevel
    
    public val requiredMFALevel: Guild.MFALevel
    
    public val explicitContentLevel: Guild.ExplicitContentLevel
    
    public val maxBitrate: Int
    
    public val maxFileSize: Long
    
    public val maxMembers: Int
    
    public val afkChannel: PolyPrivateChannel?
    
    public val systemChannel: PolyTextChannel?
    
    public val communityUpdatesChannel: PolyTextChannel?
    
    public val owner: PolyMember?
    
    public val afkTimeout: Duration
    
    public val selfMember: PolyMember
    
    public val botRole: PolyRole?
    
    public val channels: List<PolyGuildChannel>
    
    public val textChannels: List<PolyTextChannel>
    
    public val voiceChannels: List<PolyVoiceChannel>
    
    public val categories: List<PolyCategory>
    
    public val roles: List<PolyRole>
    
    public val emotes: List<PolyEmote>
    
    public suspend fun memberById(id: ULong): PolyMember
    
    public suspend fun prune(days: Int, wait: Boolean, vararg roles: PolyRole)
    
    public suspend fun kick(member: PolyMember, reason: String? = null)
    
    public suspend fun kick(member: ULong, reason: String? = null)
    
    public suspend fun ban(user: PolyUser, delDays: Int = 3, reason: String? = null)
    
    public suspend fun ban(user: ULong, delDays: Int = 3, reason: String? = null)
    
    public suspend fun unban(user: PolyUser)
    
    public suspend fun unban(user: ULong)
    
    public suspend fun deafen(member: PolyMember, deafen: Boolean)
    
    public suspend fun mute(member: PolyMember, mute: Boolean)
    
    public suspend fun addRole(member: PolyMember, role: PolyRole)
    
    public suspend fun removeRole(member: PolyMember, role: PolyRole)
    
    public suspend fun modifyRoles(member: PolyMember, rolesToAdd: List<PolyRole>? = null, rolesToRemove: List<PolyRole>? = null)
    
    public suspend fun createTextChannel(
            name: String,
            parent: PolyCategory? = null,
            position: Int? = null,
            topic: String? = null,
            slowmode: Int? = null,
            news: Boolean? = null,
                                        ): PolyTextChannel
    
    public suspend fun createVoiceChannel(
            name: String,
            parent: PolyCategory? = null,
            position: Int? = null,
            bitrate: Int? = null,
            userLimit: Int? = null,
                                         ): PolyVoiceChannel
    
    public suspend fun createCategory(
            name: String,
                                     ): PolyCategory
    
    
    public suspend fun createRole(
            name: String,
            hoisted: Boolean? = null,
            mentionable: Boolean? = null,
            color: Color? = null,
            permissions: List<Permission>? = null,
                                 ): PolyRole
    
    public suspend fun createEmote(
            name: String,
            icon: Icon
                                  )
    
}