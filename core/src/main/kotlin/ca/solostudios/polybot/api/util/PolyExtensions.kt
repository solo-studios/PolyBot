/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyExtensions.kt is part of PolyhedralBot
 * Last modified on 07-02-2022 01:16 a.m.
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

package ca.solostudios.polybot.api.util

import ca.solostudios.polybot.PolyObject
import ca.solostudios.polybot.api.entities.PolyCategory
import ca.solostudios.polybot.api.entities.PolyChannel
import ca.solostudios.polybot.api.entities.PolyEmote
import ca.solostudios.polybot.api.entities.PolyGuild
import ca.solostudios.polybot.api.entities.PolyGuildChannel
import ca.solostudios.polybot.api.entities.PolyMember
import ca.solostudios.polybot.api.entities.PolyMentionable
import ca.solostudios.polybot.api.entities.PolyMessage
import ca.solostudios.polybot.api.entities.PolyMessageChannel
import ca.solostudios.polybot.api.entities.PolyMessageEmbed
import ca.solostudios.polybot.api.entities.PolyPermissionHolder
import ca.solostudios.polybot.api.entities.PolyPrivateChannel
import ca.solostudios.polybot.api.entities.PolyRole
import ca.solostudios.polybot.api.entities.PolyTextChannel
import ca.solostudios.polybot.api.entities.PolyUser
import ca.solostudios.polybot.api.entities.PolyVoiceChannel
import net.dv8tion.jda.api.entities.AbstractChannel
import net.dv8tion.jda.api.entities.Category
import net.dv8tion.jda.api.entities.Emote
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.GuildChannel
import net.dv8tion.jda.api.entities.IMentionable
import net.dv8tion.jda.api.entities.IPermissionHolder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.PrivateChannel
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.VoiceChannel

/**
 * Wrap this JDA entity in the respective polybot entity.
 *
 * @param bot The PolyBot instance used to wrap this entity.
 * @return The wrapped JDA entity.
 */
public fun Member.poly(bot: _root_ide_package_.ca.solostudios.polybot.api.PolyBot): PolyMember = bot.poly(this)

/**
 * Wrap the provided JDA entity in the respective polybot entity.
 *
 * @param jdaEntity The JDA entity to wrap.
 * @return The wrapped JDA entity.
 */
public fun ca.solostudios.polybot.PolyObject.poly(jdaEntity: Member): PolyMember = polybot.poly(jdaEntity)

/**
 * Wrap this JDA entity in the respective polybot entity.
 *
 * @param bot The PolyBot instance used to wrap this entity.
 * @return The wrapped JDA entity.
 */
public fun User.poly(bot: _root_ide_package_.ca.solostudios.polybot.api.PolyBot): PolyUser = bot.poly(this)

/**
 * Wrap the provided JDA entity in the respective polybot entity.
 *
 * @param jdaEntity The JDA entity to wrap.
 * @return The wrapped JDA entity.
 */
public fun ca.solostudios.polybot.PolyObject.poly(jdaEntity: User): PolyUser = polybot.poly(jdaEntity)

/**
 * Wrap this JDA entity in the respective polybot entity.
 *
 * @param bot The PolyBot instance used to wrap this entity.
 * @return The wrapped JDA entity.
 */
public fun Guild.poly(bot: _root_ide_package_.ca.solostudios.polybot.api.PolyBot): PolyGuild = bot.poly(this)

/**
 * Wrap the provided JDA entity in the respective polybot entity.
 *
 * @param jdaEntity The JDA entity to wrap.
 * @return The wrapped JDA entity.
 */
public fun ca.solostudios.polybot.PolyObject.poly(jdaEntity: Guild): PolyGuild = polybot.poly(jdaEntity)

/**
 * Wrap this JDA entity in the respective polybot entity.
 *
 * @param bot The PolyBot instance used to wrap this entity.
 * @return The wrapped JDA entity.
 */
public fun Message.poly(bot: _root_ide_package_.ca.solostudios.polybot.api.PolyBot): PolyMessage = bot.poly(this)

/**
 * Wrap the provided JDA entity in the respective polybot entity.
 *
 * @param jdaEntity The JDA entity to wrap.
 * @return The wrapped JDA entity.
 */
public fun ca.solostudios.polybot.PolyObject.poly(jdaEntity: Message): PolyMessage = polybot.poly(jdaEntity)

/**
 * Wrap this JDA entity in the respective polybot entity.
 *
 * @param bot The PolyBot instance used to wrap this entity.
 * @return The wrapped JDA entity.
 */
public fun Role.poly(bot: _root_ide_package_.ca.solostudios.polybot.api.PolyBot): PolyRole = bot.poly(this)

/**
 * Wrap the provided JDA entity in the respective polybot entity.
 *
 * @param jdaEntity The JDA entity to wrap.
 * @return The wrapped JDA entity.
 */
public fun ca.solostudios.polybot.PolyObject.poly(jdaEntity: Role): PolyRole = polybot.poly(jdaEntity)

/**
 * Wrap this JDA entity in the respective polybot entity.
 *
 * @param bot The PolyBot instance used to wrap this entity.
 * @return The wrapped JDA entity.
 */
public fun AbstractChannel.poly(bot: _root_ide_package_.ca.solostudios.polybot.api.PolyBot): PolyChannel = bot.poly(this)

/**
 * Wrap the provided JDA entity in the respective polybot entity.
 *
 * @param jdaEntity The JDA entity to wrap.
 * @return The wrapped JDA entity.
 */
public fun ca.solostudios.polybot.PolyObject.poly(jdaEntity: AbstractChannel): PolyChannel = polybot.poly(jdaEntity)

/**
 * Wrap this JDA entity in the respective polybot entity.
 *
 * @param bot The PolyBot instance used to wrap this entity.
 * @return The wrapped JDA entity.
 */
public fun GuildChannel.poly(bot: _root_ide_package_.ca.solostudios.polybot.api.PolyBot): PolyGuildChannel = bot.poly(this)

/**
 * Wrap the provided JDA entity in the respective polybot entity.
 *
 * @param jdaEntity The JDA entity to wrap.
 * @return The wrapped JDA entity.
 */
public fun ca.solostudios.polybot.PolyObject.poly(jdaEntity: GuildChannel): PolyGuildChannel = polybot.poly(jdaEntity)

/**
 * Wrap this JDA entity in the respective polybot entity.
 *
 * @param bot The PolyBot instance used to wrap this entity.
 * @return The wrapped JDA entity.
 */
public fun MessageChannel.poly(bot: _root_ide_package_.ca.solostudios.polybot.api.PolyBot): PolyMessageChannel = bot.poly(this)

/**
 * Wrap the provided JDA entity in the respective polybot entity.
 *
 * @param jdaEntity The JDA entity to wrap.
 * @return The wrapped JDA entity.
 */
public fun ca.solostudios.polybot.PolyObject.poly(jdaEntity: MessageChannel): PolyMessageChannel = polybot.poly(jdaEntity)

/**
 * Wrap this JDA entity in the respective polybot entity.
 *
 * @param bot The PolyBot instance used to wrap this entity.
 * @return The wrapped JDA entity.
 */
public fun TextChannel.poly(bot: _root_ide_package_.ca.solostudios.polybot.api.PolyBot): PolyTextChannel = bot.poly(this)

/**
 * Wrap the provided JDA entity in the respective polybot entity.
 *
 * @param jdaEntity The JDA entity to wrap.
 * @return The wrapped JDA entity.
 */
public fun ca.solostudios.polybot.PolyObject.poly(jdaEntity: TextChannel): PolyTextChannel = polybot.poly(jdaEntity)

/**
 * Wrap this JDA entity in the respective polybot entity.
 *
 * @param bot The PolyBot instance used to wrap this entity.
 * @return The wrapped JDA entity.
 */
public fun Category.poly(bot: _root_ide_package_.ca.solostudios.polybot.api.PolyBot): PolyCategory = bot.poly(this)

/**
 * Wrap the provided JDA entity in the respective polybot entity.
 *
 * @param jdaEntity The JDA entity to wrap.
 * @return The wrapped JDA entity.
 */
public fun ca.solostudios.polybot.PolyObject.poly(jdaEntity: Category): PolyCategory = polybot.poly(jdaEntity)

/**
 * Wrap this JDA entity in the respective polybot entity.
 *
 * @param bot The PolyBot instance used to wrap this entity.
 * @return The wrapped JDA entity.
 */
public fun PrivateChannel.poly(bot: _root_ide_package_.ca.solostudios.polybot.api.PolyBot): PolyPrivateChannel = bot.poly(this)

/**
 * Wrap the provided JDA entity in the respective polybot entity.
 *
 * @param jdaEntity The JDA entity to wrap.
 * @return The wrapped JDA entity.
 */
public fun ca.solostudios.polybot.PolyObject.poly(jdaEntity: PrivateChannel): PolyPrivateChannel = polybot.poly(jdaEntity)

/**
 * Wrap this JDA entity in the respective polybot entity.
 *
 * @param bot The PolyBot instance used to wrap this entity.
 * @return The wrapped JDA entity.
 */
public fun VoiceChannel.poly(bot: _root_ide_package_.ca.solostudios.polybot.api.PolyBot): PolyVoiceChannel = bot.poly(this)

/**
 * Wrap the provided JDA entity in the respective polybot entity.
 *
 * @param jdaEntity The JDA entity to wrap.
 * @return The wrapped JDA entity.
 */
public fun ca.solostudios.polybot.PolyObject.poly(jdaEntity: VoiceChannel): PolyVoiceChannel = polybot.poly(jdaEntity)

public fun MessageEmbed.poly(bot: _root_ide_package_.ca.solostudios.polybot.api.PolyBot): PolyMessageEmbed = bot.poly(this)

/**
 * Wrap the provided JDA entity in the respective polybot entity.
 *
 * @param jdaEntity The JDA entity to wrap.
 * @return The wrapped JDA entity.
 */
public fun ca.solostudios.polybot.PolyObject.poly(jdaEntity: MessageEmbed): PolyMessageEmbed = polybot.poly(jdaEntity)

/**
 * Wrap this JDA entity in the respective polybot entity.
 *
 * @param bot The PolyBot instance used to wrap this entity.
 * @return The wrapped JDA entity.
 */
public fun Emote.poly(bot: _root_ide_package_.ca.solostudios.polybot.api.PolyBot): PolyEmote = bot.poly(this)

/**
 * Wrap the provided JDA entity in the respective polybot entity.
 *
 * @param jdaEntity The JDA entity to wrap.
 * @return The wrapped JDA entity.
 */
public fun ca.solostudios.polybot.PolyObject.poly(jdaEntity: Emote): PolyEmote = polybot.poly(jdaEntity)

/**
 * Wrap this JDA entity in the respective polybot entity.
 *
 * @param bot The PolyBot instance used to wrap this entity.
 * @return The wrapped JDA entity.
 */
public fun IMentionable.poly(bot: _root_ide_package_.ca.solostudios.polybot.api.PolyBot): PolyMentionable = bot.poly(this)

/**
 * Wrap the provided JDA entity in the respective polybot entity.
 *
 * @param jdaEntity The JDA entity to wrap.
 * @return The wrapped JDA entity.
 */
public fun ca.solostudios.polybot.PolyObject.poly(jdaEntity: IMentionable): PolyMentionable = polybot.poly(jdaEntity)

/**
 * Wrap this JDA entity in the respective polybot entity.
 *
 * @param bot The PolyBot instance used to wrap this entity.
 * @return The wrapped JDA entity.
 */
public fun IPermissionHolder.poly(bot: _root_ide_package_.ca.solostudios.polybot.api.PolyBot): PolyPermissionHolder = bot.poly(this)

/**
 * Wrap the provided JDA entity in the respective polybot entity.
 *
 * @param jdaEntity The JDA entity to wrap.
 * @return The wrapped JDA entity.
 */
public fun ca.solostudios.polybot.PolyObject.poly(jdaEntity: IPermissionHolder): PolyPermissionHolder = polybot.poly(jdaEntity)
