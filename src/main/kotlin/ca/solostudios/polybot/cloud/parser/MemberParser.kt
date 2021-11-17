/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file MemberParser.kt is part of PolyhedralBot
 * Last modified on 17-11-2021 03:14 p.m.
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

package ca.solostudios.polybot.cloud.parser

import ca.solostudios.polybot.PolyBot
import ca.solostudios.polybot.entities.PolyMember
import ca.solostudios.polybot.util.poly
import cloud.commandframework.arguments.parser.ArgumentParseResult
import cloud.commandframework.arguments.parser.ArgumentParser
import cloud.commandframework.context.CommandContext
import cloud.commandframework.exceptions.parsing.NoInputProvidedException
import java.util.Queue
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import net.dv8tion.jda.api.requests.ErrorResponse
import org.kodein.di.DI
import org.kodein.di.instance
import org.slf4j.kotlin.*

class MemberParser<C : Any>(di: DI) : ArgumentParser<C, PolyMember> {
    private val logger by getLogger()
    
    private val bot: PolyBot by di.instance()
    
    @Suppress("DuplicatedCode")
    override fun parse(commandContext: CommandContext<C>, inputQueue: Queue<String>): ArgumentParseResult<PolyMember> {
        val input = inputQueue.peek() ?: return ArgumentParseResult.failure(NoInputProvidedException(this::class.java, commandContext))
        if (!commandContext.contains("MessageReceivedEvent"))
            return ArgumentParseResult.failure(IllegalStateException("MessageReceivedEvent was not in the command context."))
        val event = commandContext.get<MessageReceivedEvent>("MessageReceivedEvent")
        val message = event.message
        
        if (!event.isFromGuild) {
            return ArgumentParseResult.failure(MemberParseException("This command may only be run in a guild."))
        }
        
        
        if ("^" == input.trim()) {
            val messageReference = message.messageReference
            if (messageReference != null) {
                try {
                    val parentMessage = messageReference.resolve()
                            .complete()
    
                    ArgumentParseResult.success(parentMessage.member!!)
                } catch (e: Exception) {
                    return ArgumentParseResult.failure(MemberParseException("Could not find the linked message."))
                }
            }
        }
        
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
            
            logger.info { "here's the id: $id" }
            
            val member = event.guild.retrieveMemberById(id).complete()
            logger.info { "member is ${member == null}" }
            if (member != null) {
                inputQueue.remove()
                return ArgumentParseResult.success(member.poly(bot))
            }
        } catch (e: NumberFormatException) {
        } catch (e: ErrorResponseException) {
            when (e.errorResponse) {
                ErrorResponse.UNKNOWN_MEMBER -> return ArgumentParseResult.failure(MemberNotFoundParseException(input))
                ErrorResponse.UNKNOWN_USER   -> return ArgumentParseResult.failure(MemberNotFoundParseException(input))
                
                else                         -> {
                }
            }
        } // $ban @solo#7313 yess
        
        val members = event.guild.getMembersByEffectiveName(input, true)
        
        return when {
            members.size == 1 -> inputQueue.remove().run { ArgumentParseResult.success(members[0].poly(bot)) }
            members.isEmpty() -> ArgumentParseResult.failure(MemberNotFoundParseException(input))
            else              -> ArgumentParseResult.failure(TooManyMembersFoundParseException(input))
        }
    }
    
    override fun isContextFree(): Boolean {
        return true
    }
    
    open class MemberParseException(val input: String) : IllegalArgumentException()
    
    class TooManyMembersFoundParseException(input: String) : MemberParseException(input) {
        override val message: String
            get() = "Too many members found for '$input'."
    }
    
    class MemberNotFoundParseException(input: String) : MemberParseException(input) {
        override val message: String
            get() = "Member not found for '$input'."
    }
}
