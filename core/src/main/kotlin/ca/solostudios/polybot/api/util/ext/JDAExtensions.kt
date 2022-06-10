/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file JDAExtensions.kt is part of PolyBot
 * Last modified on 10-06-2022 11:33 a.m.
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

@file:Suppress("FunctionName")

package ca.solostudios.polybot.api.util.ext

import ca.solostudios.polybot.api.jda.builder.InlineJDABuilder
import ca.solostudios.polybot.api.jda.builder.InlineWebhookEmbedBuilder
import ca.solostudios.polybot.api.jda.builder.InlineWebhookMessageBuilder
import club.minnced.discord.webhook.send.WebhookEmbed
import club.minnced.discord.webhook.send.WebhookEmbedBuilder
import club.minnced.discord.webhook.send.WebhookMessage
import club.minnced.discord.webhook.send.WebhookMessageBuilder
import java.time.Instant
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.managers.Presence
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy


/**
 *
 * Convenience Infix function for [MemberCachePolicy.or] to concatenate another policy.
 * This allows you to drop the brackets for cache policies declarations.
 *
 * This way you can do
 * ```kotlin
 * memberCachePolicy = MemberCachePolicy.ONLINE or MemberCachePolicy.VOICE
 * ```
 * instead of
 * ```kotlin
 * memberCachePolicy = MemberCachePolicy.ONLINE.or(MemberCachePolicy.VOICE)
 * ```
 *
 * @param policy The policy to concat
 * @return New policy which combines both using a logical OR
 * @see InlineJDABuilder.memberCachePolicy
 */
public infix fun MemberCachePolicy.or(policy: MemberCachePolicy): MemberCachePolicy {
    return policy.or(policy)
}

/**
 *
 * Convenience Infix function for [MemberCachePolicy.or] to require another policy.
 * This allows you to drop the brackets for cache policies declarations.
 *
 * This way you can do
 * ```kotlin
 * memberCachePolicy = MemberCachePolicy.ONLINE and MemberCachePolicy.VOICE
 * ```
 * instead of
 * ```kotlin
 * memberCachePolicy = MemberCachePolicy.ONLINE.and(MemberCachePolicy.VOICE)
 * ```
 *
 * @param policy The policy to require in addition to this one
 * @return New policy which combines both using a logical AND
 * @see InlineJDABuilder.memberCachePolicy
 */
public infix fun MemberCachePolicy.and(policy: MemberCachePolicy): MemberCachePolicy {
    return policy.and(policy)
}

public var Presence.onlineStatus: OnlineStatus
    set(value) {
        setStatus(value)
    }
    get() = status

public fun Instant.toDiscordTimestamp(timestampType: DiscordTimestampType = DiscordTimestampType.FULL): String {
    return "<t:${this.epochSecond}:${timestampType.format}>"
}

public enum class DiscordTimestampType(public val format: String) {
    RELATIVE("R"),
    DATE("D"),
    TIME("T"),
    FULL("F")
}


/**
 * Creates a new [JDA] instance with the default settings by using [JDABuilder.createDefault]
 *
 * @param token The token to log in with. If set to null, assign a value to [InlineJDABuilder.token].
 * @param builder The function used to configure the builder.
 * @return The new [JDA] instance.
 * @see JDABuilder.createDefault
 */
public inline fun DefaultJDA(
        token: String? = null,
        builder: InlineJDABuilder.() -> Unit
                            ): JDA = InlineJDABuilder(JDABuilder.createDefault(token)).also(builder).build()

/**
 * Creates a new [JDA] instance with the default settings by using [JDABuilder.createDefault]
 *
 * @param token The token to log in with. If set to null, assign a value to [InlineJDABuilder.token].
 * @param intents A list of intents to enable.
 * @param builder The function used to configure the builder.
 * @return The new [JDA] instance.
 * @see JDABuilder.createDefault
 */
public inline fun DefaultJDA(
        token: String? = null,
        vararg intents: GatewayIntent,
        builder: InlineJDABuilder.() -> Unit
                            ): JDA = InlineJDABuilder(JDABuilder.createDefault(token, intents.toList())).also(builder).build()

/**
 * Creates a new [JDABuilder] instance with the default settings by using [JDABuilder.createDefault]
 *
 * @param token The token to log in with. If set to null, assign a value to [InlineJDABuilder.token].
 * @param builder The function used to configure the builder.
 * @return The [JDABuilder] instance. You must call [JDABuilder.build] on this to finish constructing the JDA.
 * @see JDABuilder.createDefault
 */
public inline fun DefaultJDABuilder(
        token: String? = null,
        builder: InlineJDABuilder.() -> Unit
                                   ): InlineJDABuilder = InlineJDABuilder(JDABuilder.createDefault(token)).also(builder)

/**
 * Creates a new [JDABuilder] instance with the default settings by using [JDABuilder.createDefault]
 *
 * @param token The token to log in with. If set to null, assign a value to [InlineJDABuilder.token].
 * @param intents A list of intents to enable.
 * @param builder The function used to configure the builder.
 * @return The [JDABuilder] instance. You must call [JDABuilder.build] on this to finish constructing the JDA.
 * @see JDABuilder.createDefault
 */
public inline fun DefaultJDABuilder(
        token: String? = null,
        vararg intents: GatewayIntent,
        builder: InlineJDABuilder.() -> Unit
                                   ): InlineJDABuilder = InlineJDABuilder(JDABuilder.createDefault(token, intents.toList())).also(builder)

/**
 * Creates a new [JDA] instance with the default settings by using [JDABuilder.createLight]
 *
 * @param token The token to log in with. If set to null, assign a value to [InlineJDABuilder.token].
 * @param builder The function used to configure the builder.
 * @return The new [JDA] instance.
 * @see JDABuilder.createLight
 */
public inline fun LightJDA(
        token: String? = null,
        builder: InlineJDABuilder.() -> Unit
                          ): JDA = InlineJDABuilder(JDABuilder.createLight(token)).also(builder).build()

/**
 * Creates a new [JDA] instance with the default settings by using [JDABuilder.createLight]
 *
 * @param token The token to log in with. If set to null, assign a value to [InlineJDABuilder.token].
 * @param intents A list of intents to enable.
 * @param builder The function used to configure the builder.
 * @return The new [JDA] instance.
 * @see JDABuilder.createLight
 */
public inline fun LightJDA(
        token: String? = null,
        vararg intents: GatewayIntent,
        builder: InlineJDABuilder.() -> Unit
                          ): JDA = InlineJDABuilder(JDABuilder.createLight(token, intents.toList())).also(builder).build()

/**
 * Creates a new [JDABuilder] instance with the default settings by using [JDABuilder.createLight]
 *
 * @param token The token to log in with. If set to null, assign a value to [InlineJDABuilder.token].
 * @param builder The function used to configure the builder.
 * @return The [JDABuilder] instance. You must call [JDABuilder.build] on this to finish constructing the JDA.
 * @see JDABuilder.createLight
 */
public inline fun LightJDABuilder(
        token: String? = null,
        builder: InlineJDABuilder.() -> Unit
                                 ): InlineJDABuilder = InlineJDABuilder(JDABuilder.createLight(token)).also(builder)

/**
 * Creates a new [JDABuilder] instance with the default settings by using [JDABuilder.createLight]
 *
 * @param token The token to log in with. If set to null, assign a value to [InlineJDABuilder.token].
 * @param intents A list of intents to enable.
 * @param builder The function used to configure the builder.
 * @return The [JDABuilder] instance. You must call [JDABuilder.build] on this to finish constructing the JDA.
 * @see JDABuilder.createLight
 */
public inline fun LightJDABuilder(
        token: String? = null,
        vararg intents: GatewayIntent,
        builder: InlineJDABuilder.() -> Unit
                                 ): InlineJDABuilder = InlineJDABuilder(JDABuilder.createLight(token, intents.toList())).also(builder)

/**
 * Creates a new [JDA] instance with the default settings by using [JDABuilder.create]
 *
 * @param token The token to log in with. If set to null, assign a value to [InlineJDABuilder.token].
 * @param intents A list of intents to enable.
 * @param builder The function used to configure the builder.
 * @return The new [JDA] instance.
 * @see JDABuilder.create
 */
public inline fun JDA(
        token: String? = null,
        vararg intents: GatewayIntent,
        builder: InlineJDABuilder.() -> Unit
                     ): JDA = InlineJDABuilder(JDABuilder.create(token, intents.toList())).also(builder).build()

/**
 * Creates a new [JDABuilder] instance with the default settings by using [JDABuilder.create]
 *
 * @param token The token to log in with. If set to null, assign a value to [InlineJDABuilder.token].
 * @param intents A list of intents to enable.
 * @param builder The function used to configure the builder.
 * @return The [JDABuilder] instance. You must call [JDABuilder.build] on this to finish constructing the JDA.
 * @see JDABuilder.create
 */
public inline fun JDABuilder(
        token: String? = null,
        vararg intents: GatewayIntent,
        builder: InlineJDABuilder.() -> Unit
                            ): InlineJDABuilder = InlineJDABuilder(JDABuilder.create(token, intents.toList())).also(builder)


public inline fun WebhookMessage(builder: InlineWebhookMessageBuilder.() -> Unit = {}): WebhookMessage {
    return WebhookMessageBuilder().run {
        InlineWebhookMessageBuilder(this).builder()
        build()
    }
}

public inline fun WebhookMessageBuilder(builder: InlineWebhookMessageBuilder.() -> Unit = {}): InlineWebhookMessageBuilder {
    return WebhookMessageBuilder().run {
        InlineWebhookMessageBuilder(this).apply(builder)
    }
}

public inline fun WebhookEmbed(builder: InlineWebhookEmbedBuilder.() -> Unit = {}): WebhookEmbed {
    return WebhookEmbedBuilder().run {
        InlineWebhookEmbedBuilder(this).builder()
        build()
    }
}

public inline fun WebhookEmbedBuilder(builder: InlineWebhookEmbedBuilder.() -> Unit = {}): InlineWebhookEmbedBuilder {
    return WebhookEmbedBuilder().run {
        InlineWebhookEmbedBuilder(this).apply(builder)
    }
}