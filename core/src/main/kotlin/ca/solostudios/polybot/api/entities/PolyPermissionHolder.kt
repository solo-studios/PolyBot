/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyPermissionHolder.kt is part of PolyhedralBot
 * Last modified on 08-02-2022 12:28 a.m.
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

import java.util.EnumSet
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.IPermissionHolder

public interface PolyPermissionHolder : PolySnowflakeEntity {
    /**
     * The JDA permission holder that is being wrapped by this entity
     */
    public val jdaPermissionHolder: IPermissionHolder
    
    public val guild: PolyGuild
    
    public val guildId: ULong
    
    public val permissions: EnumSet<Permission>
    
    public val explicitPermissions: EnumSet<Permission>
    
    public fun getPermissions(channel: PolyGuildChannel): EnumSet<Permission>
    
    public fun getExplicitPermissions(channel: PolyGuildChannel): EnumSet<Permission>
    
    public operator fun contains(permission: Permission): Boolean
    
    public fun containsAll(permissions: Collection<Permission>): Boolean
    
    public fun containsAll(channel: PolyGuildChannel, permissions: Collection<Permission>): Boolean
    
    public fun hasAccess(channel: PolyGuildChannel): Boolean
}