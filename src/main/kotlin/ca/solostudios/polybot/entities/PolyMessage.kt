/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyMessage.kt is part of PolyhedralBot
 * Last modified on 31-12-2021 01:29 p.m.
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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package ca.solostudios.polybot.entities

import ca.solostudios.polybot.PolyBot
import ca.solostudios.polybot.util.jda.poly
import dev.minn.jda.ktx.await
import java.time.OffsetDateTime
import java.util.EnumSet
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.Message.MentionType
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.MessageReaction
import net.dv8tion.jda.api.requests.RestAction
import net.dv8tion.jda.api.requests.restaction.MessageAction

class PolyMessage(val bot: PolyBot, val jdaMessage: Message) {
    val content: String
        get() = jdaMessage.contentDisplay
    
    val contentRaw: String
        get() = jdaMessage.contentRaw
    
    val contentStripped: String
        get() = jdaMessage.contentDisplay
    
    val id: Long
        get() = jdaMessage.idLong
    
    val fromGuild: Boolean
        get() = jdaMessage.isFromGuild
    
    /**
     * Throws [NullPointerException] when this message isn't from a guild.
     */
    val member: PolyMember
        get() = jdaMessage.member?.poly(bot)!!
    
    val user: PolyUser
        get() = jdaMessage.author.poly(bot)
    
    val channel: PolyMessageChannel
        get() = jdaMessage.channel.poly(bot)
    
    val textChannel: PolyTextChannel
        get() = jdaMessage.textChannel.poly(bot)
    
    /**
     * Throws [NullPointerException] when this message isn't from a guild.
     */
    val guild: PolyGuild
        get() = jdaMessage.guild.poly(bot)
    
    val referencedMessage: PolyMessage?
        get() = jdaMessage.referencedMessage?.poly(bot)
    
    val mentionedUsers: List<PolyMember>
        get() = jdaMessage.mentionedMembers.map { it.poly(bot) }
    
    val mentionedChannels: List<PolyTextChannel>
        get() = jdaMessage.mentionedChannels.map { it.poly(bot) }
    
    val mentionedRoles: List<PolyRole>
        get() = jdaMessage.mentionedRoles.map { it.poly(bot) }
    
    val mentionedMembers: List<PolyMember>
        get() = jdaMessage.mentionedMembers.map { it.poly(bot) }
    
    val mentionedEmotes: List<PolyEmote>
        get() = jdaMessage.emotes.map { it.poly(bot) }
    
    val mentionsEveryone: Boolean
        get() = jdaMessage.mentionsEveryone()
    
    val isEdited: Boolean
        get() = jdaMessage.isEdited
    
    val timeEdited: OffsetDateTime?
        get() = jdaMessage.timeEdited
    
    val url: String
        get() = jdaMessage.jumpUrl
    
    val invites: List<String>
        get() = jdaMessage.invites
    
    val isFromBot: Boolean
        get() = jdaMessage.author.isBot
    
    val isFromWebhook: Boolean
        get() = jdaMessage.isWebhookMessage
    
    val timeCreated: OffsetDateTime
        get() = jdaMessage.timeCreated
    
    val attachments: List<Message.Attachment>
        get() = jdaMessage.attachments
    
    val reactions: List<MessageReaction>
        get() = jdaMessage.reactions
    
    val isPinned: Boolean
        get() = jdaMessage.isPinned
    
    fun matches(regex: Regex) = content.matches(regex)
    
    fun startsWith(prefix: String, ignoreCase: Boolean = false) = content.startsWith(prefix, ignoreCase)
    
    fun endsWith(prefix: String, ignoreCase: Boolean = false) = content.endsWith(prefix, ignoreCase)
    
    fun matchesRaw(regex: Regex) = contentRaw.matches(regex)
    
    fun startsWithRaw(prefix: String, ignoreCase: Boolean = false) = contentRaw.startsWith(prefix, ignoreCase)
    
    fun endsWithRaw(prefix: String, ignoreCase: Boolean = false) = contentRaw.endsWith(prefix, ignoreCase)
    
    fun matchesStripped(regex: Regex) = contentStripped.matches(regex)
    
    fun startsWithStripped(prefix: String, ignoreCase: Boolean = false) = contentStripped.startsWith(prefix, ignoreCase)
    
    fun endsWithStripped(prefix: String, ignoreCase: Boolean = false) = contentStripped.endsWith(prefix, ignoreCase)
    
    suspend fun reply(
            content: String,
            deniedMentions: List<MentionType> = listOf(MentionType.EVERYONE, MentionType.HERE),
                     ): PolyMessage {
        return replyImpl(jdaMessage.reply(content), deniedMentions).await()
    }
    
    suspend fun reply(
            embed: MessageEmbed,
            deniedMentions: List<MentionType> = listOf(MentionType.EVERYONE, MentionType.HERE),
                     ): PolyMessage {
        return replyImpl(jdaMessage.replyEmbeds(embed), deniedMentions).await()
    }
    
    fun replyAsync(
            content: String,
            deniedMentions: List<MentionType> = listOf(MentionType.EVERYONE, MentionType.HERE),
                  ) {
        replyImpl(jdaMessage.reply(content), deniedMentions).queue()
    }
    
    fun replyAsync(
            embed: MessageEmbed,
            deniedMentions: List<MentionType> = listOf(MentionType.EVERYONE, MentionType.HERE),
                  ) {
        replyImpl(jdaMessage.replyEmbeds(embed), deniedMentions).queue()
    }
    
    private fun replyImpl(action: MessageAction, deniedMentions: List<MentionType>): RestAction<PolyMessage> {
        return action.mentionRepliedUser(false)
                .allowedMentions(EnumSet.complementOf(EnumSet.copyOf(deniedMentions)))
                .map { it.poly(bot) }
    }
    
    suspend fun edit(content: String): PolyMessage {
        return editImpl(jdaMessage.editMessage(content)).await()
    }
    
    suspend fun edit(embed: MessageEmbed): PolyMessage {
        return editImpl(jdaMessage.editMessageEmbeds(embed)).await()
    }
    
    fun editAsync(content: String) {
        editImpl(jdaMessage.editMessage(content)).queue()
    }
    
    fun editAsync(embed: MessageEmbed) {
        editImpl(jdaMessage.editMessageEmbeds(embed)).queue()
    }
    
    private fun editImpl(action: MessageAction): RestAction<PolyMessage> {
        return action.mentionRepliedUser(false).map { it.poly(bot) }
    }
    
    suspend fun delete() {
        jdaMessage.delete().await()
    }
    
    fun deleteAsync() {
        jdaMessage.delete().queue()
    }
    
    suspend fun pin() {
        jdaMessage.pin().await()
    }
    
    fun pinAsync() {
        jdaMessage.pin().queue()
    }
    
    suspend fun unpin() {
        jdaMessage.unpin().await()
    }
    
    fun unpinAsync() {
        jdaMessage.unpin().queue()
    }
    
    suspend fun clearReactions() {
        jdaMessage.clearReactions().await()
    }
    
    fun clearReactionsAsync() {
        jdaMessage.clearReactions().queue()
    }
    
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        
        other as PolyMessage
        
        return jdaMessage != other.jdaMessage
    }
    
    override fun hashCode(): Int {
        return jdaMessage.hashCode()
    }
}