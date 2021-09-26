/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file UserParser.kt is part of PolyhedralBot
 * Last modified on 25-09-2021 07:07 p.m.
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

package com.solostudios.polybot.cloud.parser

import cloud.commandframework.arguments.parser.ArgumentParseResult
import cloud.commandframework.arguments.parser.ArgumentParser
import cloud.commandframework.context.CommandContext
import cloud.commandframework.exceptions.parsing.NoInputProvidedException
import com.solostudios.polybot.PolyBot
import com.solostudios.polybot.entities.PolyUser
import com.solostudios.polybot.util.poly
import java.util.Queue
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import net.dv8tion.jda.api.requests.ErrorResponse
import org.slf4j.kotlin.*

class UserParser<C : Any>(val bot: PolyBot) : ArgumentParser<C, PolyUser> {
    private val logger by getLogger()
    
    @Suppress("DuplicatedCode")
    override fun parse(commandContext: CommandContext<C>, inputQueue: Queue<String>): ArgumentParseResult<PolyUser> {
        val input = inputQueue.peek() ?: return ArgumentParseResult.failure(NoInputProvidedException(this::class.java, commandContext))
        if (!commandContext.contains("MessageReceivedEvent"))
            return ArgumentParseResult.failure(IllegalStateException("MessageReceivedEvent was not in the command context."))
        val event = commandContext.get<MessageReceivedEvent>("MessageReceivedEvent")
        val message = event.message
        
        if ("^" == input.trim()) {
            val messageReference = message.messageReference
            if (messageReference != null) {
                try {
                    val parentMessage = messageReference.resolve()
                            .complete()
                    
                    ArgumentParseResult.success(parentMessage.member!!)
                } catch (e: Exception) {
                    return ArgumentParseResult.failure(MemberParser.MemberParseException("Could not find the linked message."))
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
            
            val user = event.jda.retrieveUserById(id).complete()
            if (user != null) {
                inputQueue.remove()
                return ArgumentParseResult.success(user.poly(bot))
            }
        } catch (e: NumberFormatException) {
        } catch (e: ErrorResponseException) {
            when (e.errorResponse) {
                ErrorResponse.UNKNOWN_MEMBER -> return ArgumentParseResult.failure(UserNotFoundParseException(input))
                ErrorResponse.UNKNOWN_USER   -> return ArgumentParseResult.failure(UserNotFoundParseException(input))
                
                else                         -> {
                }
            }
        }
        
        val users = event.guild.getMembersByEffectiveName(input, true).map { it.user }
        
        return when {
            users.size == 1 -> inputQueue.remove().run { ArgumentParseResult.success(users[0].poly(bot)) }
            users.isEmpty() -> ArgumentParseResult.failure(UserNotFoundParseException(input))
            else            -> ArgumentParseResult.failure(TooManyUsersFoundParseException(input))
        }
    }
    
    override fun isContextFree(): Boolean {
        return true
    }
    
    open class UserParseException(val input: String) : IllegalArgumentException()
    
    class TooManyUsersFoundParseException(input: String) : UserParseException(input) {
        override val message: String
            get() = "Too many users found for '$input'."
    }
    
    class UserNotFoundParseException(input: String) : UserParseException(input) {
        override val message: String
            get() = "User not found for '$input'."
    }
}
