/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyTextChannel.kt is part of PolyhedralBot
 * Last modified on 07-02-2022 01:12 a.m.
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

import net.dv8tion.jda.api.entities.TextChannel

public interface PolyTextChannel : PolyGuildChannel, PolyMessageChannel {
    /**
     * The JDA text channel that is being wrapped by this entity
     */
    public override val jdaChannel: TextChannel
    
    public val topic: String?
    
    public val isNSFW: Boolean
    
    public suspend fun deleteMessages(vararg messages: PolyMessage)
    
    public suspend fun deleteMessages(messages: Collection<PolyMessage>)
    
    public suspend fun deleteMessagesById(vararg messages: Long)
    
    public suspend fun deleteMessagesById(messages: Collection<Long>)
    
    public suspend fun setTopic(topic: String?)
    
    public suspend fun setNSFW(nsfw: Boolean)
    
    public suspend fun setSlowmode(slowmode: Int?)
    
    public suspend fun setNews(news: Boolean)
}