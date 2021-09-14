/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file M0001_CreateInitialDB.kt is part of PolyhedralBot
 * Last modified on 13-09-2021 08:51 p.m.
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

package com.solostudios.polybot.entities.data.db.migrations

import gay.solonovamax.exposed.migrations.Migration
import java.time.LocalDateTime
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.`java-time`.datetime

@Suppress("ClassName", "unused")
class M0001_CreateInitialDB : Migration() {
    override fun Transaction.run() {
        SchemaUtils.create(MigrationGuildTable, MigrationMemberTable, MigrationTagTable, MigrationWarnTable)
    }
    
    object MigrationGuildTable : LongIdTable("GUILD_DATA") {
        val loggingChannel = long("logging_channel").default(-1)
        val mutedRole = long("muted_role").default(-1)
        val autoRole = long("auto_role").default(-1)
        val prefix = text("prefix").nullable()
        
        val autoDehoist = bool("auto_dehoist").default(false)
        val filterInvites = bool("filter_invites").default(false)
    }
    
    object MigrationMemberTable : UUIDTable("MEMBER_DATA") {
        val guildId = long("guild_id").index()
        val memberId = long("member_id").index()
    }
    
    object MigrationTagTable : UUIDTable("TAG_DATA") {
        val guild = reference("guild", MigrationGuildTable).index()
        val guildId = long("guild_id").index()
        val content = text("content", eagerLoading = true)
        val aliases = text("aliases").default("")
        val created = datetime("created").default(LocalDateTime.now())
        val usages = long("usages").default(0)
    }
    
    object MigrationWarnTable : UUIDTable("WARN_DATA") {
        val guild = reference("guild", MigrationGuildTable).index()
        val guildId = long("guild_id").index()
        val member = reference("member", MigrationMemberTable).index()
        val memberId = long("member").index()
        val reason = text("reason")
        val moderator = reference("moderator", MigrationMemberTable)
        val moderatorId = long("moderator")
        val time = datetime("time")
    }
}