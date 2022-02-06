/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyGuildChannel.kt is part of PolyhedralBot
 * Last modified on 06-02-2022 05:23 p.m.
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

import kotlinx.coroutines.flow.Flow
import net.dv8tion.jda.api.Permission.CREATE_INSTANT_INVITE
import net.dv8tion.jda.api.Permission.MANAGE_CHANNEL
import net.dv8tion.jda.api.Permission.MESSAGE_READ
import net.dv8tion.jda.api.entities.GuildChannel
import net.dv8tion.jda.api.entities.Invite
import net.dv8tion.jda.api.entities.PermissionOverride
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException
import net.dv8tion.jda.api.managers.ChannelManager
import net.dv8tion.jda.api.utils.cache.CacheFlag
import kotlin.time.Duration

/**
 * Represents a discord guild channel.
 *
 * This is a wrapper for a [JDA guild channel][GuildChannel].
 *
 * Guild channels are channels which belong to a specific [PolyGuild].
 *
 * @see PolyTextChannel
 * @see PolyCategory
 * @see PolyVoiceChannel
 */
public interface PolyGuildChannel : PolyChannel, Mentionable {
    /**
     * The JDA guild channel that is being wrapped by this entity
     */
    public override val jdaChannel: GuildChannel
    
    /**
     * The guild this channel belongs to
     */
    public val guild: PolyGuild
    
    /**
     * The parent category of this channel.
     *
     * Not all channels necessarily have a parent category, so this may be null.
     *
     * This will always return `null` if this is an instance of [PolyCategory], as categories cannot be nested.
     */
    public val parent: PolyCategory?
    
    /**
     * A list of all members that can access this channel.
     *
     * It returns members based on what type of channel this is:
     * - For [PolyTextChannel], this returns all members with the [MESSAGE_READ] permission.
     * - For [PolyVoiceChannel], this returns all members who have joined this channel.
     * - For [PolyCategory], this returns all members who are in its child channels.
     */
    public val members: List<PolyMember>
    
    /**
     * The position this channel is displayed at.
     *
     * Higher values correspond to being displayed lower in the client.
     * Position 0 is the top most position.
     * Channels do not have continuous positions.
     */
    public val position: Int
    
    /**
     * The actual position of the channel, as stored and given by discord.
     * Channel positions are based on a pairing of the creating time (as stored in the snowflake id) and the position.
     * If 2 or more channels share the same position, then they are sorted based on their creation date.
     * The more recent a channel was created, the lower it is in the hierarchy.
     * This is handled by [position], and it is most likely the value you want.
     * If, for some reason, you want the actual position of the channel, then this value will give you that.
     *
     * @see position
     */
    public val positionRaw: Int
    
    /**
     * Gets all of the [PermissionOverride]s that are part of this channel.
     * This combines [PolyMember] and [PolyRole] overrides.
     *
     * This requires [CacheFlag.MEMBER_OVERRIDES] to be enabled.
     * Without that cache flag, this list will only contain overrides fore th currently logged in account and roles.
     *
     * @see memberPermissionOverrides For the list of member permission overrides
     * @see rolePermissionOverrides For the list of role permission overrides
     */
    public val permissionOverrides: List<PermissionOverride>
    
    /**
     * Gets all the member [PermissionOverride]s that are part of this channel
     *
     * This requires [CacheFlag.MEMBER_OVERRIDES] to be enabled.
     * Without that cache flag, this list will only contain overrides fore th currently logged in account and roles.
     *
     * @see permissionOverrides For the complete list of permission overrides
     */
    public val memberPermissionOverrides: List<PermissionOverride>
    
    /**
     * Gets all the role [PermissionOverride]s that are part of this channel
     *
     * This requires [CacheFlag.MEMBER_OVERRIDES] to be enabled.
     * Without that cache flag, this list will only contain overrides fore th currently logged in account and roles.
     *
     * @see permissionOverrides For the complete list of permission overrides
     */
    public val rolePermissionOverrides: List<PermissionOverride>
    
    /**
     * Whether this channel's [PermissionOverride]s match those of its [parent category][PolyCategory].
     * If the channel doesn't belong to a category, this will always return `true`.
     *
     * This requires [CacheFlag.MEMBER_OVERRIDES] to be enabled.
     *
     * @see category
     */
    public val isSynced: Boolean
    
    /**
     * Returns all invites for this channel.
     *
     * Requires the [MANAGE_CHANNEL] permission in this channel.
     */
    public val invites: Flow<Invite>
    
    /**
     * The [PermissionOverride] corresponding to the provided [PolyMember] or [PolyRole].
     * If there is no [PermissionOverride] for this channel corresponding to the provided role, then this returns `null`.
     *
     * @param permissionHolder The [PolyMember] or [PolyRole] whose [PermissionOverride] to return
     * @return The [PermissionOverride] corresponding to the provided member or role
     * @throws IllegalArgumentException If the provided permission holder is from a different guild
     */
    @Throws(IllegalArgumentException::class)
    public fun getPermissionOverride(permissionHolder: PolyPermissionHolder): PermissionOverride?
    
    /**
     * Deletes this channel.
     *
     * @param reason The reason this channel is being deleted.
     *
     * @see GuildChannel.delete
     */
    public suspend fun delete(reason: String? = null)
    
    /**
     * Sets the name of this channel.
     *
     * A channel name **must not** be more than 100 characters long.
     *
     * If this is a text channel, the name may only contain alphanumeric, underscore, and dash characters.
     * Discord will automatically convert names to lowercase for text channels.
     *
     * @param name The new name for this channel
     * @param reason The reason the name for the name change
     */
    public suspend fun setName(name: String, reason: String? = null)
    
    /**
     * Sets the parent category of this channel.
     *
     * @param parent The new parent category for this channel.
     * @param reason The reason the parent category is being updated
     * @throws IllegalStateException if this channel is a category itself.
     * @throws IllegalArgumentException if the provided category is not in the same guild.
     */
    @Throws(IllegalStateException::class, IllegalArgumentException::class)
    public suspend fun setCategory(parent: PolyCategory?, reason: String? = null)
    
    /**
     * Sets the position of this guild channel.
     *
     * @param position The new position of this channel.
     * @param reason The reason the position is being updated
     * @see GuildChannel.getPosition
     * @see ChannelManager.setPosition
     */
    public suspend fun setPosition(position: Int, reason: String? = null)
    
    /**
     * Creates a new invite for this channel.
     *
     * @param maxAge The maximum age for the returned invite lasts for. Set this to 0 if the invite should never expire.
     *
     * The default is 24 hours.
     * Set this to `null` to use the default value.
     * @param maxUses The maximum number of times the returned invite can be used for. Set this to 0 if the invite should have unlimited users.
     *
     * The default is 0.
     * Set this to `null` to use the default value.
     * @param temporary Whether the returned invite should only grant temporary membership.
     *
     * Default is `false.
     * Set to `null` to use the default value.
     * @param unique Whether discord should guarantee the returned invite is unique, or if it should reuse a similar invite.
     *
     * The default value is `false`.
     * Set to `null` to use the default value.
     *
     * @throws InsufficientPermissionException If the account does not have the [CREATE_INSTANT_INVITE] permission in this channel.
     * @throws IllegalArgumentException If this is an instance of [PolyCategory]
     */
    @Throws(InsufficientPermissionException::class, IllegalArgumentException::class)
    public suspend fun createInvite(maxAge: Duration? = null, maxUses: Int? = null, temporary: Boolean? = null, unique: Boolean? = false)
}