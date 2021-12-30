/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file EmbedSuppression.kt is part of PolyhedralBot
 * Last modified on 30-12-2021 05:35 p.m.
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

package ca.solostudios.polybot.config

import ca.solostudios.polybot.config.impl.FullMatchEmbedSuppression
import ca.solostudios.polybot.config.impl.PartialMatchEmbedSuppression
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.net.URL

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
             )
@JsonSubTypes(
        JsonSubTypes.Type(value = FullMatchEmbedSuppression::class, name = "full"),
        JsonSubTypes.Type(value = PartialMatchEmbedSuppression::class, name = "partial"),
             )
abstract class EmbedSuppression(
        @JsonProperty("query")
        val query: String? = null,
        @JsonProperty("host")
        val host: String? = null,
        @JsonProperty("path")
        val path: String? = null,
        @JsonProperty("protocol")
        val protocol: String? = null,
                               ) {
    abstract fun matches(url: URL): Boolean
}