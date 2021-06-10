/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file JDAMessageCommandPreprocessor.kt is part of PolyhedralBot
 * Last modified on 10-06-2021 04:07 p.m.
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

package com.solostudios.polybot

import cloud.commandframework.execution.preprocessor.CommandPreprocessingContext
import cloud.commandframework.execution.preprocessor.CommandPreprocessor
import cloud.commandframework.jda.JDA4CommandManager
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.checkerframework.checker.units.qual.C
import org.slf4j.kotlin.getLogger
import org.slf4j.kotlin.info

/**
 * The JDA Command Preprocessor for storing JDA-specific contexts in the command contexts
 *
 * @param mgr The JDACommandManager
 */
class JDAMessageCommandPreprocessor<C>(private val mgr: JDA4CommandManager<C>) : CommandPreprocessor<C> {
    private val logger by getLogger()
    
    /**
     * Stores the [net.dv8tion.jda.api.JDA] in the context with the key "JDA",
     * the [net.dv8tion.jda.api.events.message.MessageReceivedEvent] with the key "MessageReceivedEvent", and
     * the [net.dv8tion.jda.api.entities.MessageChannel] with the key "MessageChannel".
     *
     * If the message was sent in a guild, the [net.dv8tion.jda.api.entities.Guild] will be stored in the context with the
     * key "Guild". If the message was also sent in a text channel, the [net.dv8tion.jda.api.entities.TextChannel] will be
     * stored in the context with the key "TextChannel".
     *
     * If the message was sent in a DM instead of in a guild, the [net.dv8tion.jda.api.entities.PrivateChannel] will be
     * stored in the context with the key "PrivateChannel".
     */
    override fun accept(context: CommandPreprocessingContext<C>) {
        val event: MessageReceivedEvent = try {
            mgr.backwardsCommandSenderMapper.apply(context.commandContext.sender)
        } catch (e: IllegalStateException) {
            // The event could not be resolved from the backwards command sender mapper
            logger.warn("Could not register thing")
            return
        }
        context.commandContext.store("Message", event.message)
        logger.info { "registered thing." }
        logger.info(context.commandContext.asMap()) { "Here is the context map: {}" }
    }
} //private val logger by getLogger()