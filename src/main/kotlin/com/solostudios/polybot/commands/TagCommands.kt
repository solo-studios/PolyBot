/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file TagCommands.kt is part of PolyhedralBot
 * Last modified on 19-09-2021 06:31 p.m.
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

package com.solostudios.polybot.commands

import cloud.commandframework.annotations.CommandMethod
import com.solostudios.polybot.PolyBot
import com.solostudios.polybot.cloud.PolyCommands
import com.solostudios.polybot.cloud.permission.annotations.JDAUserPermission
import com.solostudios.polybot.entities.PolyMessage
import net.dv8tion.jda.api.Permission

class TagCommands(bot: PolyBot) : PolyCommands(bot) {
    @CommandMethod("tag")
    fun tag(message: PolyMessage) {
    
    }
    
    @CommandMethod("tag alias")
    @JDAUserPermission(Permission.MESSAGE_MANAGE)
    fun addTagAlias(message: PolyMessage) {
    
    }
    
    @CommandMethod("tag create")
    @JDAUserPermission(Permission.MESSAGE_MANAGE)
    fun createTag(message: PolyMessage) {
    
    }
    
    @CommandMethod("tag delete")
    @JDAUserPermission(Permission.MESSAGE_MANAGE)
    fun deleteTag(message: PolyMessage) {
    
    }
    
    @CommandMethod("tag edit")
    @JDAUserPermission(Permission.MESSAGE_MANAGE)
    fun editTag(message: PolyMessage) {
    
    }
    
    @CommandMethod("tag info")
    fun tagInfo(message: PolyMessage) {
    
    }
    
    @CommandMethod("tag list")
    fun listTags(message: PolyMessage) {
    
    }
    
    @CommandMethod("tag raw")
    fun rawTag(message: PolyMessage) {
    
    }
    
    @CommandMethod("tag view")
    fun viewTag(message: PolyMessage) {
    
    }
    
}