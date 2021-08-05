/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file MessageCache.kt is part of PolyhedralBot
 * Last modified on 03-08-2021 11:26 p.m.
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

package com.solostudios.polybot.cache

import com.solostudios.polybot.util.set
import net.dv8tion.jda.api.entities.Message
import org.ehcache.Cache
import org.slf4j.kotlin.getLogger

class MessageCache(private val cacheManager: CacheManager,
                   private val messageCache: Cache<Long, CachedMessage>) {
    private val logger by getLogger()
    
    fun putMessage(message: Message) {
        if (!message.isFromGuild) // only save guild messages.
            return
        putMessage(CachedMessage(message.idLong,
                                 message.channel.idLong,
                                 message.category?.idLong,
                                 message.guild.idLong,
                                 message.timeCreated.toInstant().toEpochMilli(),
                                 message.timeEdited?.toInstant()?.toEpochMilli(),
                                 message.author.idLong,
                                 message.author.name,
                                 message.author.discriminator,
                                 message.jumpUrl,
                                 message.contentRaw,
                                 message.isEdited,
                                 message.attachments.isNotEmpty()))
    }
    
    private fun putMessage(message: CachedMessage) {
        messageCache[message.id] = message
    }
    
    fun getMessage(messageId: Long): CachedMessage? {
        return if (messageCache.containsKey(messageId)) messageCache[messageId] else null
    }
}
