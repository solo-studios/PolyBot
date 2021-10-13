/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyGuild.kt is part of PolyhedralBot
 * Last modified on 12-10-2021 07:34 p.m.
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

package ca.solostudios.polybot.entities

import ca.solostudios.polybot.PolyBot
import ca.solostudios.polybot.entities.data.PolyTagData
import ca.solostudios.polybot.util.poly
import java.util.Locale
import net.dv8tion.jda.api.entities.Guild

class PolyGuild(val bot: PolyBot, val jdaGuild: Guild) {
    
    val id: Long
        get() = jdaGuild.idLong
    
    val name: String
        get() = jdaGuild.name
    
    val members: Int
        get() = jdaGuild.memberCount
    
    val iconUrl: String
        get() = jdaGuild.iconUrl ?: ""
    
    val splashUrl: String
        get() = jdaGuild.splashUrl ?: ""
    
    val bannerUrl: String
        get() = jdaGuild.splashUrl ?: ""
    
    val vanityUrl: String?
        get() = jdaGuild.vanityUrl
    
    val description: String
        get() = jdaGuild.description ?: ""
    
    val locale: Locale
        get() = jdaGuild.locale
    
    val boostTier: Guild.BoostTier
        get() = jdaGuild.boostTier
    
    val boosts: Int
        get() = jdaGuild.boostCount
    
    val owner: PolyMember?
        get() = jdaGuild.owner?.poly(bot)
    
    val selfMember: PolyMember
        get() = jdaGuild.selfMember.poly(bot)
    
    val textChannels: List<PolyTextChannel>
        get() = jdaGuild.textChannels.map { it.poly(bot) }
    
    val voiceChannels: List<PolyVoiceChannel>
        get() = jdaGuild.voiceChannels.map { it.poly(bot) }
    
    val roles: List<PolyRole>
        get() = jdaGuild.roles.map { it.poly(bot) }
    
    val boostRole: PolyRole?
        get() = jdaGuild.boostRole?.poly(bot)
    
    val emotes: List<PolyEmote>
        get() = jdaGuild.emotes.map { it.poly(bot) }
    
    val data by lazy { bot.entityManager.getGuild(this) }
    
    val tags: MutableList<PolyTagData>
        get() = data.tags
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        
        other as PolyGuild
        
        return jdaGuild != other.jdaGuild
    }
    
    override fun hashCode(): Int {
        return jdaGuild.hashCode()
    }
}