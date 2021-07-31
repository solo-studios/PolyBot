/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyBot.kt is part of PolyhedralBot
 * Last modified on 25-07-2021 12:36 p.m.
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

package com.solostudios.polybot

import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.meta.SimpleCommandMeta
import com.solostudios.polybot.annotations.permission.JDABotPermission
import com.solostudios.polybot.annotations.permission.JDAUserPermission
import com.solostudios.polybot.cache.CacheManager
import com.solostudios.polybot.cache.MessageCacheListener
import com.solostudios.polybot.commands.MessageCacheCommands
import com.solostudios.polybot.commands.ModerationCommands
import com.solostudios.polybot.commands.UtilCommands
import com.solostudios.polybot.config.PolyConfig
import com.solostudios.polybot.event.EventManager
import com.solostudios.polybot.event.cloud.EventMapper
import com.solostudios.polybot.event.cloud.MessageEvent
import com.solostudios.polybot.logging.LoggingListener
import com.solostudios.polybot.parser.MemberParser
import com.solostudios.polybot.parser.UserParser
import com.solostudios.polybot.permission.BotPermissionPostprocessor
import com.solostudios.polybot.permission.PermissionMetaModifier
import com.solostudios.polybot.permission.UserPermissionPostprocessor
import com.solostudios.polybot.search.SearchManager
import com.solostudios.polybot.util.AnnotationParser
import com.solostudios.polybot.util.fixedRate
import com.solostudios.polybot.util.onlineStatus
import com.solostudios.polybot.util.parse
import com.solostudios.polybot.util.registerInjector
import com.solostudios.polybot.util.registerParserSupplier
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import net.dv8tion.jda.InlineJDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Message
import org.slf4j.kotlin.getLogger
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator as CommandCoordinator
import cloud.commandframework.jda.JDA4CommandManager as CommandManager
import com.solostudios.polybot.JDAMessageCommandPreprocessor as MessagePreprocessor

@ExperimentalTime
@Suppress("UNUSED_ANONYMOUS_PARAMETER", "MemberVisibilityCanBePrivate", "unused")
class PolyBot(val config: PolyConfig, builder: InlineJDABuilder) {
    private val logger by getLogger()
    
    val botConfig = config.botConfig
    
    val cacheManager = CacheManager(this@PolyBot)
    
    val eventManager = EventManager(this@PolyBot)
    
    val moderationManager = ModerationManager(this@PolyBot)
    
    val searchManager = SearchManager(this@PolyBot)
    
    val jda = builder.run {
        eventListeners += LoggingListener(this@PolyBot)
        eventListeners += MessageCacheListener(this@PolyBot)
        eventListeners += PolyBotListener(this@PolyBot)
        
        return@run this.build()
    }
    
    val scheduledThreadPool: ScheduledExecutorService = Executors.newScheduledThreadPool(12).apply { // magic number go brrr
        fixedRate(Duration.milliseconds(100), Duration.minutes(5)) {
            jda.presence.apply {
                val botActivity = botConfig.activities.random()
                onlineStatus = OnlineStatus.ONLINE
                activity = botActivity.getActivity()
            }
        }
    }
    
    val permissionManager = PermissionManager(this@PolyBot)
    
    val commandManager = CommandManager(jda,
                                        this::botPrefix,
                                        permissionManager::permissionCheck,
                                        CommandCoordinator.newBuilder<MessageEvent>()
                                                .withAsynchronousParsing()
                                                .build(),
                                        EventMapper::senderToMessageEvent,
                                        EventMapper::messageEventToSender).apply {
        parserRegistry.registerParserSupplier(MemberParser())
        parserRegistry.registerParserSupplier(UserParser())
    
        registerCommandPreProcessor(MessagePreprocessor(this))
        registerCommandPostProcessor(UserPermissionPostprocessor(this@PolyBot))
        registerCommandPostProcessor(BotPermissionPostprocessor())
    
        PolyExceptionHandler(this)
    }
    
    val annotationParser: AnnotationParser<MessageEvent> = AnnotationParser(commandManager) { SimpleCommandMeta.empty() }.apply {
        parameterInjectorRegistry.registerInjector { context, _ ->
            context.get<Message>("Message")
        }
    
        registerBuilderModifier(JDABotPermission::class.java, PermissionMetaModifier::botPermissionModifier)
        registerBuilderModifier(JDAUserPermission::class.java, PermissionMetaModifier::userPermissionModifier)
    
        parse(UtilCommands(this@PolyBot),
              ModerationCommands(this@PolyBot),
              MessageCacheCommands(this@PolyBot))
    }
    
    private fun botPrefix(event: MessageEvent) = botConfig.prefix
    
    fun shutdown() {
    
    }
}