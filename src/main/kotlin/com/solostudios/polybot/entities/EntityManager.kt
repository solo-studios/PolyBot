/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file EntityManager.kt is part of PolyhedralBot
 * Last modified on 09-10-2021 06:06 p.m.
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

package com.solostudios.polybot.entities

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.solostudios.polybot.PolyBot
import com.solostudios.polybot.entities.data.PolyGuildData
import com.solostudios.polybot.entities.data.PolyMemberData
import com.solostudios.polybot.entities.data.PolyTagData
import com.solostudios.polybot.entities.data.PolyWarnData
import com.solostudios.polybot.entities.data.db.entities.GuildEntity
import com.solostudios.polybot.entities.data.db.entities.MemberEntity
import com.solostudios.polybot.entities.data.db.entities.MemberTable
import com.solostudios.polybot.entities.data.db.entities.TagEntity
import com.solostudios.polybot.entities.data.db.entities.WarnEntity
import com.solostudios.polybot.service.ShutdownService
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import gay.solonovamax.exposed.migrations.loadMigrationsFrom
import gay.solonovamax.exposed.migrations.runMigrations
import java.util.concurrent.TimeUnit
import kotlinx.uuid.UUID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.kotlin.*

class EntityManager(val bot: PolyBot) : ShutdownService() {
    private val logger by getLogger()
    
    private val db: Database
    private val hikari: HikariDataSource
    
    init {
        val config = bot.config.databaseConfig
        
        logger.info { "Connecting to bot database" }
        
        val hikariConfig = HikariConfig().apply {
            jdbcUrl = config.url
            driverClassName = config.driver
            username = config.username
            password = config.password
        }
        
        hikari = HikariDataSource(hikariConfig)
        
        db = Database.connect(hikari)
        db.useNestedTransactions = true
        
        logger.info { "Connected to bot database. Loading migrations..." }
        val migrations = loadMigrationsFrom("com.solostudios.polybot.entities.data.db.migrations")
        
        logger.info { "Migrations loaded. Running migrations..." }
        
        runMigrations(migrations, db)
        
        logger.info { "Ran migrations successfully." }
    }
    
    @Suppress("UNCHECKED_CAST")
    private val guildCache = CacheBuilder.newBuilder()
            .expireAfterAccess(20, TimeUnit.MINUTES)
            .maximumSize(1_000)
            .removalListener<Long, PolyGuildData> { (key: Long?, value: PolyGuildData?) ->
                if (key != null && value != null) {
                    saveGuildData(value)
                } else {
                    logger.error { "One of key or value was null while saving guild. key: $key, value: $value" }
                }
            }.build { getGuildData(it) }
    
    @Suppress("UNCHECKED_CAST")
    private val memberCache = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .removalListener<Pair<Long, Long>, PolyMemberData> { (key: Pair<Long, Long>?, value: PolyMemberData?) ->
                if (key != null && value != null) {
                    saveMemberData(value)
                } else {
                    logger.error { "One of key or value was null while saving member. key: $key, value: $value" }
                }
            }.build { getMemberData(it.first, it.second) }
    
    fun getMember(member: PolyMember): PolyMemberData = memberCache[member.guildId to member.id]
    
    fun getWarns(member: PolyMember) = getWarnDataList(member.guildId, member.id)
    
    fun getWarn(uuid: UUID): PolyWarnData = getWarnData(uuid)!!
    
    fun deleteWarn(warnData: PolyWarnData) = deleteWarnData(warnData)
    
    fun saveWarn(warnData: PolyWarnData) = saveWarnData(warnData)
    
    fun getGuild(guild: PolyGuild): PolyGuildData = guildCache[guild.id]
    
    private fun getMemberData(guildId: Long, memberId: Long): PolyMemberData {
        return transaction(db) {
            val entity = getMemberEntity(guildId, memberId)
            
            return@transaction PolyMemberData(bot, entity.id.value, entity.guildId, entity.memberId)
        }
    }
    
    private fun saveMemberData(memberData: PolyMemberData) {
        transaction(db) {
            val entity = getMemberEntity(memberData.guildId, memberData.userId)
        }
    }
    
    private fun getWarnDataList(guildId: Long, memberId: Long): List<PolyWarnData> {
        return transaction(db) {
            val memberEntity = getMemberEntity(guildId, memberId)
            
            return@transaction memberEntity.warns.map { warn ->
                PolyWarnData(bot, warn.id.value, warn.guildId, warn.memberId, warn.moderatorId, warn.time, warn.reason)
            }
        }
    }
    
    private fun getWarnData(uuid: UUID): PolyWarnData? {
        val entity = getWarnEntity(uuid)
        
        return entity?.let {
            PolyWarnData(bot, entity.id.value, entity.guildId, entity.memberId, entity.moderatorId, entity.time, entity.reason)
        }
    }
    
    private fun deleteWarnData(warnData: PolyWarnData) {
        transaction(db) {
            getWarnEntity(warnData.uuid)?.delete()
        }
    }
    
    private fun saveWarnData(warnData: PolyWarnData) {
        transaction(db) {
            val entity = getWarnEntity(warnData.uuid) ?: WarnEntity.new(warnData.uuid) {
                guild = getGuildEntity(warnData.guildId)
                guildId = warnData.guildId
                member = getMemberEntity(warnData.guildId, warnData.memberId)
                memberId = warnData.memberId
                moderator = getMemberEntity(warnData.guildId, warnData.moderatorId)
                time = warnData.time
            }
            
            entity.reason = warnData.reason
        }
    }
    
    private fun getGuildData(guildId: Long): PolyGuildData {
        return transaction(db) {
            val entity = getGuildEntity(guildId)
            val tags = entity.tags
            
            val tagList = tags.map { tag ->
                PolyTagData(bot, tag.id.value, tag.guildId, tag.name, tag.content, tag.aliases.toMutableList(), tag.created, tag.usages)
            }.toMutableList()
            
            return@transaction PolyGuildData(
                    bot,
                    entity.id.value,
                    tagList,
                    entity.loggingChannel,
                    entity.mutedRole,
                    entity.autoRole,
                    entity.prefix,
                    entity.autoDehoist,
                    entity.filterInvites,
                                            )
        }
    }
    
    private fun saveGuildData(guildData: PolyGuildData) {
        transaction(db) {
            logger.info { "Saving guild ${guildData.guildId}" }
            val entity = getGuildEntity(guildData.guildId)
    
            val tagIdMap = guildData.tags.map { it.uuid }
    
            entity.loggingChannel = guildData.loggingChannelId
            entity.mutedRole = guildData.mutedRoleId
            entity.autoRole = guildData.autoRoleId
            entity.prefix = guildData.prefix
            entity.autoDehoist = guildData.autoDehoist
            entity.filterInvites = guildData.filterInvites
    
            entity.tags.filter {
                tagIdMap.contains(it.id.value)
            }.forEach { // delete removed tags
                it.delete()
            }
    
            guildData.tags.forEach(::saveTagData)
        }
    }
    
    private fun saveTagData(tagData: PolyTagData) {
        transaction(db) {
            logger.info { "Saving tag ${tagData.name} with content ${tagData.content}" }
            val entity = TagEntity.findById(tagData.uuid) ?: TagEntity.new {
                guild = getGuildEntity(tagData.guildId)
                guildId = tagData.guildId
                created = tagData.created
            }
    
            entity.name = tagData.name
            entity.content = tagData.content
            entity.aliases = tagData.aliases
            entity.usages = tagData.usages
        }
    }
    
    private fun getWarnEntity(uuid: UUID): WarnEntity? {
        return transaction(db) {
            return@transaction WarnEntity.findById(uuid)
        }
    }
    
    private fun getMemberEntity(guildId: Long, memberId: Long): MemberEntity {
        return transaction(db) {
            val memberEntity = MemberEntity.find {
                MemberTable.memberId eq memberId and (MemberTable.guildId eq guildId)
            }.limit(1).firstOrNull()
            
            memberEntity ?: MemberEntity.new {
                this.memberId = memberId
                this.guildId = guildId
            }
        }
    }
    
    private fun getGuildEntity(guildId: Long): GuildEntity {
        return transaction(db) {
            val guildEntity = GuildEntity.findById(guildId)
            
            return@transaction guildEntity ?: GuildEntity.new(guildId) {
                
            }
        }
    }
    
    override fun serviceShutdown() {
        guildCache.cleanUp()
        guildCache.invalidateAll()
        guildCache.cleanUp()
        guildCache.cleanUp()
        memberCache.invalidateAll()
        guildCache.cleanUp()
    
        hikari.close()
    }
}

private inline fun <T, U> CacheBuilder<T, U>.build(crossinline function: (T) -> U): LoadingCache<T, U> {
    return this.build(object : CacheLoader<T, U>() {
        override fun load(key: T): U = function(key)
    })
}
