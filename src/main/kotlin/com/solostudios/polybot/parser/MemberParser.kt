/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file MemberParser.kt is part of PolyhedralBot
 * Last modified on 02-07-2021 01:30 a.m.
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

package com.solostudios.polybot.parser

import cloud.commandframework.arguments.parser.ArgumentParseResult
import cloud.commandframework.arguments.parser.ArgumentParser
import cloud.commandframework.context.CommandContext
import cloud.commandframework.exceptions.parsing.NoInputProvidedException
import cloud.commandframework.jda.parsers.UserArgument.TooManyUsersFoundParseException
import cloud.commandframework.jda.parsers.UserArgument.UserNotFoundParseException
import java.util.Queue
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class MemberParser<C> : ArgumentParser<C, Member> {
    override fun parse(commandContext: CommandContext<C>, inputQueue: Queue<String>): ArgumentParseResult<Member> {
        val input = inputQueue.peek() ?: return ArgumentParseResult.failure(NoInputProvidedException(this::class.java, commandContext))
        
        if (!commandContext.contains("MessageReceivedEvent"))
            return ArgumentParseResult.failure(IllegalStateException("MessageReceivedEvent was not in the command context."))
        
        val event = commandContext.get<MessageReceivedEvent>("MessageReceivedEvent")
        
        val stringId = if (input.startsWith("<@") && input.endsWith(">")) {
            if (input.startsWith("<@!"))
                input.substring(3, input.length - 1)
            else
                input.substring(2, input.length - 1)
        } else {
            input
        }
        
        try {
            val id = stringId.toULong().toLong()
            
            val member = event.guild.getMemberById(id)
            if (member != null) {
                inputQueue.remove()
                return ArgumentParseResult.success(member)
            }
        } catch (e: NumberFormatException) {
        }
        
        val members = event.guild.getMembersByEffectiveName(input, true)
        
        return when {
            members.size == 1 -> inputQueue.remove().run { ArgumentParseResult.success(members[0]) }
            members.isEmpty() -> ArgumentParseResult.failure(UserNotFoundParseException(input))
            else              -> ArgumentParseResult.failure(TooManyUsersFoundParseException(input))
        }
    }
    
    override fun isContextFree(): Boolean {
        return true
    }
}