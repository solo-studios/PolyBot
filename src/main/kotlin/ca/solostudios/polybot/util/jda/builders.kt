/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file builders.kt is part of PolyhedralBot
 * Last modified on 31-12-2021 01:34 p.m.
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

@file:Suppress("MemberVisibilityCanBePrivate", "unused", "FunctionName")

package ca.solostudios.polybot.util.jda

import club.minnced.discord.webhook.send.WebhookMessage
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent


/**
 * Creates a new [JDA] instance with the default settings by using [JDABuilder.createDefault]
 *
 * @param token The token to log in with. If set to null, assign a value to [InlineJDABuilder.token].
 * @param builder The function used to configure the builder.
 * @return The new [JDA] instance.
 * @see JDABuilder.createDefault
 */
inline fun DefaultJDA(
        token: String? = null,
        builder: InlineJDABuilder.() -> Unit
                     ) = InlineJDABuilder(JDABuilder.createDefault(token)).also(builder).build()

/**
 * Creates a new [JDA] instance with the default settings by using [JDABuilder.createDefault]
 *
 * @param token The token to log in with. If set to null, assign a value to [InlineJDABuilder.token].
 * @param intents A list of intents to enable.
 * @param builder The function used to configure the builder.
 * @return The new [JDA] instance.
 * @see JDABuilder.createDefault
 */
inline fun DefaultJDA(
        token: String? = null,
        vararg intents: GatewayIntent,
        builder: InlineJDABuilder.() -> Unit
                     ) = InlineJDABuilder(JDABuilder.createDefault(token, intents.toList())).also(builder).build()

/**
 * Creates a new [JDABuilder] instance with the default settings by using [JDABuilder.createDefault]
 *
 * @param token The token to log in with. If set to null, assign a value to [InlineJDABuilder.token].
 * @param builder The function used to configure the builder.
 * @return The [JDABuilder] instance. You must call [JDABuilder.build] on this to finish constructing the JDA.
 * @see JDABuilder.createDefault
 */
inline fun DefaultJDABuilder(
        token: String? = null,
        builder: InlineJDABuilder.() -> Unit
                            ) = InlineJDABuilder(JDABuilder.createDefault(token)).also(builder)

/**
 * Creates a new [JDABuilder] instance with the default settings by using [JDABuilder.createDefault]
 *
 * @param token The token to log in with. If set to null, assign a value to [InlineJDABuilder.token].
 * @param intents A list of intents to enable.
 * @param builder The function used to configure the builder.
 * @return The [JDABuilder] instance. You must call [JDABuilder.build] on this to finish constructing the JDA.
 * @see JDABuilder.createDefault
 */
inline fun DefaultJDABuilder(
        token: String? = null,
        vararg intents: GatewayIntent,
        builder: InlineJDABuilder.() -> Unit
                            ) = InlineJDABuilder(JDABuilder.createDefault(token, intents.toList())).also(builder)

/**
 * Creates a new [JDA] instance with the default settings by using [JDABuilder.createLight]
 *
 * @param token The token to log in with. If set to null, assign a value to [InlineJDABuilder.token].
 * @param builder The function used to configure the builder.
 * @return The new [JDA] instance.
 * @see JDABuilder.createLight
 */
inline fun LightJDA(
        token: String? = null,
        builder: InlineJDABuilder.() -> Unit
                   ) = InlineJDABuilder(JDABuilder.createLight(token)).also(builder).build()

/**
 * Creates a new [JDA] instance with the default settings by using [JDABuilder.createLight]
 *
 * @param token The token to log in with. If set to null, assign a value to [InlineJDABuilder.token].
 * @param intents A list of intents to enable.
 * @param builder The function used to configure the builder.
 * @return The new [JDA] instance.
 * @see JDABuilder.createLight
 */
inline fun LightJDA(
        token: String? = null,
        vararg intents: GatewayIntent,
        builder: InlineJDABuilder.() -> Unit
                   ) = InlineJDABuilder(JDABuilder.createLight(token, intents.toList())).also(builder).build()

/**
 * Creates a new [JDABuilder] instance with the default settings by using [JDABuilder.createLight]
 *
 * @param token The token to log in with. If set to null, assign a value to [InlineJDABuilder.token].
 * @param builder The function used to configure the builder.
 * @return The [JDABuilder] instance. You must call [JDABuilder.build] on this to finish constructing the JDA.
 * @see JDABuilder.createLight
 */
inline fun LightJDABuilder(
        token: String? = null,
        builder: InlineJDABuilder.() -> Unit
                          ) = InlineJDABuilder(JDABuilder.createLight(token)).also(builder)

/**
 * Creates a new [JDABuilder] instance with the default settings by using [JDABuilder.createLight]
 *
 * @param token The token to log in with. If set to null, assign a value to [InlineJDABuilder.token].
 * @param intents A list of intents to enable.
 * @param builder The function used to configure the builder.
 * @return The [JDABuilder] instance. You must call [JDABuilder.build] on this to finish constructing the JDA.
 * @see JDABuilder.createLight
 */
inline fun LightJDABuilder(
        token: String? = null,
        vararg intents: GatewayIntent,
        builder: InlineJDABuilder.() -> Unit
                          ) = InlineJDABuilder(JDABuilder.createLight(token, intents.toList())).also(builder)

/**
 * Creates a new [JDA] instance with the default settings by using [JDABuilder.create]
 *
 * @param token The token to log in with. If set to null, assign a value to [InlineJDABuilder.token].
 * @param intents A list of intents to enable.
 * @param builder The function used to configure the builder.
 * @return The new [JDA] instance.
 * @see JDABuilder.create
 */
inline fun JDA(
        token: String? = null,
        vararg intents: GatewayIntent,
        builder: InlineJDABuilder.() -> Unit
              ) = InlineJDABuilder(JDABuilder.create(token, intents.toList())).also(builder).build()

/**
 * Creates a new [JDABuilder] instance with the default settings by using [JDABuilder.create]
 *
 * @param token The token to log in with. If set to null, assign a value to [InlineJDABuilder.token].
 * @param intents A list of intents to enable.
 * @param builder The function used to configure the builder.
 * @return The [JDABuilder] instance. You must call [JDABuilder.build] on this to finish constructing the JDA.
 * @see JDABuilder.create
 */
inline fun JDABuilder(
        token: String? = null,
        vararg intents: GatewayIntent,
        builder: InlineJDABuilder.() -> Unit
                     ) = InlineJDABuilder(JDABuilder.create(token, intents.toList())).also(builder)


inline fun WebhookMessage(builder: InlineWebhookMessage.() -> Unit = {}): WebhookMessage {
    return club.minnced.discord.webhook.send.WebhookMessageBuilder().run {
        InlineWebhookMessage(this).builder()
        build()
    }
}

inline fun WebhookMessageBuilder(builder: InlineWebhookMessage.() -> Unit = {}): InlineWebhookMessage {
    return club.minnced.discord.webhook.send.WebhookMessageBuilder().run {
        InlineWebhookMessage(this).apply(builder)
    }
}