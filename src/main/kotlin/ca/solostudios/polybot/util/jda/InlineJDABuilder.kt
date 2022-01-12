/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file InlineJDABuilder.kt is part of PolyhedralBot
 * Last modified on 31-12-2021 01:32 p.m.
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

package ca.solostudios.polybot.util.jda

import ca.solostudios.polybot.util.datastructures.DelegatingCollection
import com.neovisionaries.ws.client.WebSocketFactory
import dev.minn.jda.ktx.injectKTX
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.ScheduledExecutorService
import net.dv8tion.jda.api.GatewayEncoding
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.audio.factory.IAudioSendFactory
import net.dv8tion.jda.api.audio.factory.IAudioSendSystem
import net.dv8tion.jda.api.audio.factory.IPacketProvider
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.hooks.IEventManager
import net.dv8tion.jda.api.hooks.VoiceDispatchInterceptor
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.Compression
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.SessionController
import net.dv8tion.jda.api.utils.cache.CacheFlag
import okhttp3.OkHttpClient

/**
 * Makes construction of a [JDA] much more idiomatic and cleaner to do in kotlin.
 *
 * @property builder The delegate [JDABuilder].
 * @constructor Create an inline shard manager builder to make construction of the [JDA] more idiomatic in kotlin.
 */
class InlineJDABuilder(val builder: JDABuilder) {
    var gatewayEncoding: GatewayEncoding? = null
        set(value) {
            if (value != null) {
                builder.setGatewayEncoding(value)
            }
            field = value
        }
    
    var rawEvents: Boolean = false
        set(value) {
            builder.setRawEventsEnabled(value)
            field = value
        }
    
    var relativeRateLimit: Boolean = true
        set(value) {
            builder.setRelativeRateLimit(value)
            field = value
        }
    
    var enableCache: Collection<CacheFlag> = emptySet()
        set(value) {
            builder.enableCache(value)
            field = value
        }
    
    var disableCache: Collection<CacheFlag> = emptySet()
        set(value) {
            builder.disableCache(value)
            field = value
        }
    
    var memberCachePolicy: MemberCachePolicy? = null
        set(value) {
            builder.setMemberCachePolicy(value)
            field = value
        }
    
    var contextMap: ConcurrentMap<String, String>? = null
        set(value) {
            builder.setContextMap(value)
            field = value
        }
    
    var context: Boolean = true
        set(value) {
            builder.setContextEnabled(value)
            field = value
        }
    
    var compression: Compression? = null
        set(value) {
            if (value != null) {
                builder.setCompression(value)
            }
            field = value
        }
    
    var requestTimeoutRetry: Boolean = true
        set(value) {
            builder.setRequestTimeoutRetry(value)
            field = value
        }
    
    var token: String? = null
        set(value) {
            builder.setToken(value)
            field = value
        }
    
    var httpClientBuilder: OkHttpClient.Builder? = null
        set(value) {
            builder.setHttpClientBuilder(value)
            field = value
        }
    
    var httpClient: OkHttpClient? = null
        set(value) {
            builder.setHttpClient(value)
            field = value
        }
    
    var webSocketFactory: WebSocketFactory? = null
        set(value) {
            builder.setWebsocketFactory(value)
            field = value
        }
    
    var rateLimitPool: ScheduledExecutorService? = null
        set(value) {
            builder.setRateLimitPool(value, true)
            field = value
        }
    
    var gatewayPool: ScheduledExecutorService? = null
        set(value) {
            builder.setGatewayPool(value, true)
            field = value
        }
    
    var callbackPool: ExecutorService? = null
        set(value) {
            builder.setCallbackPool(value, true)
            field = value
        }
    
    var eventPool: ExecutorService? = null
        set(value) {
            builder.setEventPool(value, true)
            field = value
        }
    
    var audioPool: ScheduledExecutorService? = null
        set(value) {
            builder.setAudioPool(value, true)
            field = value
        }
    
    var bulkDeleteSplitting: Boolean = true
        set(value) {
            builder.setBulkDeleteSplittingEnabled(value)
            field = value
        }
    
    var enableShutdownHook: Boolean = true
        set(value) {
            builder.setEnableShutdownHook(value)
            field = value
        }
    
    var autoReconnect: Boolean = true
        set(value) {
            builder.setAutoReconnect(value)
            field = value
        }
    
    var eventManager: IEventManager? = null
        set(value) {
            builder.setEventManager(value)
            field = value
        }
    
    var audioSendFactory: IAudioSendFactory? = null
        set(value) {
            builder.setAudioSendFactory(value)
            field = value
        }
    
    /**
     * Allows an audio send factory to be set via a lambda.
     *
     * eg:
     * ```kotlin
     * audioSendFactory {
     *     DefaultSendSystem(it)
     * }
     * ```
     */
    fun audioSendFactory(factory: (IPacketProvider) -> IAudioSendSystem) {
        audioSendFactory = IAudioSendFactory(factory)
    }
    
    var idle: Boolean = false
        set(value) {
            builder.setIdle(value)
            field = value
        }
    
    var activity: Activity? = null
        set(value) {
            builder.setActivity(value)
            field = value
        }
    
    var status: OnlineStatus? = null
        set(value) {
            if (value != null) {
                builder.setStatus(value)
            }
            field = value
        }
    
    val eventListeners: MutableCollection<Any> = DelegatingCollection(
            adder = { item -> builder.addEventListeners(item) },
            remover = { item -> builder.removeEventListeners(item) },
                                                                     )
    
    var maxReconnectDelay: Int = 900
        set(value) {
            builder.setMaxReconnectDelay(value)
            field = value
        }
    
    fun sharding(shardId: Int, shardTotal: Int) = builder.useSharding(shardId, shardTotal)
    
    var sessionController: SessionController? = null
        set(value) {
            builder.setSessionController(value)
            field = value
        }
    
    var voiceDispatchInterceptor: VoiceDispatchInterceptor? = null
        set(value) {
            builder.setVoiceDispatchInterceptor(value)
            field = value
        }
    
    var chunkingFilter: ChunkingFilter? = null
        set(value) {
            builder.setChunkingFilter(value)
            field = value
        }
    
    /**
     * Sets the [chunking filter][DefaultShardManagerBuilder.setChunkingFilter] for all guilds.
     *
     * eg:
     * ```kotlin
     * chunkingFilter {
     *     return (it % 2) == 0L // only chunk guilds with an id % 2 = 0 (why tho)
     * }
     * ```
     *
     * @param filter The chunking filter
     */
    fun chunkingFilter(filter: (Long) -> Boolean) {
        chunkingFilter = ChunkingFilter(filter)
    }
    
    /**
     * Sets the [chunking filter][DefaultShardManagerBuilder.setChunkingFilter] for all guilds.
     *
     * @param ids The ids to filter for
     * @param include Whether to include or exclude these guilds for chunking
     * @see ChunkingFilter.include
     * @see ChunkingFilter.exclude
     */
    fun chunkingFilterByIds(include: Boolean = true, vararg ids: Long) {
        chunkingFilterByIds(include, ids.asList())
    }
    
    /**
     * Sets the [chunking filter][DefaultShardManagerBuilder.setChunkingFilter] for all guilds.
     *
     * @param ids The ids to filter for
     * @param include Whether to include or exclude these guilds for chunking
     * @see ChunkingFilter.include
     * @see ChunkingFilter.exclude
     */
    fun chunkingFilterByIds(include: Boolean = true, ids: Collection<Long>) {
        chunkingFilter {
            for (id in ids)
                return@chunkingFilter include
            return@chunkingFilter !include
        }
    }
    
    var enableIntents: Collection<GatewayIntent> = emptySet()
        set(value) {
            builder.enableIntents(value)
            field = value
        }
    
    var disableIntents: Collection<GatewayIntent> = emptySet()
        set(value) {
            builder.disableIntents(value)
            field = value
        }
    
    var largeThreshold: Int = 250
        set(value) {
            builder.setLargeThreshold(value)
            field = value
        }
    
    var maxBufferSize: Int = 2048
        set(value) {
            builder.setMaxBufferSize(value)
            field = value
        }
    
    var injectKtx: Boolean = true
    
    fun build(): JDA {
        if (injectKtx) {
            builder.injectKTX()
        }
        
        return builder.build()
    }
}