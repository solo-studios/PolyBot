/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyMessageEmbedImpl.kt is part of PolyBot
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

package ca.solostudios.polybot.impl.entities

import ca.solostudios.polybot.api.PolyBot
import ca.solostudios.polybot.api.entities.PolyMessageEmbed
import java.awt.Color
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import net.dv8tion.jda.api.entities.EmbedType
import net.dv8tion.jda.api.entities.MessageEmbed

internal class PolyMessageEmbedImpl(
        override val bot: PolyBot,
        override val jdaMessageEmbed: MessageEmbed,
                                   ) : PolyMessageEmbed {
    override val url: String?
        get() = jdaMessageEmbed.url
    override val title: String?
        get() = jdaMessageEmbed.title
    override val type: EmbedType
        get() = jdaMessageEmbed.type
    override val thumbnail: MessageEmbed.Thumbnail?
        get() = jdaMessageEmbed.thumbnail
    override val siteProvider: MessageEmbed.Provider?
        get() = jdaMessageEmbed.siteProvider
    override val author: MessageEmbed.AuthorInfo?
        get() = jdaMessageEmbed.author
    override val videoInfo: MessageEmbed.VideoInfo?
        get() = jdaMessageEmbed.videoInfo
    override val footer: MessageEmbed.Footer?
        get() = jdaMessageEmbed.footer
    override val image: MessageEmbed.ImageInfo?
        get() = jdaMessageEmbed.image
    override val fields: Flow<MessageEmbed.Field>
        get() = jdaMessageEmbed.fields.asFlow()
    override val color: Color?
        get() = jdaMessageEmbed.color
    override val timestamp: Instant?
        get() = jdaMessageEmbed.timestamp?.toInstant()?.toKotlinInstant()
    override val isEmpty: Boolean
        get() = jdaMessageEmbed.isEmpty
    override val length: Int
        get() = jdaMessageEmbed.length
    override val isSendable: Boolean
        get() = jdaMessageEmbed.isSendable
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PolyMessageEmbedImpl) return false
        
        if (jdaMessageEmbed != other.jdaMessageEmbed) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        return jdaMessageEmbed.hashCode()
    }
}