/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file CloudInjectionService.kt is part of PolyhedralBot
 * Last modified on 31-12-2021 01:29 p.m.
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
import ca.solostudios.polybot.cloud.commands.annotations.Author
import ca.solostudios.polybot.cloud.commands.annotations.CurrentChannel
import ca.solostudios.polybot.cloud.commands.annotations.CurrentGuild
import ca.solostudios.polybot.cloud.commands.annotations.SourceMessage
import ca.solostudios.polybot.entities.PolyGuild
import ca.solostudios.polybot.entities.PolyMember
import ca.solostudios.polybot.entities.PolyMessage
import ca.solostudios.polybot.entities.PolyMessageChannel
import ca.solostudios.polybot.entities.PolyTextChannel
import ca.solostudios.polybot.entities.PolyUser
import ca.solostudios.polybot.util.component1
import ca.solostudios.polybot.util.component2
import ca.solostudios.polybot.util.component3
import ca.solostudios.polybot.util.jda.poly
import cloud.commandframework.annotations.AnnotationAccessor
import cloud.commandframework.annotations.injection.InjectionService
import cloud.commandframework.context.CommandContext
import cloud.commandframework.types.tuples.Triplet
import net.dv8tion.jda.api.entities.Message

class CloudInjectionService<C>(val bot: PolyBot) : InjectionService<C> {
    override fun handle(triplet: Triplet<CommandContext<C>, Class<*>, AnnotationAccessor>): Any? {
        val (context, clazz, annotationAccessor) = triplet
        
        for (annotation in annotationAccessor.annotations()) {
            when (annotation.annotationClass) {
                Author::class         -> {
                    return when (clazz.kotlin) {
                        PolyMember::class -> context.get<Message>("Message").member!!.poly(bot)
                        PolyUser::class   -> context.get<Message>("Message").author.poly(bot)
                        else              -> error("Argument annotated with @Author but isn't PolyMember or PolyUser")
                    }
                }
                
                CurrentChannel::class -> {
                    return when (clazz.kotlin) {
                        PolyTextChannel::class    -> context.get<Message>("Message").textChannel.poly(bot)
                        PolyMessageChannel::class -> context.get<Message>("Message").channel.poly(bot)
                        else                      -> error("Argument annotated with @CurrentChannel but isn't PolyTextChannel or PolyMemberChannel")
                    }
                }
                
                CurrentGuild::class   -> {
                    require(clazz.kotlin == PolyGuild::class) {
                        "Argument annotated with @CurrentGuild but isn't PolyGuild"
                    }
                    return context.get<Message>("Message").guild.poly(bot)
                }
                
                SourceMessage::class  -> {
                    require(clazz.kotlin == PolyMessage::class) {
                        "Argument is annotated with @SourceMessage but isn't PolyMessage"
                    }
                    return context.get<Message>("Message").poly(bot)
                }
            }
        }
        
        return null
    }
}