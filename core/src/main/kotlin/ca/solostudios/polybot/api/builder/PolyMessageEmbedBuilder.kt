/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyMessageEmbedBuilder.kt is part of PolyhedralBot
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

package ca.solostudios.polybot.api.builder

import ca.solostudios.polybot.api.entities.PolyMessageEmbed
import java.awt.Color
import kotlinx.datetime.Instant

public interface PolyMessageEmbedBuilder {
    public var description: String?
    
    public var title: String?
    
    public var url: String?
    
    public var color: Color?
    
    public var timestamp: Instant?
    
    public val fields: MutableList<ca.solostudios.polybot.builder.PolyMessageEmbedBuilder.PolyFieldBuilder>
    
    public var thumbnail: String?
    
    public var image: String?
    
    public val author: ca.solostudios.polybot.builder.PolyMessageEmbedBuilder.PolyAuthorBuilder
    
    public val footer: ca.solostudios.polybot.builder.PolyMessageEmbedBuilder.PolyFooterBuilder
    
    public fun field(
            name: String = ca.solostudios.polybot.builder.PolyMessageEmbedBuilder.Companion.ZERO_WIDTH_SPACE,
            value: String = ca.solostudios.polybot.builder.PolyMessageEmbedBuilder.Companion.ZERO_WIDTH_SPACE,
            inline: Boolean = true,
            block: ca.solostudios.polybot.builder.PolyMessageEmbedBuilder.PolyFieldBuilder.() -> Unit,
                    )
    
    public fun author(
            name: String? = null,
            url: String? = null,
            iconUrl: String? = null,
            block: ca.solostudios.polybot.builder.PolyMessageEmbedBuilder.PolyAuthorBuilder.() -> Unit,
                     )
    
    public fun footer(
            name: String? = null,
            iconUrl: String? = null,
            proxyIconUrl: String? = null,
            block: ca.solostudios.polybot.builder.PolyMessageEmbedBuilder.PolyFooterBuilder.() -> Unit,
                     )
    
    public fun build(): PolyMessageEmbed
    
    public interface PolyFieldBuilder {
        public var name: String?
        
        public var value: String?
        
        public var inline: Boolean?
    }
    
    public interface PolyAuthorBuilder {
        public var name: String?
        
        public var url: String?
        
        public var iconUrl: String?
    }
    
    public interface PolyFooterBuilder {
        public var text: String?
        public var iconUrl: String?
    }
    
    public companion object {
        public const val ZERO_WIDTH_SPACE: String = "\u200E"
    }
}