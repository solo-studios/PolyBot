/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyBot.kt is part of PolyhedralBot
 * Last modified on 10-06-2021 02:24 p.m.
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

import cloud.commandframework.annotations.AnnotationAccessor
import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.context.CommandContext
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator
import cloud.commandframework.jda.JDA4CommandManager
import cloud.commandframework.jda.JDACommandSender
import cloud.commandframework.jda.JDAGuildSender
import cloud.commandframework.jda.JDAPrivateSender
import cloud.commandframework.meta.SimpleCommandMeta
import com.solostudios.polybot.commands.UtilCommands
import com.solostudios.polybot.event.GuildMessageEvent
import com.solostudios.polybot.event.MessageEvent
import com.solostudios.polybot.event.PrivateMessageEvent
import com.solostudios.polybot.logging.LoggingListener
import java.util.function.Function
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.Compression
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag
import net.dv8tion.jda.jda

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class PolyBot(token: String) {
    
    //    private val commandClient: CommandClient = CommandClientBuilder()
    //        .setActivity(Activity.streaming("Terra", "https://github.com/PolyhedralDev/Terra"))
    //        .setStatus(OnlineStatus.ONLINE)
    //        .setPrefix("$")
    //        .setAlternativePrefix("poly ")
    //        .setOwnerId("195735703726981120")
    //        .setCoOwnerIds("378350362236682240")
    //        .useHelpBuilder(true)
    //        .setHelpConsumer(HelpConsumer()) // todo
    //        .setHelpWord("help")
    //        .setEmojis(null, ":warning:", ":no_entry_sign:")
    //        .addAnnotatedModules(TestCommand(), UtilCommands())
    //        .setGuildSettingsManager(null)
    //        .build()
    
    val jda: JDA = jda(token) {
        disableCache(CacheFlag.VOICE_STATE,
                     CacheFlag.ACTIVITY,
                     CacheFlag.CLIENT_STATUS,
                     CacheFlag.ROLE_TAGS)
        disableIntents(GatewayIntent.DIRECT_MESSAGE_TYPING,
                       GatewayIntent.GUILD_MESSAGE_TYPING,
                       GatewayIntent.GUILD_VOICE_STATES,
                       GatewayIntent.GUILD_PRESENCES)
        
        setMemberCachePolicy(MemberCachePolicy.ONLINE)
        setChunkingFilter(ChunkingFilter.NONE)
        setCompression(Compression.ZLIB)
        setLargeThreshold(50)
        
        setRawEventsEnabled(false)
        setEnableShutdownHook(true)
        setBulkDeleteSplittingEnabled(false)
        
        //        addEventListeners(commandClient)
        
        addEventListeners(LoggingListener())
    }
    
    private val commandManager: JDA4CommandManager<MessageEvent> = JDA4CommandManager(jda,
                                                                                      { event: MessageEvent -> "!" },
                                                                                      { event: MessageEvent, command: String -> true },
                                                                                      AsynchronousCommandExecutionCoordinator.newBuilder<MessageEvent>()
                                                                                          .withAsynchronousParsing()
                                                                                          .build(),
                                                                                      createPolySenderMapper(),
                                                                                      { event -> JDACommandSender.of(event.event) })
    
    private val annotationParser = AnnotationParser(commandManager, MessageEvent::class.java) { parameters -> SimpleCommandMeta.empty() }
    
    init {
        annotationParser.parse(UtilCommands())
        annotationParser.parameterInjectorRegistry.registerInjector(Message::class.java) { context: CommandContext<MessageEvent>, annotations: AnnotationAccessor ->
            context.get<Message>("Message")
        }
        commandManager.registerCommandPreProcessor(JDAMessageCommandPreprocessor(commandManager))
        PolyExceptionHandler(commandManager)
    }
    
    private fun createPolySenderMapper() = Function<JDACommandSender, MessageEvent> { sender: JDACommandSender ->
        val event = sender.event.get()
        return@Function when (sender.javaClass) {
            JDAGuildSender::class.java   -> GuildMessageEvent(event, (sender as JDAGuildSender).member, sender.textChannel)
            JDAPrivateSender::class.java -> PrivateMessageEvent(event, sender.user, (sender as JDAPrivateSender).privateChannel)
            JDACommandSender::class.java -> throw UnsupportedOperationException("Why is this triggering for a webhook...")
            else                         -> throw UnsupportedOperationException()
        }
    }
}

