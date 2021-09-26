/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyTagData.kt is part of PolyhedralBot
 * Last modified on 25-09-2021 09:42 p.m.
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

package com.solostudios.polybot.entities.data

import com.solostudios.polybot.PolyBot
import com.solostudios.polybot.entities.PolyGuild
import java.time.Instant
import kotlinx.uuid.UUID
import kotlinx.uuid.generateUUID

data class PolyTagData(
        val bot: PolyBot,
        val uuid: UUID,
        val guildId: Long,
        var name: String,
        var content: String,
        var aliases: MutableList<String>,
        val created: Instant,
        var usages: Long,
                      ) {
    val guild by bot.guild(guildId)
}

@Suppress("FunctionName")
fun Tag(bot: PolyBot, guild: PolyGuild, name: String, content: String, aliases: MutableList<String> = mutableListOf()): PolyTagData {
    return PolyTagData(bot, UUID.generateUUID(bot.globalRandom), guild.id, name, content, aliases, created = Instant.now(), 0)
}