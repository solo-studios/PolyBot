/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyBotJDA.kt is part of PolyBot
 * Last modified on 26-06-2022 04:42 p.m.
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
import ca.solostudios.polybot.api.plugin.PolyPluginManager
import ca.solostudios.polybot.api.service.PolyServiceManager
import ca.solostudios.polybot.api.util.datastructures.BackedSuspendingReference
import ca.solostudios.polybot.api.util.ext.ScheduledThreadPool
import ca.solostudios.polybot.api.util.ext.processors
import ca.solostudios.polybot.api.util.ext.runtime
import ca.solostudios.polybot.impl.entities.PolyCategoryImpl
import ca.solostudios.polybot.impl.entities.PolyChannelImpl
import ca.solostudios.polybot.impl.entities.PolyGuildChannelImpl
import ca.solostudios.polybot.impl.entities.PolyGuildImpl
import ca.solostudios.polybot.impl.entities.PolyMemberImpl
import ca.solostudios.polybot.impl.entities.PolyMessageChannelImpl
import ca.solostudios.polybot.impl.entities.PolyMessageImpl
import ca.solostudios.polybot.impl.entities.PolyPrivateChannelImpl
import ca.solostudios.polybot.impl.entities.PolyRoleImpl
import ca.solostudios.polybot.impl.entities.PolyTextChannelImpl
import ca.solostudios.polybot.impl.entities.PolyUserImpl
import ca.solostudios.polybot.impl.entities.PolyVoiceChannelImpl
import com.uchuhimo.konf.Config
import it.unimi.dsi.util.XoShiRo256PlusPlusRandom
import java.nio.file.Path
import java.util.concurrent.ScheduledExecutorService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
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
    
    override var state: PolyBot.State = PolyBot.State.INITIALIZING
        private set
    
    override val shutdown: Boolean
        get() = state == PolyBot.State.SHUTDOWN || state == PolyBot.State.FAILED
    
    override val running: Boolean
        get() = state == PolyBot.State.RUNNING
    
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
    
    override val serviceManager: PolyServiceManager
        get() = TODO("Not yet implemented")
    
    override val polyPluginManager: PolyPluginManager
        get() = TODO("Not yet implemented")
    
    override val id: ULong
        get() = jda.selfUser.idLong.toULong()
    
    override val di: DI
        get() = TODO("Not yet implemented")
    
    @Throws(Exception::class)
    override suspend fun start() {
        if (state.active) { // If this service is active, just return
            logger.debug { "PolyBot is already active, but startup was requested. Ignoring." }
            return
        }
        
        state = PolyBot.State.STARTING
        
        // TODO: 2022-03-06 Start Polybot
        
        state = PolyBot.State.RUNNING
    }
    
    @Throws(Exception::class)
    override suspend fun shutdown(exitCode: Int, isShutdownHook: Boolean) {
        if (!running)
            return
    
        state = PolyBot.State.SHUTTING_DOWN
    
        // TODO: 2022-03-06 Stop Polybot
    
        state = PolyBot.State.SHUTDOWN
    }
    
    public override fun configDirectory(base: String, vararg subpaths: String): Path = directory(".config", base, *subpaths)
    
    public override fun directory(base: String, vararg subpaths: String): Path {
        TODO("Not yet implemented")
    }
    
    override fun guildReference(guildId: ULong): BackedSuspendingReference<ULong, PolyGuild> {
        TODO("Not yet implemented")
    }
    
    override fun guildAsync(guildId: ULong): Deferred<PolyGuild> {
        TODO("Not yet implemented")
    }
    
    override suspend fun guild(guildId: ULong): PolyGuild {
        TODO("Not yet implemented")
    }
    
    override fun roleReference(roleId: ULong): BackedSuspendingReference<ULong, PolyRole> {
        TODO("Not yet implemented")
    }
    
    override fun roleAsync(roleId: ULong): Deferred<PolyRole> {
        TODO("Not yet implemented")
    }
    
    override suspend fun role(roleId: ULong): PolyRole {
        TODO("Not yet implemented")
    }
    
    override fun userReference(userId: ULong): BackedSuspendingReference<ULong, PolyUser> {
        TODO("Not yet implemented")
    }
    
    override fun userAsync(userId: ULong): Deferred<PolyUser> {
        TODO("Not yet implemented")
    }
    
    override suspend fun user(userId: ULong): PolyUser {
        TODO("Not yet implemented")
    }
    
    override fun memberReference(guildId: ULong, userId: ULong): BackedSuspendingReference<Pair<ULong, ULong>, PolyMember> {
        TODO("Not yet implemented")
    }
    
    override fun memberAsync(guildId: ULong, userId: ULong): Deferred<PolyMember> {
        TODO("Not yet implemented")
    }
    
    override suspend fun member(guildId: ULong, userId: ULong): PolyMember {
        TODO("Not yet implemented")
    }
    
    override fun emoteReference(guildId: ULong, emoteId: ULong): BackedSuspendingReference<Pair<ULong, ULong>, PolyEmote> {
        TODO("Not yet implemented")
    }
    
    override fun emoteAsync(guildId: ULong, emoteId: ULong): Deferred<PolyEmote> {
        TODO("Not yet implemented")
    }
    
    override suspend fun emote(guildId: ULong, emoteId: ULong): PolyEmote {
        TODO("Not yet implemented")
    }
    
    override fun channelReference(guildId: ULong, channelId: ULong): BackedSuspendingReference<Pair<ULong, ULong>, PolyChannel> {
        TODO("Not yet implemented")
    }
    
    override fun channelAsync(guildId: ULong, channelId: ULong): Deferred<PolyChannel> {
        TODO("Not yet implemented")
    }
    
    override suspend fun channel(guildId: ULong, channelId: ULong): PolyChannel {
        TODO("Not yet implemented")
    }
    
    override fun categoryReference(guildId: ULong, categoryId: ULong): BackedSuspendingReference<Pair<ULong, ULong>, PolyCategory> {
        TODO("Not yet implemented")
    }
    
    override fun categoryAsync(guildId: ULong, categoryId: ULong): Deferred<PolyCategory> {
        TODO("Not yet implemented")
    }
    
    override suspend fun category(guildId: ULong, categoryId: ULong): PolyCategory {
        TODO("Not yet implemented")
    }
    
    override fun guildChannelReference(guildId: ULong,
                                       guildChannelId: ULong): BackedSuspendingReference<Pair<ULong, ULong>, PolyGuildChannel> {
        TODO("Not yet implemented")
    }
    
    override fun guildChannelAsync(guildId: ULong, guildChannelId: ULong): Deferred<PolyGuildChannel> {
        TODO("Not yet implemented")
    }
    
    override suspend fun guildChannel(guildId: ULong, guildChannelId: ULong): PolyGuildChannel {
        TODO("Not yet implemented")
    }
    
    override fun messageChannelReference(guildId: ULong,
                                         messageChannelId: ULong): BackedSuspendingReference<Pair<ULong, ULong>, PolyMessageChannel> {
        TODO("Not yet implemented")
    }
    
    override fun messageChannelAsync(guildId: ULong, messageChannelId: ULong): Deferred<PolyMessageChannel> {
        TODO("Not yet implemented")
    }
    
    override suspend fun messageChannel(guildId: ULong, messageChannelId: ULong): PolyMessageChannel {
        TODO("Not yet implemented")
    }
    
    override fun textChannelReference(guildId: ULong,
                                      textChannelId: ULong): BackedSuspendingReference<Pair<ULong, ULong>, PolyTextChannel> {
        TODO("Not yet implemented")
    }
    
    override fun textChannelAsync(guildId: ULong, textChannelId: ULong): Deferred<PolyTextChannel> {
        TODO("Not yet implemented")
    }
    
    override suspend fun textChannel(guildId: ULong, textChannelId: ULong): PolyTextChannel {
        TODO("Not yet implemented")
    }
    
    override fun voiceChannelReference(guildId: ULong,
                                       voiceChannelId: ULong): BackedSuspendingReference<Pair<ULong, ULong>, PolyVoiceChannel> {
        TODO("Not yet implemented")
    }
    
    override fun voiceChannelAsync(guildId: ULong, voiceChannelId: ULong): Deferred<PolyVoiceChannel> {
        TODO("Not yet implemented")
    }
    
    override suspend fun voiceChannel(guildId: ULong, voiceChannelId: ULong): PolyVoiceChannel {
        TODO("Not yet implemented")
    }
    
    override fun poly(jdaEntity: Member): PolyMember = PolyMemberImpl(this, jdaEntity)
    
    override fun poly(jdaEntity: User): PolyUser = PolyUserImpl(this, jdaEntity)
    
    override fun poly(jdaEntity: Guild): PolyGuild = PolyGuildImpl(this, jdaEntity)
    
    override fun poly(jdaEntity: Message): PolyMessage = PolyMessageImpl(this, jdaEntity)
    
    override fun poly(jdaEntity: Role): PolyRole = PolyRoleImpl(this, jdaEntity)
    
    override fun poly(jdaEntity: AbstractChannel): PolyChannel {
        return when (jdaEntity) {
            is PrivateChannel -> PolyPrivateChannelImpl(this, jdaEntity)
            is VoiceChannel   -> PolyVoiceChannelImpl(this, jdaEntity)
            is TextChannel    -> PolyTextChannelImpl(this, jdaEntity)
            is Category       -> PolyCategoryImpl(this, jdaEntity)
            is MessageChannel -> PolyMessageChannelImpl(this, jdaEntity)
            is GuildChannel   -> PolyGuildChannelImpl(this, jdaEntity)
            else              -> PolyChannelImpl(this, jdaEntity)
        }
    }
    
    override fun poly(jdaEntity: GuildChannel): PolyGuildChannel {
        return when (jdaEntity) {
            is VoiceChannel -> PolyVoiceChannelImpl(this, jdaEntity)
            is TextChannel  -> PolyTextChannelImpl(this, jdaEntity)
            is Category     -> PolyCategoryImpl(this, jdaEntity)
            else            -> PolyGuildChannelImpl(this, jdaEntity)
        }
    }
    
    override fun poly(jdaEntity: MessageChannel): PolyMessageChannel {
        return when (jdaEntity) {
            is PrivateChannel -> PolyPrivateChannelImpl(this, jdaEntity)
            is TextChannel    -> PolyTextChannelImpl(this, jdaEntity)
            else              -> PolyMessageChannelImpl(this, jdaEntity)
        }
    }
    
    override fun poly(jdaEntity: PrivateChannel): PolyPrivateChannel = PolyPrivateChannelImpl(this, jdaEntity)
    
    override fun poly(jdaEntity: Category): PolyCategory {
        TODO("Not yet implemented")
    }
    
    override fun poly(jdaEntity: TextChannel): PolyTextChannel {
        TODO("Not yet implemented")
    }
    
    override fun poly(jdaEntity: VoiceChannel): PolyVoiceChannel {
        TODO("Not yet implemented")
    }
    
    override fun poly(jdaEntity: MessageEmbed): PolyMessageEmbed {
        TODO("Not yet implemented")
    }
    
    override fun poly(jdaEntity: Emote): PolyEmote {
        TODO("Not yet implemented")
    }
    
    override fun poly(jdaEntity: IMentionable): PolyMentionable {
        TODO("Not yet implemented")
    }
    
    override fun poly(jdaEntity: IPermissionHolder): PolyPermissionHolder {
        TODO("Not yet implemented")
    }
}