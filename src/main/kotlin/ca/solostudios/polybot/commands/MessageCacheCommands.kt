/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file MessageCacheCommands.kt is part of PolyhedralBot
 * Last modified on 24-10-2021 09:31 p.m.
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
import ca.solostudios.polybot.cloud.commands.PolyCommandContainer
import ca.solostudios.polybot.cloud.commands.PolyCommands
import ca.solostudios.polybot.cloud.commands.annotations.JDAUserPermission
import ca.solostudios.polybot.cloud.commands.annotations.PolyCategory
import ca.solostudios.polybot.entities.PolyMessage
import ca.solostudios.polybot.util.idFooter
import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.Hidden
import dev.minn.jda.ktx.Embed
import dev.minn.jda.ktx.await
import kotlinx.coroutines.launch
import org.slf4j.kotlin.*

@Hidden
@PolyCommandContainer
@PolyCategory(BOT_ADMIN_CATEGORY)
class MessageCacheCommands(bot: PolyBot) : PolyCommands(bot) {
    private val logger by getLogger()
    
    @JDAUserPermission(ownerOnly = true)
    @CommandMethod("cache|msg-cache <id>")
    @CommandDescription("Returns a message from the message cache/")
    fun messageFromCache(message: PolyMessage,
                         @Argument(value = "id", description = "The ID of the message to get from the cache.")
                         id: Long) {
        bot.scope.launch {
            val cachedMessage = bot.cacheManager.messageCache.getMessage(id)
            
            if (cachedMessage != null) {
                logger.info(cachedMessage) { "here is the message {}" }
                
                val embed = Embed {
                    author {
                        name = "${cachedMessage.username}#${cachedMessage.discriminator}"
                        url = cachedMessage.url
                        iconUrl = bot.jda.retrieveUserById(cachedMessage.author)
                                .map { it.effectiveAvatarUrl }
                                .onErrorMap { null }
                                .await() ?: ca.solostudios.polybot.Constants.defaultAvatarUrl
                    }
                    color = 0x2ECC70
                    
                    description = "**<@${cachedMessage.author}> sent a message in <#${cachedMessage.channel}>.**\n${cachedMessage.content}"
                    
                    idFooter(message.timeCreated, message.guild, message.channel, message.member, message)
                }
                
                message.reply(embed)
            }
        }
    }
}
