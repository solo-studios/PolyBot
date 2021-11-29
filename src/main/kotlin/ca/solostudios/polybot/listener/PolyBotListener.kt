/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyBotListener.kt is part of PolyhedralBot
 * Last modified on 29-11-2021 03:03 p.m.
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

package ca.solostudios.polybot.listener

import ca.solostudios.polybot.PolyBot
import java.time.format.DateTimeFormatter
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.DisconnectEvent
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.ReconnectedEvent
import net.dv8tion.jda.api.events.ResumedEvent
import net.dv8tion.jda.api.events.ShutdownEvent
import net.dv8tion.jda.api.events.StatusChangeEvent
import net.dv8tion.jda.api.events.guild.GuildAvailableEvent
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent
import net.dv8tion.jda.api.events.guild.GuildUnavailableEvent
import net.dv8tion.jda.api.events.guild.UnavailableGuildJoinedEvent
import net.dv8tion.jda.api.events.guild.UnavailableGuildLeaveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.kotlin.*

class PolyBotListener(val bot: PolyBot) : ListenerAdapter() {
    private val logger by getLogger()
    
    override fun onReady(event: ReadyEvent) {
        logger.info { "PolyBot is now fully initialized. There are ${event.guildAvailableCount} available guilds and ${event.guildTotalCount} total guilds." }
    }
    
    override fun onResumed(event: ResumedEvent) {
        logger.debug { "PolyBot resumed connection with Discord websocket." }
    }
    
    override fun onReconnected(event: ReconnectedEvent) {
        logger.debug { "PolyBot reconnected to Discord websocket." }
    }
    
    override fun onDisconnect(event: DisconnectEvent) {
        logger.debug { "PolyBot disconnected from Discord websocket at ${event.timeDisconnected.format(DateTimeFormatter.ISO_DATE_TIME)} with code ${event.closeCode}" }
    }
    
    override fun onShutdown(event: ShutdownEvent) {
        logger.debug { "PolyBot has finished shutting down." }
    }
    
    override fun onStatusChange(event: StatusChangeEvent) {
        logger.debug { "Status changed from ${event.oldStatus} -> ${event.newStatus}" }
        
        when (event.newStatus) {
            JDA.Status.SHUTTING_DOWN   -> logger.warn { "Shutdown process initiated" }
            JDA.Status.FAILED_TO_LOGIN -> logger.error { "Failed to login" }
            JDA.Status.CONNECTED       -> logger.info { "PolyBot connected successfully" }
    
            else                       -> {}
        }
    }
    
    override fun onGuildJoin(event: GuildJoinEvent) {
        val guild = event.guild
        logger.debug { "Joined guild ${guild.name}, id: ${guild.id}" }
    }
    
    override fun onGuildLeave(event: GuildLeaveEvent) {
        val guild = event.guild
        logger.debug { "Left guild ${guild.name}, id: ${guild.id}" }
    }
    
    override fun onGuildAvailable(event: GuildAvailableEvent) {
        val guild = event.guild
        logger.debug { "Guild ${guild.name}, id: ${guild.id}, changed status from unavailable to available." }
    }
    
    override fun onGuildUnavailable(event: GuildUnavailableEvent) {
        val guild = event.guild
        logger.debug { "Guild ${guild.name}, id: ${guild.id}, changed from status unavailable to available." }
    }
    
    override fun onUnavailableGuildJoined(event: UnavailableGuildJoinedEvent) {
        val guildId = event.guildId
        logger.debug { "Joined unavailable guild with id $guildId" }
    }
    
    override fun onUnavailableGuildLeave(event: UnavailableGuildLeaveEvent) {
        val guildId = event.guildId
        logger.debug { "Left unavailable guild with id $guildId" }
    }
}