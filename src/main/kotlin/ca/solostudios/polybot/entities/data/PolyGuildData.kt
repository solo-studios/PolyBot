/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyGuildData.kt is part of PolyhedralBot
 * Last modified on 20-10-2021 12:14 p.m.
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

package ca.solostudios.polybot.entities.data

import ca.solostudios.polybot.PolyBot
import ca.solostudios.polybot.entities.PolyTextChannel

@Suppress("unused")
data class PolyGuildData(
        val bot: PolyBot,
        val guildId: Long,
        val tags: MutableList<PolyTagData>,
        var loggingChannelId: Long,
        var mutedRoleId: Long,
        var autoRoleId: Long,
        var prefix: String?,
        var autoDehoist: Boolean,
        var filterInvites: Boolean,
                        ) {
    val autoRoleEnabled: Boolean
        get() = autoRoleId != -1L
    
    val hasPrefix: Boolean
        get() = prefix != null
    
    val loggingChannel: PolyTextChannel?
        get() = bot.polyTextChannel(loggingChannelId)
    
    val mutedRole
        get() = bot.polyRole(mutedRoleId)
    
    val autoRole
        get() = bot.polyRole(autoRoleId)
}