/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file UtilCommands.kt is part of PolyhedralBot
 * Last modified on 09-07-2021 03:32 p.m.
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

package com.solostudios.polybot.commands

import cloud.commandframework.annotations.CommandMethod
import com.solostudios.polybot.PolyBot
import com.solostudios.polybot.event.GuildMessageEvent
import dev.minn.jda.ktx.Embed
import java.time.temporal.ChronoUnit
import net.dv8tion.jda.api.entities.Message
import org.slf4j.kotlin.getLogger

class UtilCommands(val bot: PolyBot) {
    private val logger by getLogger()
    
    @CommandMethod("ping|pong")
    fun ping(message: Message) {
        message.textChannel.sendTyping().queue()
        message.jda.restPing.queue { restPing ->
            message.reply("Checking ping...").queue { msg ->
                val ping = message.timeCreated.until(msg.timeCreated, ChronoUnit.MILLIS)
                msg.editMessage("Ping: ${ping / 3}ms | Heartbeat: ${message.jda.gatewayPing}ms | Rest: ${restPing}ms").queue()
            }
        }
    }
    
    @CommandMethod("info|polybot|bot|botinfo")
    fun info(message: Message) {
    
    }
    
    @CommandMethod("serverinfo|server")
    fun serverInfo(event: GuildMessageEvent) {
        val embed = Embed {
            title = "$polydevEmoji Polyhedral Development Discord Server"
            color = 0x8fd032
            description = loremIpsum
            thumbnail = githubImage
            
            field {
                name = "Lorem ipsum"
                value = loremIpsum
                inline = false
            }
            field {
                name = "Lorem ipsum"
                value = loremIpsum
                inline = false
            }
            
            footer {
                name = "Requested by ${event.member.effectiveName} (${event.user.name}#${event.user.discriminator})"
                iconUrl = event.user.avatarUrl
            }
        }
        
        event.event.message.reply(embed)
                .mentionRepliedUser(false)
                .queue()
    }
    
    companion object {
        const val loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat." +
                " Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
                "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
        const val polydevEmoji = "<:polydev:853123841038352384>"
        const val githubImage = "https://github.com/PolyhedralDev.png"
    }
}