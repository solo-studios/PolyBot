/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file CachedMessage.kt is part of PolyhedralBot
 * Last modified on 31-07-2021 02:24 a.m.
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

import kotlinx.serialization.Serializable

@Serializable
data class CachedMessage(
        val id: Long,
        val channel: Long,
        val category: Long?,
        val guild: Long?,
        val timeCreated: Long,
        val timeEdited: Long? = null,
        val author: Long,
        val username: String,
        val discriminator: String,
        val url: String,
        val content: String,
        val edited: Boolean,
        val hasAttachments: Boolean,
        /*
        val attachments: Array<Long>,
        val author: Long,
        val category: Long?,
        val channel: Long,
        val content: String,
        val discriminator: String,
        val edited: Boolean,
        val guild: Long?,
        val id: Long,
        val timeCreated: Long,
        val timeEdited: Long? = null,
        val url: String,
        val username: String,
         */
                        ) {
    
    @Suppress("DuplicatedCode")
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        
        other as CachedMessage
        
        if (id != other.id) return false
        if (channel != other.channel) return false
        if (category != other.category) return false
        if (guild != other.guild) return false
        if (timeCreated != other.timeCreated) return false
        if (timeEdited != other.timeEdited) return false
        if (author != other.author) return false
        if (username != other.username) return false
        if (discriminator != other.discriminator) return false
        if (url != other.url) return false
        if (content != other.content) return false
        if (edited != other.edited) return false
        if (hasAttachments != other.hasAttachments) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + channel.hashCode()
        result = 31 * result + (category?.hashCode() ?: 0)
        result = 31 * result + (guild?.hashCode() ?: 0)
        result = 31 * result + timeCreated.hashCode()
        result = 31 * result + (timeEdited?.hashCode() ?: 0)
        result = 31 * result + author.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + discriminator.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + edited.hashCode()
        result = 31 * result + hasAttachments.hashCode()
        return result
    }
}