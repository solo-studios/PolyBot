/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyCloudCommandManager.kt is part of PolyhedralBot
 * Last modified on 22-12-2021 11:41 p.m.
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

package ca.solostudios.polybot.cloud.manager

import ca.solostudios.polybot.PolyBot
import ca.solostudios.polybot.cloud.commands.permission.BotPermissionPostprocessor
import ca.solostudios.polybot.cloud.commands.permission.GuildCommandPostProcessor
import ca.solostudios.polybot.cloud.commands.permission.UserPermissionPostprocessor
import ca.solostudios.polybot.cloud.event.EventMapper
import ca.solostudios.polybot.cloud.event.MessageEvent
import ca.solostudios.polybot.cloud.parser.MemberParser
import ca.solostudios.polybot.cloud.parser.MessageChannelParser
import ca.solostudios.polybot.cloud.parser.RoleParser
import ca.solostudios.polybot.cloud.parser.TagParser
import ca.solostudios.polybot.cloud.parser.TextChannelParser
import ca.solostudios.polybot.cloud.parser.UserParser
import ca.solostudios.polybot.util.registerCommandPostProcessors
import ca.solostudios.polybot.util.registerParserSupplier
import cloud.commandframework.CommandManager
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator
import cloud.commandframework.internal.CommandRegistrationHandler
import cloud.commandframework.meta.CommandMeta
import cloud.commandframework.meta.SimpleCommandMeta
import net.dv8tion.jda.api.JDA

class PolyCloudCommandManager(
        val bot: PolyBot,
                             ) : CommandManager<MessageEvent>(
        AsynchronousCommandExecutionCoordinator.newBuilder<MessageEvent>()
                .withAsynchronousParsing()
                .build(),
        CommandRegistrationHandler.nullCommandRegistrationHandler(),
                                                             ) {
    private val jda: JDA = bot.jda
    val eventMapper: EventMapper = bot.eventMapper
    val botId: Long
        get() = jda.selfUser.idLong
    private val permissionManager = bot.permissionManager
    
    init {
        jda.addEventListener(PolyCloudCommandListener(this))
        
        registerCommandPreProcessor(CloudCommandPreprocessor())
        
        parserRegistry.registerParserSupplier(MemberParser(bot))
        parserRegistry.registerParserSupplier(UserParser(bot))
        parserRegistry.registerParserSupplier(MessageChannelParser(bot))
        parserRegistry.registerParserSupplier(TextChannelParser(bot))
        parserRegistry.registerParserSupplier(RoleParser(bot))
        parserRegistry.registerParserSupplier(TagParser(bot))
        
        registerCommandPostProcessors(GuildCommandPostProcessor(bot), UserPermissionPostprocessor(bot), BotPermissionPostprocessor(bot))
    }
    
    override fun hasPermission(sender: MessageEvent, permission: String): Boolean = permissionManager.permissionCheck(sender, permission)
    
    override fun createDefaultCommandMeta(): CommandMeta = SimpleCommandMeta.empty()
    
}