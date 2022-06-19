/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file MessagesExtensions.kt is part of PolyBot
 * Last modified on 10-06-2022 12:22 p.m.
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

package ca.solostudios.polybot.api.util.ext

import ca.solostudios.polybot.api.entities.PolyMessage
import ca.solostudios.polybot.api.entities.PolyMessageChannel
import ca.solostudios.polybot.api.entities.PolyPrivateChannel
import ca.solostudios.polybot.api.entities.PolyTextChannel
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.dv8tion.jda.api.Permission.MESSAGE_ATTACH_FILES
import net.dv8tion.jda.api.Permission.MESSAGE_READ
import net.dv8tion.jda.api.Permission.MESSAGE_WRITE
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException

/**
 * Sends a specified message to this channel.
 *
 * This will fail if this channel is an instance of [PolyTextChannel],
 * and the currently logged in account does not have the required permission
 * to send a message to this channel.
 *
 * To determine if the currently logged in account has permission to send messages
 * in this channel, use [PolyMessageChannel.canSendMessages].
 *
 * @param text The plain text message to send
 *
 * @throws InsufficientPermissionException If this is a [PolyTextChannel],
 * and the currently logged in account does not have
 *   - [MESSAGE_READ]
 *   - [MESSAGE_WRITE]
 * @throws IllegalArgumentException if the provided message is empty,
 * or is longer than 2000 characters.
 * @throws UnsupportedOperationException If this is a [PolyPrivateChannel]
 * and both the logged in account and the target user are bots.
 */
public suspend fun PolyMessageChannel.sendMessage(text: String): PolyMessage {
    return sendMessage {
        content = text
    }
}


/**
 * Sends a specified message to this channel.
 *
 * This will fail if this channel is an instance of [PolyTextChannel],
 * and the currently logged in account does not have the required permission
 * to send a message to this channel.
 *
 * To determine if the currently logged in account has permission to send messages
 * in this channel, use [PolyMessageChannel.canSendMessages].
 *
 * @param textBlock The plain text message to send
 *
 * @throws InsufficientPermissionException If this is a [PolyTextChannel],
 * and the currently logged in account does not have
 *   - [MESSAGE_READ]
 *   - [MESSAGE_WRITE]
 * @throws IllegalArgumentException if the provided message is empty,
 * or is longer than 2000 characters.
 * @throws UnsupportedOperationException If this is a [PolyPrivateChannel]
 * and both the logged in account and the target user are bots.
 */
public suspend fun PolyMessageChannel.sendMessage(textBlock: StringBuilder.() -> Unit): PolyMessage {
    return sendMessage {
        content(textBlock)
    }
}

/**
 * Uploads a file to the Discord servers and sends it to this channel.
 *
 * This will fail if this channel is an instance of [PolyTextChannel],
 * and the currently logged in account does not have the required permission
 * to send a message embed to this channel.
 *
 * To determine if the currently logged in account has permission to send messages
 * in this channel, use [PolyMessageChannel.canSendMessages].
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
public suspend fun PolyMessageChannel.sendFile(name: String, data: ByteArray, spoiler: Boolean = false): PolyMessage {
    return sendFile(name, ByteArrayInputStream(data), spoiler)
}

/**
 * Uploads a file to the Discord servers and sends it to this channel.
 *
 * This will fail if this channel is an instance of [PolyTextChannel],
 * and the currently logged in account does not have the required permission
 * to send a message embed to this channel.
 *
 * To determine if the currently logged in account has permission to send messages
 * in this channel, use [PolyMessageChannel.canSendMessages].
 *
 * @param file The file to upload.
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
public suspend fun PolyMessageChannel.sendFile(file: File, spoiler: Boolean = false) {
    return sendFile(file.name, file, spoiler)
}

/**
 * Uploads a file to the Discord servers and sends it to this channel.
 *
 * This will fail if this channel is an instance of [PolyTextChannel],
 * and the currently logged in account does not have the required permission
 * to send a message embed to this channel.
 *
 * To determine if the currently logged in account has permission to send messages
 * in this channel, use [PolyMessageChannel.canSendMessages].
 *
 * @param name The name of the file to upload.
 * @param file The file to upload.
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
public suspend fun PolyMessageChannel.sendFile(name: String, file: File, spoiler: Boolean = false) {
    return withContext(Dispatchers.IO) {
        sendFile(name, FileInputStream(file), spoiler)
    }
}
