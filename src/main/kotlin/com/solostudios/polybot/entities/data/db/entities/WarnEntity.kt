/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file WarnEntity.kt is part of PolyhedralBot
 * Last modified on 18-09-2021 08:14 p.m.
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
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.`java-time`.datetime

object WarnTable : UUIDTable("WARN_DATA") {
    val guild = reference("guild", GuildTable).index()
    val guildId = long("guild_id").index()
    val member = reference("member", MemberTable).index()
    val memberId = long("member_id").index()
    val reason = text("reason")
    val moderator = reference("moderator", MemberTable)
    val moderatorId = long("moderator_id")
    val time = datetime("time")
}

class WarnEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    var guild: GuildEntity by GuildEntity referencedOn WarnTable.guild
    var guildId: Long by WarnTable.guildId
    var reason: String by WarnTable.reason
    var member: MemberEntity by MemberEntity referencedOn WarnTable.member
    var memberId: Long by WarnTable.memberId
    var moderator: MemberEntity by MemberEntity referencedOn WarnTable.moderator
    var moderatorId: Long by WarnTable.moderatorId
    var time: LocalDateTime by WarnTable.time
    
    companion object : EntityClass<UUID, WarnEntity>(WarnTable)
}