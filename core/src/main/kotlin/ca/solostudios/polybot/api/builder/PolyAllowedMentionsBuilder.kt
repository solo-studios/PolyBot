/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyAllowedMentionsBuilder.kt is part of PolyhedralBot
 * Last modified on 23-01-2022 04:43 p.m.
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

package ca.solostudios.polybot.api.builder

import ca.solostudios.polybot.api.entities.PolyRole
import ca.solostudios.polybot.api.entities.PolyUser
import net.dv8tion.jda.api.entities.Message

public interface PolyAllowedMentionsBuilder {
    public val roles: MutableSet<PolyRole>
    
    public val users: MutableSet<PolyUser>
    
    public val types: MutableSet<Message.MentionType>
    
    public var repliedUser: Boolean
    
    public fun user(user: PolyUser)
    
    public fun user(id: ULong)
    
    public fun role(role: PolyRole)
    
    public fun role(role: ULong)
    
    public fun clear()
}