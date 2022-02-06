/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyMessageChannel.kt is part of PolyhedralBot
 * Last modified on 06-02-2022 06:26 p.m.
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

import ca.solostudios.polybot.api.builder.PolyMessageBuilder
import ca.solostudios.polybot.api.builder.PolyMessageEmbedBuilder
import java.io.InputStream
import kotlinx.coroutines.flow.Flow
import net.dv8tion.jda.api.AccountType
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.Permission.MESSAGE_ATTACH_FILES
import net.dv8tion.jda.api.Permission.MESSAGE_EMBED_LINKS
import net.dv8tion.jda.api.Permission.MESSAGE_HISTORY
import net.dv8tion.jda.api.Permission.MESSAGE_READ
import net.dv8tion.jda.api.Permission.MESSAGE_WRITE
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.exceptions.AccountTypeException
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException

public interface PolyMessageChannel : PolyChannel {
    /**
     * The JDA channel that is being wrapped by this entity
     */
    public override val jdaChannel: MessageChannel
    
    /**
     * Returns a [Flow] of the message history in this channel.
     *
     * This iterates chronologically backwards (from present to past) in the channel messages.
     * It can only retrieve messages that were sent *before* the first access.
     */
    public val messages: Flow<PolyMessage>
    
    /**
     * Returns a [Flow] of all the pinned messages in this channel.
     *
     * This iterates chronologically backwards (from present to past) in the channel messages.
     * It can only retrieve messages that were sent *before* the first access.
     */
    public val pinnedMessages: Flow<PolyMessage>
    
    /**
     * Whether the current user can send messages in this channel.
     *
     * @see PolyPermissionHolder.contains
     * @see Permission.MESSAGE_WRITE
     */
    public val canSendMessages: Boolean
    
    /**
     * Sends a specified message to this channel.
     *
     * This will fail if this channel is an instance of [PolyTextChannel],
     * and the currently logged in account does not have the required permission
     * to send a message to this channel.
     *
     * To determine if the currently logged in account has permission to send messages
     * in this channel, use [canSendMessages].
     *
     * @param messageBuilder The message builder used for the message
     * @receiver The message to send
     *
     * @throws InsufficientPermissionException If this is a [PolyTextChannel],
     * and the currently logged in account does not have
     *   - [MESSAGE_READ]
     *   - [MESSAGE_WRITE]
     *   - [MESSAGE_EMBED_LINKS] (if this message is only an embed)
     * @throws IllegalArgumentException if the provided message is empty,
     * is longer than 2000 characters, or is not [sendable][PolyMessageEmbed.sendable].
     * @throws UnsupportedOperationException If this is a [PolyPrivateChannel]
     * and both the logged in account and the target user are bots.
     */
    @Throws(InsufficientPermissionException::class, IllegalArgumentException::class, UnsupportedOperationException::class)
    public suspend fun sendMessage(messageBuilder: suspend PolyMessageBuilder.() -> Unit)
    
    /**
     * Send message embed to this channel.
     *
     * This will fail if this channel is an instance of [PolyTextChannel],
     * and the currently logged in account does not have the required permission
     * to send a message embed to this channel.
     *
     * To determine if the currently logged in account has permission to send messages
     * in this channel, use [canSendMessages].
     *
     * @param messageEmbedBuilder The message embed builder used for the message embed
     * @receiver The message embed to send
     *
     * @throws InsufficientPermissionException If this is a [PolyTextChannel],
     * and the currently logged in account does not have
     *   - [MESSAGE_READ]
     *   - [MESSAGE_WRITE]
     *   - [MESSAGE_EMBED_LINKS] (if this message is only an embed)
     * @throws IllegalArgumentException if the provided message is empty,
     * is longer than 2000 characters, or is not [sendable][PolyMessageEmbed.sendable].
     * @throws UnsupportedOperationException If this is a [PolyPrivateChannel]
     * and both the logged in account and the target user are bots.
     *
     * @see sendMessage
     */
    @Throws(InsufficientPermissionException::class, IllegalArgumentException::class, UnsupportedOperationException::class)
    public suspend fun sendMessageEmbed(messageEmbedBuilder: suspend PolyMessageEmbedBuilder.() -> Unit)
    
    /**
     * Uploads a file to the Discord servers and sends it to this channel.
     *
     * This will fail if this channel is an instance of [PolyTextChannel],
     * and the currently logged in account does not have the required permission
     * to send a message embed to this channel.
     *
     * To determine if the currently logged in account has permission to send messages
     * in this channel, use [canSendMessages].
     *
     * @param name The name of the file to upload.
     * @param data The data to upload
     * @param spoiler Whether this upload should be marked as a spoiler.
     *
     * @throws InsufficientPermissionException If this is a [PolyTextChannel],
     * and the currently logged in account does not have
     *   - [MESSAGE_READ]
     *   - [MESSAGE_WRITE]
     *   - [MESSAGE_ATTACH_FILES] (if this message is only an embed)
     * @throws UnsupportedOperationException If this is a [PolyPrivateChannel]
     * and both the logged in account and the target user are bots.
     *
     * @see sendMessage
     */
    public suspend fun sendFile(name: String, data: InputStream, spoiler: Boolean = false)
    
    /**
     * Attempts to get a [PolyMessage] from Discord's servers that has the same id as the id provided.
     *
     * Note: when retrieving a message, you must retrieve it from the channel it was sent in.
     *
     * @param id The id of the message to retrieve.
     * @return The message defined by the provided id.
     *
     * @throws AccountTypeException If the currently logged in account is not of the type [AccountType.BOT]
     * @throws InsufficientPermissionException If this is a [PolyTextChannel] and the logged in account does not have
     *   - [MESSAGE_READ]
     *   - [MESSAGE_HISTORY]
     */
    @Throws(AccountTypeException::class, IllegalArgumentException::class, InsufficientPermissionException::class)
    public suspend fun getMessage(id: Long): PolyMessage
    
    /**
     * Uses the provided id of a messages as a marker and retrieves all messages before the marker.
     *
     * In the case that there are insufficient messages before the marker,
     * then the amount of messages returned their total count *might not* equal the [perActionLimit].
     *
     * @param id The id of the message to act as a marker.
     * @param perActionLimit The maximum amount of messages to retrieve in a single action.
     * @return A flow of the message history before the marker.
     *
     * @throws InsufficientPermissionException If this is a [PolyTextChannel] and the logged in account does not have
     *   - [MESSAGE_READ]
     *   - [MESSAGE_HISTORY]
     */
    public fun getMessagesBefore(id: Long, perActionLimit: Int = Int.MAX_VALUE): Flow<PolyMessage>
    
    /**
     * Uses the provided id of a messages as a marker and retrieves all messages after the marker.
     *
     * In the case that there are insufficient messages after the marker,
     * then discord may return a different amount of messages after,
     * and their total count *might not* equal the [perActionLimit].
     *
     * @param id The id of the message to act as a marker.
     * @param perActionLimit The amount of messages to be retrieved per action.
     * @return A flow of the message history after the marker.
     *
     * @throws InsufficientPermissionException If this is a [PolyTextChannel] and the logged in account does not have
     *   - [MESSAGE_READ]
     *   - [MESSAGE_HISTORY]
     */
    @Throws(InsufficientPermissionException::class)
    public fun getMessagesAfter(id: Long, perActionLimit: Int = Int.MAX_VALUE): Flow<PolyMessage>
    
    /**
     * Uses the provided id of a messages as a marker and retrieves all messages around the marker.
     *
     * Discord will attempt to evently split the messages before and after the marker.
     * However, in the case that there are insufficient messages before or after the marker,
     * then discord may return a different amount of messages before and after,
     * and their total count *might not* equal the [limit].
     *
     * @param id The id of the message to act as a marker.
     * @param limit The amount of messages to be retrieved around the marker.
     * @return A list of the message history around the marker.
     *
     * @throws InsufficientPermissionException If this is a [PolyTextChannel] and the logged in account does not have
     *   - [MESSAGE_READ]
     *   - [MESSAGE_HISTORY]
     */
    @Throws(InsufficientPermissionException::class)
    public suspend fun getMessagesAround(id: Long, limit: Int = 100): List<PolyMessage>
    
    /**
     * Deletes a message from the channel with the same id as the one provided.
     *
     * @param id The id of the message to delete.
     * @param reason The reason the message is being deleted.
     *
     * @throws InsufficientPermissionException If this is a [PolyTextChannel] and the logged in account does not have [MESSAGE_READ].
     */
    @Throws(InsufficientPermissionException::class)
    public suspend fun deleteMessageById(id: Long, reason: String? = null)
    
    /**
     * Sends the typing status to discord.
     *
     * This only lasts for 10 seconds or until a message is sent.
     * It must be called continuously if you wish to type for longer.
     *
     * @throws InsufficientPermissionException If this is a [PolyTextChannel] and the logged in account does not have
     *   - [MESSAGE_WRITE]
     *   - [MESSAGE_READ]
     */
    @Throws(InsufficientPermissionException::class)
    public suspend fun sendTyping()
}