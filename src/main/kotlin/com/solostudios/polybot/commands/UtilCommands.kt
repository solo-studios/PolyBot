/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file UtilCommands.kt is part of PolyhedralBot
 * Last modified on 20-09-2021 12:53 a.m.
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
import com.solostudios.polybot.Version
import com.solostudios.polybot.cloud.PolyCommandContainer
import com.solostudios.polybot.cloud.PolyCommands
import com.solostudios.polybot.cloud.event.GuildMessageEvent
import com.solostudios.polybot.cloud.permission.annotations.JDAGuildCommand
import com.solostudios.polybot.entities.PolyMessage
import com.solostudios.polybot.util.commandCount
import com.solostudios.polybot.util.freeMemory
import com.solostudios.polybot.util.maxMemory
import com.solostudios.polybot.util.runtime
import com.solostudios.polybot.util.runtimeMXBean
import com.solostudios.polybot.util.shortFormat
import com.solostudios.polybot.util.totalMemory
import dev.minn.jda.ktx.Embed
import dev.minn.jda.ktx.await
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.JDAInfo
import org.intellij.lang.annotations.Language
import org.slf4j.kotlin.*
import kotlin.time.Duration.Companion.milliseconds

@PolyCommandContainer
class UtilCommands(bot: PolyBot) : PolyCommands(bot) {
    private val logger by getLogger()
    
    @CommandMethod("ping|pong")
    fun ping(message: PolyMessage) {
        bot.scope.launch {
            message.textChannel.sendTyping()
            val restPing = bot.jda.restPing.await()
            
            val msg = message.reply("Checking ping...")
            
            val ping = message.timeCreated.until(msg.timeCreated, ChronoUnit.MILLIS)
            msg.edit("Ping: ${ping / 3}ms | Heartbeat: ${bot.jda.gatewayPing}ms | Rest: ${restPing}ms")
        }
    }
    
    @CommandMethod("info|polybot|bot|botinfo")
    fun info(message: PolyMessage) {
        bot.scope.launch {
            val embed = Embed {
                author {
                    name = "Polybot"
                    iconUrl = bot.avatarUrl
                }
                title = "PolyBot Info"
                
                field("Description", inline = false) {
                    value = """
                    PolyBot is a multipurpose bot designed for the Polyhedral Development discord server.
                    It is created to help manage the server and perform various tasks automatically to aid the moderators.
                    
                    The goal of this bot was to create a FOSS discord bot for managing servers centered around Open Source projects.
                """.trimIndent()
                }
                
                field("Author", "solonovamax#6983")
                field("Repository", "[PolyBot](https://github.com/solonovamax/PolyBot)")
                field("Library", "[JDA](https://github.com/DV8FromTheWorld/JDA)")
                
                field("Version") {
                    value = Version.version
                }
                field("Uptime", milliseconds(runtimeMXBean.uptime).shortFormat())
                field("Members") {
                    value = "%,d".format(bot.totalMembers)
                }
                
                field("JDA Version") {
                    value = JDAInfo.VERSION
                }
                field("Memory Usage") {
                    val free = runtime.freeMemory
                    val total = runtime.totalMemory
                    val max = runtime.maxMemory
                    val used = total - free
                    
                    value = "%.2f MB/%.2f MB".format(used.toFloat() / (1 shl 20), max.toFloat() / (1 shl 20))
                }
                field("JVM Version") {
                    value = System.getProperty("java.runtime.name") + "\n" + System.getProperty("java.runtime.version")
                }
                
                field("Commands") {
                    value = bot.commandManager.commandCount.toString()
                }
                
                timestamp = Instant.now()
            }
            
            message.reply(embed)
        }
    }
    
    @JDAGuildCommand
    @CommandMethod("serverinfo|server")
    fun serverInfo(event: GuildMessageEvent) {
        bot.scope.launch {
            val embed = Embed {
                title = "$polydevEmoji Polyhedral Development Discord Server"
                color = 0x8fd032
                description = serverDescription
                thumbnail = githubImage
                
                field {
                    name = "Who We Are"
                    value = whoWeAreDescription
                    inline = false
                }
                
                field {
                    name = "Rules"
                    value = rulesDescription
                    inline = false
                }
                
                field {
                    name = "Useful Links"
                    value = usefulLinks
                    inline = false
                }
                
                field {
                    name = "Getting Support"
                    value = supportDescription
                    inline = false
                }
                
                footer {
                    name = "Requested by ${event.member.effectiveName} (${event.author.name}#${event.author.discriminator})"
                    iconUrl = event.author.avatarUrl
                }
            }
            
            event.event.message.replyEmbeds(embed)
                    .mentionRepliedUser(false)
                    .queue()
        }
    }
    
    companion object {
        const val polydevEmoji = "<:polydev:853123841038352384>"
        const val githubImage = "https://github.com/PolyhedralDev.png"
        const val serverDescription = "Welcome to the **Polyhedral Development** Discord server!"
        const val whoWeAreDescription = "Polyhedral Development is an open source initiative aimed at creating awesome projects " +
                "to benefit everyone. We focus entirely on developing things for the community, rather than building software to profit.\n" +
                "Currently our main focus is on the Terra World Generator, and minor projects surrounding it."
        
        @Language("Markdown")
        val usefulLinks = """
            [Discord Invite Link](https://discord.gg/PXUEbbF)
            [Polyhedral Development GitHub Organization](https://github.com/PolyhedralDev/)
            [Terra Generator Main Repository](https://github.com/PolyhedralDev/Terra)
            [dfsek's Patreon](https://www.patreon.com/dfsek)
        """.trimIndent()
        
        @Language("Markdown")
        val supportDescription = """
            **Asking for help**
            When asking for help, do not expect someone to always answer you instantly. You must wait for people to be available to answer.
            Do not ping admins, developers, or other users without valid reason. We will see your message without a ping.
            **Before asking**
            Before asking for help, make sure you have the following:
            - Game logs
            - Steps to cause the issue
            - A list of any mods/plugins you have installed
            **What channel to use**
            - For Fabric use <#814334748222160907>
            - For Bukkit/Spigot/Paper use <#765260067812540416>
            - For Forge use <#838226038751232010>
        """.trimIndent()
        
        @Language("Markdown")
        val rulesDescription = """
            You will find a list of rules in <#715684015989981304> (shocking, I know)
        """.trimIndent()
    }
}
