/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file RoleParser.kt is part of PolyhedralBot
 * Last modified on 23-12-2021 03:37 p.m.
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
import ca.solostudios.polybot.entities.PolyRole
import ca.solostudios.polybot.util.poly
import cloud.commandframework.arguments.parser.ArgumentParseResult
import cloud.commandframework.arguments.parser.ArgumentParser
import cloud.commandframework.context.CommandContext
import cloud.commandframework.exceptions.parsing.NoInputProvidedException
import java.util.Queue
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import net.dv8tion.jda.api.requests.ErrorResponse

class RoleParser<C : Any>(val bot: PolyBot) : ArgumentParser<C, PolyRole> {
    @Suppress("DuplicatedCode")
    override fun parse(commandContext: CommandContext<C>, inputQueue: Queue<String>): ArgumentParseResult<PolyRole> {
        val input = inputQueue.peek() ?: return ArgumentParseResult.failure(NoInputProvidedException(this::class.java, commandContext))
        if (!commandContext.contains("MessageReceivedEvent"))
            return ArgumentParseResult.failure(IllegalStateException("MessageReceivedEvent was not in the command context."))
        val event = commandContext.get<MessageReceivedEvent>("MessageReceivedEvent")
        
        if (!event.isFromGuild) {
            return ArgumentParseResult.failure(IllegalArgumentException("Channel arguments can only be parsed in guilds"))
        }
        
        val stringId = if (input.startsWith("<@&") && input.endsWith(">")) {
            if (input.startsWith("<@&!"))
                input.substring(3, input.length - 1)
            else
                input.substring(2, input.length - 1)
        } else {
            input
        }
    
        try {
            val id = stringId.toULong().toLong()
        
            val role = event.jda.getRoleById(id)
            if (role != null) {
                inputQueue.remove()
                return ArgumentParseResult.success(role.poly(bot))
            }
        } catch (_: NumberFormatException) {
        } catch (e: ErrorResponseException) {
            when (e.errorResponse) {
                ErrorResponse.UNKNOWN_ROLE -> return ArgumentParseResult.failure(RoleNotFoundParseException(input))
            
                else                       -> {
                }
            }
        }
        
        val roles = event.guild.getRolesByName(input, true)
        
        return when {
            roles.size == 1 -> inputQueue.remove().run { ArgumentParseResult.success(roles[0].poly(bot)) }
            roles.isEmpty() -> ArgumentParseResult.failure(RoleNotFoundParseException(input))
            else            -> ArgumentParseResult.failure(TooManyRolesFoundParseException(input))
        }
    }
    
    override fun isContextFree(): Boolean {
        return true
    }
    
    open class RoleParseException(val input: String) : IllegalArgumentException() {
        companion object {
            private const val serialVersionUID: Long = 639388168580009352L
        }
    }
    
    class TooManyRolesFoundParseException(input: String) : RoleParseException(input) {
        override val message: String
            get() = "Too many roles found for '$input'."
        
        companion object {
            private const val serialVersionUID: Long = -2540591927167319772L
        }
    }
    
    class RoleNotFoundParseException(input: String) : RoleParseException(input) {
        override val message: String
            get() = "Role not found for '$input'."
    
        companion object {
            private const val serialVersionUID: Long = -1843528356813695942L
        }
    }
}
