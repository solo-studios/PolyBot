/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file CloudCommandPreprocessor.kt is part of PolyhedralBot
 * Last modified on 22-12-2021 11:41 p.m.
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

package ca.solostudios.polybot.cloud.manager

import ca.solostudios.polybot.cloud.event.MessageEvent
import cloud.commandframework.execution.preprocessor.CommandPreprocessingContext
import cloud.commandframework.execution.preprocessor.CommandPreprocessor
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class CloudCommandPreprocessor(override val di: DI) : CommandPreprocessor<MessageEvent>, DIAware {
    override fun accept(context: CommandPreprocessingContext<MessageEvent>) {
        
        val event: MessageReceivedEvent = context.commandContext.sender.event
        
        context.commandContext.store<JDA>("JDA", event.jda)
        context.commandContext.store("MessageReceivedEvent", event)
        context.commandContext.store("MessageChannel", event.channel)
        context.commandContext.store("Message", event.message)
        
        if (event.isFromGuild) {
            val guild = event.guild
            context.commandContext.store("Guild", guild)
            if (event.isFromType(ChannelType.TEXT)) {
                context.commandContext.store("TextChannel", event.textChannel)
            }
        } else if (event.isFromType(ChannelType.PRIVATE)) {
            context.commandContext.store("PrivateChannel", event.privateChannel)
        }
    }
}