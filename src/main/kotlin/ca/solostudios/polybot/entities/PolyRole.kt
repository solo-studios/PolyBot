/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyRole.kt is part of PolyhedralBot
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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package ca.solostudios.polybot.entities

import ca.solostudios.polybot.PolyBot
import ca.solostudios.polybot.util.jda.poly
import java.awt.Color
import net.dv8tion.jda.api.entities.Role

class PolyRole(val bot: PolyBot, val jdaRole: Role) {
    val id: Long
        get() = jdaRole.idLong
    
    val name: String
        get() = jdaRole.name
    
    val isManaged: Boolean
        get() = jdaRole.isManaged
    
    val isHoisted: Boolean
        get() = jdaRole.isHoisted
    
    val isMentionable: Boolean
        get() = jdaRole.isMentionable
    
    val color: Color?
        get() = jdaRole.color
    
    val guild: PolyGuild
        get() = jdaRole.guild.poly(bot)
    
    val isBot: Boolean
        get() = jdaRole.tags.isBot
    
    val botId: Long?
        get() = if (jdaRole.tags.botIdLong == 0L) null else jdaRole.tags.botIdLong
    
    val isBoost: Boolean
        get() = jdaRole.tags.isBoost
    
    val isIntegration: Boolean
        get() = jdaRole.tags.isIntegration
    
    val integrationId: Long?
        get() = if (jdaRole.tags.integrationIdLong == 0L) null else jdaRole.tags.integrationIdLong
    
    override fun toString(): String = jdaRole.asMention
    
}