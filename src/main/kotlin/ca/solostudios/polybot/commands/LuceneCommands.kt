/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file LuceneCommands.kt is part of PolyhedralBot
 * Last modified on 17-11-2021 02:51 p.m.
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

import ca.solostudios.polybot.cloud.commands.PolyCommandContainer
import ca.solostudios.polybot.cloud.commands.PolyCommands
import ca.solostudios.polybot.cloud.commands.annotations.CommandLongDescription
import ca.solostudios.polybot.cloud.commands.annotations.CommandName
import ca.solostudios.polybot.cloud.commands.annotations.JDAUserPermission
import ca.solostudios.polybot.cloud.commands.annotations.PolyCategory
import ca.solostudios.polybot.entities.PolyMessage
import ca.solostudios.polybot.entities.PolyUser
import ca.solostudios.polybot.search.SearchManager
import ca.solostudios.polybot.util.MarkdownHeaderVisitor
import ca.solostudios.polybot.util.PaginationMenu
import ca.solostudios.polybot.util.get
import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.Flag
import cloud.commandframework.annotations.Hidden
import cloud.commandframework.annotations.specifier.Greedy
import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import java.awt.Color
import net.dv8tion.jda.api.JDA
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser
import org.kodein.di.DI
import org.kodein.di.direct
import org.kodein.di.instance
import org.slf4j.kotlin.*

@PolyCommandContainer
@PolyCategory(UTIL_CATEGORY)
class LuceneCommands(di: DI) : PolyCommands(di) {
    private val logger by getLogger()
    
    private val searchManager: SearchManager by instance()
    private val eventWaiter = EventWaiter(direct.instance(), false).apply { direct.instance<JDA>().addEventListener(this) }
    
    @Hidden
    @CommandName("Lucene Markdown")
    @JDAUserPermission(ownerOnly = true)
    @CommandMethod("lucene markdown <markdown>")
    @CommandDescription("Internal command for Apache lucene bullshit.")
    suspend fun lucene(message: PolyMessage,
                       @Greedy
                       @Argument("markdown")
                       markdown: String) {
        val flavour = CommonMarkFlavourDescriptor()
        val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(markdown)
        
        val visitor = MarkdownHeaderVisitor()
        
        visitor.visitNode(parsedTree)
        
        visitor.headers.forEach {
            logger.info { "Header: ${markdown[it.first, it.last].trim()}" }
            message.reply("Header: ${markdown[it.first, it.last].trim()}")
        }
        
        // message.reply(writer.writeValueAsString(parsedTree)).mentionRepliedUser(false).queue()
    }
    
    @CommandName("Search Docs")
    // @CommandMethod("lucene search <query>")
    @CommandMethod("search|s <query>")
    @CommandDescription("Searches the Terra documentation.")
    @CommandLongDescription("Searches through the Terra documentation and returns a list of links (or a single link) of possible results to your query.")
    suspend fun search(message: PolyMessage,
                       user: PolyUser,
                       @Greedy
                       @Argument(value = "query", description = "The string to search for.")
                       query: String,
                       @Flag(value = "quick", aliases = ["q"], description = "Return only a single result.")
                       quick: Boolean = false) {
        try {
            val results = searchManager.defaultIndex.search(query, maxResults = 50)
            
            if (quick) {
                message.reply(results.first().simple)
            }
            
            val menu = PaginationMenu.Builder().apply {
                title = "Search"
                text = { _, _ -> """Search results for "$query"""" }
                color = { _, _ -> Color.GREEN }
                eventWaiter = this@LuceneCommands.eventWaiter
                users = listOf(user.jdaUser)
                finalAction = { msg -> msg.clearReactions().queue() }
                
                itemsPerPage = 6
                showPageNumbers = true
                numberItems = true
                waitOnSinglePage = false
                bulkSkipNumber = 5
                wrapPageEnds = false
                allowTextInput = false
                
                items = results.map { it.title to it.body }
            }.build()
            
            menu.display(message.channel.jdaChannel)
        } catch (e: Exception) {
            logger.warn(e) { "Error while searching" }
        }
    }
    
    @Hidden
    @CommandName("Lucene Update")
    @CommandMethod("lucene update")
    @JDAUserPermission(ownerOnly = true)
    suspend fun update(message: PolyMessage) {
        try {
            searchManager.defaultIndex.updateIndex()
            
            message.reply("Updated index")
        } catch (e: Exception) {
            logger.warn(e) { "Error while updating" }
        }
    }
}