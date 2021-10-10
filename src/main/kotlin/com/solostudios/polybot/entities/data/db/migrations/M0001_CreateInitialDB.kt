/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file M0001_CreateInitialDB.kt is part of PolyhedralBot
 * Last modified on 09-10-2021 09:50 p.m.
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

import com.solostudios.polybot.entities.data.db.entities.GuildTable
import com.solostudios.polybot.entities.data.db.entities.MemberTable
import gay.solonovamax.exposed.migrations.Migration
import java.time.Instant
import kotlinx.uuid.exposed.KotlinxUUIDTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction

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
    
    object MigrationMemberTable : KotlinxUUIDTable("MEMBER_DATA") {
        val guildId = long("guild_id").index()
        val memberId = long("member_id").index()
    }
    
    object MigrationTagTable : KotlinxUUIDTable("TAG_DATA") {
        val guild = reference("guild", GuildTable).index()
        val guildId = long("guild_id").index()
        val name = text("name", eagerLoading = true)
        val content = text("content", eagerLoading = true)
        val aliases = text("aliases", eagerLoading = true).default("")
        val created = timestamp.clientDefault(Instant::now)
        val usages = long("usages").default(0)
    }
    
    object MigrationWarnTable : KotlinxUUIDTable("WARN_DATA") {
        val guild = reference("guild", GuildTable).index()
        val guildId = long("guild_id").index()
        val member = reference("member", MemberTable).index()
        val memberId = long("member_id").index()
        val reason = text("reason")
        val moderator = reference("moderator", MemberTable)
        val moderatorId = long("moderator_id")
        val time = datetime("time")
    }
}