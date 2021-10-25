/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyMeta.kt is part of PolyhedralBot
 * Last modified on 25-10-2021 05:05 p.m.
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

package ca.solostudios.polybot.cloud.commands

import ca.solostudios.polybot.cloud.commands.annotations.CommandLongDescription
import ca.solostudios.polybot.cloud.commands.annotations.CommandName
import ca.solostudios.polybot.cloud.commands.annotations.JDABotPermission
import ca.solostudios.polybot.cloud.commands.annotations.JDAGuildCommand
import ca.solostudios.polybot.cloud.commands.annotations.JDAUserPermission
import ca.solostudios.polybot.cloud.commands.annotations.PolyCategory
import ca.solostudios.polybot.commands.Category
import ca.solostudios.polybot.commands.getCategory
import cloud.commandframework.Command
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.meta.CommandMeta
import io.leangen.geantyref.TypeToken
import net.dv8tion.jda.api.Permission

object PolyMeta {
    val CATEGORY = CommandMeta.Key.of(Category::class.java, "category") { null }
    
    val BOT_PERMISSIONS = CommandMeta.Key.of(object : TypeToken<List<Permission>>() {}, "bot-permissions") { listOf() }
    val USER_PERMISSIONS = CommandMeta.Key.of(object : TypeToken<List<Permission>>() {}, "user-permissions") { listOf() }
    
    val OWNER_ONLY = CommandMeta.Key.of(Boolean::class.javaObjectType, "owner-only") { false }
    val CO_OWNER_ONLY = CommandMeta.Key.of(Boolean::class.javaObjectType, "co-owner-only") { false }
    
    val GUILD_ONLY = CommandMeta.Key.of(Boolean::class.javaObjectType, "guild-only") { false }
    
    val COMMAND_NAME = CommandMeta.Key.of(String::class.java, "command-name")
    
    fun <T> botPermissionModifier(botPermission: JDABotPermission, builder: Command.Builder<T>): Command.Builder<T> {
        return builder.meta(BOT_PERMISSIONS, botPermission.permissions.asList())
    }
    
    fun <T> userPermissionModifier(userPermission: JDAUserPermission, builder: Command.Builder<T>): Command.Builder<T> {
        return builder.meta(USER_PERMISSIONS, userPermission.permissions.asList())
                .meta(OWNER_ONLY, userPermission.ownerOnly)
                .meta(CO_OWNER_ONLY, userPermission.coOwnerOnly)
    }
    
    @Suppress("UNUSED_PARAMETER")
    fun <T> guildCommandModifier(guildCommand: JDAGuildCommand, builder: Command.Builder<T>): Command.Builder<T> {
        return builder.meta(GUILD_ONLY, true)
    }
    
    fun <T> categoryCommandModifier(polyCategory: PolyCategory, builder: Command.Builder<T>): Command.Builder<T> {
        return builder.meta(CATEGORY, getCategory(polyCategory.category))
    }
    
    fun <T> descriptionCommandModifier(commandDescription: CommandDescription, builder: Command.Builder<T>): Command.Builder<T> {
        return builder.meta(CommandMeta.DESCRIPTION, commandDescription.value)
    }
    
    fun <T> longDescriptionCommandModifier(commandLongDescription: CommandLongDescription,
                                           builder: Command.Builder<T>): Command.Builder<T> {
        return builder.meta(CommandMeta.LONG_DESCRIPTION, commandLongDescription.longDescription)
    }
    
    fun <T> nameCommandModifier(commandName: CommandName, builder: Command.Builder<T>): Command.Builder<T> {
        return builder.meta(COMMAND_NAME, commandName.name)
    }
}