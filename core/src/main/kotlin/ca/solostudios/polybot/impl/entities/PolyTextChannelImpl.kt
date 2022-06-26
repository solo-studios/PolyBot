/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyTextChannelImpl.kt is part of PolyBot
 * Last modified on 26-06-2022 04:42 p.m.
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
import ca.solostudios.polybot.api.builder.PolyMessageBuilder
import ca.solostudios.polybot.api.entities.PolyCategory
import ca.solostudios.polybot.api.entities.PolyGuild
import ca.solostudios.polybot.api.entities.PolyMember
import ca.solostudios.polybot.api.entities.PolyMessage
import ca.solostudios.polybot.api.entities.PolyMessageEmbed
import ca.solostudios.polybot.api.entities.PolyPermissionHolder
import ca.solostudios.polybot.api.entities.PolyTextChannel
import ca.solostudios.polybot.api.entities.Snowflake
import ca.solostudios.polybot.api.entities.Snowflake.Companion.snowflake
import ca.solostudios.polybot.api.util.ext.poly
import dev.minn.jda.ktx.await
import java.io.InputStream
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import net.dv8tion.jda.api.entities.Invite
import net.dv8tion.jda.api.entities.PermissionOverride
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.utils.AttachmentOption
import kotlin.time.Duration

internal class PolyTextChannelImpl(
        override val bot: PolyBot,
        override val jdaChannel: TextChannel,
                                  ) : PolyTextChannel {
    override val snowflake: Snowflake by lazy { jdaChannel.idLong.snowflake() }
    
    override val name: String
        get() = jdaChannel.name
    
    override val guild: PolyGuild
        get() = jdaChannel.guild.poly(bot)
    override val parent: PolyCategory?
        get() = jdaChannel.parent?.poly(bot)
    override val members: Flow<PolyMember>
        get() = jdaChannel.members.asFlow().map { it.poly(bot) }
    override val position: Int
        get() = jdaChannel.position
    override val positionRaw: Int
        get() = jdaChannel.positionRaw
    override val permissionOverrides: Flow<PermissionOverride>
        get() = jdaChannel.permissionOverrides.asFlow()
    override val memberPermissionOverrides: Flow<PermissionOverride>
        get() = jdaChannel.memberPermissionOverrides.asFlow()
    override val rolePermissionOverrides: Flow<PermissionOverride>
        get() = jdaChannel.rolePermissionOverrides.asFlow()
    override val isSynced: Boolean
        get() = jdaChannel.isSynced
    override val invites: Flow<Invite>
        get() = flow {
            val invites = jdaChannel.retrieveInvites().await()
            
            for (invite in invites)
                emit(invite)
        }.flowOn(Dispatchers.IO)
    override val messages: Flow<PolyMessage>
        get() = getMessagesAfter(ULong.MIN_VALUE)
    override val pinnedMessages: Flow<PolyMessage>
        get() = flow {
            val pinnedMessages = jdaChannel.retrievePinnedMessages()
                    .await()
            
            for (message in pinnedMessages)
                emit(message.poly(bot))
        }.flowOn(Dispatchers.IO)
    override val canSendMessages: Boolean
        get() = true
    override val topic: String?
        get() = jdaChannel.topic
    override val isNSFW: Boolean
        get() = jdaChannel.isNSFW
    
    override suspend fun deleteMessages(vararg messages: PolyMessage) {
        if (messages.size == 1)
            messages.first().delete()
        else
            jdaChannel.deleteMessages(messages.map { it.jdaMessage })
    }
    
    override suspend fun deleteMessages(messages: Collection<PolyMessage>) {
        if (messages.size == 1)
            messages.first().delete()
        else
            jdaChannel.deleteMessages(messages.map { it.jdaMessage })
    }
    
    @OptIn(ExperimentalUnsignedTypes::class)
    override suspend fun deleteMessagesById(vararg messages: ULong) {
        if (messages.size == 1)
            jdaChannel.deleteMessageById(messages.first().toLong())
        else
            jdaChannel.deleteMessagesByIds(messages.map { it.toLong().toString() })
    }
    
    override suspend fun deleteMessagesById(messages: Collection<ULong>) {
        if (messages.size == 1)
            jdaChannel.deleteMessageById(messages.first().toLong())
        else
            jdaChannel.deleteMessagesByIds(messages.map { it.toLong().toString() })
    }
    
    override suspend fun setTopic(topic: String?, reason: String?) {
        jdaChannel.manager.setTopic(topic)
                .reason(reason)
                .await()
    }
    
    override suspend fun setNSFW(nsfw: Boolean, reason: String?) {
        jdaChannel.manager.setNSFW(nsfw)
                .reason(reason)
                .await()
    }
    
    override suspend fun setSlowmode(slowmode: Int?, reason: String?) {
        jdaChannel.manager.setSlowmode(slowmode ?: 0)
                .reason(reason)
                .await()
    }
    
    override suspend fun setNews(news: Boolean, reason: String?) {
        jdaChannel.manager.setNews(news)
                .reason(reason)
                .await()
    }
    
    override suspend fun sendMessage(message: PolyMessage): PolyMessage {
        return jdaChannel.sendMessage(message.jdaMessage)
                .await()
                .poly(bot)
    }
    
    override suspend fun sendMessage(block: PolyMessageBuilder.() -> Unit): PolyMessage {
        TODO("Not yet implemented")
    }
    
    override suspend fun sendMessageEmbed(messageEmbed: PolyMessageEmbed): PolyMessage {
        return jdaChannel.sendMessageEmbeds(messageEmbed.jdaMessageEmbed)
                .await()
                .poly(bot)
    }
    
    override suspend fun sendFile(name: String, data: InputStream, spoiler: Boolean): PolyMessage {
        val messageAction = if (spoiler)
            jdaChannel.sendFile(data, name, AttachmentOption.SPOILER)
        else
            jdaChannel.sendFile(data, name)
        
        return messageAction.await().poly(bot)
    }
    
    override suspend fun getMessage(id: ULong): PolyMessage {
        return jdaChannel.retrieveMessageById(id.toLong()).await().poly(bot)
    }
    
    override fun getMessagesBefore(id: ULong, perActionLimit: Int): Flow<PolyMessage> {
        return flow {
            val history = jdaChannel.getHistoryBefore(id.toLong(), perActionLimit)
                    .await()
            
            for (message in history.retrievedHistory)
                emit(message.poly(bot))
            
            while (true) {
                val messages = history.retrievePast(perActionLimit).await()
                
                for (message in messages)
                    emit(message.poly(bot))
            }
        }
    }
    
    override fun getMessagesAfter(id: ULong, perActionLimit: Int): Flow<PolyMessage> {
        return flow {
            val history = jdaChannel.getHistoryAfter(id.toLong(), perActionLimit)
                    .await()
            
            for (message in history.retrievedHistory)
                emit(message.poly(bot))
            
            while (true) {
                val messages = history.retrieveFuture(perActionLimit).await()
                
                for (message in messages)
                    emit(message.poly(bot))
            }
        }
    }
    
    override suspend fun getMessagesAround(id: ULong, limit: Int): Flow<PolyMessage> {
        return flow {
            val messages = jdaChannel.getHistoryAround(id.toLong(), limit).await()
            
            for (message in messages.retrievedHistory)
                emit(message.poly(bot))
        }
    }
    
    override suspend fun deleteMessage(id: ULong, reason: String?) {
        jdaChannel.deleteMessageById(id.toLong())
                .reason(reason)
                .await()
    }
    
    override suspend fun sendTyping() {
        jdaChannel.sendTyping()
                .await()
    }
    
    override fun getPermissionOverride(permissionHolder: PolyPermissionHolder): PermissionOverride? {
        return jdaChannel.getPermissionOverride(permissionHolder.jdaPermissionHolder)
    }
    
    override suspend fun delete(reason: String?) {
        jdaChannel.delete()
                .await()
    }
    
    override suspend fun setName(name: String, reason: String?) {
        jdaChannel.manager.setName(name)
                .reason(reason)
                .await()
    }
    
    override suspend fun setCategory(parent: PolyCategory?, reason: String?) {
        jdaChannel.manager.setParent(parent?.jdaCategory)
                .reason(reason)
                .await()
    }
    
    override suspend fun setPosition(position: Int, reason: String?) {
        jdaChannel.manager.setPosition(position)
                .reason(reason)
                .await()
    }
    
    override suspend fun createInvite(maxAge: Duration?, maxUses: Int?, temporary: Boolean?, unique: Boolean?): Invite {
        return jdaChannel.createInvite()
                .setMaxAge(maxAge?.inWholeMilliseconds, TimeUnit.MILLISECONDS)
                .setMaxUses(maxUses)
                .setTemporary(temporary)
                .setUnique(unique)
                .await()
    }
    
    override val asMention: String
        get() = jdaChannel.asMention
    
    override fun toString(): String = asMention
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PolyTextChannelImpl) return false
        
        if (jdaChannel != other.jdaChannel) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        return jdaChannel.hashCode()
    }
}