/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyGuild.kt is part of PolyBot
 * Last modified on 10-06-2022 11:33 a.m.
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

package ca.solostudios.polybot.api.entities

import java.awt.Color
import java.util.Locale
import kotlinx.coroutines.flow.Flow
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.Permission.BAN_MEMBERS
import net.dv8tion.jda.api.Permission.KICK_MEMBERS
import net.dv8tion.jda.api.Permission.MANAGE_CHANNEL
import net.dv8tion.jda.api.Permission.MANAGE_EMOTES
import net.dv8tion.jda.api.Permission.MANAGE_ROLES
import net.dv8tion.jda.api.Permission.VOICE_DEAF_OTHERS
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Icon
import net.dv8tion.jda.api.exceptions.HierarchyException
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException
import kotlin.time.Duration

public interface PolyGuild : PolySnowflakeEntity {
    /**
     * The JDA guild that is being wrapped by this entity
     */
    public val jdaGuild: Guild
    
    /**
     * Whether this guild has loaded members.
     *
     * @see Guild.isLoaded
     */
    public val isLoaded: Boolean
    
    
    public val isAvailable: Boolean
    
    /**
     * The expected member count for this guild.
     *
     * @see Guild.getMemberCount
     */
    public val memberCount: Int
    
    /**
     * The human-readable name of the guild.
     */
    public val name: String
    
    /**
     * The discord hash-id of the guild icon image.
     * If no icon has been set, returns `null`.
     */
    public val iconId: String?
    
    /**
     * The URL of the guild icon image.
     *
     * If no icon has been set, this returns null.
     */
    public val iconUrl: String?
    
    /**
     * A set of all features, which apply to the guild.
     *
     * @see Guild.getFeatures
     */
    public val features: Set<String>
    
    /**
     * The discord hash-id of the splash image url for this guild.
     *
     * A splash image is displayed when viewing the discord guild invite
     * on the web or in the client before accepting or declining the invite.
     * If no splash image has been set, this returns `null`.
     *
     * Splash images are VIP/Partner guilds only.
     */
    public val splashId: String?
    
    /**
     * The URL of the splash image for this guild.
     *
     * A splash image is displayed when viewing the discord guild invite
     * on the web or in the client before accepting or declining the invite.
     * If no splash image has been set, this returns `null`.
     *
     * Splash images are VIP/Partner guilds only.
     */
    public val splashUrl: String?
    
    /**
     * The vanity URL code for this guild.
     * The vanity URL is the custom invite code of partered/official/boosted guilds.
     *
     * The returned string will be the code that can be appended to `discord.gg/` to get the invite link.
     */
    public val vanityCode: String?
    
    /**
     * The vanity URL for this guild.
     * The vanity URL is the custom invite code of partnered/official/boosted guilds.
     */
    public val vanityUrl: String?
    
    /**
     * The description for this guild.
     *
     * This is displayed in the server browser below the guild name for verified guilds.
     */
    public val description: String?
    
    /**
     * The preferred locale for this guild.
     *
     * If the guild doesn't have the `COMMUNITY` feature, this returns the default locale.
     *
     * Default: [Locale.US]
     */
    public val locale: Locale
    
    /**
     * The guild banner id.
     *
     * This is shown in guilds below the guild name.
     */
    public val bannerId: String?
    
    /**
     * The guild banner URL.
     *
     * This is shown in guilds below the guild name.
     */
    public val bannerUrl: String?
    
    /**
     * The boost tier for this guild.
     *
     * Each tier unlocks new perks for a guild that can be seen in the [features].
     */
    public val boostTier: Guild.BoostTier
    
    /**
     * The amount of boosts this guild currently has.
     */
    public val boosts: Int
    
    public val boostRole: PolyRole?
    
    /**
     * A [Flow] of all the members who boosted this guild, sorted by [PolyMember.timeBoosted],
     * where the first element has the longest time.
     */
    public val boosters: Flow<PolyMember>
    
    public val verificationLevel: Guild.VerificationLevel
    
    public val notificationLevel: Guild.NotificationLevel
    
    public val requiredMFALevel: Guild.MFALevel
    
    public val explicitContentLevel: Guild.ExplicitContentLevel
    
    /**
     * The maximum bitrate that can be applied to a voice channel in this guild.
     *
     * This depends on the features of this guild that can be unlocked for partners or through boosting.
     */
    public val maxBitrate: Int
    
    /**
     * Returns the maximum size for files that can be uploaded to this Guild.
     * This returns 8 MiB for Guilds without a Boost Tier or Guilds with Boost Tier 1,
     * 50 MiB for Guilds with Boost Tier 2 and 100 MiB for Guilds with Boost Tier 3.
     */
    public val maxFileSize: Long
    
    /**
     * The maximum amount of emotes a guild can have based on the guilds boost tier.
     */
    public val maxEmotes: Int
    
    /**
     * The maximum amount of members that can join this guild.
     */
    public val maxMembers: Int
    
    /**
     * The maximum amount of connected members this guild can have at a time.
     *
     * This includes members that are invisible but still connected to discord.
     * If too many members are online the guild will become unavailable for others.
     */
    public val maxPresences: Int
    
    /**
     * Provides the [PolyVoiceChannel] that has been set as the channel,
     * which [PolyMember] will be moved to after they have been inactive in a
     * [PolyVoiceChannel] for longer than [afkTimeout].
     *
     * If no channel has been set as the AFK channel, this returns `null`.
     */
    public val afkChannel: PolyVoiceChannel?
    
    /**
     * Provides the [PolyTextChannel] that has been set as the channel,
     * which newly joined [PolyMember]s will be announced in.
     *
     * If no channel has been set as the system channel, this returns `null`.
     */
    public val systemChannel: PolyTextChannel?
    
    /**
     * Provides the [PolyTextChannel] that lists the rules of the guild.
     *
     * If this guild doesn't have the `COMMUNITY` [features], this returns `null`.
     */
    public val rulesChannel: PolyTextChannel?
    
    /**
     * Provides the [PolyTextChannel] that receives community updates.
     *
     * If this guild doesn't have the `COMMUNITY` feature, this returns `null`.
     */
    public val communityUpdatesChannel: PolyTextChannel?
    
    /**
     * The [PolyMember] object for the owner of this guild.
     *
     * This is `null`, when the owner is no longer in this guild or not yet loaded.
     * Sometimes owners of guilds delete their account or get banned by discord.
     */
    public val owner: PolyMember?
    
    /**
     * The afk timeout for the guild.
     *
     * If a [PolyMember] is in a [PolyVoiceChannel] and this amount of time passed
     * with the member having no activity in the channel, they are moved to the [afkChannel].
     */
    public val afkTimeout: Duration
    
    /**
     * The [PolyMember] object of the currently logged in account for this guild.
     */
    public val selfMember: PolyMember
    
    public val botRole: PolyRole?
    
    public val channels: Flow<PolyGuildChannel>
    
    public val textChannels: Flow<PolyTextChannel>
    
    public val voiceChannels: Flow<PolyVoiceChannel>
    
    public val categories: Flow<PolyCategory>
    
    public val roles: Flow<PolyRole>
    
    public val emotes: Flow<PolyEmote>
    
    /**
     * Loads the [Guild.MetaData] for this guild instance.
     *
     * @return The guild metadata.
     */
    public suspend fun getMetadata(): Guild.MetaData
    
    /**
     * Gets a member object via the id of the user.
     *
     * @param id The id of the member to retrieve.
     * @return The member object of the user with the specified id. `null` if the user is not in this guild.
     */
    public suspend fun memberById(id: ULong): PolyMember?
    
    /**
     * Gets a member object via the user tag.
     *
     * @param tag The tag of the member to retrieve.
     * @return The member object of the user with the specified tag. `null` if the user is not in this guild.
     */
    public suspend fun memberByTag(tag: String): PolyMember?
    
    /**
     * Gets a member object via the user tag.
     *
     * If the member is already loaded, returns immediately.
     *
     * @param user The user to load.
     * @return The loaded member for the provided user.
     */
    public suspend fun memberByUser(user: PolyUser): PolyMember?
    
    /**
     * Retrieves members of this guild based on the provided filter.
     *
     * @param filter The filter used to decide, which members to include.
     * @return A [Flow] of members that match the filter.
     */
    public suspend fun findMembers(filter: (PolyMember) -> Boolean): Flow<PolyMember>
    
    /**
     * Retrieves members of this guild, which have the provided role
     *
     * @param roles The roles used to filter the members.
     * @return A [Flow] of members that have the specified roles.
     */
    public suspend fun findMembersWithRoles(roles: Collection<PolyRole>): Flow<PolyMember>
    
    /**
     * Retrieves all members of this guild, and applies the callback to each.
     *
     * @param callback The callback to invoke on each member.
     */
    public suspend fun loadMembers(callback: (PolyMember) -> Unit)
    
    /**
     * Prunes the members of this guild who have been offline for at least [days] days.
     *
     * @param days
     * @param wait
     * @param roles
     *
     * @throws InsufficientPermissionException If the account doesn't have [KICK_MEMBERS] permission.
     * @throws IllegalArgumentException If
     *   - The provided days are not within 1-30 (inclusive)
     *   - If the provided roles are not from this guild
     */
    @Throws(InsufficientPermissionException::class, IllegalArgumentException::class)
    public suspend fun prune(days: Int, wait: Boolean, vararg roles: PolyRole)
    
    /**
     * Kicks the specified member from the guild.
     *
     * @param member The member to kick
     * @param reason The reason the member was kicked.
     *
     * @throws InsufficientPermissionException If the logged in account does not have the [KICK_MEMBERS] permission.
     * @throws IllegalArgumentException If the provided member is not a member of the guild, or the reason is longer than 512 characters.
     * @throws HierarchyException If the logged in account cannot kick the mbmer due to a permission hierarchy position.
     */
    @Throws(InsufficientPermissionException::class, IllegalArgumentException::class, HierarchyException::class)
    public suspend fun kick(member: PolyMember, reason: String? = null)
    
    /**
     * Kicks the specified member from the guild.
     *
     * @param member The member to kick
     * @param reason The reason the member was kicked.
     *
     * @throws InsufficientPermissionException If the logged in account does not have the [KICK_MEMBERS] permission.
     * @throws IllegalArgumentException If the provided member is not a member of the guild, or the reason is longer than 512 characters.
     * @throws HierarchyException If the logged in account cannot kick the mbmer due to a permission hierarchy position.
     */
    @Throws(InsufficientPermissionException::class, IllegalArgumentException::class, HierarchyException::class)
    public suspend fun kick(member: ULong, reason: String? = null)
    
    /**
     * Bans a user and deletes the last [delDays] days of messages sent by the user.
     * To ban a user without deleting any days, set [delDays] to 0.
     *
     * @param user The user to ban.
     * @param delDays The amount of days to delete.
     * Must be between 0 and 7.
     * @param reason The reason this user is being banned.
     *
     * @throws InsufficientPermissionException If the logged in account is missing [BAN_MEMBERS] permission.
     * @throws IllegalArgumentException
     *   - If the provided amount of days is less than 0
     *   - If the provided amount of days is more than 7
     *   - If the provided reason is greater than 512 characters
     * @throws HierarchyException If the logged in account cannot ban the other user due to permission hierarchy position
     */
    @Throws(InsufficientPermissionException::class, IllegalArgumentException::class, HierarchyException::class)
    public suspend fun ban(user: PolyUser, delDays: Int = 3, reason: String? = null)
    
    /**
     * Bans a user and deletes the last [delDays] days of messages sent by the user.
     * To ban a user without deleting any days, set [delDays] to 0.
     *
     * @param user The user to ban.
     * @param delDays The amount of days to delete.
     * Must be between 0 and 7.
     * @param reason The reason this user is being banned.
     *
     * @throws InsufficientPermissionException If the logged in account is missing [BAN_MEMBERS] permission.
     * @throws IllegalArgumentException
     *   - If the provided amount of days is less than 0
     *   - If the provided amount of days is more than 7
     *   - If the provided reason is greater than 512 characters
     * @throws HierarchyException If the logged in account cannot ban the other user due to permission hierarchy position
     */
    @Throws(InsufficientPermissionException::class, IllegalArgumentException::class, HierarchyException::class)
    public suspend fun ban(user: ULong, delDays: Int = 3, reason: String? = null)
    
    /**
     * Unbans the specified user from this guild.
     *
     * @param user The user to unban.
     * @param reason The reason for this unban.
     *
     * @throws InsufficientPermissionException If the logged in account is missing [BAN_MEMBERS] permission.
     */
    @Throws(InsufficientPermissionException::class)
    public suspend fun unban(user: PolyUser, reason: String? = null)
    
    /**
     * Unbans the specified user from this guild.
     *
     * @param user The user to unban.
     * @param reason The reason for this unban.
     *
     * @throws InsufficientPermissionException If the logged in account is missing [BAN_MEMBERS] permission.
     */
    @Throws(InsufficientPermissionException::class)
    public suspend fun unban(user: ULong, reason: String? = null)
    
    /**
     * Sets the guild deafened state of the member based on [deafen].
     *
     * @param member The member whose deafen state to modify.
     * @param deafen Whether this member should be deafened or undeafened.
     * @param reason The reason for this change.
     *
     * @throws InsufficientPermissionException If the logged in account does not have the [VOICE_DEAF_OTHERS] permission.
     * @throws IllegalStateException If the provided member is not currently connected to a voice channel.
     * @throws IllegalArgumentException If the provided member is not from this guild.
     */
    @Throws(InsufficientPermissionException::class, IllegalStateException::class, IllegalArgumentException::class)
    public suspend fun deafen(member: PolyMember, deafen: Boolean, reason: String? = null)
    
    /**
     * Sets the guild muted state of the member based on [mute].
     *
     * @param member The member whose muted state to modify.
     * @param mute Whether this member should be muted or unmuted.
     * @param reason The reason for this change.
     *
     * @throws InsufficientPermissionException If the logged in account does not have the [VOICE_DEAF_OTHERS] permission.
     * @throws IllegalStateException If the provided member is not currently connected to a voice channel.
     * @throws IllegalArgumentException If the provided member is not from this guild.
     */
    @Throws(InsufficientPermissionException::class, IllegalStateException::class, IllegalStateException::class)
    public suspend fun mute(member: PolyMember, mute: Boolean, reason: String? = null)
    
    /**
     * Assigns the provided role to the specified member.
     *
     * @param member The member whose roles to modify.
     * @param role The role to add.
     * @param reason The reason for this modification.
     *
     * @throws HierarchyException If the provided roles are higher in the guild's hierarchy,
     * and thus cannot be modified by the currently logged in account.
     * @throws InsufficientPermissionException If the logged in account does not have the [MANAGE_ROLES] permission.
     * @throws IllegalArgumentException If the provided member is not from this guild or the role is not from this guild.
     */
    @Throws(InsufficientPermissionException::class, HierarchyException::class, IllegalStateException::class)
    public suspend fun addRole(member: PolyMember, role: PolyRole, reason: String? = null)
    
    /**
     * Removes the provided role from the specified member.
     *
     * @param member The member whose roles to modify.
     * @param role The role to remove.
     * @param reason The reason for this modification.
     *
     * @throws HierarchyException If the provided roles are higher in the guild's hierarchy,
     * and thus cannot be modified by the currently logged in account.
     * @throws InsufficientPermissionException If the logged in account does not have the [MANAGE_ROLES] permission.
     * @throws IllegalArgumentException If the provided member is not from this guild or the role is not from this guild.
     */
    @Throws(InsufficientPermissionException::class, HierarchyException::class, IllegalStateException::class)
    public suspend fun removeRole(member: PolyMember, role: PolyRole, reason: String? = null)
    
    /**
     * Modifies the roles of the specified member by adding and removing a collection of roles.
     *
     * @param member The member whose roles to modify.
     * @param rolesToAdd The roles to add to the member.
     * @param rolesToRemove The roles to remove from the member.
     * @param reason The reason for this modification.
     *
     * @throws HierarchyException If the provided roles are higher in the guild's hierarchy,
     * and thus cannot be modified by the currently logged in account.
     * @throws InsufficientPermissionException If the logged in account does not have the [MANAGE_ROLES] permission.
     * @throws IllegalArgumentException If the provided member is not from this guild or the role is not from this guild.
     */
    @Throws(InsufficientPermissionException::class, HierarchyException::class, IllegalStateException::class)
    public suspend fun modifyRoles(
            member: PolyMember,
            rolesToAdd: List<PolyRole>? = null,
            rolesToRemove: List<PolyRole>? = null,
            reason: String? = null,
                                  )
    
    /**
     * Creates a new [PolyTextChannel] in this guild.
     * For this to be successful, the logged in account must have [MANAGE_CHANNEL].
     *
     * Set params to `null` to use defaults.
     *
     * @param name The name of the new channel. Less than 100 characters.
     * @param parent The parent [PolyCategory] of the new channel.
     * @param position The position of the new channel. See [PolyGuildChannel.position] for more info.
     * @param topic The topic of the new channel.
     * @param slowmode The slowmode setting of the new channel.
     * @param news Whether the new channel is a news channel or not.
     * @return The new [PolyTextChannel].
     *
     * @throws InsufficientPermissionException If the logged in account does not have the [MANAGE_CHANNEL] permission.
     * @throws IllegalArgumentException If the provided name is empty or greater than 100 characters.
     */
    @Throws(InsufficientPermissionException::class, IllegalArgumentException::class)
    public suspend fun createTextChannel(
            name: String,
            parent: PolyCategory? = null,
            position: Int? = null,
            topic: String? = null,
            slowmode: Int? = null,
            news: Boolean? = null,
                                        ): PolyTextChannel
    
    /**
     * Creates a new [PolyVoiceChannel] in this guild.
     * For this to be successful, the logged in account must have [MANAGE_CHANNEL].
     *
     * Set params to `null` to use defaults.
     *
     * @param name The name of the new channel. Less than 100 characters.
     * @param parent The parent [PolyCategory] of the new channel.
     * @param position The position of the new channel. See [PolyGuildChannel.position] for more info.
     * @param bitrate The bitrate of the new channel.
     * @param userLimit The userlimit of the new channel.
     * @return The new [PolyVoiceChannel].
     *
     * @throws InsufficientPermissionException If the logged in account does not have the [MANAGE_CHANNEL] permission.
     * @throws IllegalArgumentException If the provided name is empty or greater than 100 characters.
     */
    @Throws(InsufficientPermissionException::class, IllegalArgumentException::class)
    public suspend fun createVoiceChannel(
            name: String,
            parent: PolyCategory? = null,
            position: Int? = null,
            bitrate: Int? = null,
            userLimit: Int? = null,
                                         ): PolyVoiceChannel
    
    /**
     * Creates a new [PolyCategory] in this guild.
     * For this to be successful, the logged in account must have [MANAGE_CHANNEL].
     *
     * Set params to `null` to use defaults.
     *
     * @param name The name of the new channel. Less than 100 characters.
     * @return The new [PolyCategory].
     *
     * @throws InsufficientPermissionException If the logged in account does not have the [MANAGE_CHANNEL] permission.
     * @throws IllegalArgumentException If the provided name is empty or greater than 100 characters.
     */
    @Throws(InsufficientPermissionException::class, IllegalArgumentException::class)
    public suspend fun createCategory(
            name: String,
                                     ): PolyCategory
    
    /**
     * Creates a new [PolyRole] in this guild.
     * For this to be successful, the logged in account must have [MANAGE_ROLES].
     *
     * Set params to `null` to use defaults.
     *
     * @param name The name of the new role. Less than 100 characters.
     * @param hoisted Whether to hoist the role in the member list.
     * @param mentionable Whether the role is mentionable.
     * @param color The color of the role.
     * @param permissions The permissions that the role has.
     * @return The new [PolyRole].
     *
     * @throws InsufficientPermissionException If the logged in account does not have the [MANAGE_ROLES] permission,
     * or if the currently logged in account does not have hold all the specified permissions.
     * @throws IllegalArgumentException If the provided name is empty or greater than 100 characters.
     */
    @Throws(InsufficientPermissionException::class, IllegalArgumentException::class)
    public suspend fun createRole(
            name: String,
            hoisted: Boolean? = null,
            mentionable: Boolean? = null,
            color: Color? = null,
            permissions: List<Permission>? = null,
                                 ): PolyRole
    
    /**
     * Create a new [PolyEmote] in this guild.
     * For this to be successfully, the logged in account must have [MANAGE_EMOTES].
     *
     * @param name The name of the new emote.
     * @param icon The icon for the new emote.
     *
     * @return The new emote.
     *
     * @throws InsufficientPermissionException If the logged in account does not have the [MANAGE_EMOTES] permission.
     * @throws IllegalArgumentException If the provided name is empty or greater than 100 characters.
     */
    @Throws(InsufficientPermissionException::class, IllegalArgumentException::class)
    public suspend fun createEmote(
            name: String,
            icon: Icon,
                                  ): PolyEmote
    
}