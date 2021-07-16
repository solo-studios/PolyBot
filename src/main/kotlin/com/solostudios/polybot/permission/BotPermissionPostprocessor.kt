/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file BotPermissionPostprocessor.kt is part of PolyhedralBot
 * Last modified on 16-07-2021 01:58 a.m.
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
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.TextChannel

class BotPermissionPostprocessor<T> : CommandPostprocessor<T> {
    override fun accept(postprocessingContext: CommandPostprocessingContext<T>) {
        val context = postprocessingContext.commandContext
        
        if (context.contains("Guild")) {
            val guild = context.get<Guild>("Guild")
            val bot = guild.selfMember
            
            val permissions =
                    if (context.contains("TextChannel"))
                        context.get<TextChannel>("TextChannel").getPermissionOverride(bot)?.allowed ?: bot.permissions
                    else
                        bot.permissions
            if (permissions.containsAll(context[BOT_PERMISSIONS]))
                ConsumerService.interrupt()
        }
    }
}