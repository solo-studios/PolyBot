/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyMessage.kt is part of PolyhedralBot
 * Last modified on 22-01-2022 05:18 p.m.
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

import kotlinx.datetime.Instant

public interface PolyMessage : PolySnowflakeEntity {
    
    public val mentionedUsers: List<PolyUser>
    
    public val mentionedChannels: List<PolyChannel>
    
    public val mentionedRoles: List<PolyRole>
    
    public val mentionedMembers: List<PolyMember>
    
    public val mentionsEveryone: Boolean
    
    public val isEdited: Boolean
    
    public val timeEdited: Instant?
    
    public val author: PolyUser
    
    public val member: PolyMember?
    
    public val jumpUrl: String
    
    public val content: String
    
    public val contentRaw: String
    
    public val contentStripped: String
    
    public val invites: String
    
    public val nonce: String
    
    public val fromGuild: Boolean
    
    public val isWebhook: Boolean
    
    public val privateChannel: PolyPrivateChannel
    
    public val textChannel: PolyTextChannel
    
    public val guild: PolyGuild
    
    public val embeds: List<PolyMessageEmbed>
    
    public val emotes: List<PolyEmote>
    
    public val isTTS: Boolean
    
    public val isPinned: Boolean
    
    public val isSuppressEmbeds: Boolean
    
    public suspend fun edit(content: String? = null, embeds: List<PolyMessageEmbed>? = null)
    
    public suspend fun edit(content: String? = null, vararg embeds: PolyMessageEmbed) {
        edit(content, embeds.toList())
    }
    
    public suspend fun reply(content: String? = null, embeds: List<PolyMessageEmbed>? = null)
    
    public suspend fun reply(content: String? = null, vararg embeds: PolyMessageEmbed) {
        reply(content, embeds.toList())
    }
    
    public suspend fun delete()
    
    public suspend fun pin()
    
    public suspend fun addReaction(emote: PolyEmote)
    
    public suspend fun addReaction(unicode: String)
    
    public suspend fun clearReactions()
    
    public suspend fun suppressEmbeds(suppress: Boolean)
    
    public suspend fun referencedMessage(): PolyMessage?
    
    public enum class AllowedMentionTypes {
        USER,
        ROLE,
        CHANNEL,
        EMOTE,
        EVERYONE,
    }
}