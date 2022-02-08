/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyMessageBuilder.kt is part of PolyhedralBot
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

package ca.solostudios.polybot.api.builder

import ca.solostudios.polybot.api.data.PolyFile
import ca.solostudios.polybot.api.entities.PolyMessage
import ca.solostudios.polybot.api.entities.PolyMessageChannel
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

public interface PolyMessageBuilder {
    public var tts: Boolean?
    
    public val channel: PolyMessageChannel?
    
    public var content: String?
    
    public var nonce: String?
    
    public var referencedMessage: PolyMessage?
    
    public val embeds: MutableList<PolyMessageEmbedBuilder>
    
    public var mentions: PolyAllowedMentionsBuilder
    
    public val files: MutableList<PolyFile>
    
    public var mentionRepliedUser: Boolean
    
    public val empty: Boolean
    
    public val edit: Boolean
    
    public fun content(contentBlock: StringBuilder.() -> Unit)
    
    public fun addFile(name: String, data: InputStream, spoiler: Boolean = false): PolyFile
    
    public fun addFile(name: String, data: ByteArray, spoiler: Boolean = false): PolyFile {
        return addFile(name, ByteArrayInputStream(data), spoiler)
    }
    
    public fun addFile(file: File, spoiler: Boolean = false): PolyFile {
        return addFile(file.name, file, spoiler)
    }
    
    public fun addFile(name: String, file: File, spoiler: Boolean = false): PolyFile {
        return addFile(name, FileInputStream(file), spoiler)
    }
    
    public fun embed(embedBuilder: PolyMessageEmbedBuilder.() -> Unit)
    
    public fun mentions(allowedMentionsBuilder: PolyAllowedMentionsBuilder.() -> Unit)
    
    public fun build(): PolyMessage
}