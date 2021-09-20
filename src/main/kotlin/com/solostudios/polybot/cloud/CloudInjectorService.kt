/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file CloudInjectorService.kt is part of PolyhedralBot
 * Last modified on 19-09-2021 08:48 p.m.
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

package com.solostudios.polybot.cloud

import cloud.commandframework.annotations.AnnotationAccessor
import cloud.commandframework.annotations.injection.InjectionService
import cloud.commandframework.context.CommandContext
import cloud.commandframework.types.tuples.Triplet
import com.solostudios.polybot.PolyBot
import com.solostudios.polybot.entities.PolyGuild
import com.solostudios.polybot.entities.PolyMember
import com.solostudios.polybot.entities.PolyMessage
import com.solostudios.polybot.entities.PolyMessageChannel
import com.solostudios.polybot.entities.PolyUser
import com.solostudios.polybot.util.component1
import com.solostudios.polybot.util.component2
import com.solostudios.polybot.util.poly
import net.dv8tion.jda.api.entities.Message

class CloudInjectorService<C>(val bot: PolyBot) : InjectionService<C> {
    override fun handle(triplet: Triplet<CommandContext<C>, Class<*>, AnnotationAccessor>): Any? {
        val (context, clazz) = triplet
        
        return when (clazz.kotlin) {
            PolyBot::class            -> bot
            PolyMessage::class        -> context.get<Message>("Message").poly(bot)
            PolyUser::class           -> context.get<Message>("Message").author.poly(bot)
            PolyMember::class         -> context.get<Message>("Message").member?.poly(bot)
            PolyMessageChannel::class -> context.get<Message>("Message").channel.poly(bot)
            PolyGuild::class          -> context.get<Message>("Message").guild.poly(bot)
            else                      -> null
        }
    }
}