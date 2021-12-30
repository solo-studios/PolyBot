/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyMessageChannel.kt is part of PolyhedralBot
 * Last modified on 30-12-2021 03:35 p.m.
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
import ca.solostudios.polybot.util.poly
import dev.minn.jda.ktx.await
import java.util.EnumSet
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.requests.RestAction
import net.dv8tion.jda.api.requests.restaction.MessageAction

open class PolyMessageChannel(bot: PolyBot, override val jdaChannel: MessageChannel) : PolyAbstractChannel(bot, jdaChannel) {
    suspend fun sendTyping() {
        jdaChannel.sendTyping().await()
    }
    
    fun sendTypingAsync() {
        jdaChannel.sendTyping().queue()
    }
    
    suspend fun sendMessage(
            content: String,
            deniedMentions: List<Message.MentionType> = listOf(Message.MentionType.EVERYONE, Message.MentionType.HERE),
                           ): PolyMessage {
        return sendMessageImpl(jdaChannel.sendMessage(content), deniedMentions).await()
    }
    
    suspend fun sendMessage(
            embed: MessageEmbed,
            deniedMentions: List<Message.MentionType> = listOf(Message.MentionType.EVERYONE, Message.MentionType.HERE),
                           ): PolyMessage {
        return sendMessageImpl(jdaChannel.sendMessageEmbeds(embed), deniedMentions).await()
    }
    
    fun sendMessageAsync(
            content: String,
            deniedMentions: List<Message.MentionType> = listOf(Message.MentionType.EVERYONE, Message.MentionType.HERE),
                        ) {
        sendMessageImpl(jdaChannel.sendMessage(content), deniedMentions).queue()
    }
    
    fun sendMessageAsync(
            embed: MessageEmbed,
            deniedMentions: List<Message.MentionType> = listOf(Message.MentionType.EVERYONE, Message.MentionType.HERE),
                        ) {
        sendMessageImpl(jdaChannel.sendMessageEmbeds(embed), deniedMentions).queue()
    }
    
    private fun sendMessageImpl(action: MessageAction, deniedMentions: List<Message.MentionType>): RestAction<PolyMessage> {
        return action.allowedMentions(EnumSet.complementOf(EnumSet.copyOf(deniedMentions))).map { it.poly(bot) }
    }
}