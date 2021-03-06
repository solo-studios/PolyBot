/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file TagParser.kt is part of PolyhedralBot
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

package ca.solostudios.polybot.cloud.parser

import ca.solostudios.polybot.PolyBot
import ca.solostudios.polybot.entities.data.PolyTagData
import ca.solostudios.polybot.util.jda.poly
import cloud.commandframework.arguments.parser.ArgumentParseResult
import cloud.commandframework.arguments.parser.ArgumentParser
import cloud.commandframework.context.CommandContext
import cloud.commandframework.exceptions.parsing.NoInputProvidedException
import java.util.Queue
import kotlinx.uuid.toUUID
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class TagParser<C : Any>(override val di: DI) : ArgumentParser<C, PolyTagData>,
                                                DIAware {
    private val bot: PolyBot by instance()
    
    override fun parse(commandContext: CommandContext<C>, inputQueue: Queue<String>): ArgumentParseResult<PolyTagData> {
        val input = inputQueue.peek() ?: return ArgumentParseResult.failure(NoInputProvidedException(this::class.java, commandContext))
        
        if (!commandContext.contains("MessageReceivedEvent"))
            return ArgumentParseResult.failure(IllegalStateException("MessageReceivedEvent was not in the command context."))
        
        val event = commandContext.get<MessageReceivedEvent>("MessageReceivedEvent")
        
        if (!event.isFromGuild) {
            return ArgumentParseResult.failure(IllegalArgumentException("Channel arguments can only be parsed in guilds"))
        }
        
        val message = event.message.poly(bot)
        val guild = message.guild
        
        val tags = guild.tags
        
        try {
            val uuid = input.toUUID()
            
            val tag = tags.find { it.uuid == uuid }
            
            if (tag != null) {
                inputQueue.remove()
                return ArgumentParseResult.success(tag)
            }
        } catch (ignored: Exception) {
        }
        
        val filteredTags = tags.filter { input == it.name || input in it.aliases }
        
        return when {
            filteredTags.size == 1 -> inputQueue.remove().run { ArgumentParseResult.success(filteredTags.first()) }
            filteredTags.isEmpty() -> ArgumentParseResult.failure(TagNotFoundParseException(input))
            
            else                   -> {
                val reFilteredTags = filteredTags.filter { input == it.name } // if found too many tags, prioritize by name
    
                if (reFilteredTags.size == 1)
                    ArgumentParseResult.success(reFilteredTags.first())
                else
                    ArgumentParseResult.failure(TooManyTagsFoundParseException(input))
    
            }
        }
    }
    
    override fun isContextFree(): Boolean = true
    
    open class TagParseException(val input: String) : IllegalArgumentException(input) {
        companion object {
            private const val serialVersionUID: Long = 6609600520848966746L
        }
    }
    
    class TooManyTagsFoundParseException(input: String) : TagParseException(input) {
        override val message: String
            get() = "Too many tags found for '$input'."
        
        companion object {
            private const val serialVersionUID: Long = 2871108544911135195L
        }
    }
    
    class TagNotFoundParseException(input: String) : TagParseException(input) {
        override val message: String
            get() = "Tag not found for '$input'."
    
        companion object {
            private const val serialVersionUID: Long = -1160192389683812730L
        }
    }
}