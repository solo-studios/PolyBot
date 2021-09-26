/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file TagEntity.kt is part of PolyhedralBot
 * Last modified on 25-09-2021 10:13 p.m.
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

import java.time.Instant
import kotlinx.uuid.UUID
import kotlinx.uuid.exposed.KotlinxUUIDEntity
import kotlinx.uuid.exposed.KotlinxUUIDEntityClass
import kotlinx.uuid.exposed.KotlinxUUIDTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.`java-time`.timestamp

object TagTable : KotlinxUUIDTable("TAG_DATA") {
    val guild = reference("guild", GuildTable).index()
    val guildId = long("guild_id").index()
    val name = text("name", eagerLoading = true)
    val content = text("content", eagerLoading = true)
    val aliases = text("aliases", eagerLoading = true).default("")
    val created = timestamp("created").clientDefault(Instant::now)
    val usages = long("usages").default(0)
}

class TagEntity(id: EntityID<UUID>) : KotlinxUUIDEntity(id) {
    companion object : KotlinxUUIDEntityClass<TagEntity>(TagTable) {
        const val SEPARATOR = ";"
    }
    
    var guild by GuildEntity referencedOn TagTable.guild
    var guildId by TagTable.guildId
    var name by TagTable.name
    var content by TagTable.content
    var aliases by TagTable.aliases.transform({ alias -> alias.joinToString(separator = SEPARATOR) },
                                              { aliases -> aliases.split(SEPARATOR).filter { it.isNotEmpty() } })
    var created by TagTable.created
    var usages by TagTable.usages
}