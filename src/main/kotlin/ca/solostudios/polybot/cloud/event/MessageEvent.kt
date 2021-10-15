/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file MessageEvent.kt is part of PolyhedralBot
 * Last modified on 09-10-2021 10:30 p.m.
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
import ca.solostudios.polybot.entities.PolyMessage
import ca.solostudios.polybot.entities.PolyMessageChannel
import ca.solostudios.polybot.entities.PolyUser
import ca.solostudios.polybot.util.poly
import cloud.commandframework.jda.JDACommandSender
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

open class MessageEvent(
        open val bot: PolyBot,
        open val sender: JDACommandSender,
                       ) {
    
    val event: MessageReceivedEvent
        get() = sender.event.get()
    
    open val author: PolyUser
        get() = event.author.poly(bot)
    
    val message: PolyMessage
        get() = event.message.poly(bot)
    
    open val channel: PolyMessageChannel
        get() = event.channel.poly(bot)
}