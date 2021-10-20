/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PaginationMenu.kt is part of PolyhedralBot
 * Last modified on 20-10-2021 12:24 p.m.
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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package ca.solostudios.polybot.util

import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import com.jagrosh.jdautilities.menu.Menu
import dev.minn.jda.ktx.Embed
import java.awt.Color
import java.util.concurrent.TimeUnit
import java.util.function.BiFunction
import java.util.function.Consumer
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.GenericMessageEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.exceptions.PermissionException
import net.dv8tion.jda.api.requests.RestAction
import kotlin.math.ceil


/**
 * ## Credit
 * This is a hacked version of [Paginator][com.jagrosh.jdautilities.menu.Paginator] to support fields,
 * as well as have a kotlinized builder.
 *
 * ## Changes
 *
 * - Transformed from Java to Kotlin using IntelliJ's automatic Java to Kotlin converter.
 * - Cleaned up after conversion (core logic was preserved)
 * - Moved a few complex lambdas to their own methods
 * - Removed column logic + added support for field titles.
 *
 * ## Original Javadoc
 *
 * A [Menu][com.jagrosh.jdautilities.menu.Menu] implementation that paginates a
 * set of one or more text items across one or more pages.
 *
 *
 * When displayed, a Paginator will add three reactions in the following order:
 *
 *  * **Left Arrow** - Causes the Paginator to traverse one page backwards.
 *  * **Stop** - Stops the Paginator.
 *  * **Right Arrow** - Causes the Paginator to traverse one page forwards.
 *
 *
 * Additionally, if specified in the [PaginationMenu.Builder], two "bulk skip" reactions
 * will be added to allow a certain number of pages to be skipped left or right.
 *
 * [PaginationMenu.Builder]s can also set a Paginator to accept various forms of text-input,
 * such as left and right text commands, and even user specified page number via text.
 *
 * @author John Grosh, with minor modifications by solonovamax
 */
class PaginationMenu internal constructor(
        waiter: EventWaiter?,
        users: Set<User?>?,
        roles: Set<Role?>?,
        timeout: Long,
        unit: TimeUnit?,
        val title: String?,
        val color: BiFunction<Int, Int, Color?>,
        val text: BiFunction<Int, Int, String?>?,
        val finalAction: Consumer<Message>,
        val itemsPerPage: Int,
        val showPageNumbers: Boolean,
        val numberItems: Boolean,
        val items: List<Pair<String, String>>,
        val waitOnSinglePage: Boolean,
        val bulkSkipNumber: Int,
        val wrapPageEnds: Boolean,
        val leftText: String?,
        val rightText: String?,
        val allowTextInput: Boolean,
                                         ) : Menu(waiter, users, roles, timeout, unit) {
    val pages: Int = ceil(items.size.toDouble() / itemsPerPage).toInt()
    
    
    /**
     * Begins pagination on page 1 as a new [Message][net.dv8tion.jda.api.entities.Message]
     * in the provided [MessageChannel][net.dv8tion.jda.api.entities.MessageChannel].
     *
     *
     * Starting on another page is available via [ ][PaginationMenu.paginate].
     *
     * @param  channel
     * The MessageChannel to send the new Message to
     */
    override fun display(channel: MessageChannel) {
        paginate(channel, 1)
    }
    
    /**
     * Begins pagination on page 1 displaying this Pagination by editing the provided
     * [Message][net.dv8tion.jda.api.entities.Message].
     *
     *
     * Starting on another page is available via
     * [Paginator#paginate(Message, int)][PaginationMenu.paginate].
     *
     * @param  message
     * The Message to display the Menu in
     */
    override fun display(message: Message) {
        paginate(message, 1)
    }
    
    /**
     * Begins pagination as a new [Message][net.dv8tion.jda.api.entities.Message]
     * in the provided [MessageChannel][net.dv8tion.jda.api.entities.MessageChannel], starting
     * on whatever page number is provided.
     *
     * @param  channel
     * The MessageChannel to send the new Message to
     * @param  pageNum
     * The page number to begin on
     */
    fun paginate(channel: MessageChannel, pageNum: Int) {
        var actualPageNum = pageNum
        if (actualPageNum < 1)
            actualPageNum = 1
        else if (actualPageNum > pages)
            actualPageNum = pages
        
        val msg = renderPage(actualPageNum)
        initialize(channel.sendMessageEmbeds(msg), actualPageNum)
    }
    
    /**
     * Begins pagination displaying this Pagination by editing the provided
     * [Message][net.dv8tion.jda.api.entities.Message], starting on whatever
     * page number is provided.
     *
     * @param  message
     * The MessageChannel to send the new Message to
     * @param  pageNum
     * The page number to begin on
     */
    fun paginate(message: Message, pageNum: Int) {
        var actualPageNum = pageNum
        
        if (actualPageNum < 1)
            actualPageNum = 1
        else if (actualPageNum > pages)
            actualPageNum = pages
        
        val msg = renderPage(actualPageNum)
        initialize(message.editMessageEmbeds(msg), actualPageNum)
    }
    
    private fun initialize(action: RestAction<Message>, pageNum: Int) {
        action.queue { m ->
            if (pages > 1) {
                if (bulkSkipNumber > 1)
                    m.addReaction(BIG_LEFT).queue()
                m.addReaction(LEFT).queue()
                m.addReaction(STOP).queue()
                
                if (bulkSkipNumber > 1)
                    m.addReaction(RIGHT).queue()
                
                
                m.addReaction(if (bulkSkipNumber > 1) BIG_RIGHT else RIGHT)
                        .queue({ pagination(m, pageNum) }, { pagination(m, pageNum) })
            } else if (waitOnSinglePage) {
                // Go straight to without text-input because only one page is available
                m.addReaction(STOP).queue({ paginationWithoutTextInput(m, pageNum) }, { paginationWithoutTextInput(m, pageNum) })
            } else {
                finalAction.accept(m)
            }
        }
    }
    
    private fun pagination(message: Message, pageNum: Int) {
        if (allowTextInput || leftText != null && rightText != null)
            paginationWithTextInput(message, pageNum) else paginationWithoutTextInput(message, pageNum)
    }
    
    fun checkMessage(message: Message, pageNum: Int, event: GenericMessageEvent): Boolean {
        if (event is MessageReactionAddEvent)
            return checkReaction(event, message.idLong)
        else if (event is MessageReceivedEvent) {
            
            // Wrong channel
            if (event.channel != message.channel)
                return false
            
            val rawContent = event.message.contentRaw.trim()
            
            if (leftText != null && rightText != null) {
                if (rawContent.equals(leftText, true) || rawContent.equals(rightText, true))
                    return isValidUser(event.author, event.takeIf { it.isFromGuild }?.guild)
            }
            
            if (allowTextInput) {
                try {
                    val i = rawContent.toInt()
                    // Minimum 1, Maximum the number of pages, never the current page number
                    if (i in 1 .. pages && i != pageNum)
                        return isValidUser(event.author, event.takeIf { it.isFromGuild }?.guild)
                } catch (ignored: NumberFormatException) {
                }
            }
        }
        return false
    }
    
    private fun handleMessageSendEvent(message: Message, pageNum: Int, event: GenericMessageEvent) {
        if (event is MessageReactionAddEvent) {
            handleMessageReactionAddAction(event, message, pageNum)
        } else {
            val mre = event as MessageReceivedEvent
            val rawContent = mre.message.contentRaw.trim()
            
            val targetPage: Int
            
            when {
                leftText != null && rawContent.equals(leftText, true) -> {
                    targetPage =
                            if (1 < pageNum || wrapPageEnds) {
                                if (pageNum - 1 < 1 && wrapPageEnds) {
                                    pages
                                } else {
                                    pageNum - 1
                                }
                            } else if (rightText != null && rawContent.equals(rightText, true)) {
                                if (pageNum < pages || wrapPageEnds) {
                                    if (pageNum + 1 > pages && wrapPageEnds) {
                                        1
                                    } else {
                                        pageNum + 1
                                    }
                                } else {
                                    // This will run without fail because we know the above conditions don't apply but our logic
                                    // when checking the event in the block above this action block has guaranteed this is the only
                                    // option at this point
                                    rawContent.toInt()
                                }
                            } else {
                                // This will run without fail because we know the above conditions don't apply but our logic
                                // when checking the event in the block above this action block has guaranteed this is the only
                                // option at this point
                                rawContent.toInt()
                            }
                }
                
                rightText != null                                     -> {
                    targetPage =
                            if (rawContent.equals(rightText, true)) {
                                if (pageNum < pages || wrapPageEnds) {
                                    if (pageNum + 1 > pages && wrapPageEnds) {
                                        1
                                    } else {
                                        pageNum + 1
                                    }
                                } else {
                                    // This will run without fail because we know the above conditions don't apply but our logic
                                    // when checking the event in the block above this action block has guaranteed this is the only
                                    // option at this point
                                    rawContent.toInt()
                                }
                            } else {
                                // This will run without fail because we know the above conditions don't apply but our logic
                                // when checking the event in the block above this action block has guaranteed this is the only
                                // option at this point
                                rawContent.toInt()
                            }
                }
                
                else                                                  -> {
                    // This will run without fail because we know the above conditions don't apply but our logic
                    // when checking the event in the block above this action block has guaranteed this is the only
                    // option at this point
                    targetPage = rawContent.toInt()
                }
            }
    
            message.editMessageEmbeds(renderPage(targetPage)).queue { m -> pagination(m, targetPage) }
            mre.message.delete().queue() // delete the calling message so it doesn't get spammy
        }
    }
    
    private fun paginationWithTextInput(message: Message, pageNum: Int) {
        waiter.waitForEvent(GenericMessageEvent::class.java,
                            { event -> checkMessage(message, pageNum, event) }, // Check Message
                            { event -> handleMessageSendEvent(message, pageNum, event) }, // Handle Message
                            timeout, unit) {
            finalAction.accept(message)
        }
    }
    
    private fun paginationWithoutTextInput(message: Message, pageNum: Int) {
        waiter.waitForEvent(MessageReactionAddEvent::class.java,
                            { event -> checkReaction(event, message.idLong) },  // Check Reaction
                            { event -> handleMessageReactionAddAction(event, message, pageNum) },  // Handle Reaction
                            timeout, unit) {
            finalAction.accept(message)
        }
    }
    
    // Private method that checks MessageReactionAddEvents
    private fun checkReaction(event: MessageReactionAddEvent, messageId: Long): Boolean {
        return if (event.messageIdLong != messageId) false else when (event.reactionEmote.name) {
            LEFT, STOP, RIGHT   -> isValidUser(event.user, if (event.isFromGuild) event.guild else null)
            BIG_LEFT, BIG_RIGHT -> bulkSkipNumber > 1 && isValidUser(event.user, if (event.isFromGuild) event.guild else null)
            else                -> false
        }
    }
    
    // Private method that handles MessageReactionAddEvents
    private fun handleMessageReactionAddAction(event: MessageReactionAddEvent, message: Message, pageNum: Int) {
        var newPageNum = pageNum
        when (event.reaction.reactionEmote.name) {
            LEFT      -> {
                if (newPageNum == 1 && wrapPageEnds) newPageNum = pages + 1
                if (newPageNum > 1) newPageNum--
            }
            
            RIGHT     -> {
                if (newPageNum == pages && wrapPageEnds) newPageNum = 0
                if (newPageNum < pages) newPageNum++
            }
            
            BIG_LEFT  -> if (newPageNum > 1 || wrapPageEnds) {
                var i = 1
                while ((newPageNum > 1 || wrapPageEnds) && i < bulkSkipNumber) {
                    if (newPageNum == 1 && wrapPageEnds) newPageNum = pages + 1
                    newPageNum--
                    i++
                }
            }
            BIG_RIGHT -> if (newPageNum < pages || wrapPageEnds) {
                var i = 1
                while ((newPageNum < pages || wrapPageEnds) && i < bulkSkipNumber) {
                    if (newPageNum == pages && wrapPageEnds) newPageNum = 0
                    newPageNum++
                    i++
                }
            }
            
            STOP      -> {
                finalAction.accept(message)
                return
            }
        }
        
        try {
            event.reaction.removeReaction(event.user!!).queue()
        } catch (ignored: PermissionException) {
        }
        
        val n = newPageNum
        message.editMessageEmbeds(renderPage(newPageNum)).queue { m -> pagination(m, n) }
    }
    
    private fun renderPage(pageNum: Int): MessageEmbed {
        return Embed {
            title = this@PaginationMenu.title
            val start = (pageNum - 1) * itemsPerPage
            
            val end = if (items.size < pageNum * itemsPerPage) items.size else pageNum * itemsPerPage
            
            for (i in start until end) {
                field {
                    val item = items[i]
                    
                    name = if (numberItems)
                        "${i.inc()}. ${item.first}"
                    else
                        item.first
                    
                    value = item.second
                    
                    inline = false
                }
            }
            
            color = this@PaginationMenu.color.apply(pageNum, pages)?.rgb
            
            if (showPageNumbers)
                footer {
                    name = "Page $pageNum/$pages"
                }
            
            if (text != null)
                description = text.apply(pageNum, pages)
        }
    }
    
    /**
     * The [Menu.Builder][com.jagrosh.jdautilities.menu.Menu.Builder] for
     * a [Paginator][com.jagrosh.jdautilities.menu.Paginator].
     *
     * @author John Grosh
     */
    class Builder : Menu.Builder<Builder, PaginationMenu>() {
        var title: String? = null
        
        /**
         * Sets the [EventWaiter][com.jagrosh.jdautilities.commons.waiter.EventWaiter]
         * that will do [Menu][com.jagrosh.jdautilities.menu.Menu] operations.
         *
         *
         * **NOTE:** All Menus will only work with an EventWaiter set!
         *
         * Not setting an EventWaiter means the Menu will not work.
         */
        var eventWaiter: EventWaiter?
            get() = super.waiter
            set(value) {
                setEventWaiter(value)
            }
        
        /**
         * Sets [User][net.dv8tion.jda.api.entities.User]s that are allowed to use the
         * [Menu][com.jagrosh.jdautilities.menu.Menu] that will be built.
         *
         * This clears any Users already registered before adding the ones specified.
         *
         * The Users allowed to use the Menu
         */
        var users: List<User>
            get() = super.users.toList()
            set(value) {
                setUsers(*value.toTypedArray())
            }
        
        /**
         * Sets [Role][net.dv8tion.jda.api.entities.Role]s that are allowed to use the
         * [Menu][com.jagrosh.jdautilities.menu.Menu] that will be built.
         *
         * This clears any Roles already registered before adding the ones specified.
         *
         * The Roles allowed to use the Menu
         */
        var roles: List<Role>
            get() = super.roles.toList()
            set(value) {
                setRoles(*value.toTypedArray())
            }
        
        /**
         * Sets the timeout that the [Menu][com.jagrosh.jdautilities.menu.Menu] should
         * stay available.
         *
         *
         * After this has expired, the a final action in the form of a
         * [Runnable][java.lang.Runnable] may execute.
         *
         * The amount of time for the Menu to stay available
         */
        var timeout: Long
            get() = super.timeout
            set(value) {
                setTimeout(value, super.unit)
            }
        
        /**
         * Sets the timeout that the [Menu][com.jagrosh.jdautilities.menu.Menu] should
         * stay available.
         *
         *
         * After this has expired, the a final action in the form of a
         * [Runnable][java.lang.Runnable] may execute.
         *
         * The [TimeUnit][java.util.concurrent.TimeUnit] for the timeout
         */
        var unit: TimeUnit
            get() = super.unit
            set(value) {
                setTimeout(super.timeout, value)
            }
        
        /**
         * Sets the [Color][java.awt.Color] of the [MessageEmbed][net.dv8tion.jda.api.entities.MessageEmbed],
         * relative to the total page number and the current page as determined by the provided
         * [BiFunction][java.util.function.BiFunction].
         *
         *
         * As the page changes, the BiFunction will re-process the current page number and the total
         * page number, allowing for the color of the embed to change depending on the page number.
         */
        var color: (Int, Int) -> Color? = { _, _ -> null }
        
        /**
         * Sets the text of the [Message][net.dv8tion.jda.api.entities.Message] to be displayed
         * relative to the total page number and the current page as determined by the provided
         * [BiFunction][java.util.function.BiFunction].
         *
         *
         * As the page changes, the BiFunction will re-process the current page number and the total
         * page number, allowing for the displayed text of the Message to change depending on the page number.
         */
        var text: (Int, Int) -> String? = { _, _ -> null }
        
        /**
         * Sets the action to perform if the menu times out.
         */
        var finalAction: (Message) -> Unit = { m -> m.delete().queue() }
        
        /**
         * Sets the number of items that will appear on each page.
         *
         * Always positive, never-zero number of items per page
         *
         * @throws java.lang.IllegalArgumentException If the provided number is less than 1
         */
        var itemsPerPage: Int = 12
            set(value) {
                require(value >= 1) { "There must be at least one item per page" }
                field = value
            }
        
        /**
         * Sets whether or not the page number will be shown.
         *
         * If `true`, the page number should be shown, if `false` they will not.
         */
        var showPageNumbers: Boolean = true
        
        /**
         * Sets whether or not the items will be automatically numbered.
         *
         * If `true`, the items will be numbered, if `false` they will not.
         */
        var numberItems: Boolean = false
        
        /**
         * Sets whether the menu will instantly time out, and possibly run a provided expression,
         * if only a single slide is available to display.
         *
         * If `true`, the Paginator will still generate
         */
        var waitOnSinglePage: Boolean = false
        
        /**
         * The number of pages to skip when the bulk-skip reactions are used.
         */
        var bulkSkipNumber: Int = 1
        
        /**
         * If `true` wrapping will be enabled.
         */
        var wrapPageEnds: Boolean = false
        
        private var textToLeft: String? = null
        private var textToRight: String? = null
        
        /**
         * Sets the [Paginator][com.jagrosh.jdautilities.menu.Paginator] to allow
         * a page number to be specified by a user via text.
         *
         *
         * Note that setting this doesn't mean that left and right text inputs
         * provided via [PaginationMenu.Builder.setLeftRightText] will
         * be invalidated if they were set previously! To invalidate those, provide
         * `null` for one or both of the parameters of that method.
         *
         * If `true`, the Paginator will allow page-number text input
         */
        var allowTextInput = false
        
        /**
         *  Sets the list of items to paginate.
         *
         *  The first item of the pair is the field title,
         *  the second item of the pair is the field body.
         */
        var items: List<Pair<String, String>> = listOf()
        
        /**
         * Builds the [Paginator][com.jagrosh.jdautilities.menu.Paginator]
         * with this Builder.
         *
         * @return The Paginator built from this Builder.
         *
         * @throws java.lang.IllegalArgumentException
         * If one of the following is violated:
         *
         *  * No [EventWaiter][com.jagrosh.jdautilities.commons.waiter.EventWaiter] was set.
         *  * No items were set to paginate.
         *
         */
        override fun build(): PaginationMenu {
            return PaginationMenu(waiter, users, roles, timeout, unit, title, color, text, finalAction,
                                  itemsPerPage, showPageNumbers, numberItems, items, waitOnSinglePage,
                                  bulkSkipNumber, wrapPageEnds, textToLeft, textToRight, allowTextInput)
        }
        
        /**
         * Sets the [Paginator][com.jagrosh.jdautilities.menu.Paginator] to traverse
         * left or right when a provided text input is sent in the form of a Message to
         * the [GuildChannel][net.dv8tion.jda.api.entities.GuildChannel] the menu is displayed in.
         *
         *
         * If one or both these parameters are provided `null` this resets
         * both of them and they will no longer be available when the Paginator is built.
         *
         * @param  left
         * The left text input, causes the Paginator to traverse one page left
         * @param  right
         * The right text input, causes the Paginator to traverse one page right
         *
         * @return This builder
         */
        fun setLeftRightText(left: String?, right: String?): Builder {
            textToLeft = left
            textToRight = right
            
            return this
        }
    }
    
    companion object {
        const val BIG_LEFT = "\u23EA"
        const val LEFT = "\u25C0"
        const val STOP = "\u23F9"
        const val RIGHT = "\u25B6"
        const val BIG_RIGHT = "\u23E9"
    }
}