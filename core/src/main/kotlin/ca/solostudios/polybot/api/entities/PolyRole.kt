/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyRole.kt is part of PolyhedralBot
 * Last modified on 26-01-2022 01:04 p.m.
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

package ca.solostudios.polybot.api.entities

import java.awt.Color
import net.dv8tion.jda.api.entities.Role

public interface PolyRole : Mentionable, PolySnowflakeEntity {
    public val jdaRole: Role
    
    public val name: String
    
    public val managed: Boolean
    
    public val hoisted: Boolean
    
    public val mentionable: Boolean
    
    public val color: Color?
    
    public val publicRole: Boolean
    
    public val guild: PolyGuild
    
    public val bot: Boolean
    
    public val botId: ULong
    
    public val boost: Boolean
    
    public val integration: Boolean
    
    public val integrationId: ULong
    
    public fun canInteract(role: Role): Boolean
    
    public suspend fun delete()
}