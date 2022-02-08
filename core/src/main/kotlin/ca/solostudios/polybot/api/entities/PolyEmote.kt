/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyEmote.kt is part of PolyhedralBot
 * Last modified on 07-02-2022 01:17 a.m.
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

import net.dv8tion.jda.api.Permission.MANAGE_EMOTES
import net.dv8tion.jda.api.entities.Emote
import net.dv8tion.jda.api.entities.Guild.BoostTier
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException

public interface PolyEmote : PolyMentionable, PolySnowflakeEntity {
    /**
     * The JDA emote that is being wrapped by this entity
     */
    public val jdaEmote: Emote
    
    /**
     * The guild this emote is attached to.
     *
     * Will be null if this emote is created from a message.
     */
    public val guild: PolyGuild?
    
    /**
     * A list of roles this emote is active for.
     */
    public val roles: List<PolyRole>
    
    /**
     * Whether this emote has attached roles.
     * This may be false when the emote is retrieved through special cases like audit logs.
     */
    public val providesRoles: Boolean
    
    /**
     * The name of this emote.
     *
     * Does not include colons.
     */
    public val name: String
    
    /**
     * Whether this emote is managed by an external service.
     *
     * A managed emote is controlled by a discord application, not the guild administrators.
     */
    public val managed: Boolean
    
    /**
     * Whether this emote is available.
     * When an emote becomes unavailable, it cannot be used in messages.
     * An emote becomes unavailable when the [BoostTier] of the guild drops,
     * such that the maximum number of allowed emotes is lower than
     * the total amount of emotes added to the guild.
     *
     * If an emote is added to the guild when the boost tier allows for more than 50 normal and 50 animated emotes
     * (BoostTier is at least [BoostTier.TIER_1] and the emote is at least
     * the 51st one added, then the emote becomes unavailable when the BoostTier drops below a level that allows those emotes
     * to be used.
     * Emotes that where added as part of a lower BoostTier (i.e. the 51st emote on BoostTier 2) will remain available,
     * as long as the BoostTier stays above the required level.
     */
    public val available: Boolean
    
    /**
     * Whether this emote is animated.
     *
     * Animated emotes are available to discord nitro users, as well as bot accounts.
     */
    public val animated: Boolean
    
    /**
     * A string representation of the URL, which leads to the image displayed in the official discord client when this emote is used.
     */
    public val imageUrl: String
    
    /**
     * Deletes this emote.
     *
     * @param reason The reason the emote is being deleted.
     *
     * @throws UnsupportedOperationException If this emote is managed by discord. ([managed])
     * @throws InsufficientPermissionException If the currently logged in account does not have the permission [MANAGE_EMOTES].
     */
    @Throws(UnsupportedOperationException::class, InsufficientPermissionException::class)
    public suspend fun delete(reason: String? = null)
}