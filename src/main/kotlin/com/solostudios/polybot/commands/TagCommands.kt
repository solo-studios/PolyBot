/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file TagCommands.kt is part of PolyhedralBot
 * Last modified on 01-09-2021 06:18 p.m.
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
import com.solostudios.polybot.cloud.permission.annotations.JDAUserPermission
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message

class TagCommands(val bot: PolyBot) {
    @CommandMethod("tag")
    fun tag(message: Message) {
    
    }
    
    @CommandMethod("tag alias")
    @JDAUserPermission(Permission.MESSAGE_MANAGE)
    fun addTagAlias(message: Message) {
    
    }
    
    @CommandMethod("tag create")
    @JDAUserPermission(Permission.MESSAGE_MANAGE)
    fun createTag(message: Message) {
    
    }
    
    @CommandMethod("tag delete")
    @JDAUserPermission(Permission.MESSAGE_MANAGE)
    fun deleteTag(message: Message) {
    
    }
    
    @CommandMethod("tag edit")
    @JDAUserPermission(Permission.MESSAGE_MANAGE)
    fun editTag(message: Message) {
    
    }
    
    @CommandMethod("tag info")
    fun tagInfo(message: Message) {
    
    }
    
    @CommandMethod("tag list")
    fun listTags(message: Message) {
    
    }
    
    @CommandMethod("tag raw")
    fun rawTag(message: Message) {
    
    }
    
    @CommandMethod("tag view")
    fun viewTag(message: Message) {
    
    }
    
}