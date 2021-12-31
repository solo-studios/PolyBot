/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyBot.kt is part of PolyhedralBot
 * Last modified on 30-12-2021 05:28 p.m.
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
import ca.solostudios.polybot.cloud.CloudInjectionService
import ca.solostudios.polybot.cloud.commands.PolyCommands
import ca.solostudios.polybot.cloud.commands.PolyMeta
import ca.solostudios.polybot.cloud.commands.annotations.CommandLongDescription
import ca.solostudios.polybot.cloud.commands.annotations.CommandName
import ca.solostudios.polybot.cloud.commands.annotations.JDABotPermission
import ca.solostudios.polybot.cloud.commands.annotations.JDAGuildCommand
import ca.solostudios.polybot.cloud.commands.annotations.JDAUserPermission
import ca.solostudios.polybot.cloud.commands.annotations.PolyCategory
import ca.solostudios.polybot.cloud.event.EventMapper
import ca.solostudios.polybot.cloud.event.MessageEvent
import ca.solostudios.polybot.cloud.manager.PolyCloudCommandManager
import ca.solostudios.polybot.config.PolyConfig
import ca.solostudios.polybot.entities.EntityManager
import ca.solostudios.polybot.entities.PolyEmote
import ca.solostudios.polybot.entities.PolyGuild
import ca.solostudios.polybot.entities.PolyMember
import ca.solostudios.polybot.entities.PolyRole
import ca.solostudios.polybot.entities.PolyTextChannel
import ca.solostudios.polybot.entities.PolyUser
import ca.solostudios.polybot.entities.PolyVoiceChannel
import ca.solostudios.polybot.event.EventManager
import ca.solostudios.polybot.listener.AntiEmbedListener
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
import ca.solostudios.polybot.util.poly
import ca.solostudios.polybot.util.processors
import ca.solostudios.polybot.util.runtime
import ca.solostudios.polybot.util.subTypesOf
import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.kotlin.coroutines.annotations.installCoroutineSupport
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
import net.dv8tion.jda.api.entities.Emote
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.VoiceChannel
import net.dv8tion.jda.api.requests.restaction.MessageAction
import org.reflections.Reflections
import org.slf4j.kotlin.*
import kotlin.io.path.Path
import kotlin.random.asKotlinRandom
import kotlin.system.exitProcess
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes


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
        eventListeners += AntiEmbedListener(this@PolyBot)
    }.build()
    
    // val databaseManager = DatabaseManager(this@PolyBot)
    
    val entityManager = EntityManager(this@PolyBot)
    
    val permissionManager = PermissionManager(this@PolyBot)
    
    val eventMapper = EventMapper(this@PolyBot)
    
    val commandManager = PolyCloudCommandManager(this)
    
    val annotationParser: AnnotationParser<MessageEvent> = AnnotationParser(commandManager) { SimpleCommandMeta.empty() }.apply {
        parameterInjectorRegistry.registerInjectionService(CloudInjectionService(this@PolyBot))
    
        installCoroutineSupport(this@PolyBot.scope)
    
        registerBuilderModifier(JDABotPermission::class.java, PolyMeta::botPermissionModifier)
        registerBuilderModifier(JDAUserPermission::class.java, PolyMeta::userPermissionModifier)
        registerBuilderModifier(JDAGuildCommand::class.java, PolyMeta::guildCommandModifier)
        registerBuilderModifier(PolyCategory::class.java, PolyMeta::categoryCommandModifier)
        registerBuilderModifier(CommandDescription::class.java, PolyMeta::descriptionCommandModifier)
        registerBuilderModifier(CommandLongDescription::class.java, PolyMeta::longDescriptionCommandModifier)
        registerBuilderModifier(CommandName::class.java, PolyMeta::nameCommandModifier)
    }
    
    val exceptionHandler = PolyExceptionHandler(this@PolyBot, commandManager)
    
    init {
        scheduledThreadPool.fixedRate(100.milliseconds, 5.minutes) {
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
    
    fun guildReference(guildId: Long): BackedReference<Guild?, Long> {
        return BackedReference(guildId, { jda.getGuildById(it) }, { it?.idLong ?: 0 })
    }
    
    fun polyGuildReference(guildId: Long): BackedReference<PolyGuild?, Long> {
        return BackedReference(guildId, { jda.getGuildById(it)?.poly(this) }, { it?.id ?: 0 })
    }
    
    fun guild(guildId: Long): Guild? {
        return jda.getGuildById(guildId)
    }
    
    fun polyGuild(guildId: Long): PolyGuild? {
        return jda.getGuildById(guildId)?.poly(this)
    }
    
    fun textChannelReference(channelId: Long): BackedReference<TextChannel?, Long> {
        return BackedReference(channelId, { jda.getTextChannelById(it) }, { it?.idLong ?: 0 })
    }
    
    fun polyTextChannelReference(channelId: Long): BackedReference<PolyTextChannel?, Long> {
        return BackedReference(channelId, { jda.getTextChannelById(it)?.poly(this) }, { it?.id ?: 0 })
    }
    
    fun textChannel(channelId: Long): TextChannel? {
        return jda.getTextChannelById(channelId)
    }
    
    fun polyTextChannel(channelId: Long): PolyTextChannel? {
        return jda.getTextChannelById(channelId)?.poly(this)
    }
    
    fun voiceChannelReference(channelId: Long): BackedReference<VoiceChannel?, Long> {
        return BackedReference(channelId, { jda.getVoiceChannelById(it) }, { it?.idLong ?: 0 })
    }
    
    fun polyVoiceChannelReference(channelId: Long): BackedReference<PolyVoiceChannel?, Long> {
        return BackedReference(channelId, { jda.getVoiceChannelById(it)?.poly(this) }, { it?.id ?: 0 })
    }
    
    fun voiceChannel(channelId: Long): VoiceChannel? {
        return jda.getVoiceChannelById(channelId)
    }
    
    fun polyVoiceChannel(channelId: Long): PolyVoiceChannel? {
        return jda.getVoiceChannelById(channelId)?.poly(this)
    }
    
    fun roleReference(roleId: Long): BackedReference<Role?, Long> {
        return BackedReference(roleId, { jda.getRoleById(it) }, { it?.idLong ?: 0 })
    }
    
    fun polyRoleReference(roleId: Long): BackedReference<PolyRole?, Long> {
        return BackedReference(roleId, { jda.getRoleById(it)?.poly(this) }, { it?.id ?: 0 })
    }
    
    fun role(roleId: Long): Role? {
        return jda.getRoleById(roleId)
    }
    
    fun polyRole(roleId: Long): PolyRole? {
        return jda.getRoleById(roleId)?.poly(this)
    }
    
    fun userReference(userId: Long): BackedReference<User?, Long> {
        return BackedReference(userId, { jda.getUserById(it) }, { it?.idLong ?: 0 })
    }
    
    fun polyUserReference(userId: Long): BackedReference<PolyUser?, Long> {
        return BackedReference(userId, { jda.getUserById(it)?.poly(this) }, { it?.id ?: 0 })
    }
    
    fun user(userId: Long): User? {
        return jda.getUserById(userId)
    }
    
    fun polyUser(userId: Long): PolyUser? {
        return jda.getUserById(userId)?.poly(this)
    }
    
    fun memberReference(guildId: Long, userId: Long): BackedReference<Member?, Pair<Long, Long>> {
        return BackedReference(guildId to userId,
                               { jda.getGuildById(it.first)?.getMemberById(it.second) },
                               { it?.let { it.idLong to it.guild.idLong } ?: (0L to 0L) })
    }
    
    fun polyMemberReference(guildId: Long, userId: Long): BackedReference<PolyMember?, Pair<Long, Long>> {
        return BackedReference(guildId to userId,
                               { jda.getGuildById(it.first)?.getMemberById(it.second)?.poly(this) },
                               { it?.let { it.id to it.guild.id } ?: (0L to 0L) })
    }
    
    fun member(guildId: Long, userId: Long): Member? {
        return jda.getGuildById(guildId)?.getMemberById(userId)
    }
    
    fun polyMember(guildId: Long, userId: Long): PolyMember? {
        return jda.getGuildById(guildId)?.getMemberById(userId)?.poly(this)
    }
    
    fun emoteReference(emoteId: Long): BackedReference<Emote?, Long> {
        return BackedReference(emoteId, { jda.getEmoteById(it) }, { it?.idLong ?: 0 })
    }
    
    fun polyEmoteReference(emoteId: Long): BackedReference<PolyEmote?, Long> {
        return BackedReference(emoteId, { jda.getEmoteById(it)?.poly(this) }, { it?.id ?: 0 })
    }
    
    fun emote(emoteId: Long): Emote? {
        return jda.getEmoteById(emoteId)
    }
    
    fun polyEmote(emoteId: Long): PolyEmote? {
        return jda.getEmoteById(emoteId)?.poly(this)
    }
    
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