/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyMessage.kt is part of PolyhedralBot
 * Last modified on 25-09-2021 09:53 p.m.
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

package com.solostudios.polybot.entities

import com.solostudios.polybot.PolyBot
import com.solostudios.polybot.util.poly
import dev.minn.jda.ktx.await
import java.time.OffsetDateTime
import java.util.EnumSet
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.Message.MentionType
import net.dv8tion.jda.api.entities.MessageEmbed
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
    
    val member: PolyMember
        get() = jdaMessage.member?.poly(bot)!!
    
    val author: PolyUser
        get() = jdaMessage.author.poly(bot)
    
    val fromBot: Boolean
        get() = jdaMessage.author.isBot
    
    val channel: PolyMessageChannel
        get() = jdaMessage.channel.poly(bot)
    
    val textChannel: PolyTextChannel
        get() = jdaMessage.textChannel.poly(bot)
    
    val guild: PolyGuild
        get() = jdaMessage.guild.poly(bot)
    
    val timeCreated: OffsetDateTime
        get() = jdaMessage.timeCreated
    
    fun matches(regex: Regex) = content.matches(regex)
    
    fun startsWith(prefix: String, ignoreCase: Boolean = false) = content.startsWith(prefix, ignoreCase)
    
    fun endsWith(prefix: String, ignoreCase: Boolean = false) = content.endsWith(prefix, ignoreCase)
    
    fun matchesRaw(regex: Regex) = contentRaw.matches(regex)
    
    fun startsWithRaw(prefix: String, ignoreCase: Boolean = false) = contentRaw.startsWith(prefix, ignoreCase)
    
    fun endsWithRaw(prefix: String, ignoreCase: Boolean = false) = contentRaw.endsWith(prefix, ignoreCase)
    
    fun matchesStripped(regex: Regex) = contentStripped.matches(regex)
    
    fun startsWithStripped(prefix: String, ignoreCase: Boolean = false) = contentStripped.startsWith(prefix, ignoreCase)
    
    fun endsWithStripped(prefix: String, ignoreCase: Boolean = false) = contentStripped.endsWith(prefix, ignoreCase)
    
    suspend fun reply(content: String, deniedMentions: List<MentionType> = listOf(MentionType.EVERYONE, MentionType.HERE)): PolyMessage {
        return reply(jdaMessage.reply(content), deniedMentions).await().poly(bot)
    }
    
    fun replyAsync(content: String,
                   deniedMentions: List<MentionType> = listOf(MentionType.EVERYONE, MentionType.HERE)): RestAction<PolyMessage> {
        return reply(jdaMessage.reply(content), deniedMentions).map { it.poly(bot) }
    }
    
    fun replyAsync(embed: MessageEmbed,
                   deniedMentions: List<MentionType> = listOf(MentionType.EVERYONE, MentionType.HERE)): RestAction<PolyMessage> {
        return reply(jdaMessage.replyEmbeds(embed), deniedMentions).map { it.poly(bot) }
    }
    
    suspend fun reply(embed: MessageEmbed,
                      deniedMentions: List<MentionType> = listOf(MentionType.EVERYONE, MentionType.HERE)): PolyMessage {
        return reply(jdaMessage.replyEmbeds(embed), deniedMentions).await().poly(bot)
    }
    
    private fun reply(action: MessageAction, deniedMentions: List<MentionType>): MessageAction {
        return action.mentionRepliedUser(false)
                .allowedMentions(EnumSet.complementOf(EnumSet.copyOf(deniedMentions)))
    }
    
    suspend fun edit(content: String): PolyMessage {
        return jdaMessage.editMessage(content)
                .mentionRepliedUser(false)
                .await()
                .poly(bot)
    }
    
    suspend fun delete() {
        jdaMessage.delete()
                .await()
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