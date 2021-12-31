/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file jda.kt is part of PolyhedralBot
 * Last modified on 31-12-2021 01:29 p.m.
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

@file:Suppress("unused")

package ca.solostudios.polybot.util.jda

import ca.solostudios.polybot.PolyBot
import ca.solostudios.polybot.entities.PolyAbstractChannel
import ca.solostudios.polybot.entities.PolyEmote
import ca.solostudios.polybot.entities.PolyGuild
import ca.solostudios.polybot.entities.PolyMember
import ca.solostudios.polybot.entities.PolyMessage
import ca.solostudios.polybot.entities.PolyMessageChannel
import ca.solostudios.polybot.entities.PolyRole
import ca.solostudios.polybot.entities.PolyTextChannel
import ca.solostudios.polybot.entities.PolyUser
import ca.solostudios.polybot.entities.PolyVoiceChannel
import java.time.Instant
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.AbstractChannel
import net.dv8tion.jda.api.entities.Emote
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.VoiceChannel
import net.dv8tion.jda.api.managers.Presence
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
infix fun MemberCachePolicy.or(policy: MemberCachePolicy): MemberCachePolicy {
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
infix fun MemberCachePolicy.and(policy: MemberCachePolicy): MemberCachePolicy {
    return policy.and(policy)
}

var Presence.onlineStatus: OnlineStatus
    set(value) {
        setStatus(value)
    }
    get() = status

fun Instant.toDiscordTimestamp(timestampType: DiscordTimestampType = DiscordTimestampType.FULL): String {
    return "<t:${this.epochSecond}:${timestampType.format}>"
}

enum class DiscordTimestampType(val format: String) {
    RELATIVE("R"),
    DATE("D"),
    TIME("T"),
    FULL("F")
}

fun Member.poly(bot: PolyBot): PolyMember = PolyMember(bot, this)

fun User.poly(bot: PolyBot): PolyUser = PolyUser(bot, this)

fun Guild.poly(bot: PolyBot): PolyGuild = PolyGuild(bot, this)

fun Message.poly(bot: PolyBot): PolyMessage = PolyMessage(bot, this)

fun Role.poly(bot: PolyBot): PolyRole = PolyRole(bot, this)

fun AbstractChannel.poly(bot: PolyBot): PolyAbstractChannel = PolyAbstractChannel(bot, this)

fun MessageChannel.poly(bot: PolyBot): PolyMessageChannel = PolyMessageChannel(bot, this)

fun TextChannel.poly(bot: PolyBot): PolyTextChannel = PolyTextChannel(bot, this)

fun VoiceChannel.poly(bot: PolyBot): PolyVoiceChannel = PolyVoiceChannel(bot, this)

fun Emote.poly(bot: PolyBot): PolyEmote = PolyEmote(bot, this)