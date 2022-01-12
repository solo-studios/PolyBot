/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file EventMapper.kt is part of PolyhedralBot
 * Last modified on 22-12-2021 11:40 p.m.
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

package ca.solostudios.polybot.cloud.event

import ca.solostudios.polybot.PolyBot
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class EventMapper(override val di: DI) : DIAware {
    private val bot: PolyBot by instance()
    
    fun jdaEventToPlatformEvent(event: MessageReceivedEvent): MessageEvent {
        return when {
            event.isFromGuild && !event.isWebhookMessage -> GuildMessageEvent(bot, event)
            !event.isFromGuild                           -> PrivateMessageEvent(bot, event)
            else                                         -> MessageEvent(bot, event)
        }
    }
    
    @Suppress("unused")
    fun platformEventToJDAEvent(event: MessageEvent): MessageReceivedEvent = event.event
}
