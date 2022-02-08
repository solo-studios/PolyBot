/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyMessage.kt is part of PolyhedralBot
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

import kotlinx.datetime.Instant
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageReaction

public interface PolyMessage : PolySnowflakeEntity {
    /**
     * The JDA message that is being wrapped by this entity
     */
    public val jdaMessage: Message
    
    public val content: String
    
    public val contentRaw: String
    
    public val contentStripped: String
    
    public val edited: Boolean
    
    public val timeEdited: Instant?
    
    public val author: PolyUser
    
    public val user: PolyUser
    
    public val member: PolyMember?
    
    public val url: String
    
    public val mentionedUsers: List<PolyUser>
    
    public val mentionedRoles: List<PolyRole>
    
    public val mentionedMembers: List<PolyMember>
    
    public val mentionedChannels: List<PolyChannel>
    
    public val emotes: List<PolyEmote>
    
    public val mentionsEveryone: Boolean
    
    public val invites: List<String>
    
    public val nonce: String
    
    public val fromGuild: Boolean
    
    public val fromBot: Boolean
    
    public val fromWebhook: Boolean
    
    public val fromPrivateChannel: Boolean
    
    public val privateChannel: PolyPrivateChannel
    
    public val textChannel: PolyTextChannel
    
    public val guild: PolyGuild
    
    public val embeds: List<PolyMessageEmbed>
    
    public val attachments: List<Message.Attachment>
    
    public val reactions: List<MessageReaction>
    
    public val tts: Boolean
    
    public val pinned: Boolean
    
    public val suppressEmbeds: Boolean
    
    public fun matches(regex: Regex): Boolean
    
    public fun startsWith(prefix: String, ignoreCase: Boolean = false): Boolean
    
    public fun endsWith(postfix: String, ignoreCase: Boolean = false): Boolean
    
    public fun matchesRaw(regex: Regex): Boolean
    
    public fun startsWithRaw(prefix: String, ignoreCase: Boolean = false): Boolean
    
    public fun endsWithRaw(postfix: String, ignoreCase: Boolean = false): Boolean
    
    public fun matchesStripped(regex: Regex): Boolean
    
    public fun startsWithStripped(prefix: String, ignoreCase: Boolean = false): Boolean
    
    public fun endsWithStripped(postfix: String, ignoreCase: Boolean = false): Boolean
    
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
    
    public suspend fun unpin()
    
    public suspend fun addReaction(emote: PolyEmote)
    
    public suspend fun addReaction(unicode: String)
    
    public suspend fun clearReactions()
    
    public suspend fun suppressEmbeds(suppress: Boolean)
    
    public suspend fun referencedMessage(): PolyMessage?
}