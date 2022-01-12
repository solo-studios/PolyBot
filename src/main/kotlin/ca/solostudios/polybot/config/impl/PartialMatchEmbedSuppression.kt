/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PartialMatchEmbedSuppression.kt is part of PolyhedralBot
 * Last modified on 30-12-2021 05:43 p.m.
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

package ca.solostudios.polybot.config.impl

import ca.solostudios.polybot.config.EmbedSuppression
import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URL

class PartialMatchEmbedSuppression(
        @JsonProperty("ignoreCase")
        val ignoreCase: Boolean = true,
        query: String? = null,
        host: String? = null,
        path: String? = null,
        protocol: String? = null,
                                  ) : EmbedSuppression(query, host, path, protocol) {
    override fun matches(url: URL): Boolean {
        if (query != null) {
            if (url.query.contains(query, ignoreCase))
                return true
        }
        if (host != null) {
            if (url.host.contains(host, ignoreCase))
                return true
        }
        if (path != null) {
            if (url.path.contains(path, ignoreCase))
                return true
        }
        
        if (protocol != null) {
            if (url.protocol.contains(protocol, ignoreCase))
                return true
        }
        
        return false
    }
}