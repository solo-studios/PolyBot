/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file HelpCommands.kt is part of PolyhedralBot
 * Last modified on 03-10-2021 06:58 p.m.
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

package com.solostudios.polybot.commands

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.specifier.Greedy
import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import com.solostudios.polybot.PolyBot
import com.solostudios.polybot.cloud.commands.CategoryHelpTopic
import com.solostudios.polybot.cloud.commands.HelpCommandHandler
import com.solostudios.polybot.cloud.commands.IndexHelpTopic
import com.solostudios.polybot.cloud.commands.PolyCommandContainer
import com.solostudios.polybot.cloud.commands.PolyCommands
import com.solostudios.polybot.cloud.commands.SingleCommandHelpTopic
import com.solostudios.polybot.cloud.commands.annotations.PolyCategory
import com.solostudios.polybot.entities.PolyMember
import com.solostudios.polybot.entities.PolyMessage
import com.solostudios.polybot.entities.PolyUser
import com.solostudios.polybot.util.PaginationMenu
import dev.minn.jda.ktx.Embed
import java.awt.Color
import org.slf4j.kotlin.*

@PolyCategory(UTIL_CATEGORY)
@PolyCommandContainer
class HelpCommands(bot: PolyBot) : PolyCommands(bot) {
    private val logger by getLogger()
    
    private val commandManager = bot.commandManager
    private val prefix = bot.config.botConfig.prefix
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
    
    @CommandMethod("help [query] [page]")
    @CommandDescription("Help command")
    suspend fun help(message: PolyMessage,
                     member: PolyMember,
                     @Greedy
                     @Argument("query")
                     query: String?,
                     @Argument("page")
                     page: Int?) {
        val embed = Embed {
            title = "Help"
            description = "Searching for results..."
        }
        
        val helpMessage = message.reply(embed)
        
        when (val helpTopic = helpHandler.queryHelp(member, query)) {
            is IndexHelpTopic         -> {
                printIndexHelpTopic(helpMessage, message, member.user, page, query, helpTopic)
            }
            
            is CategoryHelpTopic      -> {
                printCategoryHelpTopic(helpMessage, message, member.user, page, query, helpTopic)
            }
            
            is SingleCommandHelpTopic -> {
                printSingleCommandHelpTopic(helpMessage, message, member.user, page, query, helpTopic)
            }
        }
    }
    
    private suspend fun printIndexHelpTopic(helpMessage: PolyMessage,
                                            message: PolyMessage,
                                            user: PolyUser,
                                            page: Int?,
                                            query: String?,
                                            helpTopic: IndexHelpTopic) {
        
        val commands = helpTopic.commands
        
        val pagination = helpPaginationBuilder(user, query).apply {
            items = commands.map { cmd ->
                (cmd.key?.name ?: noCategory) to cmd.value.joinToString(separator = "`, `",
                                                                        prefix = "`",
                                                                        postfix = "`") { entry ->
                    entry.literals.joinToString(separator = " ") { literal -> literal.name }
                }
            }
        }.build()
        
        if (page != null)
            pagination.paginate(helpMessage.jdaMessage, page)
        else
            pagination.display(helpMessage.jdaMessage)
        
    }
    
    private suspend fun printCategoryHelpTopic(helpMessage: PolyMessage,
                                               message: PolyMessage,
                                               user: PolyUser,
                                               page: Int?,
                                               query: String?,
                                               helpTopic: CategoryHelpTopic) {
        val commands = helpTopic.commands
        
        val pagination = helpPaginationBuilder(user, query).apply {
            items = commands.map { cmd ->
                cmd.literals.joinToString(separator = " ", prefix = "**", postfix = "**") { literal -> literal.name } to buildString {
                    
                    if (cmd.literals.last().aliases.isNotEmpty())
                        append("**").append(aliases).append("**: ").appendLine(cmd.literals.last().aliases.joinToString())
                    
                    append("**").append(syntax).append("**: ")
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
        
        if (page != null)
            pagination.paginate(helpMessage.jdaMessage, page)
        else {
            pagination.display(helpMessage.jdaMessage)
        }
    }
    
    private suspend fun printSingleCommandHelpTopic(helpMessage: PolyMessage,
                                                    message: PolyMessage,
                                                    user: PolyUser,
                                                    page: Int?,
                                                    query: String?,
                                                    helpTopic: SingleCommandHelpTopic) {
        // TODO: 2021-10-03 Finish this
    }
    
    private suspend fun helpPaginationBuilder(user: PolyUser, query: String?): PaginationMenu.Builder = PaginationMenu.Builder().apply {
        title = helpTitle
        eventWaiter = this@HelpCommands.eventWaiter
        users = listOf(user.jdaUser)
        color = { _, _ -> Color.GREEN }
        text = { _, _ -> query?.let { noResultsDescription.replace("{search}", query) } ?: allCommands }
        
        finalAction = { msg -> msg.clearReactions().queue() }
        
        itemsPerPage = maxResultsPerPage
        showPageNumbers = true
        numberItems = false
        waitOnSinglePage = false
        bulkSkipNumber = 5
        wrapPageEnds = false
        allowTextInput = false
    }
    
    companion object {
        const val maxResultsPerPage = 6
        const val header = ""
        const val footer = ""
        const val helpTitle = "Help"
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
        const val syntax = "Syntax"
        const val aliases = "Aliases"
        
        const val allCommands = "List of all commands."
        
        const val noCommands = "No Commands Found"
        const val noCommandsDescription = "Could not find any commands either because you don't have access to any, " +
                "or there are no commands for this bot."
        
        const val noResults = "No Results Found"
        const val noResultsDescription = "Could not find any results for the search {search}."
        
        const val nextPage = "➡: Next page."
        const val previousPage = "⬅: Previous page."
        const val gotoPage = "Use {prefix}help [page number] [query] to select a specific page."
        
        const val invalidPage = "Invalid Page."
        const val pageNotInRange = "Page {page} does not exist. Must be within range [1, {max_pages}]."
    }
}