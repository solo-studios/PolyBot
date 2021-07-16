/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file UserPermissionPostprocessor.kt is part of PolyhedralBot
 * Last modified on 16-07-2021 02:12 a.m.
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

package com.solostudios.polybot.permission

import cloud.commandframework.execution.postprocessor.CommandPostprocessingContext
import cloud.commandframework.execution.postprocessor.CommandPostprocessor
import cloud.commandframework.services.types.ConsumerService
import com.solostudios.polybot.PolyBot
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class UserPermissionPostprocessor<T>(val bot: PolyBot) : CommandPostprocessor<T> {
    override fun accept(postprocessingContext: CommandPostprocessingContext<T>) {
        val context = postprocessingContext.commandContext
        val event = context.get<MessageReceivedEvent>("MessageReceivedEvent")
        
        if (context[CO_OWNER_ONLY]) {
            if (event.author.idLong !in bot.botConfig.ownerIds && event.author.idLong !in bot.botConfig.coOwnerIds)
                ConsumerService.interrupt()
        } else if (context[OWNER_ONLY]) {
            if (event.author.idLong !in bot.botConfig.ownerIds)
                ConsumerService.interrupt()
        }
        
        if (event.author.idLong in bot.botConfig.ownerIds)
            return
        
        if (context.contains("Guild")) {
            val user = event.member ?: return
            
            val permissions =
                    if (context.contains("TextChannel"))
                        context.get<TextChannel>("TextChannel").getPermissionOverride(user)?.allowed ?: user.permissions
                    else
                        user.permissions
            if (permissions.containsAll(context[USER_PERMISSIONS]))
                ConsumerService.interrupt()
        }
    }
}