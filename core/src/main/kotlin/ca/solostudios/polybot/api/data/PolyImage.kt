/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyImage.kt is part of PolyhedralBot
 * Last modified on 08-02-2022 03:33 p.m.
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

package ca.solostudios.polybot.api.data

import java.util.Base64

public class PolyImage private constructor(public val data: ByteArray, public val format: Format) {
    public val dataUri: String
        get() {
            val hash = Base64.getEncoder().encode(data)
            return "data:${format.mimeTypes.first()};base64,$hash"
        }
    
    public companion object {
        public fun raw(data: ByteArray, format: PolyImage.Format): PolyImage {
            return PolyImage(data, format)
        }
    }
    
    public enum class Format(public val extensions: List<String>, public val mimeTypes: List<String>) {
        JPEG(listOf("jpeg", "jpg"), listOf("image/jpeg", "image/pjpeg")),
        PNG(listOf("png"), listOf("image/png")),
        WEBP(listOf("webp"), listOf("image/webp")),
        GIF(listOf("gif"), listOf("image/gif"));
        
        public companion object {
            public fun fromMimeType(mimeType: String): Format? {
                return values().find {
                    it.mimeTypes.contains(mimeType)
                }
            }
    
            public fun fromFileName(fileName: String): Format? {
                return values().find {
                    it.extensions.any { ext -> fileName.endsWith(".$ext", ignoreCase = true) }
                }
            }
        }
    }
}