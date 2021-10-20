/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyBot.kt is part of PolyhedralBot
 * Last modified on 20-10-2021 11:59 a.m.
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

package ca.solostudios.polybot

import ca.solostudios.polybot.cache.CacheManager
import ca.solostudios.polybot.cloud.CloudInjectorService
import ca.solostudios.polybot.cloud.commands.PolyCommands
import ca.solostudios.polybot.cloud.commands.PolyMeta
import ca.solostudios.polybot.cloud.commands.annotations.JDABotPermission
import ca.solostudios.polybot.cloud.commands.annotations.JDAGuildCommand
import ca.solostudios.polybot.cloud.commands.annotations.JDAUserPermission
import ca.solostudios.polybot.cloud.commands.annotations.PolyCategory
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
import ca.solostudios.polybot.cloud.preprocessor.AntiBotPreProcessor
import ca.solostudios.polybot.cloud.preprocessor.AntiWebhookPreProcessor
import ca.solostudios.polybot.cloud.preprocessor.JDAMessagePreprocessor
import ca.solostudios.polybot.config.PolyConfig
import ca.solostudios.polybot.entities.EntityManager
import ca.solostudios.polybot.event.EventManager
import ca.solostudios.polybot.listener.AutoQuoteListener
import ca.solostudios.polybot.listener.LoggingListener
import ca.solostudios.polybot.listener.PolyBotListener
import ca.solostudios.polybot.search.SearchManager
import ca.solostudios.polybot.util.AnnotationParser
import ca.solostudios.polybot.util.BackedReference
import ca.solostudios.polybot.util.ScheduledThreadPool
import ca.solostudios.polybot.util.currentThread
import ca.solostudios.polybot.util.fixedRate
import ca.solostudios.polybot.util.onlineStatus
import ca.solostudios.polybot.util.parseCommands
import ca.solostudios.polybot.util.processors
import ca.solostudios.polybot.util.registerCommandPostProcessors
import ca.solostudios.polybot.util.registerCommandPreProcessors
import ca.solostudios.polybot.util.registerParserSupplier
import ca.solostudios.polybot.util.runtime
import ca.solostudios.polybot.util.subTypesOf
import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.kotlin.coroutines.installCoroutineSupport
import cloud.commandframework.meta.SimpleCommandMeta
import dev.minn.jda.ktx.InlineJDABuilder
import it.unimi.dsi.util.XoShiRo256PlusPlusRandom
import java.util.EnumSet
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ThreadFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.requests.restaction.MessageAction
import org.reflections.Reflections
import org.slf4j.kotlin.*
import kotlin.io.path.Path
import kotlin.random.asKotlinRandom
import kotlin.system.exitProcess
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator as CommandCoordinator
import cloud.commandframework.jda.JDA4CommandManager as CommandManager


@ExperimentalTime
@Suppress("MemberVisibilityCanBePrivate", "unused")
class PolyBot(val config: PolyConfig, builder: InlineJDABuilder) {
    private val logger by getLogger()
    
    var shutdown = false
        private set
    
    val running
        get() = !shutdown
    
    val botConfig = config.botConfig
    
    val globalRandom = XoShiRo256PlusPlusRandom().asKotlinRandom()
    
    val scheduledThreadPool: ScheduledExecutorService = ScheduledThreadPool((runtime.processors - 1).coerceAtLeast(1), PolyThreadFactory)
    
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
    
    // val databaseManager = DatabaseManager(this@PolyBot)
    
    val entityManager = EntityManager(this@PolyBot)
    
    val permissionManager = PermissionManager(this@PolyBot)
    
    val eventMapper = EventMapper(this@PolyBot)
    
    val commandManager: CommandManager<MessageEvent> = CommandManager(jda,
                                                                      this::botPrefix,
                                                                      permissionManager::permissionCheck,
                                                                      CommandCoordinator.newBuilder<MessageEvent>()
                                                                              .withAsynchronousParsing()
                                                                              .build(),
                                                                      eventMapper::senderToMessageEvent,
                                                                      eventMapper::messageEventToSender).apply {
        parserRegistry.registerParserSupplier(MemberParser(this@PolyBot))
        parserRegistry.registerParserSupplier(UserParser(this@PolyBot))
        parserRegistry.registerParserSupplier(MessageChannelParser(this@PolyBot))
        parserRegistry.registerParserSupplier(TextChannelParser(this@PolyBot))
        parserRegistry.registerParserSupplier(RoleParser(this@PolyBot))
        parserRegistry.registerParserSupplier(TagParser(this@PolyBot))
    
        registerCommandPreProcessors(JDAMessagePreprocessor(this), AntiWebhookPreProcessor(this), AntiBotPreProcessor(this))
    
        registerCommandPostProcessors(GuildCommandPostProcessor(this@PolyBot),
                                      UserPermissionPostprocessor(this@PolyBot),
                                      BotPermissionPostprocessor(this@PolyBot))
    }
    
    val annotationParser: AnnotationParser<MessageEvent> = AnnotationParser(commandManager) { SimpleCommandMeta.empty() }.apply {
        parameterInjectorRegistry.registerInjectionService(CloudInjectorService(this@PolyBot))
    
        installCoroutineSupport(this@PolyBot.scope)
    
        registerBuilderModifier(JDABotPermission::class.java, PolyMeta::botPermissionModifier)
        registerBuilderModifier(JDAUserPermission::class.java, PolyMeta::userPermissionModifier)
        registerBuilderModifier(JDAGuildCommand::class.java, PolyMeta::guildCommandModifier)
        registerBuilderModifier(PolyCategory::class.java, PolyMeta::categoryCommandModifier)
    }
    
    val exceptionHandler = PolyExceptionHandler(this@PolyBot, commandManager)
    
    init {
        scheduledThreadPool.fixedRate(Duration.milliseconds(100), Duration.minutes(5)) {
            jda.presence.apply {
                val botActivity = botConfig.activities.random()
                onlineStatus = OnlineStatus.ONLINE
                activity = botActivity.getActivity()
            }
        }
    
        val reflections = Reflections("ca.solostudios.polybot.commands")
        
        
        val commands = reflections.subTypesOf<PolyCommands>().map { klass ->
            val constructor = klass.constructors.single()
    
            return@map constructor.call(this@PolyBot)
        }
    
        annotationParser.parseCommands(commands)
    
        MessageAction.setDefaultMentionRepliedUser(true)
        MessageAction.setDefaultMentions(EnumSet.complementOf(EnumSet.of(Message.MentionType.EVERYONE, Message.MentionType.HERE)))
    }
    
    val id: Long
        get() = jda.selfUser.idLong
    
    val avatarUrl: String
        get() = jda.selfUser.effectiveAvatarUrl
    
    val totalMembers: Long
        get() = jda.guildCache.sumOf { it.memberCount }.toLong()
    
    val guilds: Long
        get() = jda.guildCache.size() + jda.unavailableGuilds.size
    
    val commands: Int
        get() = commandManager.commandHelpHandler.allCommands.size
    
    fun getCacheDirectory(vararg name: String) = Path(".cache", *name)
    
    @Suppress("UNUSED_PARAMETER")
    private fun botPrefix(event: MessageEvent) = botConfig.prefix
    
    fun guild(guildId: Long) = BackedReference(guildId, { jda.getGuildById(it) }, { it?.idLong ?: 0 })
    
    fun textChannel(channelId: Long) = BackedReference(channelId, { jda.getTextChannelById(it) }, { it?.idLong ?: 0 })
    
    fun voiceChannel(channelId: Long) = BackedReference(channelId, { jda.getVoiceChannelById(it) }, { it?.idLong ?: 0 })
    
    fun role(roleId: Long) = BackedReference(roleId, { jda.getRoleById(it) }, { it?.idLong ?: 0 })
    
    fun user(userId: Long) = BackedReference(userId, { jda.getUserById(it) }, { it?.idLong ?: 0 })
    
    fun member(guildId: Long, userId: Long) = BackedReference(guildId to userId,
                                                              { jda.getGuildById(it.first)?.getMemberById(it.second) },
                                                              { it?.let { it.idLong to it.guild.idLong } ?: (0L to 0L) })
    
    fun shutdown(exitCode: Int = ExitCodes.EXIT_CODE_NORMAL, isShutdownHook: Boolean = false) {
        shutdown = true
        
        jda.presence.apply {
            onlineStatus = OnlineStatus.DO_NOT_DISTURB
            activity = Activity.watching("Shutting down PolyBot...")
        }
        
        val exitStatus = try {
            
            jda.shutdownNow()
            
            entityManager.shutdown()
            // databaseManager.shutdown()
            cacheManager.shutdown()
            searchManager.shutdown()
            
            scheduledThreadPool.shutdown()
            coroutineDispatcher.close()
            
            scope.cancel("Shutdown")
            
            logger.info { "Shutdown of PolyBot finished" }
            
            exitCode
        } catch (e: Exception) {
            logger.error(e) { "Exception occurred while shutting down." }
            
            ExitCodes.EXIT_CODE_ERROR
        }
        
        if (!isShutdownHook) {
            removeShutdownThread()
            exitProcess(exitStatus)
        }
    }
    
    object PolyThreadFactory : ThreadFactory {
        private val threadGroup: ThreadGroup = currentThread.threadGroup
        private var threadCount: Int = 0
        
        override fun newThread(runnable: Runnable): Thread = Thread(threadGroup, runnable, "PolyBot-Worker-${threadCount++}", 0)
    }
}