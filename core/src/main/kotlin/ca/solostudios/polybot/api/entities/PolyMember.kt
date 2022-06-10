/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyMember.kt is part of PolyBot
 * Last modified on 10-06-2022 11:32 a.m.
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

package ca.solostudios.polybot.api.entities

import java.awt.Color
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import net.dv8tion.jda.api.entities.Member

public interface PolyMember : PolyPermissionHolder, PolyUser {
    /**
     * The JDA member that is being wrapped by this entity
     */
    public val jdaMember: Member
    
    public override val guild: PolyGuild
    
    public override val guildId: ULong
    
    public val timeJoined: Instant
    
    public val timeBoosted: Instant?
    
    public val nickname: String?
    
    public val hasNickname: Boolean
    
    public val effectiveName: String
    
    public val roles: Flow<PolyRole>
    
    public val color: Color?
    
    public val isGuildOwner: Boolean
    
    public suspend fun changeNickname(nickname: String?)
}