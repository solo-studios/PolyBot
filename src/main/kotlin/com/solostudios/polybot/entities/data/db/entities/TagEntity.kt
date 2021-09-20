/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file TagEntity.kt is part of PolyhedralBot
 * Last modified on 20-09-2021 01:08 a.m.
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

package com.solostudios.polybot.entities.data.db.entities

import java.time.LocalDateTime
import java.util.UUID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.`java-time`.datetime

object TagTable : UUIDTable("TAG_DATA") {
    val guild = reference("guild", GuildTable).index()
    val guildId = long("guild_id").index()
    val name = text("name", eagerLoading = true)
    val content = text("content", eagerLoading = true)
    val aliases = text("aliases", eagerLoading = true).default("")
    val created = datetime("created").default(LocalDateTime.now())
    val usages = long("usages").default(0)
}

class TagEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<TagEntity>(TagTable) {
        const val SEPARATOR = ","
    }
    
    var guild by GuildEntity referencedOn TagTable.guild
    var guildId by TagTable.guildId
    var name by TagTable.name
    var content by TagTable.content
    var aliases by TagTable.aliases.transform({ alias -> alias.joinToString(separator = SEPARATOR) },
                                              { aliases -> aliases.split(SEPARATOR) })
    var created by TagTable.created
    var usages by TagTable.usages
}