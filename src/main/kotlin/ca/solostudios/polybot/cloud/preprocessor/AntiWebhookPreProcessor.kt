/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file AntiWebhookPreProcessor.kt is part of PolyhedralBot
 * Last modified on 09-10-2021 11:14 p.m.
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

package ca.solostudios.polybot.cloud.preprocessor

import cloud.commandframework.execution.preprocessor.CommandPreprocessingContext
import cloud.commandframework.execution.preprocessor.CommandPreprocessor
import cloud.commandframework.jda.JDA4CommandManager
import cloud.commandframework.services.types.ConsumerService
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class AntiWebhookPreProcessor<C>(private val mgr: JDA4CommandManager<C>) : CommandPreprocessor<C> {
    override fun accept(context: CommandPreprocessingContext<C>) {
        val event: MessageReceivedEvent = try {
            mgr.backwardsCommandSenderMapper.apply(context.commandContext.sender!!)
        } catch (e: IllegalStateException) {
            return
        }
        
        if (event.isWebhookMessage)
            ConsumerService.interrupt()
    }
}