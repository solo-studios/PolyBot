/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file AutoQuoteListener.kt is part of PolyhedralBot
 * Last modified on 13-09-2021 08:56 p.m.
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

package com.solostudios.polybot.listener

import club.minnced.discord.webhook.WebhookClientBuilder
import club.minnced.discord.webhook.external.JDAWebhookClient
import club.minnced.discord.webhook.send.AllowedMentions
import com.solostudios.polybot.Constants
import com.solostudios.polybot.PolyBot
import com.solostudios.polybot.util.WebhookMessage
import dev.minn.jda.ktx.await
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.kotlin.*
import kotlin.time.Duration.Companion.hours

class AutoQuoteListener(val bot: PolyBot) : ListenerAdapter() {
    private val logger by org.slf4j.kotlin.getLogger()
    
    /**
     * Webhook cache.
     *
     * The key is the webhook id + webhook token
     *
     * Returns a [JDAWebhookClient], or null.
     */
    private val webhookCache: Cache<Pair<Long, String>, JDAWebhookClient> = Cache.Builder()
            .expireAfterAccess(hours(4))
            .maximumCacheSize(40)
            .build()
    
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        when {
            event.isWebhookMessage -> return
            event.author.isBot     -> return
        }
        
        bot.scope.launch {
            val jda = event.jda
            val textChannel = event.channel
            val messageContentRaw = event.message.contentRaw
            val matcher = Constants.messageLinkRegex.find(messageContentRaw)
            
            try {
                if (matcher != null) {
                    logger.info { "Found match with msg '$messageContentRaw'" }
                    logger.info { "Groups: ${matcher.groupValues}" }
                    val guildId = matcher.groups["guild"]!!.value.toLong()
                    val channelId = matcher.groups["channel"]!!.value.toLong()
                    val messageId = matcher.groups["message"]!!.value.toLong()
                    
                    val quotedMessage = jda.getGuildById(guildId)
                            ?.getTextChannelById(channelId)
                            ?.retrieveMessageById(messageId)
                            ?.await() ?: return@launch
                    
                    val webhook = textChannel.retrieveWebhooks().await().find { jda.selfUser.idLong == it.ownerAsUser?.idLong }
                        ?: textChannel.createWebhook("PolyBot Message Quoter").await()
                    
                    val webhookClient = webhookCache.get(webhook.idLong to webhook.token!!) {
                        WebhookClientBuilder.fromJDA(webhook)
                                .setHttpClient(jda.httpClient)
                                .setExecutorService(bot.scheduledThreadPool)
                                .setAllowedMentions(AllowedMentions.none())
                                .buildJDA()
                    }
                    
                    val webhookMessage = WebhookMessage {
                        username = quotedMessage.author.asTag
                        avatarUrl = quotedMessage.author.effectiveAvatarUrl
                        content = quotedMessage.contentRaw
                    }
                    
                    webhookClient.send(webhookMessage)
                }
            } catch (ignored: NumberFormatException) {
                logger.info(ignored) { "number format" }
            } catch (ignored: RuntimeException) {
                logger.info(ignored) { "runtime exception" }
            }
        }
    }
}