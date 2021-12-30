/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file TagCommands.kt is part of PolyhedralBot
 * Last modified on 30-12-2021 03:26 p.m.
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

package ca.solostudios.polybot.commands

import ca.solostudios.polybot.PolyBot
import ca.solostudios.polybot.cloud.commands.PolyCommandContainer
import ca.solostudios.polybot.cloud.commands.PolyCommands
import ca.solostudios.polybot.cloud.commands.annotations.CommandLongDescription
import ca.solostudios.polybot.cloud.commands.annotations.CommandName
import ca.solostudios.polybot.cloud.commands.annotations.CurrentGuild
import ca.solostudios.polybot.cloud.commands.annotations.JDAGuildCommand
import ca.solostudios.polybot.cloud.commands.annotations.JDAUserPermission
import ca.solostudios.polybot.cloud.commands.annotations.PolyCategory
import ca.solostudios.polybot.cloud.commands.annotations.SourceMessage
import ca.solostudios.polybot.entities.PolyGuild
import ca.solostudios.polybot.entities.PolyMessage
import ca.solostudios.polybot.entities.data.Tag
import ca.solostudios.polybot.util.chunkedBy
import ca.solostudios.polybot.util.toDiscordTimestamp
import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.ProxiedBy
import cloud.commandframework.annotations.specifier.Greedy
import dev.minn.jda.ktx.Embed
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message.MentionType
import net.dv8tion.jda.api.utils.MarkdownSanitizer
import ca.solostudios.polybot.entities.data.PolyTagData as PolyTag

@PolyCommandContainer
@PolyCategory(TAG_CATEGORY)
@Suppress("DuplicatedCode")
class TagCommands(bot: PolyBot) : PolyCommands(bot) {
    @JDAGuildCommand
    @ProxiedBy("tag")
    @CommandName("View Tag")
    @CommandMethod("tag|t view|v <tag>")
    @CommandDescription("Sends the specified tag in the current channel.")
    @CommandLongDescription("Displays a tag in the current channel.\nThis is the main functionality of tags, allowing you to specify reusable messages, to later send them in the chat.")
    suspend fun tag(
            @SourceMessage
            message: PolyMessage,
            @Argument(value = "tag", description = "The tag to send.")
            tag: PolyTag,
                   ) {
        tag.usages++
        
        message.channel.sendMessage(tag.content, listOf(MentionType.EVERYONE, MentionType.HERE, MentionType.ROLE, MentionType.USER))
    }
    
    @JDAGuildCommand
    @CommandName("Create Tag")
    @JDAUserPermission(Permission.MESSAGE_MANAGE)
    @CommandMethod("tag|t create|c <name> <content>")
    @CommandDescription("Creates a new tag for this server.")
    @CommandLongDescription("Creates a new tag to be used in this server. Tags can later be displayed with `\$tag view [tag name]`, which will send the content you specified.")
    suspend fun createTag(
            @SourceMessage
            message: PolyMessage,
            @CurrentGuild
            guild: PolyGuild,
            @Argument(value = "name", description = "The name of the new tag.")
            name: String,
            @Greedy
            @Argument(value = "content", description = "The contents of the tag.")
            content: String,
                         ) {
        if (!isValidTagNameAlias(name)) {
            message.reply(invalidTag(name))
            return
        }
        
        val matchingTag = guild.tags.find { name == it.name || name in it.aliases }
        
        if (matchingTag != null) {
            if (matchingTag.name == name)
                message.reply("The name of a tag cannot be the same as the name of an already existing tag. This name conflicts with the tag '${matchingTag.name}' with the UUID `${matchingTag.uuid}`.")
            else
                message.reply("The name of a tag cannot be the same as the alias of an already existing tag. This name conflicts with the tag '${matchingTag.name}' with the UUID `${matchingTag.uuid}`")
            
            return
        }
        
        val tag = Tag(bot, guild, name, content)
        
        guild.tags.add(tag)
        
        message.reply("Created tag '$name' with the UUID `${tag.uuid}`.")
    }
    
    @JDAGuildCommand
    @CommandName("Rename Tag")
    @JDAUserPermission(Permission.MESSAGE_MANAGE)
    @CommandMethod("tag|t rename|ren <tag> <new-name>")
    @CommandDescription("Changes the name of an existing tag.")
    @CommandLongDescription("Replaces the name of an already existing tag with a new one.")
    suspend fun renameTag(
            @SourceMessage
            message: PolyMessage,
            @CurrentGuild
            guild: PolyGuild,
            @Argument(value = "tag", description = "The current name of the tag.")
            tag: PolyTag,
            @Argument(value = "new-name", description = "The new name of the tag.")
            newName: String,
                         ) {
        if (!isValidTagNameAlias(newName)) {
            message.reply(invalidTag(newName))
            return
        }
        
        val matchingTag = guild.tags.find { newName == it.name || newName in it.aliases }
        
        if (matchingTag != null) {
            if (matchingTag.name == newName)
                message.reply("The name of a tag cannot be the same as the name of an already existing tag. This name conflicts with the tag '${matchingTag.name}' with the UUID `${matchingTag.uuid}`.")
            else
                message.reply("The name of a tag cannot be the same as the alias of an already existing tag. This name conflicts with the tag '${matchingTag.name}' with the UUID `${matchingTag.uuid}`")
            
            return
        }
        
        val oldName = tag.name
        tag.name = newName
        
        message.reply("Renamed tag '$oldName' with UUID `${tag.uuid}` to '$newName'.")
    }
    
    @JDAGuildCommand
    @CommandName("Edit Tag")
    @JDAUserPermission(Permission.MESSAGE_MANAGE)
    @CommandMethod("tag|t edit|ed|e <tag> <content>")
    @CommandDescription("Edits the contents of an existing tag.")
    @CommandLongDescription("Alters the contents of an existing tag, replacing it. Tags can later be displayed with `\$tag view [tag name]`, which will send the content you specified.")
    suspend fun editTag(
            @SourceMessage
            message: PolyMessage,
            @Argument(value = "tag", description = "The tag to edit.")
            tag: PolyTag,
            @Greedy
            @Argument(value = "content", description = "The new content of the tag.")
            content: String,
                       ) {
        tag.content = content
        
        message.reply("Updated the content of tag '${tag.name}' with UUID `${tag.uuid}`")
    }
    
    @JDAGuildCommand
    @CommandName("Add Tag Alias")
    @JDAUserPermission(Permission.MESSAGE_MANAGE)
    @CommandMethod("tag|t alias|a add|a <tag> <alias>")
    @CommandDescription("Adds an alias to a tag.")
    @CommandLongDescription("Adds a new alias to the specified tag. Tags can be referenced either with their name, or with an alias.")
    suspend fun addTagAlias(
            @SourceMessage
            message: PolyMessage,
            @CurrentGuild
            guild: PolyGuild,
            @Argument(value = "tag", description = "The tag to add an alias to.")
            tag: PolyTag,
            @Argument(value = "alias", description = "The alias to add to the tag.")
            alias: String,
                           ) {
        if (!isValidTagNameAlias(alias)) {
            message.reply(invalidTag(alias))
            return
        }
        
        if (tag.aliases.size > 8) {
            message.reply("Tags cannot have more than 8 aliases. What are you doing with more than 8 aliases, anyway?")
            return
        }
        
        val matchingTag = guild.tags.find { alias == it.name || alias in it.aliases }
        
        if (matchingTag != null) {
            if (matchingTag.name == alias)
                message.reply("An alias cannot be the same as the name of an already existing tag. This alias conflicts with the tag '${matchingTag.name}' with the UUID `${matchingTag.uuid}`.")
            else
                message.reply("An alias cannot be the same as the alias of an already existing tag. This alias conflicts with the tag '${matchingTag.name}' with the UUID `${matchingTag.uuid}`")
            
            return
        }
        
        tag.aliases.add(alias)
        
        message.reply("Added alias '$alias' to the tag '${tag.name}'")
    }
    
    @JDAGuildCommand
    @CommandName("Remove Tag Alias")
    @JDAUserPermission(Permission.MESSAGE_MANAGE)
    @CommandMethod("tag|t alias|a delete|d|remove|rm|r <tag> <alias>")
    @CommandDescription("Removes an alias from a tag.")
    @CommandLongDescription("Removes an alias from the specified tag. Tags can be referenced either with their name, or with an alias.")
    suspend fun deleteTagAlias(
            @SourceMessage
            message: PolyMessage,
            @Argument(value = "tag", description = "The tag to remove an alias from.")
            tag: PolyTag,
            @Argument(value = "alias", description = "The alias to remove from the tag.")
            alias: String,
                              ) {
        if (tag.aliases.remove(alias))
            message.reply("Deleted alias '${alias}' from tag '${tag.name}' with UUID `${tag.uuid}`.")
        else
            message.reply("Could not find the alias '${alias}' on the tag '${tag.name}' with UUID `${tag.uuid}`")
        
    }
    
    @JDAGuildCommand
    @CommandName("Delete Tag")
    @JDAUserPermission(Permission.MESSAGE_MANAGE)
    @CommandMethod("tag|t delete|del|d|remove|rm <tag>")
    @CommandDescription("Deletes a tag from this guild.")
    @CommandLongDescription("Permanently deletes a tag. This will remove the tag from the guild.\n\n**This operation is non-reversible!! Be careful!**")
    suspend fun deleteTag(
            @SourceMessage
            message: PolyMessage,
            @CurrentGuild
            guild: PolyGuild,
            @Argument(value = "tag", description = "The tag to permanently delete from this guild.")
            tag: PolyTag,
                         ) {
        if (guild.tags.remove(tag))
            message.reply("Deleted tag '${tag.name}'.")
        else
            message.reply("Could not find tag '${tag.name}' with UUID `${tag.uuid}` in this guild's tags, even though we found it earlier. Please report this to the developers")
    }
    
    @JDAGuildCommand
    @CommandName("Tag Info")
    @CommandMethod("tag|t info|i <tag>")
    @CommandDescription("Queries the info of the specified tag.")
    @CommandLongDescription("Queries the info of the specified tag.\nThis will return a list of aliases the tag has, when the tag was created, how many times the tag has been used, and the UUID of the tag.")
    suspend fun tagInfo(
            @SourceMessage
            message: PolyMessage,
            @Argument(value = "tag", description = "The tag to query the info for.")
            tag: PolyTag,
                       ) {
        val tagEmbed = Embed {
            title = "Info for Tag ${tag.name}."
            description = tag.content
            
            if (tag.aliases.isNotEmpty()) {
                field {
                    val aliases = tag.aliases.joinToString(prefix = "`", separator = ", ", postfix = "`")
                    
                    name = "Aliases"
                    value = "This tag has the following aliases: $aliases."
                    inline = false
                }
            } else {
                field {
                    name = "Aliases"
                    value = "This tag has no aliases"
                    inline = false
                }
            }
            
            field {
                name = "Created"
                value = "This tag was created on: ${tag.created.toDiscordTimestamp()}."
                inline = false
            }
            
            field {
                name = "Usages"
                value = "This tag has been used ${tag.usages} times."
                inline = false
            }
            
            field {
                name = "UUID"
                value = "`${tag.uuid}`"
                inline = false
            }
        }
        
        message.reply(tagEmbed)
    }
    
    @JDAGuildCommand
    @ProxiedBy("tags")
    @CommandName("Tag List")
    @CommandMethod("tag|t list|l")
    @CommandDescription("Lists all the tags existing in this guild.")
    @CommandLongDescription("Prints out a list of all the tags that exist in this guild. Useful if you forget the name of a tag.")
    suspend fun listTags(
            @SourceMessage
            message: PolyMessage,
            @CurrentGuild
            guild: PolyGuild,
                        ) {
        val chunkedTags = guild.tags.chunkedBy(1800) { this.name.length + 4 /* 2 to include ", ". */ }
        
        val splitTagNameList = chunkedTags.map { tagList ->
            tagList.joinToString(prefix = "`", separator = "`, `", postfix = "`.") { tag -> tag.name }
        }
        
        when {
            splitTagNameList.isEmpty() -> message.reply("There are no tags for this guild.")
            splitTagNameList.size == 1 -> message.reply("Here is a list of all the tags for this guild:\n${splitTagNameList.first()}")
            
            else                       -> {
                message.reply("There are too many tags in this guild for one message. " +
                                      "They will be split into multiple messages. " +
                                      "Here are the tags for this guild:")
                
                val size = splitTagNameList.size
                
                splitTagNameList.forEachIndexed { index, tagNames ->
                    message.replyAsync("""
                        $index/$size:
                        $tagNames
                    """.trimIndent())
                }
            }
        }
    }
    
    @JDAGuildCommand
    @CommandName("Raw Tag")
    @CommandMethod("tag|t raw|r <tag>")
    @CommandDescription("Returns the raw value of this tag.")
    @CommandLongDescription("Returns the raw (unformatted) contents of a tag.\nThis will give you the tag *without any* Markdown formatting.")
    suspend fun rawTag(
            @SourceMessage
            message: PolyMessage,
            @Argument(value = "tag", description = "The tag to get the raw value of.")
            tag: PolyTag,
                      ) {
        val escapedTag = MarkdownSanitizer.sanitize(tag.content, MarkdownSanitizer.SanitizationStrategy.ESCAPE)
        
        if (escapedTag.length <= 2000)
            message.reply(escapedTag, listOf(MentionType.EVERYONE,
                                             MentionType.HERE,
                                             MentionType.ROLE,
                                             MentionType.USER,
                                             MentionType.CHANNEL,
                                             MentionType.EMOTE))
        else
            message.reply("Could not send raw tag because it exceeded 2000 characters")
    }
    
    private fun isValidTagNameAlias(name: String): Boolean {
        return name.none { it !in 'A' .. 'Z' && it !in 'a' .. 'z' && it !in '0' .. '9' && it != '.' && it != '_' && it != '-' } && name.length <= 64
    }
    
    private fun invalidTag(name: String): String {
        //language=Markdown
        return "The name '$name' is invalid. " +
                "A tag name or alias must *only* contain A-Z, a-z, 0-9, `.`, `_`, or `-` and no other characters, " +
                "and cannot be longer than 64 characters. Please choose a new name."
    }
}