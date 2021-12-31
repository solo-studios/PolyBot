/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file BotConfig.kt is part of PolyhedralBot
 * Last modified on 30-12-2021 05:44 p.m.
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

import ca.solostudios.polybot.config.automod.AutomodConfig
import com.fasterxml.jackson.annotation.JsonProperty

@Suppress("MemberVisibilityCanBePrivate")
data class BotConfig(
        @JsonProperty("token")
        val token: String,
        @JsonProperty("prefix")
        val prefix: String,
        @JsonProperty("prefixes")
        val prefixes: List<String>,
        @JsonProperty("ownerIds")
        val ownerIds: List<Long>,
        @JsonProperty("coOwnerIds")
        val coOwnerIds: List<Long>,
        @JsonProperty("activities")
        val activities: List<BotActivity>,
        @JsonProperty("automod")
        val automodConfig: AutomodConfig,
        @JsonProperty("embedSuppression")
        val embedSuppression: List<EmbedSuppression>
                    )