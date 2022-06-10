/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyMessageImpl.kt is part of PolyBot
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

package ca.solostudios.polybot.impl.entities

import ca.solostudios.polybot.api.PolyBot
import ca.solostudios.polybot.api.entities.PolyChannel
import ca.solostudios.polybot.api.entities.PolyEmote
import ca.solostudios.polybot.api.entities.PolyGuild
import ca.solostudios.polybot.api.entities.PolyMember
import ca.solostudios.polybot.api.entities.PolyMessage
import ca.solostudios.polybot.api.entities.PolyMessageEmbed
import ca.solostudios.polybot.api.entities.PolyPrivateChannel
import ca.solostudios.polybot.api.entities.PolyRole
import ca.solostudios.polybot.api.entities.PolyTextChannel
import ca.solostudios.polybot.api.entities.PolyUser
import ca.solostudios.polybot.api.entities.Snowflake
import ca.solostudios.polybot.api.entities.Snowflake.Companion.snowflake
import ca.solostudios.polybot.api.util.ext.poly
import dev.minn.jda.ktx.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageReaction

internal class PolyMessageImpl(
        override val bot: PolyBot,
        override val jdaMessage: Message,
                              ) : PolyMessage {
    override val snowflake: Snowflake by lazy { jdaMessage.idLong.snowflake() }
    
    override val content: String
        get() = jdaMessage.contentDisplay
    override val contentRaw: String
        get() = jdaMessage.contentRaw
    override val contentStripped: String
        get() = jdaMessage.contentStripped
    override val isEdited: Boolean
        get() = jdaMessage.isEdited
    override val timeEdited: Instant?
        get() = jdaMessage.timeEdited?.toInstant()?.toKotlinInstant()
    override val author: PolyUser
        get() = jdaMessage.author.poly(bot)
    override val member: PolyMember?
        get() = jdaMessage.member?.poly(bot)
    override val url: String
        get() = jdaMessage.jumpUrl
    override val mentionedUsers: Flow<PolyUser>
        get() = jdaMessage.mentionedUsers.asFlow().map { it.poly(bot) }
    override val mentionedRoles: Flow<PolyRole>
        get() = jdaMessage.mentionedRoles.asFlow().map { it.poly(bot) }
    override val mentionedMembers: Flow<PolyMember>
        get() = jdaMessage.mentionedMembers.asFlow().map { it.poly(bot) }
    override val mentionedChannels: Flow<PolyChannel>
        get() = jdaMessage.mentionedChannels.asFlow().map { it.poly(bot) }
    override val emotes: Flow<PolyEmote>
        get() = jdaMessage.emotes.asFlow().map { it.poly(bot) }
    override val mentionsEveryone: Boolean
        get() = jdaMessage.mentionsEveryone()
    override val invites: Flow<String>
        get() = jdaMessage.invites.asFlow()
    override val nonce: String?
        get() = jdaMessage.nonce
    override val fromGuild: Boolean
        get() = jdaMessage.isFromGuild
    override val fromBot: Boolean
        get() = jdaMessage.author.isBot
    override val fromWebhook: Boolean
        get() = jdaMessage.isWebhookMessage
    override val fromPrivateChannel: Boolean
        get() = jdaMessage.isFromType(ChannelType.PRIVATE)
    override val privateChannel: PolyPrivateChannel
        get() = jdaMessage.privateChannel.poly(bot)
    override val textChannel: PolyTextChannel
        get() = jdaMessage.textChannel.poly(bot)
    override val guild: PolyGuild
        get() = jdaMessage.guild.poly(bot)
    override val embeds: Flow<PolyMessageEmbed>
        get() = jdaMessage.embeds.asFlow().map { it.poly(bot) }
    override val attachments: Flow<Message.Attachment>
        get() = jdaMessage.attachments.asFlow()
    override val reactions: Flow<MessageReaction>
        get() = jdaMessage.reactions.asFlow()
    override val tts: Boolean
        get() = jdaMessage.isTTS
    override val pinned: Boolean
        get() = jdaMessage.isPinned
    override val suppressEmbeds: Boolean
        get() = jdaMessage.isSuppressedEmbeds
    
    override fun matches(regex: Regex): Boolean {
        return content.matches(regex)
    }
    
    override fun startsWith(prefix: String, ignoreCase: Boolean): Boolean {
        return content.startsWith(prefix, ignoreCase)
    }
    
    override fun endsWith(postfix: String, ignoreCase: Boolean): Boolean {
        return content.endsWith(postfix, ignoreCase)
    }
    
    override fun matchesRaw(regex: Regex): Boolean {
        return contentRaw.matches(regex)
    }
    
    override fun startsWithRaw(prefix: String, ignoreCase: Boolean): Boolean {
        return contentRaw.startsWith(prefix, ignoreCase)
    }
    
    override fun endsWithRaw(postfix: String, ignoreCase: Boolean): Boolean {
        return contentRaw.endsWith(postfix, ignoreCase)
    }
    
    override fun matchesStripped(regex: Regex): Boolean {
        return contentStripped.matches(regex)
    }
    
    override fun startsWithStripped(prefix: String, ignoreCase: Boolean): Boolean {
        return contentStripped.startsWith(prefix, ignoreCase)
    }
    
    override fun endsWithStripped(postfix: String, ignoreCase: Boolean): Boolean {
        return contentStripped.endsWith(postfix, ignoreCase)
    }
    
    override suspend fun edit(content: String?, embeds: List<PolyMessageEmbed>?): PolyMessage {
        val edit = jdaMessage.editMessage(content ?: "")
        
        if (embeds != null && embeds.isNotEmpty())
            edit.setEmbeds(embeds.map { it.jdaMessageEmbed })
        
        return edit.await().poly(bot)
    }
    
    override suspend fun reply(content: String?, embeds: List<PolyMessageEmbed>?): PolyMessage {
        val reply = jdaMessage.reply(content ?: "")
        
        if (embeds != null && embeds.isNotEmpty())
            reply.setEmbeds(embeds.map { it.jdaMessageEmbed })
        
        return reply.await().poly(bot)
    }
    
    override suspend fun delete() {
        jdaMessage.delete()
                .await()
    }
    
    override suspend fun pin() {
        jdaMessage.pin()
                .await()
    }
    
    override suspend fun unpin() {
        jdaMessage.unpin()
                .await()
    }
    
    override suspend fun addReaction(emote: PolyEmote) {
        jdaMessage.addReaction(emote.jdaEmote)
    }
    
    override suspend fun addReaction(unicode: String) {
        jdaMessage.addReaction(unicode)
    }
    
    override suspend fun clearReactions() {
        jdaMessage.clearReactions()
                .await()
    }
    
    override suspend fun suppressEmbeds(suppress: Boolean) {
        jdaMessage.suppressEmbeds(suppress)
                .await()
    }
    
    override suspend fun referencedMessage(): PolyMessage? {
        return jdaMessage.messageReference?.resolve()?.await()?.poly(bot)
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PolyMessageImpl) return false
        
        if (jdaMessage != other.jdaMessage) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        return jdaMessage.hashCode()
    }
}