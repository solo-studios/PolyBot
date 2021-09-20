/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file TagCommands.kt is part of PolyhedralBot
 * Last modified on 20-09-2021 01:46 a.m.
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

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.specifier.Greedy
import com.solostudios.polybot.PolyBot
import com.solostudios.polybot.cloud.PolyCommandContainer
import com.solostudios.polybot.cloud.PolyCommands
import com.solostudios.polybot.cloud.permission.annotations.JDAGuildCommand
import com.solostudios.polybot.cloud.permission.annotations.JDAUserPermission
import com.solostudios.polybot.entities.PolyGuild
import com.solostudios.polybot.entities.PolyMessage
import com.solostudios.polybot.entities.data.Tag
import net.dv8tion.jda.api.Permission

@PolyCommandContainer
class TagCommands(bot: PolyBot) : PolyCommands(bot) {
    @JDAGuildCommand
    @CommandMethod("tag <tag>")
    suspend fun tag(message: PolyMessage,
                    guild: PolyGuild,
                    @Argument("tag")
                    tag: String) {
        val tags = guild.data.tags
        
        val tag = tags.find { tag == it.name || tag in it.aliases }
        
        if (tag == null) {
            message.reply("Could not find tag,")
            
            return
        }
        
        message.channel.sendMessage(tag.content)
    }
    
    @JDAGuildCommand
    @CommandMethod("tag alias")
    @JDAUserPermission(Permission.MESSAGE_MANAGE)
    fun addTagAlias(message: PolyMessage) {
    
    }
    
    @JDAGuildCommand
    @CommandMethod("tag create <name> <content>")
    @JDAUserPermission(Permission.MESSAGE_MANAGE)
    suspend fun createTag(message: PolyMessage,
                          guild: PolyGuild,
                          @Argument("name")
                          name: String,
                          @Greedy
                          @Argument("content")
                          content: String) {
        val tag = Tag(bot, guild, name, content)
        
        guild.data.tags.add(tag)
        
        message.reply("Added tag $name")
    }
    
    @JDAGuildCommand
    @CommandMethod("tag delete")
    @JDAUserPermission(Permission.MESSAGE_MANAGE)
    fun deleteTag(message: PolyMessage) {
    
    }
    
    @JDAGuildCommand
    @CommandMethod("tag edit")
    @JDAUserPermission(Permission.MESSAGE_MANAGE)
    fun editTag(message: PolyMessage) {
    
    }
    
    @JDAGuildCommand
    @CommandMethod("tag info")
    fun tagInfo(message: PolyMessage) {
    
    }
    
    @JDAGuildCommand
    @CommandMethod("tag list")
    fun listTags(message: PolyMessage) {
    
    }
    
    @JDAGuildCommand
    @CommandMethod("tag raw")
    fun rawTag(message: PolyMessage) {
    
    }
    
    @JDAGuildCommand
    @CommandMethod("tag view")
    fun viewTag(message: PolyMessage) {
    
    }
    
}