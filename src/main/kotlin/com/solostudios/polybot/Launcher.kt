/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Launcher.kt is part of PolyhedralBot
 * Last modified on 15-07-2021 11:36 p.m.
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

package com.solostudios.polybot

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.jasonclawson.jackson.dataformat.hocon.HoconFactory
import com.solostudios.polybot.config.PolyConfig
import com.solostudios.polybot.util.onJvmShutdown
import com.solostudios.polybot.util.or
import java.io.File
import java.sql.Connection
import net.dv8tion.jda.DefaultJDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.Compression
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.slf4j.kotlin.getLogger
import kotlin.system.exitProcess

private val logger by getLogger()

fun main() {
    
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    
    val config = readConfig("polybot.conf")
    
    val jda = DefaultJDABuilder(config.botConfig.token) {
        disableCache = listOf(CacheFlag.ACTIVITY,
                              CacheFlag.VOICE_STATE,
                              CacheFlag.EMOTE,
                              CacheFlag.CLIENT_STATUS,
                              CacheFlag.MEMBER_OVERRIDES,
                              CacheFlag.ROLE_TAGS)
        
        disableIntents = listOf(GatewayIntent.DIRECT_MESSAGE_TYPING,
                                GatewayIntent.GUILD_MESSAGE_TYPING,
                                GatewayIntent.GUILD_VOICE_STATES,
                                GatewayIntent.GUILD_PRESENCES)
        
        enableIntents = listOf(GatewayIntent.GUILD_MEMBERS,
                               GatewayIntent.GUILD_BANS,
                               GatewayIntent.GUILD_EMOJIS,
                               GatewayIntent.GUILD_WEBHOOKS,
                               GatewayIntent.GUILD_INVITES,
                               GatewayIntent.GUILD_MESSAGES,
                               GatewayIntent.GUILD_MESSAGE_REACTIONS,
                               GatewayIntent.DIRECT_MESSAGES,
                               GatewayIntent.DIRECT_MESSAGE_REACTIONS)
        
        memberCachePolicy = MemberCachePolicy.ONLINE or MemberCachePolicy.VOICE or MemberCachePolicy.OWNER
        chunkingFilter = ChunkingFilter.NONE
        compression = Compression.ZLIB
        largeThreshold = 250
        
        status = OnlineStatus.IDLE
        activity = Activity.watching("Starting up...")
        
        
        rawEvents = false
        enableShutdownHook = true
        bulkDeleteSplitting = false
    }
    
    val polybot = PolyBot(config, jda)
    
    onJvmShutdown("Bot-Shutdown") {
        polybot.shutdown()
    }
}

private fun readConfig(fileName: String) = readConfig(File(fileName))

private fun readConfig(file: File): PolyConfig {
    if (!file.exists()) {
        createConfigFileAndExit(file)
    }
    val mapper = ObjectMapper(HoconFactory())
            .registerKotlinModule()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true)
            .configure(JsonParser.Feature.IGNORE_UNDEFINED, true)
    
    return mapper.readValue(file)
}

private fun createConfigFileAndExit(file: File) {
    logger.error("File '${file.name}' not present. Writing default config to '${file.name}'.\n" +
                         "Please edit this file to include the bot token + all other fields.")
    
    val stream = PolyBot::class.java.getResourceAsStream("/default.conf")
    
    if (stream != null)
        stream.transferTo(file.outputStream())
    else {
        logger.error("Could not write default config as it was not found.")
        exitProcess(-2)
    }
    
    logger.info("Wrote default config to '${file.name}'. Please edit the file before running again.")
    
    exitProcess(1)
}