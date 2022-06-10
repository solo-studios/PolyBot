/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file InlineWebhookEmbedBuilder.kt is part of PolyBot
 * Last modified on 10-06-2022 11:33 a.m.
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

import club.minnced.discord.webhook.send.WebhookEmbed
import club.minnced.discord.webhook.send.WebhookEmbedBuilder
import java.time.OffsetDateTime

public class InlineWebhookEmbedBuilder(public val builder: WebhookEmbedBuilder) {
    public fun build(): WebhookEmbed = builder.build()
    
    public var timestamp: OffsetDateTime? = null
        set(value) {
            builder.setTimestamp(value)
            field = value
        }
    
    public var color: Int? = null
        set(value) {
            builder.setColor(value)
            field = value
        }
    
    public var description: String? = null
        set(value) {
            builder.setDescription(value)
            field = value
        }
    
    public var thumbnailUrl: String? = null
        set(value) {
            builder.setThumbnailUrl(value)
            field = value
        }
    
    public var imageUrl: String? = null
        set(value) {
            builder.setImageUrl(value)
            field = value
        }
    
    public inline fun footer(text: String, icon: String?) {
        builder.setFooter(WebhookEmbed.EmbedFooter(text, icon))
    }
    
    public inline fun title(text: String, url: String?) {
        builder.setTitle(WebhookEmbed.EmbedTitle(text, url))
    }
    
    public inline fun author(name: String, iconUrl: String?, url: String?) {
        builder.setAuthor(WebhookEmbed.EmbedAuthor(name, iconUrl, url))
    }
    
    public inline fun field(name: String, content: String, inline: Boolean = false) {
        builder.addField(WebhookEmbed.EmbedField(inline, name, content))
    }
}