/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyPluginDependencySerializer.kt is part of PolyBot
 * Last modified on 20-08-2022 05:43 p.m.
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

package ca.solostudios.polybot.impl.serializers

import ca.solostudios.polybot.api.plugin.info.PluginDependency
import ca.solostudios.strata.kotlin.toVersionRange
import ca.solostudios.strata.parser.tokenizer.ParseException
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public object PolyPluginDependencySerializer : KSerializer<PluginDependency> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("PolyPluginDependency", PrimitiveKind.STRING)
    
    override fun serialize(encoder: Encoder, value: PluginDependency) {
        encoder.encodeString("${value.group}:${value.id}:${value.versionRange.formatted}")
    }
    
    override fun deserialize(decoder: Decoder): PluginDependency {
        val string = decoder.decodeString()
        val (group, id, versionRange) = string.split(":")
        
        try {
            return PluginDependency(group, id, versionRange.toVersionRange())
        } catch (e: ParseException) {
            throw SerializationException(e)
        }
    }
}