/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file util.kt is part of PolyhedralBot
 * Last modified on 04-08-2021 10:06 p.m.
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

package com.solostudios.polybot.util

import dev.minn.jda.ktx.InlineEmbed
import java.time.OffsetDateTime
import java.util.EnumSet
import net.dv8tion.jda.api.entities.AbstractChannel
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.User


inline fun <T> stringIfNotNull(fromObject: T?, transform: (T) -> String): String = if (fromObject != null) transform(fromObject) else ""

inline fun <reified T : Enum<T>> enumSetOf(vararg elems: T): EnumSet<T> = EnumSet.noneOf(T::class.java).apply { addAll(elems) }

fun InlineEmbed.idFooter(time: OffsetDateTime = OffsetDateTime.now(),
                         guild: Guild,
                         channel: AbstractChannel? = null,
                         user: User? = null,
                         message: Message? = null,
                         separator: String = " | ") {
    footer {
        name = buildString {
            if (user != null)
                append("Author: ").append(user.id).append(separator)
            if (message != null)
                append("Message: ").append(message.id).append(separator)
            if (channel != null)
                append("Channel: ").append(channel.id).append(separator)
            
            append("Guild: ")
            append(guild.id)
            
            footerDate(time)
        }
    }
}

fun InlineEmbed.idFooter(time: OffsetDateTime = OffsetDateTime.now(),
                         guild: Long,
                         channel: Long? = null,
                         message: Long? = null,
                         user: Long? = null,
                         separator: String = " | ") {
    footer {
        name = buildString {
            if (user != null)
                append("Author: ").append(user).append(separator)
            if (message != null)
                append("Message: ").append(message).append(separator)
            if (channel != null)
                append("Channel: ").append(channel).append(separator)
            
            append("Guild: ")
            append(guild)
            
            println(this.toString())
            
            footerDate(time)
            
        }
        println(name)
    }
}

// fun formatEmbedFooter(guild: Guild, channel: MessageChannel, message: Message, user: User, separator: String = "|") = ""
//
// fun formatEmbedFooter(guild: Guild, channel: MessageChannel, user: User, separator: String = "|") = ""
//
// fun formatEmbedFooter(guild: Guild, user: User, separator: String = "|") = ""
//
// fun formatEmbedFooter(guild: Guild, separator: String = "|") = ""