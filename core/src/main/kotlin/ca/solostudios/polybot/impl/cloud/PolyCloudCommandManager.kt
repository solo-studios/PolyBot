/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyCloudCommandManager.kt is part of PolyBot
 * Last modified on 10-03-2023 03:29 p.m.
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
 * POLYBOT IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ca.solostudios.polybot.impl.cloud

import ca.solostudios.polybot.api.cloud.CommandExecutionCoordinatorSupplier
import ca.solostudios.polybot.api.cloud.event.MessageEvent
import ca.solostudios.polybot.api.cloud.manager.PolyCommandManager
import ca.solostudios.polybot.api.permissions.PermissionManager
import ca.solostudios.polybot.impl.PolyBotJDA
import cloud.commandframework.internal.CommandRegistrationHandler
import cloud.commandframework.meta.CommandMeta
import cloud.commandframework.meta.SimpleCommandMeta
import net.dv8tion.jda.api.JDA
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

internal class PolyCloudCommandManager(
        override val di: DI,
        override val bot: PolyBotJDA,
        commandExecutionCoordinator: CommandExecutionCoordinatorSupplier,
        commandRegistrationHandler: CommandRegistrationHandler,
                                      ) : PolyCommandManager(bot, commandExecutionCoordinator, commandRegistrationHandler), DIAware {
    private val jda: JDA
        get() = bot.jda
    
    private val permissionManager: PermissionManager by instance()
    
    val botId: Long
        get() = jda.selfUser.idLong
    
    init {
        jda.addEventListener(PolyCloudCommandListener(this, bot, di))
        
        registerCommandPreProcessor(CloudCommandPreprocessor(di))
        
        // parserRegistry().registerParserSupplier(MemberParser(di))
        // parserRegistry().registerParserSupplier(UserParser(di))
        // parserRegistry().registerParserSupplier(MessageChannelParser(di))
        // parserRegistry().registerParserSupplier(TextChannelParser(di))
        // parserRegistry().registerParserSupplier(RoleParser(di))
        // parserRegistry().registerParserSupplier(TagParser(di))
        //
        // registerCommandPostProcessors(GuildCommandPostProcessor(di), UserPermissionPostprocessor(di), BotPermissionPostprocessor(di))
    }
    
    override fun hasPermission(message: MessageEvent, permission: String): Boolean = permissionManager.hasPermission(message.author, permission)
    
    override fun createDefaultCommandMeta(): CommandMeta = SimpleCommandMeta.empty()
    
}