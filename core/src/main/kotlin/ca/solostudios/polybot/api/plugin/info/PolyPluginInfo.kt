/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyPluginInfo.kt is part of PolyBot
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

package ca.solostudios.polybot.api.plugin.info

import ca.solostudios.polybot.api.plugin.info.serializers.StrataVersionSerializer
import ca.solostudios.strata.version.Version
import kotlinx.serialization.Serializable

@Serializable
public data class PolyPluginInfo(
        public val schemaVersion: Int,
        public val group: String,
        public val id: String,
        @Serializable(with = StrataVersionSerializer::class)
        public val version: Version,
        public val entrypoints: List<String>?,
        public val depends: List<PolyPluginDependency>?,
        public val breaks: List<PolyPluginDependency>?,
        public val name: String?,
        public val description: String?,
        public val authors: List<String>?,
        public val contributors: List<String>?,
        public val contact: Map<String, String>?,
        public val license: String?,
                                )