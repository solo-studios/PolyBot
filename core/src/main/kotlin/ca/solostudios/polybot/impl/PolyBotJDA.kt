/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyBotJDA.kt is part of PolyBot
 * Last modified on 23-11-2022 12:42 p.m.
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

package ca.solostudios.polybot.impl

import ca.solostudios.polybot.api.PolyBot
import ca.solostudios.polybot.api.PolyBot.State
import ca.solostudios.polybot.api.entities.PolyCategory
import ca.solostudios.polybot.api.entities.PolyChannel
import ca.solostudios.polybot.api.entities.PolyEmote
import ca.solostudios.polybot.api.entities.PolyGuild
import ca.solostudios.polybot.api.entities.PolyGuildChannel
import ca.solostudios.polybot.api.entities.PolyMember
import ca.solostudios.polybot.api.entities.PolyMentionable
import ca.solostudios.polybot.api.entities.PolyMessage
import ca.solostudios.polybot.api.entities.PolyMessageChannel
import ca.solostudios.polybot.api.entities.PolyMessageEmbed
import ca.solostudios.polybot.api.entities.PolyPermissionHolder
import ca.solostudios.polybot.api.entities.PolyPrivateChannel
import ca.solostudios.polybot.api.entities.PolyRole
import ca.solostudios.polybot.api.entities.PolyTextChannel
import ca.solostudios.polybot.api.entities.PolyUser
import ca.solostudios.polybot.api.entities.PolyVoiceChannel
import ca.solostudios.polybot.api.event.PolyEventManager
import ca.solostudios.polybot.api.jda.builder.InlineJDABuilder
import ca.solostudios.polybot.api.plugin.finder.ClasspathCandidateFinder
import ca.solostudios.polybot.api.plugin.finder.FlatDirectoryCandidateFinder
import ca.solostudios.polybot.api.plugin.loader.PolyClassLoader
import ca.solostudios.polybot.api.service.PolyServiceManager
import ca.solostudios.polybot.api.util.ext.ScheduledThreadPool
import ca.solostudios.polybot.api.util.ext.poly
import ca.solostudios.polybot.api.util.ext.processors
import ca.solostudios.polybot.api.util.ext.runtime
import ca.solostudios.polybot.api.util.path
import ca.solostudios.polybot.impl.entities.PolyCategoryImpl
import ca.solostudios.polybot.impl.entities.PolyChannelImpl
import ca.solostudios.polybot.impl.entities.PolyEmoteImpl
import ca.solostudios.polybot.impl.entities.PolyGuildChannelImpl
import ca.solostudios.polybot.impl.entities.PolyGuildImpl
import ca.solostudios.polybot.impl.entities.PolyMemberImpl
import ca.solostudios.polybot.impl.entities.PolyMessageChannelImpl
import ca.solostudios.polybot.impl.entities.PolyMessageEmbedImpl
import ca.solostudios.polybot.impl.entities.PolyMessageImpl
import ca.solostudios.polybot.impl.entities.PolyPrivateChannelImpl
import ca.solostudios.polybot.impl.entities.PolyRoleImpl
import ca.solostudios.polybot.impl.entities.PolyTextChannelImpl
import ca.solostudios.polybot.impl.entities.PolyUserImpl
import ca.solostudios.polybot.impl.entities.PolyVoiceChannelImpl
import ca.solostudios.polybot.impl.plugin.PolyPluginManagerImpl
import ca.solostudios.polybot.impl.plugin.dsl.PolyPluginDslImpl
import com.uchuhimo.konf.Config
import dev.minn.jda.ktx.await
import it.unimi.dsi.util.XoShiRo256PlusPlusRandom
import java.nio.file.Path
import java.util.concurrent.ScheduledExecutorService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.AbstractChannel
import net.dv8tion.jda.api.entities.Category
import net.dv8tion.jda.api.entities.Emote
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.GuildChannel
import net.dv8tion.jda.api.entities.IMentionable
import net.dv8tion.jda.api.entities.IPermissionHolder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.PrivateChannel
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.VoiceChannel
import org.kodein.di.DI
import org.slf4j.kotlin.*
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random
import kotlin.random.asKotlinRandom

public class PolyBotJDA(
        override val config: Config,
        builder: InlineJDABuilder,
                       ) : PolyBot, CoroutineScope {
    private val logger by getLogger()
    
    override var state: State = State.INITIALIZING
        private set
    
    override val shutdown: Boolean
        get() = state == State.SHUTDOWN || state == State.FAILED
    
    override val running: Boolean
        get() = state == State.RUNNING
    
    override val active: Boolean
        get() = state.active
    
    override val globalRandom: Random = XoShiRo256PlusPlusRandom().asKotlinRandom()
    
    override val scheduledThreadPool: ScheduledExecutorService =
            ScheduledThreadPool((runtime.processors - 1).coerceAtLeast(1), PolyThreadFactory)
    
    private val coroutineDispatcher: ExecutorCoroutineDispatcher = scheduledThreadPool.asCoroutineDispatcher()
    
    override val scope: CoroutineScope = CoroutineScope(SupervisorJob() + coroutineDispatcher)
    
    override val context: CoroutineContext
        get() = scope.coroutineContext
    
    override val coroutineContext: CoroutineContext
        get() = scope.coroutineContext
    
    override val jda: JDA = builder.build {
    
    }
    
    override val eventManager: PolyEventManager
        get() = TODO("Not yet implemented")
    
    override val serviceManager: PolyServiceManager<*, *>
        get() = TODO("Not yet implemented")
    
    override val polyPluginManager: PolyPluginManagerImpl = PolyPluginManagerImpl(
            this,
            listOf(
                    ClasspathCandidateFinder(),
                    FlatDirectoryCandidateFinder(directory("plugins")),
                  )
                                                                                 )
    
    override val id: ULong
        get() = jda.selfUser.idLong.toULong()
    
    override val di: DI = DI {
    
    }
    
    public override val classLoader: PolyClassLoader = PolyClassLoader(javaClass.classLoader)
    
    @Throws(Exception::class)
    override suspend fun start() {
        if (state.active) { // If this service is active, just return
            logger.debug { "PolyBot is already active, but startup was requested. Ignoring." }
            return
        }
    
        state = State.STARTING
        logger.info { "Starting polybot..." }
    
        logger.debug { "Loading plugins..." }
        polyPluginManager.loadPlugins()
    
        val polyDsl = PolyPluginDslImpl()
    
        logger.debug { "Starting plugins..." }
        polyPluginManager.startPlugins(polyDsl)
    
        logger.debug { "Plugins started successfully" }
    
        TODO("Start Polybot") // TODO: 2022-08-17
    
        state = State.RUNNING
    }
    
    @Throws(Exception::class)
    override suspend fun shutdown(exitCode: Int, isShutdownHook: Boolean) {
        if (!running)
            return
    
        state = State.SHUTTING_DOWN
    
        polyPluginManager.shutdownPlugins()
    
        TODO("Polybot shutdown") // TODO: 2022-03-06
    
        state = State.SHUTDOWN
    }
    
    public override fun configDirectory(base: String, vararg subpaths: String): Path = directory(".config", base, *subpaths)
    
    public override fun directory(base: String, vararg subpaths: String): Path {
        return path("./", base, *subpaths)
    }
    
    override fun guildAsync(guildId: ULong): Deferred<PolyGuild?> {
        return async { guild(guildId) }
    }
    
    override suspend fun guild(guildId: ULong): PolyGuild? {
        return jda.getGuildById(guildId.toLong())?.poly(this)
    }
    
    override fun roleAsync(guildId: ULong, roleId: ULong): Deferred<PolyRole?> {
        return async { role(guildId, roleId) }
    }
    
    override suspend fun role(guildId: ULong, roleId: ULong): PolyRole? {
        return jda.getGuildById(guildId.toLong())?.getRoleById(roleId.toLong())?.poly(this)
    }
    
    override fun userAsync(userId: ULong): Deferred<PolyUser?> {
        return async { user(userId) }
    }
    
    override suspend fun user(userId: ULong): PolyUser {
        return jda.retrieveUserById(userId.toLong()).await().poly(this)
    }
    
    override fun memberAsync(guildId: ULong, userId: ULong): Deferred<PolyMember?> {
        return async { member(guildId, userId) }
    }
    
    override suspend fun member(guildId: ULong, userId: ULong): PolyMember? {
        return jda.getGuildById(guildId.toLong())?.retrieveMemberById(userId.toLong())?.await()?.poly(this)
    }
    
    override fun emoteAsync(guildId: ULong, emoteId: ULong): Deferred<PolyEmote?> {
        return async { emote(guildId, emoteId) }
    }
    
    override suspend fun emote(guildId: ULong, emoteId: ULong): PolyEmote? {
        return jda.getGuildById(guildId.toLong())?.retrieveEmoteById(emoteId.toLong())?.await()?.poly(this)
    }
    
    override fun categoryAsync(categoryId: ULong): Deferred<PolyCategory?> {
        return async { category(categoryId) }
    }
    
    override suspend fun category(categoryId: ULong): PolyCategory? {
        return jda.getCategoryById(categoryId.toLong())?.poly(this)
    }
    
    override fun guildChannelAsync(guildChannelId: ULong): Deferred<PolyGuildChannel?> {
        return async { guildChannel(guildChannelId) }
    }
    
    override suspend fun guildChannel(guildChannelId: ULong): PolyGuildChannel? {
        return jda.getGuildChannelById(guildChannelId.toLong())?.poly(this)
    }
    
    override fun textChannelAsync(textChannelId: ULong): Deferred<PolyTextChannel?> {
        return async { textChannel(textChannelId) }
    }
    
    override suspend fun textChannel(textChannelId: ULong): PolyTextChannel? {
        return jda.getTextChannelById(textChannelId.toLong())?.poly(this)
    }
    
    override fun voiceChannelAsync(voiceChannelId: ULong): Deferred<PolyVoiceChannel?> {
        return async { voiceChannel(voiceChannelId) }
    }
    
    override suspend fun voiceChannel(voiceChannelId: ULong): PolyVoiceChannel? {
        return jda.getVoiceChannelById(voiceChannelId.toLong())?.poly(this)
    }
    
    override fun poly(jdaEntity: Member): PolyMember = PolyMemberImpl(this, jdaEntity)
    
    override fun poly(jdaEntity: User): PolyUser = PolyUserImpl(this, jdaEntity)
    
    override fun poly(jdaEntity: Guild): PolyGuild = PolyGuildImpl(this, jdaEntity)
    
    override fun poly(jdaEntity: Message): PolyMessage = PolyMessageImpl(this, jdaEntity)
    
    override fun poly(jdaEntity: Role): PolyRole = PolyRoleImpl(this, jdaEntity)
    
    override fun poly(jdaEntity: AbstractChannel): PolyChannel {
        return when (jdaEntity) {
            is PrivateChannel -> poly(jdaEntity)
            is VoiceChannel   -> poly(jdaEntity)
            is TextChannel    -> poly(jdaEntity)
            is Category       -> poly(jdaEntity)
            is MessageChannel -> poly(jdaEntity)
            is GuildChannel   -> poly(jdaEntity)
            else              -> PolyChannelImpl(this, jdaEntity) // Unknown channel type (default channel)
        }
    }
    
    override fun poly(jdaEntity: GuildChannel): PolyGuildChannel {
        return when (jdaEntity) {
            is VoiceChannel -> poly(jdaEntity)
            is TextChannel  -> poly(jdaEntity)
            is Category     -> poly(jdaEntity)
            else            -> PolyGuildChannelImpl(this, jdaEntity) // Unknown channel type (default guild channel)
        }
    }
    
    override fun poly(jdaEntity: MessageChannel): PolyMessageChannel {
        return when (jdaEntity) {
            is PrivateChannel -> poly(jdaEntity)
            is TextChannel    -> poly(jdaEntity)
            else              -> PolyMessageChannelImpl(this, jdaEntity) // Unknown channel type (default message channel)
        }
    }
    
    override fun poly(jdaEntity: PrivateChannel): PolyPrivateChannel = PolyPrivateChannelImpl(this, jdaEntity)
    
    override fun poly(jdaEntity: Category): PolyCategory {
        return PolyCategoryImpl(this, jdaEntity)
    }
    
    override fun poly(jdaEntity: TextChannel): PolyTextChannel {
        return PolyTextChannelImpl(this, jdaEntity)
    }
    
    override fun poly(jdaEntity: VoiceChannel): PolyVoiceChannel {
        return PolyVoiceChannelImpl(this, jdaEntity)
    }
    
    override fun poly(jdaEntity: MessageEmbed): PolyMessageEmbed {
        return PolyMessageEmbedImpl(this, jdaEntity)
    }
    
    override fun poly(jdaEntity: Emote): PolyEmote {
        return PolyEmoteImpl(this, jdaEntity)
    }
    
    override fun poly(jdaEntity: IMentionable): PolyMentionable {
        return when (jdaEntity) {
            is VoiceChannel -> poly(jdaEntity)
            is TextChannel  -> poly(jdaEntity)
            is Category     -> poly(jdaEntity)
            is GuildChannel -> poly(jdaEntity)
            is Role         -> poly(jdaEntity)
            is User         -> poly(jdaEntity)
            is Member       -> poly(jdaEntity)
            is Emote        -> poly(jdaEntity)
            else            -> error("Unknown type ${jdaEntity::class}, could not find assignable ${PolyMentionable::class}.")
        }
    }
    
    override fun poly(jdaEntity: IPermissionHolder): PolyPermissionHolder {
        return when (jdaEntity) {
            is Role   -> poly(jdaEntity)
            is Member -> poly(jdaEntity)
            else      -> error("Unknown type ${jdaEntity::class}, Could not find assignable ${PolyPermissionHolder::class}.")
        }
    }
}