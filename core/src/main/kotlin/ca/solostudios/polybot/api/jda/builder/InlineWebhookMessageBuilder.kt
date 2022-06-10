/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file InlineWebhookMessageBuilder.kt is part of PolyBot
 * Last modified on 10-06-2022 11:32 a.m.
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

@file:Suppress("NOTHING_TO_INLINE")

package ca.solostudios.polybot.api.jda.builder

import club.minnced.discord.webhook.send.AllowedMentions
import club.minnced.discord.webhook.send.WebhookEmbedBuilder
import club.minnced.discord.webhook.send.WebhookMessage
import club.minnced.discord.webhook.send.WebhookMessageBuilder
import java.io.File
import java.io.InputStream

public class InlineWebhookMessageBuilder(public val builder: WebhookMessageBuilder) {
    public fun build(): WebhookMessage = builder.build()
    
    public val empty: Boolean
        get() = builder.isEmpty
    
    public fun reset() {
        builder.reset()
    }
    
    public fun resetFiles() {
        builder.resetFiles()
    }
    
    public fun resetEmbeds() {
        builder.resetEmbeds()
    }
    
    public var allowedMentions: AllowedMentions = AllowedMentions.all()
        set(value) {
            builder.setAllowedMentions(value)
            field = value
        }
    
    public inline fun embed(builder: InlineWebhookEmbedBuilder.() -> Unit) {
        this.builder.addEmbeds(InlineWebhookEmbedBuilder(WebhookEmbedBuilder()).also(builder).build())
    }
    
    public var content: String? = null
        set(value) {
            builder.setContent(value)
            field = value
        }
    
    public var username: String? = null
        set(value) {
            builder.setUsername(value)
            field = value
        }
    
    public var avatarUrl: String? = null
        set(value) {
            builder.setAvatarUrl(value)
            field = value
        }
    
    public var tts: Boolean = false
        set(value) {
            builder.setTTS(value)
            field = value
        }
    
    public inline fun file(file: File, name: String = file.name) {
        builder.addFile(name, file)
    }
    
    public inline fun file(name: String, data: ByteArray) {
        builder.addFile(name, data)
    }
    
    public inline fun file(name: String, data: InputStream) {
        builder.addFile(name, data)
    }
}
