/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file BotActivity.kt is part of PolyhedralBot
 * Last modified on 09-10-2021 10:30 p.m.
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

import ca.solostudios.polybot.config.impl.CompetingActivity
import ca.solostudios.polybot.config.impl.DefaultActivity
import ca.solostudios.polybot.config.impl.ListeningActivity
import ca.solostudios.polybot.config.impl.StreamingActivity
import ca.solostudios.polybot.config.impl.WatchingActivity
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import net.dv8tion.jda.api.entities.Activity

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
             )
@JsonSubTypes(
        JsonSubTypes.Type(value = DefaultActivity::class, names = ["default", "playing"]),
        JsonSubTypes.Type(value = WatchingActivity::class, name = "watching"),
        JsonSubTypes.Type(value = ListeningActivity::class, name = "listening"),
        JsonSubTypes.Type(value = StreamingActivity::class, name = "streaming"),
        JsonSubTypes.Type(value = CompetingActivity::class, name = "competing"),
             )
abstract class BotActivity(
        @JsonProperty("name")
        val name: String,
        @JsonProperty("url")
        val url: String? = null,
                          ) {
    
    abstract fun getActivity(): Activity
}