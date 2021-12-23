/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file HelpCommands.kt is part of PolyhedralBot
 * Last modified on 23-12-2021 03:28 p.m.
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

package ca.solostudios.polybot.commands

import ca.solostudios.polybot.Constants
import ca.solostudios.polybot.PolyBot
import ca.solostudios.polybot.cloud.commands.CategoryHelpTopic
import ca.solostudios.polybot.cloud.commands.HelpCommandHandler
import ca.solostudios.polybot.cloud.commands.IndexHelpTopic
import ca.solostudios.polybot.cloud.commands.PolyCommandContainer
import ca.solostudios.polybot.cloud.commands.PolyCommands
import ca.solostudios.polybot.cloud.commands.SingleCommandHelpTopic
import ca.solostudios.polybot.cloud.commands.annotations.Author
import ca.solostudios.polybot.cloud.commands.annotations.CommandLongDescription
import ca.solostudios.polybot.cloud.commands.annotations.CommandName
import ca.solostudios.polybot.cloud.commands.annotations.PolyCategory
import ca.solostudios.polybot.cloud.commands.annotations.SourceMessage
import ca.solostudios.polybot.entities.PolyMember
import ca.solostudios.polybot.entities.PolyMessage
import ca.solostudios.polybot.entities.PolyUser
import ca.solostudios.polybot.util.PaginationMenu
import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.specifier.Greedy
import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import dev.minn.jda.ktx.Embed

@PolyCommandContainer
@PolyCategory(UTIL_CATEGORY)
class HelpCommands(bot: PolyBot) : PolyCommands(bot) {
    private val helpHandler = HelpCommandHandler(bot).apply {
        addCommandFilter { _, command -> command.hidden }
        addCommandFilter { member, command -> if (command.ownerOnly) !member.isOwner else false }
        addCommandFilter { member, command -> if (command.coOwnerOnly) !member.isCoOwner else false }
        addCommandFilter { member, command ->
            if (command.userPermissions.isNotEmpty())
                !command.userPermissions.all { member.guildPermissions.contains(it) }
            else
                false
        }
    }
    private val eventWaiter = EventWaiter(bot.scheduledThreadPool, false).apply { bot.jda.addEventListener(this) }
    
    @CommandName("Help")
    @CommandMethod("help [query]")
    @CommandDescription("Display help information about PolyBot commands.")
    @CommandLongDescription("Displays all help information about PolyBot commands.\nThis can be used to search for commands, check what permissions are required for commands, or to figure out what arguments are required for commands.")
    suspend fun help(
            @SourceMessage
            message: PolyMessage,
            @Author
            member: PolyMember,
            @Greedy
            @Argument(value = "query", description = "Name of the command to search for.")
            query: String?,
                    ) {
        val embed = Embed {
            title = "Help"
            description = "Searching for results..."
        }
        
        val helpMessage = message.reply(embed)
        
        when (val helpTopic = helpHandler.queryHelp(member, query)) {
            is IndexHelpTopic         -> {
                printIndexHelpTopic(helpMessage, member.user, query, helpTopic)
            }
            
            is CategoryHelpTopic      -> {
                printCategoryHelpTopic(helpMessage, member.user, query, helpTopic)
            }
            
            is SingleCommandHelpTopic -> {
                printSingleCommandHelpTopic(helpMessage, helpTopic)
            }
        }
    }
    
    private fun printIndexHelpTopic(helpMessage: PolyMessage,
                                    user: PolyUser,
                                    query: String?,
                                    helpTopic: IndexHelpTopic) {
        val commands = helpTopic.commands
        
        val pagination = helpPaginationBuilder(user, query) {
            items = commands.map { cmd ->
                (cmd.key?.name ?: noCategory) to cmd.value.joinToString(separator = "`, `",
                                                                        prefix = "`",
                                                                        postfix = "`") { entry ->
                    entry.literals.joinToString(separator = " ") { literal -> literal.name }
                }
            }
        }.build()
        
        pagination.display(helpMessage.jdaMessage)
    }
    
    private fun printCategoryHelpTopic(helpMessage: PolyMessage,
                                       user: PolyUser,
                                       query: String?,
                                       helpTopic: CategoryHelpTopic) {
        val commands = helpTopic.commands
        
        val pagination = helpPaginationBuilder(user, query) {
            items = commands.map { cmd ->
                cmd.literals.joinToString(separator = " ", prefix = "**", postfix = "**") { literal -> literal.name } to buildString {
                    
                    if (cmd.literals.last().aliases.isNotEmpty())
                        append("**").append(aliases).append("**: ").appendLine(cmd.literals.last().aliases.joinToString())
                    
                    append("**").append(usage).append("**: ")
                    appendLine(cmd.syntax)
                    
                    append("**").append(description).append("**: ")
                    appendLine(cmd.description ?: noDescription)
                    
                    
                    if (cmd.hidden)
                        append("**").append(hidden).appendLine("**: true")
                    if (cmd.ownerOnly)
                        append("**").append(ownerOnly).appendLine("**: true")
                    if (cmd.coOwnerOnly)
                        append("**").append(coOwnerOnly).appendLine("**: true")
                    
                    append("**").append(requiredPermissions).append("**: ")
                    appendLine(cmd.userPermissions.takeIf { it.isNotEmpty() }
                                       ?.joinToString(separator = "`, `", prefix = "`", postfix = "`") { it.getName() }
                                   ?: none)
                    
                    if (cmd.guildOnly)
                        append("**").append(guildOnly).appendLine("**: true")
                    
                    appendLine()
                }
            }
        }.build()
        
        
        pagination.display(helpMessage.jdaMessage)
    }
    
    private suspend fun printSingleCommandHelpTopic(helpMessage: PolyMessage,
                                                    helpTopic: SingleCommandHelpTopic) {
        val cmd = helpTopic.command
    
    
        val embed = Embed {
            title = cmd.name?.let { "$it $command" } ?: helpTitle
        
            description = cmd.longDescription ?: cmd.description
        
            color = Constants.polyhedralDevColourCode
            field {
                name = command
                value = cmd.literals.joinToString(separator = " ", prefix = "`", postfix = "`") {
                    if (it.aliases.isEmpty()) it.name else it.name + it.aliases.joinToString(separator = "|", prefix = "|")
                }
                inline = false
            }
        
            field {
                name = usage
                value = if (cmd.description != null)
                    "`${cmd.syntax}` - ${cmd.description}"
                else
                    cmd.syntax
                inline = false
            }
        
            if (cmd.literals.last().aliases.isNotEmpty())
                field {
                    name = aliases
                    value = cmd.literals.last().aliases.joinToString(separator = "`, `", prefix = "`", postfix = "`")
                    inline = false
                }
        
            if (cmd.userPermissions.isNotEmpty())
                field {
                    name = requiredPermissions
                    value = cmd.userPermissions.joinToString(separator = "`, `", prefix = "`", postfix = "`") { it.getName() }
                    inline = false
                }
        
        }
    
    
        helpMessage.edit(embed)
    }
    
    private fun helpPaginationBuilder(
            user: PolyUser,
            query: String?,
            builder: PaginationMenu.Builder.() -> Unit = {},
                                     ): PaginationMenu.Builder {
        return PaginationMenu.Builder().also {
            it.title = helpTitle
            it.eventWaiter = this@HelpCommands.eventWaiter
            it.users = listOf(user.jdaUser)
            it.color = { _, _ -> Constants.polyhedralDevColour }
            it.text = { _, _ -> query?.let { resultsForDescription.replace("{search}", query) } ?: allCommands }
            
            it.finalAction = { msg -> msg.clearReactions().queue() }
            
            it.itemsPerPage = maxResultsPerPage
            it.showPageNumbers = true
            it.numberItems = false
            it.waitOnSinglePage = false
            it.bulkSkipNumber = 5
            it.wrapPageEnds = false
            it.allowTextInput = false
            it.builder()
        }
    }
    
    @Suppress("unused")
    companion object {
        const val maxResultsPerPage = 6
        const val header = ""
        const val footer = ""
        const val helpTitle = "Help Results"
        const val command = "Command"
        const val description = "Description"
        const val noDescription = "No description provided."
        const val noCategory = "No Category"
        const val arguments = "Arguments"
        const val optional = "Optional"
        const val requiredPermissions = "Required Permissions"
        const val ownerOnly = "Owner Only"
        const val coOwnerOnly = "Co Owner Only"
        const val guildOnly = "GuildOnly"
        const val hidden = "Hidden"
        const val none = "None"
        const val usage = "Usage"
        const val aliases = "Aliases"
        
        const val allCommands = "List of all commands."
        
        const val noCommands = "No Commands Found"
        const val noCommandsDescription = "Could not find any commands either because you don't have access to any, " +
                "or there are no commands for this bot."
        
        const val noResults = "No Results Found"
        const val noResultsDescription = "Could not find any results for the search {search}."
        const val resultsForDescription = "Results for the search {search}."
        
        const val nextPage = "➡: Next page."
        const val previousPage = "⬅: Previous page."
        const val gotoPage = "Use {prefix}help [page number] [query] to select a specific page."
        
        const val invalidPage = "Invalid Page."
        const val pageNotInRange = "Page {page} does not exist. Must be within range [1, {max_pages}]."
    }
}