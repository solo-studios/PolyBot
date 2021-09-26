/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file jda.kt is part of PolyhedralBot
 * Last modified on 25-09-2021 07:30 p.m.
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

import com.solostudios.polybot.PolyBot
import com.solostudios.polybot.entities.PolyAbstractChannel
import com.solostudios.polybot.entities.PolyGuild
import com.solostudios.polybot.entities.PolyMember
import com.solostudios.polybot.entities.PolyMessage
import com.solostudios.polybot.entities.PolyMessageChannel
import com.solostudios.polybot.entities.PolyRole
import com.solostudios.polybot.entities.PolyTextChannel
import com.solostudios.polybot.entities.PolyUser
import com.solostudios.polybot.entities.PolyVoiceChannel
import java.time.Instant
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.AbstractChannel
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

infix fun MemberCachePolicy.or(policy: MemberCachePolicy): MemberCachePolicy {
    return policy.or(policy)
}

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