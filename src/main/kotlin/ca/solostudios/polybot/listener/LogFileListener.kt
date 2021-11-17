/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file LogFileListener.kt is part of PolyhedralBot
 * Last modified on 17-11-2021 05:07 p.m.
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
import ca.solostudios.polybot.entities.PolyMessage
import ca.solostudios.polybot.util.poly
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.await
import com.github.kittinunf.fuel.coroutines.awaitString
import com.github.kittinunf.fuel.serialization.kotlinxDeserializerOf
import java.net.URI
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.kotlin.*

class LogFileListener(val bot: PolyBot) : ListenerAdapter() {
    
    private val logger by getLogger()
    
    private val fuel = FuelManager().also {
        it.basePath = hastebinServer
    }
    
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        val message = event.message.poly(bot)
        
        logger.info { "Message received" }
        
        bot.scope.launch {
            when {
                message.hasAttachments     -> { // process attachments
                    logger.info { "Message has attachments" }
                    val validAttachments = message.attachments
                            .filter { it.fileExtension == "log" || it.fileExtension == "txt" }
                            .filterNot { it.size >= 1_000_000 }
                    
                    logger.info { "Valid Attachments: " + validAttachments.joinToString { "Attachment(${it.url}, ${it.fileName}, ${it.fileExtension})" } }
                    logger.info { "All Attachments" + message.attachments.joinToString { "Attachment(${it.url}, ${it.fileName}, ${it.fileExtension})" } }
                    
                    for (attachment in validAttachments) {
                        
                        val logContents = fuel.get(attachment.url).awaitString()
                        
                        
                        uploadLog(message, logContents)
                    }
                }
                
                isLogFile(message.content) -> {
                    uploadLog(message, message.content)
                }
                
                else                       -> { // Not log file, ignore
                    
                }
            }
        }
    }
    
    private suspend fun uploadLog(message: PolyMessage, logContents: String) {
        try {
            val filteredLogContents = filterLogContents(logContents)
            
            val response: HastebinResponse = fuel.post("/documents")
                    .body(filteredLogContents)
                    .await(kotlinxDeserializerOf())
            
            val hastebinUrl = URI("$hastebinServer/${response.key}").normalize()
            
            message.reply("""
            Re-uploaded log file to $hastebinUrl
        """.trimIndent())
        } catch (e: Exception) { // Silently ignore
        }
    }
    
    private fun isLogFile(logContents: String): Boolean = minecraftLogRegex.findAll(logContents).count() > 8 // magic number
    
    private fun filterLogContents(logContents: String): String {
        return logContents
                .replace(ipRegex, "[censored ip address]")
                .replace(homeDirRegex, "[censored user home directory]")
    }
    
    @Serializable
    data class HastebinResponse(
            val key: String,
                               )
    
    companion object {
        const val hastebinServer = "https://hst.sh/"
        
        val ipRegex = Regex(
                """(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)""",
                setOf(RegexOption.IGNORE_CASE),
                           )
        
        val homeDirRegex = Regex(
                """(/Users/)([\w\s]+)|(/home/)(\w+)|(C:\\Users\\)([\w\s]+)""",
                setOf(RegexOption.MULTILINE, RegexOption.MULTILINE),
                                )
        
        val minecraftLogRegex = Regex(
                """^\[\d{2}:\d{2}:\d{2}\]\s+\[(?:(?:Client|Server|Render)\s+thread|main|Sound\s+Library\s+Loader|Sound\s+Engine|Worker-Main-\d+|Thread-\d+|Chunk\s+Batcher\s+\d+)\/(?:INFO|DEBUG|TRACE|WARN|ERROR|FATAL)\]""",
                setOf(RegexOption.MULTILINE)
                                     )
    }
}