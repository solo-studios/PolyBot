/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file AntiEmbedListener.kt is part of PolyhedralBot
 * Last modified on 12-01-2022 06:00 p.m.
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

import ca.solostudios.polybot.Constants
import ca.solostudios.polybot.config.PolyEmbedSuppression
import dev.minn.jda.ktx.await
import java.net.URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import org.slf4j.kotlin.*

class AntiEmbedListener(override val di: DI) : ListenerAdapter(),
                                               DIAware {
    private val logger by getLogger()
    
    private val suppressionConfigs: List<PolyEmbedSuppression> by instance()
    private val scope: CoroutineScope by instance()
    
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        scope.launch {
            val message = event.message
            val messageContentRaw = message.contentRaw
            
            if (message.embeds.isNotEmpty()) { // Only run if the message has embeds
                val suppress = Constants.urlRegex.findAll(messageContentRaw)
                        .any {
                            runCatching {
                                val url = URL(it.value)
                                
                                
                                suppressionConfigs.any { suppressionConfig ->
                                    val matches: Boolean = suppressionConfig.matches(url)
                                    logger.debug { "Evaluating config $suppressionConfig on url $url. Matches: $matches" }
                                    matches
                                }
                            }.getOrElse { false }
                        }
                
                if (suppress) {
                    logger.debug { "Should suppress the embed for message ${message.idLong}." }
                    message.suppressEmbeds(true)
                            .await()
                }
            }
        }
    }
}