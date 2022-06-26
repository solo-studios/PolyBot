/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyBot.kt is part of PolyBot
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

package ca.solostudios.polybot.api

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
import ca.solostudios.polybot.api.plugin.PolyPluginManager
import ca.solostudios.polybot.api.service.PolyServiceManager
import ca.solostudios.polybot.api.util.datastructures.BackedSuspendingReference
import ca.solostudios.polybot.common.ExitCodes
import com.uchuhimo.konf.Config
import java.nio.file.Path
import java.util.concurrent.ScheduledExecutorService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
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
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

/**
 * PolyBot interface.
 *
 * This is the core of the polybot api.
 * All polybot components can be accessed starting from this class.
 *
 * The bot class itself is a coroutine scope and can be used to launch coroutines.
 */
public interface PolyBot : CoroutineScope {
    /**
     * The state of PolyBot
     */
    public enum class State(
            /**
             * Any state where this is true indicates that the service is capable of performing actions outside its own class namespace.
             *
             * This includes, but is not limited to:
             * - reading/writing to any files
             * - sending HTTP requests
             * - manipulating objects outside its own namespace
             */
            public val active: Boolean,
                           ) {
        /**
         * The service is setting up supporting *internal* systems like thread pools, etc.
         *
         * It is **not** starting up the service.
         */
        INITIALIZING(false),
        
        /**
         * The service has set up all supporting systems and is ready to be started.
         *
         * It is dormant in this state.
         */
        INITIALIZED(false),
        
        /**
         * The service is starting up and is connecting to all required systems.
         */
        STARTING(true),
        
        /**
         * The service is actively running
         */
        RUNNING(true),
        
        /**
         * The service has begun the shutdown proces and is cleaning up all remaining processes/tasks.
         */
        SHUTTING_DOWN(true),
        
        /**
         * The service has shutdown successfully.
         *
         * It is dormant in this state.
         */
        SHUTDOWN(false),
        
        /**
         * The service failed to start successfully.
         *
         * It is dormant during this state.
         */
        FAILED(false)
    }
    
    /**
     * The state the service is currently in
     *
     * @see State
     */
    public val state: State
    
    /**
     * True if this service has been shutdown successfully. False otherwise.
     *
     * Corresponds to when `state` is `SHUTDOWN` or `FAILED`.
     *
     * @see state
     * @see State.SHUTDOWN
     * @see State.FAILED
     */
    public val shutdown: Boolean
    
    /**
     * True if this service is running. False otherwise.
     *
     * Corresponds to when `state` is `RUNNING`
     *
     * @see state
     * @see State.RUNNING
     */
    public val running: Boolean
    
    /**
     * True if this service is active. False otherwise.
     *
     * Corresponds to any state with [State.active]
     *
     * When a service is in an active state, this indicates that it is capable of performing actions outside its own class namespace.
     *
     * This includes, but is not limited to:
     * - reading/writing to any files
     * - sending HTTP requests
     * - manipulating objects outside its own namespace
     *
     * @see state
     * @see State.active
     */
    public val active: Boolean
    
    /**
     * The global bot Konf [Config] class.
     */
    public val config: Config
    
    /**
     * The global random instance for the bot.
     */
    public val globalRandom: Random
    
    /**
     * A global thread pool that can be used to schedule any tasks for the bot.
     */
    public val scheduledThreadPool: ScheduledExecutorService
    
    /**
     * The coroutine scope that is used as a delegate.
     */
    public val scope: CoroutineScope
    
    /**
     * The coroutine context that is used as a delegate.
     */
    public val context: CoroutineContext
    
    /**
     * JDA instance
     */
    public val jda: JDA
    
    /**
     * The discord id of the bot.
     */
    public val id: ULong
    
    /**
     * The poly event manager.
     *
     * This class is used to register to and listen for events.
     */
    public val eventManager: PolyEventManager
    
    /**
     * The poly service manager.
     *
     * Use this class to retrieve and interface with services directly.
     */
    public val serviceManager: PolyServiceManager
    
    /**
     * The plugin manager
     *
     * This class manages and handles the loading of all plugins
     */
    public val polyPluginManager: PolyPluginManager
    
    /**
     * The dependency injection instance
     */
    public val di: DI
    
    /**
     * Shutdown the running service and blocks until it is fully shutdown.
     *
     * Exceptions may be thrown during shutdown.
     *
     * @param exitCode The exit code to exit the JVM with.
     * @param isShutdownHook Whether this is invoked by a shutdown hook or not.
     */
    @Throws(Exception::class)
    public suspend fun shutdown(exitCode: Int = ExitCodes.EXIT_CODE_NORMAL, isShutdownHook: Boolean)
    
    /**
     * Starts PolyBot and blocks until it is fully started.
     *
     * Exceptions may be thrown during startup.
     *
     * Note: services may be restarted several times.
     */
    @Throws(Exception::class)
    public suspend fun start()
    
    public fun configDirectory(base: String, vararg subpaths: String): Path
    
    public fun directory(base: String, vararg subpaths: String): Path
    
    /**
     * Returns a backed reference for a guild.
     *
     * @param guildId The id of the guild.
     * @return The backed reference.
     */
    public fun guildReference(guildId: ULong): BackedSuspendingReference<ULong, PolyGuild>
    
    /**
     * Retrieves a guild asynchronously.
     *
     * @param guildId The id of the guild.
     * @return The deferred for the guild.
     */
    public fun guildAsync(guildId: ULong): Deferred<PolyGuild>
    
    /**
     * Retrieves a guild.
     *
     * @param guildId The id of the guild.
     * @return The guild.
     */
    public suspend fun guild(guildId: ULong): PolyGuild
    
    /**
     * Returns a backed reference for a role.
     *
     * @param roleId The id of the guild.
     * @return The backed reference.
     */
    public fun roleReference(roleId: ULong): BackedSuspendingReference<ULong, PolyRole>
    
    /**
     * Retrieves a role asynchronously.
     *
     * @param roleId The id of the role.
     * @return The deferred for the role.
     */
    public fun roleAsync(roleId: ULong): Deferred<PolyRole>
    
    /**
     * Retrieves a role.
     *
     * @param roleId The id of the role.
     * @return The role.
     */
    public suspend fun role(roleId: ULong): PolyRole
    
    /**
     * Returns a backed reference for a user.
     *
     * @param userId The id of the user.
     * @return The backed reference.
     */
    public fun userReference(userId: ULong): BackedSuspendingReference<ULong, PolyUser>
    
    /**
     * Retrieves a user asynchronously.
     *
     * @param userId The id of the user.
     * @return The deferred for the user.
     */
    public fun userAsync(userId: ULong): Deferred<PolyUser>
    
    /**
     * Retrieves a user.
     *
     * @param userId The id of the user.
     * @return The guild.
     */
    public suspend fun user(userId: ULong): PolyUser
    
    /**
     * Returns a backed reference for a member.
     *
     * @param guildId The id of the guild.
     * @param userId The id of the member.
     * @return The backed reference.
     */
    public fun memberReference(guildId: ULong, userId: ULong): BackedSuspendingReference<Pair<ULong, ULong>, PolyMember>
    
    /**
     * Retrieves a member asynchronously.
     *
     * @param guildId The id of the guild.
     * @param userId The id of the member.
     * @return The deferred for the member.
     */
    public fun memberAsync(guildId: ULong, userId: ULong): Deferred<PolyMember>
    
    /**
     * Retrieves a member.
     *
     * @param guildId The id of the guild.
     * @param userId The id of the user.
     * @return The member.
     */
    public suspend fun member(guildId: ULong, userId: ULong): PolyMember
    
    /**
     * Returns a backed reference for an emote.
     *
     * @param guildId The id of the guild.
     * @param emoteId The id of the emote.
     * @return The backed reference.
     */
    public fun emoteReference(guildId: ULong, emoteId: ULong): BackedSuspendingReference<Pair<ULong, ULong>, PolyEmote>
    
    /**
     * Retrieves an emote asynchronously.
     *
     * @param guildId The id of the guild.
     * @param emoteId The id of the emote.
     * @return The deferred for the emote.
     */
    public fun emoteAsync(guildId: ULong, emoteId: ULong): Deferred<PolyEmote>
    
    /**
     * Retrieves an emote.
     *
     * @param guildId The id of the guild.
     * @param emoteId The id of the emote.
     * @return The emote.
     */
    public suspend fun emote(guildId: ULong, emoteId: ULong): PolyEmote
    
    /**
     * Returns a backed reference for a channel.
     *
     * @param guildId The id of the guild.
     * @param channelId The id of the channel.
     * @return The backed reference.
     */
    public fun channelReference(guildId: ULong, channelId: ULong): BackedSuspendingReference<Pair<ULong, ULong>, PolyChannel>
    
    /**
     * Retrieves a channel asynchronously.
     *
     * @param guildId The id of the guild.
     * @param channelId The id of the channel.
     * @return The deferred for the channel.
     */
    public fun channelAsync(guildId: ULong, channelId: ULong): Deferred<PolyChannel>
    
    /**
     * Retrieves a channel.
     *
     * @param guildId The id of the guild.
     * @param channelId The id of the channel.
     * @return The channel.
     */
    public suspend fun channel(guildId: ULong, channelId: ULong): PolyChannel
    
    /**
     * Returns a backed reference for a category.
     *
     * @param guildId The id of the guild.
     * @param categoryId The id of the category.
     * @return The backed reference.
     */
    public fun categoryReference(guildId: ULong, categoryId: ULong): BackedSuspendingReference<Pair<ULong, ULong>, PolyCategory>
    
    /**
     * Retrieves a category asynchronously.
     *
     * @param guildId The id of the guild.
     * @param categoryId The id of the category.
     * @return The deferred for the category.
     */
    public fun categoryAsync(guildId: ULong, categoryId: ULong): Deferred<PolyCategory>
    
    /**
     * Retrieves a category.
     *
     * @param guildId The id of the guild.
     * @param categoryId The id of the category.
     * @return The category.
     */
    public suspend fun category(guildId: ULong, categoryId: ULong): PolyCategory
    
    /**
     * Returns a backed reference for a guild channel.
     *
     * @param guildId The id of the guild.
     * @param guildChannelId The id of the guild channel.
     * @return The backed reference.
     */
    public fun guildChannelReference(guildId: ULong, guildChannelId: ULong): BackedSuspendingReference<Pair<ULong, ULong>, PolyGuildChannel>
    
    /**
     * Retrieves a guild channel asynchronously.
     *
     * @param guildId The id of the guild.
     * @param guildChannelId The id of the guild channel.
     * @return The deferred for the guild channel.
     */
    public fun guildChannelAsync(guildId: ULong, guildChannelId: ULong): Deferred<PolyGuildChannel>
    
    /**
     * Retrieves a guild channel.
     *
     * @param guildId The id of the guild.
     * @param guildChannelId The id of the guild channel.
     * @return The guild channel.
     */
    public suspend fun guildChannel(guildId: ULong, guildChannelId: ULong): PolyGuildChannel
    
    /**
     * Returns a backed reference for a message channel.
     *
     * @param guildId The id of the guild.
     * @param messageChannelId The id of the message channel.
     * @return The backed reference.
     */
    public fun messageChannelReference(
            guildId: ULong,
            messageChannelId: ULong,
                                      ): BackedSuspendingReference<Pair<ULong, ULong>, PolyMessageChannel>
    
    /**
     * Retrieves a message channel asynchronously.
     *
     * @param guildId The id of the guild.
     * @param messageChannelId The id of the message channel.
     * @return The deferred for the message channel.
     */
    public fun messageChannelAsync(guildId: ULong, messageChannelId: ULong): Deferred<PolyMessageChannel>
    
    /**
     * Retrieves a message channel.
     *
     * @param guildId The id of the guild.
     * @param messageChannelId The id of the message channel.
     * @return The message channel.
     */
    public suspend fun messageChannel(guildId: ULong, messageChannelId: ULong): PolyMessageChannel
    
    /**
     * Returns a backed reference for a text channel.
     *
     * @param guildId The id of the guild.
     * @param textChannelId The id of the text channel.
     * @return The backed reference.
     */
    public fun textChannelReference(guildId: ULong, textChannelId: ULong): BackedSuspendingReference<Pair<ULong, ULong>, PolyTextChannel>
    
    /**
     * Retrieves a text channel asynchronously.
     *
     * @param guildId The id of the guild.
     * @param textChannelId The id of the text channel.
     * @return The deferred for the text channel.
     */
    public fun textChannelAsync(guildId: ULong, textChannelId: ULong): Deferred<PolyTextChannel>
    
    /**
     * Retrieves a text channel.
     *
     * @param guildId The id of the guild.
     * @param textChannelId The id of the text channel.
     * @return The text channel.
     */
    public suspend fun textChannel(guildId: ULong, textChannelId: ULong): PolyTextChannel
    
    /**
     * Returns a backed reference for a voice channel.
     *
     * @param guildId The id of the guild.
     * @param voiceChannelId The id of the voice channel.
     * @return The backed reference.
     */
    public fun voiceChannelReference(guildId: ULong, voiceChannelId: ULong): BackedSuspendingReference<Pair<ULong, ULong>, PolyVoiceChannel>
    
    /**
     * Retrieves a voice channel asynchronously.
     *
     * @param guildId The id of the guild.
     * @param voiceChannelId The id of the voice channel.
     * @return The deferred for the voice channel.
     */
    public fun voiceChannelAsync(guildId: ULong, voiceChannelId: ULong): Deferred<PolyVoiceChannel>
    
    /**
     * Retrieves a voice channel.
     *
     * @param guildId The id of the guild.
     * @param voiceChannelId The id of the voice channel.
     * @return The a voice channel.
     */
    public suspend fun voiceChannel(guildId: ULong, voiceChannelId: ULong): PolyVoiceChannel
    
    /**
     * Wrap the provided JDA entity in the respective polybot entity.
     *
     * @param jdaEntity The JDA entity to wrap.
     * @return The wrapped JDA entity.
     */
    public fun poly(jdaEntity: Member): PolyMember
    
    /**
     * Wrap the provided JDA entity in the respective polybot entity.
     *
     * @param jdaEntity The JDA entity to wrap.
     * @return The wrapped JDA entity.
     */
    public fun poly(jdaEntity: User): PolyUser
    
    /**
     * Wrap the provided JDA entity in the respective polybot entity.
     *
     * @param jdaEntity The JDA entity to wrap.
     * @return The wrapped JDA entity.
     */
    public fun poly(jdaEntity: Guild): PolyGuild
    
    /**
     * Wrap the provided JDA entity in the respective polybot entity.
     *
     * @param jdaEntity The JDA entity to wrap.
     * @return The wrapped JDA entity.
     */
    public fun poly(jdaEntity: Message): PolyMessage
    
    /**
     * Wrap the provided JDA entity in the respective polybot entity.
     *
     * @param jdaEntity The JDA entity to wrap.
     * @return The wrapped JDA entity.
     */
    public fun poly(jdaEntity: Role): PolyRole
    
    /**
     * Wrap the provided JDA entity in the respective polybot entity.
     *
     * @param jdaEntity The JDA entity to wrap.
     * @return The wrapped JDA entity.
     */
    public fun poly(jdaEntity: AbstractChannel): PolyChannel
    
    /**
     * Wrap the provided JDA entity in the respective polybot entity.
     *
     * @param jdaEntity The JDA entity to wrap.
     * @return The wrapped JDA entity.
     */
    public fun poly(jdaEntity: GuildChannel): PolyGuildChannel
    
    /**
     * Wrap the provided JDA entity in the respective polybot entity.
     *
     * @param jdaEntity The JDA entity to wrap.
     * @return The wrapped JDA entity.
     */
    public fun poly(jdaEntity: MessageChannel): PolyMessageChannel
    
    /**
     * Wrap the provided JDA entity in the respective polybot entity.
     *
     * @param jdaEntity The JDA entity to wrap.
     * @return The wrapped JDA entity.
     */
    public fun poly(jdaEntity: PrivateChannel): PolyPrivateChannel
    
    /**
     * Wrap the provided JDA entity in the respective polybot entity.
     *
     * @param jdaEntity The JDA entity to wrap.
     * @return The wrapped JDA entity.
     */
    public fun poly(jdaEntity: Category): PolyCategory
    
    /**
     * Wrap the provided JDA entity in the respective polybot entity.
     *
     * @param jdaEntity The JDA entity to wrap.
     * @return The wrapped JDA entity.
     */
    public fun poly(jdaEntity: TextChannel): PolyTextChannel
    
    /**
     * Wrap the provided JDA entity in the respective polybot entity.
     *
     * @param jdaEntity The JDA entity to wrap.
     * @return The wrapped JDA entity.
     */
    public fun poly(jdaEntity: VoiceChannel): PolyVoiceChannel
    
    /**
     * Wrap the provided JDA entity in the respective polybot entity.
     *
     * @param jdaEntity The JDA entity to wrap.
     * @return The wrapped JDA entity.
     */
    public fun poly(jdaEntity: MessageEmbed): PolyMessageEmbed
    
    /**
     * Wrap the provided JDA entity in the respective polybot entity.
     *
     * @param jdaEntity The JDA entity to wrap.
     * @return The wrapped JDA entity.
     */
    public fun poly(jdaEntity: Emote): PolyEmote
    
    /**
     * Wrap the provided JDA entity in the respective polybot entity.
     *
     * @param jdaEntity The JDA entity to wrap.
     * @return The wrapped JDA entity.
     */
    public fun poly(jdaEntity: IMentionable): PolyMentionable
    
    /**
     * Wrap the provided JDA entity in the respective polybot entity.
     *
     * @param jdaEntity The JDA entity to wrap.
     * @return The wrapped JDA entity.
     */
    public fun poly(jdaEntity: IPermissionHolder): PolyPermissionHolder
}