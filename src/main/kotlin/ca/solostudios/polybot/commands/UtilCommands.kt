/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file UtilCommands.kt is part of PolyhedralBot
 * Last modified on 31-12-2021 11:58 p.m.
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

package ca.solostudios.polybot.commands

import ca.solostudios.polybot.PolyBot
import ca.solostudios.polybot.Version
import ca.solostudios.polybot.cloud.commands.PolyCommandContainer
import ca.solostudios.polybot.cloud.commands.PolyCommands
import ca.solostudios.polybot.cloud.commands.annotations.Author
import ca.solostudios.polybot.cloud.commands.annotations.CommandLongDescription
import ca.solostudios.polybot.cloud.commands.annotations.CommandName
import ca.solostudios.polybot.cloud.commands.annotations.JDAGuildCommand
import ca.solostudios.polybot.cloud.commands.annotations.PolyCategory
import ca.solostudios.polybot.cloud.commands.annotations.SourceMessage
import ca.solostudios.polybot.entities.PolyMember
import ca.solostudios.polybot.entities.PolyMessage
import ca.solostudios.polybot.util.commandCount
import ca.solostudios.polybot.util.freeMemory
import ca.solostudios.polybot.util.maxMemory
import ca.solostudios.polybot.util.runtime
import ca.solostudios.polybot.util.runtimeMXBean
import ca.solostudios.polybot.util.shortFormat
import ca.solostudios.polybot.util.totalMemory
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import dev.minn.jda.ktx.Embed
import dev.minn.jda.ktx.await
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.JDAInfo
import org.intellij.lang.annotations.Language
import kotlin.time.Duration.Companion.milliseconds

@PolyCategory(UTIL_CATEGORY)
@PolyCommandContainer
class UtilCommands(bot: PolyBot) : PolyCommands(bot) {
    
    private val polydevEmoji by bot.polyEmoteReference(853123841038352384L)
    
    private val rulesChannel by bot.polyTextChannelReference(715684015989981304L)
    
    private val fabricSupportChannel by bot.polyTextChannelReference(814334748222160907L)
    
    private val bukkitSupportChannel by bot.polyTextChannelReference(765260067812540416L)
    
    private val forgeSupportChannel by bot.polyTextChannelReference(838226038751232010L)
    
    @Language("Markdown")
    private val supportDescription = """
            **Asking for help**
            When asking for help, do not expect someone to always answer you instantly. You must wait for people to be available to answer.
            Do not ping admins, developers, or other users without valid reason. We will see your message without a ping.
            **Before asking**
            Before asking for help, make sure you have the following:
            - Game logs
            - Steps to cause the issue
            - A list of any mods/plugins you have installed
        """.trimIndent() + buildString {
        if (fabricSupportChannel != null || bukkitSupportChannel != null || forgeSupportChannel != null)
            appendLine("**What channel to use**")
        
        if (fabricSupportChannel != null)
            appendLine("- For Fabric use $fabricSupportChannel")
        
        if (bukkitSupportChannel != null)
            appendLine("- For Bukkit/Spigot/Paper use $bukkitSupportChannel")
        
        if (forgeSupportChannel != null)
            appendLine("- For Forge use $forgeSupportChannel")
    }
    
    @Language("Markdown")
    private val rulesDescription = """
            You will find a list of rules in $rulesChannel (shocking, I know)
        """.trimIndent()
    
    @Language("Markdown")
    private val githubImage = "https://github.com/PolyhedralDev.png"
    
    @Language("Markdown")
    private val serverDescription = "Welcome to the **Polyhedral Development** Discord server!"
    
    @Language("Markdown")
    private val whoWeAreDescription = """
                    Polyhedral Development is an open source initiative aimed at creating awesome projects to benefit everyone.
                    We focus entirely on developing things for the community, rather than building software to profit.
                    Our current primary focus is on the Terra World Generator, and any minor projects surrounding it.
                    We are working to create the *best* open source generator to benefit *everyone*!
                """.trimIndent()
    
    @Language("Markdown")
    private val usefulLinks = """
            [Discord Invite Link](https://discord.gg/PXUEbbF)
            [Polyhedral Development GitHub Organization](https://github.com/PolyhedralDev/)
            [Terra Generator Main Repository](https://github.com/PolyhedralDev/Terra)
            [dfsek's Patreon](https://www.patreon.com/dfsek)
        """.trimIndent()
    
    @CommandName("Ping")
    @CommandMethod("ping|pong")
    @CommandDescription("Checks the ping of the bot.")
    suspend fun ping(@SourceMessage message: PolyMessage) {
        bot.scope.launch {
            message.textChannel.sendTyping()
            val restPing = bot.jda.restPing.await()
            
            val msg = message.reply("Checking ping...")
            
            val ping = message.timeCreated.until(msg.timeCreated, ChronoUnit.MILLIS)
            msg.edit("Ping: ${ping / 3}ms | Heartbeat: ${bot.jda.gatewayPing}ms | Rest: ${restPing}ms")
        }
    }
    
    @CommandName("Info")
    @CommandMethod("info|polybot|bot|botinfo")
    @CommandDescription("Returns information about the bot.")
    @CommandLongDescription("Returns any information regarding the bot, as well as the source code for the bot.")
    suspend fun info(@SourceMessage message: PolyMessage) {
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
                field("Uptime", runtimeMXBean.uptime.milliseconds.shortFormat())
                if (bot.runConfig.crashes != 0)
                    field("Recent Crashes") {
                        value = "${bot.runConfig.crashes} crashes within 30 seconds at the time of launch"
                    }
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
                
                if (bot.runConfig.crashes != 0)
                    field() // for alignment
                
                field("Commands") {
                    value = bot.commandManager.commandCount.toString()
                }
                
                timestamp = Instant.now()
            }
            
            message.reply(embed)
        }
    }
    
    @JDAGuildCommand
    @CommandName("Server Info")
    @CommandMethod("serverinfo|server|polydev|polyhedral|p|")
    @CommandDescription("Returns information about the Polyhedral Development discord server.")
    @CommandLongDescription("Returns information about the Polyhedral Development discord server, any projects we're working on, as well as where to get support. ")
    suspend fun serverInfo(@SourceMessage message: PolyMessage, @Author member: PolyMember) {
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
                    name = "Requested by ${member.effectiveName} (${member.name}#${member.discriminator})"
                    iconUrl = member.avatarUrl
                }
            }
            
            message.reply(embed)
        }
    }
}
