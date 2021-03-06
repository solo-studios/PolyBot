/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file InlineWebhookBuilder.kt is part of PolyhedralBot
 * Last modified on 31-12-2021 01:33 p.m.
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

@file:Suppress("NOTHING_TO_INLINE")

package ca.solostudios.polybot.util.jda

import club.minnced.discord.webhook.send.AllowedMentions
import club.minnced.discord.webhook.send.WebhookEmbedBuilder
import club.minnced.discord.webhook.send.WebhookMessageBuilder
import java.io.File
import java.io.InputStream

class InlineWebhookMessage(val builder: WebhookMessageBuilder) {
    fun build() = builder.build()
    
    val empty: Boolean
        get() = builder.isEmpty
    
    fun reset() = builder.reset()
    
    fun resetFiles() = builder.resetFiles()
    
    fun resetEmbeds() = builder.resetEmbeds()
    
    var allowedMentions: AllowedMentions = AllowedMentions.all()
        set(value) {
            builder.setAllowedMentions(value)
            field = value
        }
    
    inline fun embed(builder: InlineWebhookEmbed.() -> Unit) {
        this.builder.addEmbeds(InlineWebhookEmbed(WebhookEmbedBuilder()).also(builder).build())
    }
    
    var content: String? = null
        set(value) {
            builder.setContent(value)
            field = value
        }
    
    var username: String? = null
        set(value) {
            builder.setUsername(value)
            field = value
        }
    
    var avatarUrl: String? = null
        set(value) {
            builder.setAvatarUrl(value)
            field = value
        }
    
    var tts: Boolean = false
        set(value) {
            builder.setTTS(value)
            field = value
        }
    
    inline fun file(file: File, name: String = file.name) {
        builder.addFile(name, file)
    }
    
    inline fun file(name: String, data: ByteArray) {
        builder.addFile(name, data)
    }
    
    inline fun file(name: String, data: InputStream) {
        builder.addFile(name, data)
    }
}
