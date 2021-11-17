/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file CloudInjectorService.kt is part of PolyhedralBot
 * Last modified on 17-11-2021 03:09 p.m.
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

package ca.solostudios.polybot.cloud

import ca.solostudios.polybot.PolyBot
import ca.solostudios.polybot.entities.PolyGuild
import ca.solostudios.polybot.entities.PolyMember
import ca.solostudios.polybot.entities.PolyMessage
import ca.solostudios.polybot.entities.PolyMessageChannel
import ca.solostudios.polybot.entities.PolyUser
import ca.solostudios.polybot.util.component1
import ca.solostudios.polybot.util.component2
import ca.solostudios.polybot.util.poly
import cloud.commandframework.annotations.AnnotationAccessor
import cloud.commandframework.annotations.injection.InjectionService
import cloud.commandframework.context.CommandContext
import cloud.commandframework.types.tuples.Triplet
import net.dv8tion.jda.api.entities.Message
import org.kodein.di.DI
import org.kodein.di.instance

class CloudInjectorService<C>(di: DI) : InjectionService<C> {
    
    private val bot: PolyBot by di.instance()
    
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