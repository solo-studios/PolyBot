/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file HelpCommandHandler.kt is part of PolyhedralBot
 * Last modified on 30-12-2021 03:31 p.m.
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

package ca.solostudios.polybot.cloud.commands

import ca.solostudios.polybot.cloud.event.MessageEvent
import ca.solostudios.polybot.commands.Category
import ca.solostudios.polybot.entities.PolyMember
import ca.solostudios.polybot.util.or
import ca.solostudios.polybot.util.orNull
import cloud.commandframework.Command
import cloud.commandframework.CommandManager
import cloud.commandframework.arguments.StaticArgument
import cloud.commandframework.meta.CommandMeta
import net.dv8tion.jda.api.Permission
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class HelpCommandHandler(override val di: DI) : DIAware {
    private val commandManager: CommandManager<MessageEvent> by instance()
    private val commandFilters = mutableListOf<(PolyMember, CommandEntry) -> Boolean>()
    
    fun addCommandFilter(filter: (PolyMember, CommandEntry) -> Boolean) {
        commandFilters += filter
    }
    
    private fun getAllCommands(member: PolyMember): List<CommandEntry> {
        val syntaxHints = mutableListOf<CommandEntry>()
        
        for (command in commandManager.commands) {
            val commandEntry = getEntry(command)
            if (member.isOwner || member.isCoOwner) {
                syntaxHints += commandEntry
                continue
            }
            
            if (commandFilters.any { it.invoke(member, commandEntry) }) {
                continue
            }
            
            syntaxHints += commandEntry
        }
        
        return syntaxHints
    }
    
    fun queryHelp(member: PolyMember, query: String?): HelpTopic {
        
        val commands = getAllCommands(member)
        
        
        if (query == null || query.isBlank())
            return IndexHelpTopic(commands.groupBy { it.category })
        
        val categories = commands.mapNotNull { it.category }.distinct()
        
        
        val category = categories.find {
            it.name.equals(query, ignoreCase = true) || it.aliases.any { alias -> alias.equals(query, ignoreCase = true) }
        }
        if (category != null)
            return CategoryHelpTopic(query, commands.filter { it.category == category })
        
        val fragments = query.split(" ")
        
        val exactCommand = commands.find {
            if (it.literals.size != fragments.size)
                return@find false
            
            fragments.forEachIndexed { i, fragment ->
                val literal = it.literals[i]
                if (!literal.name.equals(fragment, ignoreCase = true) && !literal.aliases.any { alias ->
                        alias.equals(fragment, ignoreCase = true)
                    })
                    return@find false
            }
            
            return@find true
        }
        
        return if (exactCommand != null)
            SingleCommandHelpTopic(exactCommand)
        else
            IndexHelpTopic(commands.groupBy { it.category }, true)
    }
    
    private fun getEntry(command: Command<MessageEvent>): CommandEntry {
        val name = command.commandMeta.get(PolyMeta.COMMAND_NAME).orNull()
        val category = command.commandMeta.get(PolyMeta.CATEGORY).orNull()
        val description = command.commandMeta.get(CommandMeta.DESCRIPTION).orNull()
        val longDescription = command.commandMeta.get(CommandMeta.LONG_DESCRIPTION).orNull()
        val hidden = command.commandMeta.get(CommandMeta.HIDDEN).or(false)
        val ownerOnly = command.commandMeta.get(PolyMeta.OWNER_ONLY).or(false)
        val coOwnerOnly = command.commandMeta.get(PolyMeta.CO_OWNER_ONLY).or(false)
        val userPermissions = command.commandMeta.get(PolyMeta.USER_PERMISSIONS).or(listOf())
        val botPermissions = command.commandMeta.get(PolyMeta.BOT_PERMISSIONS).or(listOf())
        val guildOnly = command.commandMeta.get(PolyMeta.GUILD_ONLY).or(false)
    
        val literals = command.arguments.filter {
            it is StaticArgument<*>
        }.map {
            it as StaticArgument<*>
        }.map {
            CommandLiteral(it.name, it.alternativeAliases.distinct().filter { alias -> alias != it.name })
        }
    
        return CommandEntry(name,
                            command,
                            literals,
                            commandManager.commandSyntaxFormatter.apply(command.arguments, null),
                            category,
                            description,
                            longDescription,
                            hidden,
                            ownerOnly,
                            coOwnerOnly,
                            userPermissions,
                            botPermissions,
                            guildOnly)
    }
}


sealed interface HelpTopic

data class IndexHelpTopic(val commands: Map<Category?, List<CommandEntry>>, val noMatch: Boolean = false) : HelpTopic

data class SingleCommandHelpTopic(val command: CommandEntry) : HelpTopic

data class CategoryHelpTopic(val categoryName: String, val commands: List<CommandEntry>) : HelpTopic

data class CommandEntry(
        val name: String?,
        val command: Command<MessageEvent>,
        val literals: List<CommandLiteral>,
        val syntax: String,
        val category: Category?,
        val description: String?,
        val longDescription: String?,
        val hidden: Boolean,
        val ownerOnly: Boolean,
        val coOwnerOnly: Boolean,
        val userPermissions: List<Permission>,
        val botPermissions: List<Permission>,
        val guildOnly: Boolean
                       )

data class CommandLiteral(
        val name: String,
        val aliases: List<String>
                         )