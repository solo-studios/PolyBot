/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyBot.kt is part of PolyhedralBot
 * Last modified on 13-09-2021 08:56 p.m.
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
import com.solostudios.polybot.cache.CacheManager
import com.solostudios.polybot.cloud.event.EventMapper
import com.solostudios.polybot.cloud.event.MessageEvent
import com.solostudios.polybot.cloud.parser.MemberParser
import com.solostudios.polybot.cloud.parser.UserParser
import com.solostudios.polybot.cloud.permission.BotPermissionPostprocessor
import com.solostudios.polybot.cloud.permission.PermissionMetaModifier
import com.solostudios.polybot.cloud.permission.UserPermissionPostprocessor
import com.solostudios.polybot.cloud.permission.annotations.JDABotPermission
import com.solostudios.polybot.cloud.permission.annotations.JDAUserPermission
import com.solostudios.polybot.cloud.preprocessor.AntiWebhookPreProcessor
import com.solostudios.polybot.commands.BotAdminCommands
import com.solostudios.polybot.commands.EasterEggCommands
import com.solostudios.polybot.commands.GithubCommands
import com.solostudios.polybot.commands.LuceneCommands
import com.solostudios.polybot.commands.MessageCacheCommands
import com.solostudios.polybot.commands.ModerationCommands
import com.solostudios.polybot.commands.TagCommands
import com.solostudios.polybot.commands.UtilCommands
import com.solostudios.polybot.config.PolyConfig
import com.solostudios.polybot.database.DatabaseManager
import com.solostudios.polybot.entities.EntityManager
import com.solostudios.polybot.event.EventManager
import com.solostudios.polybot.listener.AutoQuoteListener
import com.solostudios.polybot.logging.LoggingListener
import com.solostudios.polybot.search.SearchManager
import com.solostudios.polybot.service.ShutdownService
import com.solostudios.polybot.util.AnnotationParser
import com.solostudios.polybot.util.BackedReference
import com.solostudios.polybot.util.ScheduledThreadPool
import com.solostudios.polybot.util.currentThread
import com.solostudios.polybot.util.fixedRate
import com.solostudios.polybot.util.onlineStatus
import com.solostudios.polybot.util.parse
import com.solostudios.polybot.util.processors
import com.solostudios.polybot.util.registerInjector
import com.solostudios.polybot.util.registerParserSupplier
import com.solostudios.polybot.util.runtime
import dev.minn.jda.ktx.InlineJDABuilder
import java.util.concurrent.ThreadFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Message
import org.slf4j.kotlin.*
import kotlin.io.path.Path
import kotlin.system.exitProcess
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator as CommandCoordinator
import cloud.commandframework.jda.JDA4CommandManager as CommandManager
import com.solostudios.polybot.cloud.preprocessor.JDAMessagePreprocessor as MessagePreprocessor

@ExperimentalTime
@Suppress("MemberVisibilityCanBePrivate", "unused")
class PolyBot(val config: PolyConfig, builder: InlineJDABuilder) : ShutdownService() {
    private val logger by getLogger()
    
    val botConfig = config.botConfig
    
    @Suppress("HasPlatformType")
    val scheduledThreadPool = ScheduledThreadPool((runtime.processors - 1).takeIf { it > 0 } ?: 1, PolyThreadFactory)
    
    val coroutineDispatcher: ExecutorCoroutineDispatcher = scheduledThreadPool.asCoroutineDispatcher()
    
    val scope = CoroutineScope(SupervisorJob() + coroutineDispatcher)
    
    val cacheManager = CacheManager(this@PolyBot)
    
    val eventManager = EventManager(this@PolyBot)
    
    val moderationManager = ModerationManager(this@PolyBot)
    
    val searchManager = SearchManager(this@PolyBot)
    
    val jda = builder.apply {
        eventListeners += LoggingListener(this@PolyBot)
        eventListeners += PolyBotListener(this@PolyBot)
        eventListeners += AutoQuoteListener(this@PolyBot)
    }.build()
    
    val databaseManager = DatabaseManager(this@PolyBot)
    
    val entityManager = EntityManager(this@PolyBot)
    
    val permissionManager = PermissionManager(this@PolyBot)
    
    val commandManager: CommandManager<MessageEvent> = CommandManager(jda,
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
        registerCommandPreProcessor(AntiWebhookPreProcessor(this))
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
    }
    
    init {
        scheduledThreadPool.fixedRate(Duration.milliseconds(100), Duration.minutes(5)) {
            jda.presence.apply {
                val botActivity = botConfig.activities.random()
                onlineStatus = OnlineStatus.ONLINE
                activity = botActivity.getActivity()
            }
        }
    
        annotationParser.parse(UtilCommands(this@PolyBot),
                               ModerationCommands(this@PolyBot),
                               MessageCacheCommands(this@PolyBot),
                               EasterEggCommands(this@PolyBot),
                               GithubCommands(this@PolyBot),
                               LuceneCommands(this@PolyBot),
                               BotAdminCommands(this@PolyBot),
                               TagCommands(this@PolyBot))
    }
    
    fun getCacheDirectory(vararg name: String) = Path(".cache", *name)
    
    @Suppress("UNUSED_PARAMETER")
    private fun botPrefix(event: MessageEvent) = botConfig.prefix
    
    fun guild(guildId: Long) = BackedReference(guildId, { jda.getGuildById(it) }, { it.idLong ?: 0 })
    
    fun textChannel(channelId: Long) = BackedReference(channelId, { jda.getTextChannelById(it) }, { it.idLong ?: 0 })
    
    fun voiceChannel(channelId: Long) = BackedReference(channelId, { jda.getVoiceChannelById(it) }, { it.idLong ?: 0 })
    
    fun role(roleId: Long) = BackedReference(roleId, { jda.getRoleById(it) }, { it.idLong ?: 0 })
    
    fun user(userId: Long) = BackedReference(userId, { jda.getUserById(it) }, { it.idLong ?: 0 })
    
    fun member(guildId: Long, userId: Long) = BackedReference(guildId to userId,
                                                              { jda.getGuildById(it.first)?.getMemberById(it.second) },
                                                              { it?.let { it.idLong to it.guild.idLong } ?: (0L to 0L) })
    
    override fun serviceShutdown() {
        jda.presence.apply {
            onlineStatus = OnlineStatus.DO_NOT_DISTURB
            activity = Activity.watching("Shutting down PolyBot...")
        }
        
        jda.shutdownNow()
        
        databaseManager.shutdown()
        cacheManager.shutdown()
        searchManager.shutdown()
        
        scheduledThreadPool.shutdown()
        coroutineDispatcher.close()
        
        scope.cancel("Shutdown")
        
        logger.info { "Shutdown of PolyBot finished" }
        
        exitProcess(0)
    }
    
    object PolyThreadFactory : ThreadFactory {
        private val threadGroup: ThreadGroup = currentThread.threadGroup
        private var threadCount: Int = 0
        
        override fun newThread(runnable: Runnable): Thread = Thread(threadGroup, runnable, "PolyBot-Worker-${threadCount++}", 0)
    }
}