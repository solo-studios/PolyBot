/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyMessageChannel.kt is part of PolyhedralBot
 * Last modified on 23-01-2022 05:15 p.m.
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
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import kotlinx.coroutines.flow.Flow
import net.dv8tion.jda.api.entities.MessageChannel

public interface PolyMessageChannel : PolyChannel {
    public override val jdaChannel: MessageChannel
    
    public val messages: Flow<PolyMessage>
    
    public val pinnedMessages: Flow<PolyMessage>
    
    public suspend fun sendMessage(messageBuilder: suspend PolyMessageBuilder.() -> Unit)
    
    public suspend fun sendMessageEmbed(messageEmbedBuilder: suspend PolyMessageEmbedBuilder.() -> Unit)
    
    public fun sendFile(name: String, data: InputStream, spoiler: Boolean = false)
    
    public fun sendFile(name: String, data: ByteArray, spoiler: Boolean = false) {
        return sendFile(name, ByteArrayInputStream(data), spoiler)
    }
    
    public fun sendFile(file: File, spoiler: Boolean = false) {
        return sendFile(file.name, file, spoiler)
    }
    
    public fun sendFile(name: String, file: File, spoiler: Boolean = false) {
        return sendFile(name, FileInputStream(file), spoiler)
    }
    
    public suspend fun getMessage(id: Long): PolyMessage
    
    public suspend fun getMessagesBefore(id: Long, limit: Int = Int.MAX_VALUE): Flow<PolyMessage>
    
    public suspend fun getMessagesAfter(id: Long, limit: Int = Int.MAX_VALUE): Flow<PolyMessage>
    
    public suspend fun getMessagesAround(id: Long, limit: Int = 100): Flow<PolyMessage>
    
    public suspend fun deleteMessage(message: PolyMessage, reason: String? = null)
    
    public suspend fun deleteMessageById(id: Long, reason: String? = null)
    
    public suspend fun sendTyping()
}