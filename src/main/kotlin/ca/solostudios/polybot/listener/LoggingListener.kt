/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file LoggingListener.kt is part of PolyhedralBot
 * Last modified on 31-12-2021 01:29 p.m.
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

package ca.solostudios.polybot.listener

import ca.solostudios.polybot.Constants
import ca.solostudios.polybot.PolyBot
import ca.solostudios.polybot.cache.CacheManager
import ca.solostudios.polybot.util.idFooter
import ca.solostudios.polybot.util.jda.poly
import dev.minn.jda.ktx.Embed
import dev.minn.jda.ktx.InlineEmbed
import dev.minn.jda.ktx.await
import java.awt.Color
import java.time.OffsetDateTime
import java.util.Locale
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.audit.ActionType
import net.dv8tion.jda.api.entities.Category
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.GuildChannel
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.VoiceChannel
import net.dv8tion.jda.api.events.UpdateEvent
import net.dv8tion.jda.api.events.channel.category.CategoryCreateEvent
import net.dv8tion.jda.api.events.channel.category.CategoryDeleteEvent
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdatePositionEvent
import net.dv8tion.jda.api.events.channel.category.update.GenericCategoryUpdateEvent
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent
import net.dv8tion.jda.api.events.channel.text.update.GenericTextChannelUpdateEvent
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdatePositionEvent
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateSlowmodeEvent
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent
import net.dv8tion.jda.api.events.channel.voice.update.GenericVoiceChannelUpdateEvent
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateBitrateEvent
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdatePositionEvent
import net.dv8tion.jda.api.events.emote.EmoteAddedEvent
import net.dv8tion.jda.api.events.emote.EmoteRemovedEvent
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateRolesEvent
import net.dv8tion.jda.api.events.emote.update.GenericEmoteUpdateEvent
import net.dv8tion.jda.api.events.guild.GuildBanEvent
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent
import net.dv8tion.jda.api.events.guild.member.update.GenericGuildMemberUpdateEvent
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdatePendingEvent
import net.dv8tion.jda.api.events.guild.override.PermissionOverrideCreateEvent
import net.dv8tion.jda.api.events.guild.override.PermissionOverrideDeleteEvent
import net.dv8tion.jda.api.events.guild.override.PermissionOverrideUpdateEvent
import net.dv8tion.jda.api.events.guild.update.GenericGuildUpdateEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBannerEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostCountEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateFeaturesEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateIconEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateVanityCodeEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent
import net.dv8tion.jda.api.events.role.RoleCreateEvent
import net.dv8tion.jda.api.events.role.RoleDeleteEvent
import net.dv8tion.jda.api.events.role.update.GenericRoleUpdateEvent
import net.dv8tion.jda.api.events.role.update.RoleUpdateColorEvent
import net.dv8tion.jda.api.events.role.update.RoleUpdatePermissionsEvent
import net.dv8tion.jda.api.events.role.update.RoleUpdatePositionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class LoggingListener(override val di: DI) : ListenerAdapter(),
                                             DIAware {
    private val bot: PolyBot by instance()
    private val cacheManager: CacheManager by instance()
    
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if (!event.message.isFromGuild || event.message.author.isSystem || event.isWebhookMessage || event.message.author.isBot)
            return
        
        cacheManager.messageCache.putMessage(event.message)
    }
    
    override fun onGuildMessageUpdate(event: GuildMessageUpdateEvent) {
        if (!event.message.isFromGuild || event.message.author.isSystem || event.message.author.isBot)
            return
        
        bot.scope.launch {
            val oldMessage = cacheManager.messageCache.getMessage(event.messageIdLong)
            val message = event.message
            
            if (!message.isEdited)
                return@launch
            
            cacheManager.messageCache.putMessage(message)
            
            loggingEmbed(event.guild, event.author, event.channel, message, message.timeEdited!!) {
                description = "**${message.author.asMention} edited a message in ${message.textChannel.asMention}.**"
                
                field {
                    name = "Before"
                    value = oldMessage?.content?.takeIf { it.length < 1024 } ?: oldMessage?.content?.substring(0, 1020)?.plus("\n...")
                            ?: "???"
                    inline = false
                }
                field {
                    name = "After"
                    value = message.contentRaw.takeIf { it.length < 1024 } ?: (message.contentRaw.substring(0, 1020) + "\n...")
                    inline = false
                }
            }
        }
    }
    
    override fun onGuildMessageDelete(event: GuildMessageDeleteEvent) {
        bot.scope.launch {
            val message = cacheManager.messageCache.getMessage(event.messageIdLong)
    
            if (message != null) {
                val user: User? = bot.jda.retrieveUserById(message.author)
                        .onErrorMap { null }
                        .await()
        
                loggingEmbed(event.guild, user, event.channel) {
                    description = buildString {
                        append("**")
                        append("<@").append(message.author).append('>')
                        append("'s message in ")
                        append("<#").append(message.channel).append('>')
                        append(" was deleted.**\n")
    
                        if (message.content.length > 1024 - this.length)
                            append(message.content.substring(0, 1020 - this.length)).append("\n...")
                        else
                            append(message.content)
                    }
                }
            }
        }
    }
    
    override fun onPermissionOverrideDelete(event: PermissionOverrideDeleteEvent) {
        val allowedPermissions = event.permissionOverride.allowed
        val deniedPermissions = event.permissionOverride.denied
    
        loggingEmbed("Guild Text Channel Permission Delete", event.guild, event.channel) {
            description = "**Removed Permissions for ${event.channel.asMention}.**\nChanged permissions for " +
                    if (event.isMemberOverride)
                        "member ${event.member!!.asMention}."
                    else if (event.isRoleOverride)
                        "role ${event.role!!.asMention}."
                    else
                        "unknown."
        
            @Suppress("DuplicatedCode")
            field {
                name = "Permissions"
                value = allowedPermissions.joinToString(separator = "\n", postfix = "\n") { "Allow ${it.getName()}" } +
                        deniedPermissions.joinToString(separator = "\n", postfix = "\n") { "Deny ${it.getName()}" }
            }
        }
    }
    
    override fun onPermissionOverrideUpdate(event: PermissionOverrideUpdateEvent) {
        val finalAllow = event.permissionOverride.allowed
        val finalDeny = event.permissionOverride.denied
    
        val oldAllow = event.oldAllow.subtract(finalAllow)
        val oldDeny = event.oldDeny.subtract(finalDeny)
    
        val newAllow = event.permissionOverride.allowed.subtract(event.oldAllow)
        val newDeny = event.permissionOverride.denied.subtract(event.oldDeny)
    
        loggingEmbed("Channel Permission Update", event.guild, event.channel) {
            description = "**Updated Permissions for ${event.channel.asMention}.**\nChanged permissions for " +
                    if (event.isMemberOverride)
                        "member ${event.member!!.asMention}."
                    else if (event.isRoleOverride)
                        "role ${event.role!!.asMention}."
                    else
                        "unknown."
        
            field {
                name = "Removed Permissions"
                value = oldAllow.joinToString(separator = "\n", postfix = "\n") { "Allow ${it.getName()}" } +
                        oldDeny.joinToString(separator = "\n", postfix = "\n") { "Deny ${it.getName()}" }
            }
        
            field {
                name = "Added Permissions"
                value = newAllow.joinToString(separator = "\n", postfix = "\n") { "Allow ${it.getName()}" } +
                        newDeny.joinToString(separator = "\n", postfix = "\n") { "Deny ${it.getName()}" }
            }
        
            field {
                name = "Final Permissions"
                value = finalAllow.joinToString(separator = "\n", postfix = "\n") { "Allow ${it.getName()}" } +
                        finalDeny.joinToString(separator = "\n", postfix = "\n") { "Deny ${it.getName()}" }
                inline = false
            }
        }
    }
    
    override fun onPermissionOverrideCreate(event: PermissionOverrideCreateEvent) {
        val allowedPermissions = event.permissionOverride.allowed
        val deniedPermissions = event.permissionOverride.denied
    
        loggingEmbed("Channel Permission Created", event.guild, event.channel) {
            description = "**Created Permissions for ${event.channel.asMention}.**\nChanged permissions for " +
                    if (event.isMemberOverride)
                        "member ${event.member!!.asMention}."
                    else if (event.isRoleOverride)
                        "role ${event.role!!.asMention}."
                    else
                        "unknown."
        
            @Suppress("DuplicatedCode")
            field {
                name = "Permissions"
                value = allowedPermissions.joinToString(separator = "\n", postfix = "\n") { "Allow ${it.getName()}" } +
                        deniedPermissions.joinToString(separator = "\n", postfix = "\n") { "Deny ${it.getName()}" }
            }
        }
    }
    
    override fun onTextChannelDelete(event: TextChannelDeleteEvent) {
        loggingEmbed("Text Channel Deleted", event.guild, event.channel) {
            description = "**Deleted text channel `#${event.channel.name}`.**"
        }
    }
    
    override fun onTextChannelCreate(event: TextChannelCreateEvent) {
        loggingEmbed("Text Channel Created", event.guild, event.channel) {
            description = "**Created text channel ${event.channel.asMention}.**"
        }
    }
    
    override fun onGenericTextChannelUpdate(event: GenericTextChannelUpdateEvent<*>) {
        if (event is TextChannelUpdatePositionEvent)
            return
    
        loggingEmbed("Text Channel Updated", event.guild, event.channel) {
            description = "**Updated text channel ${event.channel.asMention} ${event.propertyIdentifier.replace('_', ' ')} setting.**"
        
            val (old, new) = event.valueString
        
            field {
                name = "Previous Setting"
                value = old
            }
            field {
                name = "Current Setting"
                value = new
            }
        }
    }
    
    override fun onVoiceChannelDelete(event: VoiceChannelDeleteEvent) {
        loggingEmbed("Voice Channel Deleted", event.guild, event.channel) {
            description = "**Deleted voice channel `#${event.channel.name}`.**"
        }
    }
    
    override fun onVoiceChannelCreate(event: VoiceChannelCreateEvent) {
        loggingEmbed("Voice Channel Created", event.guild, event.channel) {
            description = "**Created voice channel ${event.channel.asMention}.**"
        }
    }
    
    override fun onGenericVoiceChannelUpdate(event: GenericVoiceChannelUpdateEvent<*>) {
        if (event is VoiceChannelUpdatePositionEvent)
            return
    
        loggingEmbed("Voice Channel Updated", event.guild, event.channel) {
            description = "**Updated voice channel ${event.channel.asMention} ${event.propertyIdentifier.replace('_', ' ')} setting.**"
        
            val (old, new) = event.valueString
        
            field {
                name = "Previous Setting"
                value = old
            }
            field {
                name = "Current Setting"
                value = new
            }
        }
    }
    
    override fun onCategoryDelete(event: CategoryDeleteEvent) {
        loggingEmbed("Category Deleted", event.guild, event.category) {
            description = "**Deleted category `#${event.category.name}`.**"
        }
    }
    
    override fun onCategoryCreate(event: CategoryCreateEvent) {
        loggingEmbed("Voice Channel Created", event.guild, event.category) {
            description = "**Created category channel ${event.category.asMention}.**"
        }
    }
    
    override fun onGenericCategoryUpdate(event: GenericCategoryUpdateEvent<*>) {
        if (event is CategoryUpdatePositionEvent)
            return
    
        loggingEmbed("Category Updated", event.guild, event.category) {
            description = "**Updated category channel ${event.category.asMention} ${event.propertyIdentifier.replace('_', ' ')} setting.**"
        
            val (old, new) = event.valueString
        
            field {
                name = "Previous Setting"
                value = old
            }
            field {
                name = "Current Setting"
                value = new
            }
        }
    }
    
    override fun onGuildBan(event: GuildBanEvent) {
        bot.scope.launch {
            val bannedUser: Guild.Ban? = event.guild.retrieveBan(event.user).onErrorMap { null }.await()
    
            loggingEmbed(event.guild, event.user) {
                description = """
                **Banned user ${event.user.asTag}.**
                Banned by ${bannedUser?.user ?: "Unknown"} for ${bannedUser?.run { reason ?: "No reason provided" } ?: "Unknown"}
                """.trimIndent()
            }
        }
    }
    
    override fun onGuildUnban(event: GuildUnbanEvent) {
        bot.scope.launch {
            val bannedUser = event.guild.retrieveAuditLogs().type(ActionType.UNBAN).takeUntilAsync(1) {
                return@takeUntilAsync it.targetIdLong == event.user.idLong
            }.await().firstOrNull()
    
            loggingEmbed(event.guild, event.user) {
                description = if (bannedUser == null)
                    "**Unbanned user ${event.user.asTag}.**"
                else
                    """
                    **Unbanned user ${event.user.asTag}.**
                    Unbanned by ${bannedUser.user ?: "Unknown"} for ${bannedUser.reason ?: "No reason provided"}}
                """.trimIndent()
            }
        }
    }
    
    override fun onGuildMemberRemove(event: GuildMemberRemoveEvent) {
        loggingEmbed(event.guild, event.user) {
            description = "**User ${event.user.asTag} has left the guild.**"
        }
    }
    
    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        loggingEmbed(event.guild, event.user) {
            description = "**Used ${event.user.asMention} has joined the guild.**"
        }
    }
    
    override fun onGenericGuildUpdate(event: GenericGuildUpdateEvent<*>) {
        loggingEmbed("Guild Updated", event.guild) {
            description = "**Updated guild ${event.propertyIdentifier.replace('_', ' ')} setting.**"
    
            val (old, new) = event.valueString
    
            field {
                name = "Previous Setting"
                value = old
            }
            field {
                name = "Current Setting"
                value = new
            }
        }
    }
    
    override fun onGenericGuildMemberUpdate(event: GenericGuildMemberUpdateEvent<*>) {
        if (event is GuildMemberUpdateBoostTimeEvent || event is GuildMemberUpdatePendingEvent)
            return
    
        loggingEmbed("Guild Member Updated", event.guild) {
            description = "**Updated guild member ${event.member.asMention} ${event.propertyIdentifier.replace('_', ' ')} setting.**"
        
            val (old, new) = event.valueString
        
            field {
                name = "Previous Setting"
                value = old
            }
            field {
                name = "Current Setting"
                value = new
            }
        }
    }
    
    override fun onGuildInviteCreate(event: GuildInviteCreateEvent) {
        loggingEmbed("Guild Invite Created", event.guild, event.channel) {
            description = "**Created guild invite ${event.invite.url} for channel ${event.channel.asMention}.**" +
                    event.invite.inviter?.run { "Created by $asMention (ID: $id)" }.orEmpty()
        }
    }
    
    override fun onGuildInviteDelete(event: GuildInviteDeleteEvent) {
        loggingEmbed("Guild Invite Deleted", event.guild, event.channel) {
            description = "**Deleted guild invite for channel ${event.channel.asMention}.**"
        }
    }
    
    override fun onGuildMemberRoleAdd(event: GuildMemberRoleAddEvent) {
        loggingEmbed("Member Roles Updated", event.guild) {
            description = "**Added roles ${event.roles.joinToString { it.asMention }} to user ${event.user}.**"
        }
    }
    
    override fun onGuildMemberRoleRemove(event: GuildMemberRoleRemoveEvent) {
        loggingEmbed("Member Roles Updated", event.guild) {
            description = "**Removed roles ${event.roles.joinToString { it.asMention }} to user ${event.user}.**"
        }
    }
    
    override fun onRoleCreate(event: RoleCreateEvent) {
        loggingEmbed("Role Created", event.guild) {
            description = "**Created role ${event.role.asMention}**"
        }
    }
    
    override fun onRoleDelete(event: RoleDeleteEvent) {
        loggingEmbed("Role Deleted", event.guild) {
            description = "**Deleted role `@${event.role.name}`.**"
        }
    }
    
    override fun onGenericRoleUpdate(event: GenericRoleUpdateEvent<*>) {
        if (event is RoleUpdatePositionEvent)
            return
    
        loggingEmbed("Role Updated", event.guild) {
            description = "**Updated role ${event.role.asMention} ${event.propertyIdentifier.replace('_', ' ')} setting.**"
        
            val (old, new) = event.valueString
        
            field {
                name = "Previous Setting"
                value = old
            }
            field {
                name = "Current Setting"
                value = new
            }
        }
    }
    
    override fun onEmoteAdded(event: EmoteAddedEvent) {
        loggingEmbed("Emote Created", event.guild) {
            description = "**Created emote ${event.emote.asMention}**"
        }
    }
    
    override fun onEmoteRemoved(event: EmoteRemovedEvent) {
        loggingEmbed("Emote Deleted", event.guild) {
            description = "**Deleted emote `:${event.emote.name}:`.**"
        }
    }
    
    override fun onGenericEmoteUpdate(event: GenericEmoteUpdateEvent<*>) {
        loggingEmbed("Guild Emote Updated", event.guild) {
            description = "**Updated guild emote ${event.emote.asMention} ${event.propertyIdentifier.replace('_', ' ')} setting.**"
    
            val (old, new) = event.valueString
    
            field {
                name = "Previous Setting"
                value = old
            }
            field {
                name = "Current Setting"
                value = new
            }
        }
    }
    
    private fun loggingEmbed(guild: Guild,
                             user: User?,
                             channel: GuildChannel? = null,
                             message: Message? = null,
                             time: OffsetDateTime = OffsetDateTime.now(),
                             block: (InlineEmbed).() -> Unit) {
        val author = user?.asTag ?: Constants.defaultUsername
        val icon = user?.effectiveAvatarUrl ?: Constants.defaultAvatarUrl
    
        loggingEmbedImpl(author, icon, guild, channel ?: message?.textChannel, user, message, time, block)
    }
    
    
    private fun loggingEmbed(eventName: String,
                             guild: Guild,
                             channel: GuildChannel? = null,
                             time: OffsetDateTime = OffsetDateTime.now(),
                             block: (InlineEmbed).() -> Unit) {
        val authorName = "$eventName | ${channel?.name ?: guild.name}"
        
        loggingEmbedImpl(authorName, guild.iconUrl, guild, channel, time = time, block = block)
    }
    
    private fun loggingEmbedImpl(eventName: String,
                                 eventIcon: String?,
                                 guild: Guild,
                                 channel: GuildChannel?,
                                 user: User? = null,
                                 message: Message? = null,
                                 time: OffsetDateTime,
                                 block: InlineEmbed.() -> Unit) {
        val polyGuild = guild.poly(bot)
        val loggingChannel = polyGuild.data.loggingChannel ?: return
    
        bot.scope.launch {
        
            val embed = Embed {
                color = message?.member?.colorRaw ?: Constants.logEmbedColourCode
            
                author {
                    name = eventName
                    iconUrl = eventIcon
                }
                block(this)
            
                idFooter(time = time, guild = guild.idLong, channel = channel?.idLong, message = message?.idLong, user = user?.idLong)
            }
        
            loggingChannel.sendMessage(embed)
        }
    }
    
    private fun Any?.humanString(): String {
        return if (this == null)
            "null"
        else when (this) {
            is Category                   -> this.name
            is VoiceChannel               -> this.asMention
            is TextChannel                -> this.asMention
            is GuildChannel               -> this.asMention
            is Guild.Timeout              -> "$seconds Seconds"
            is Guild.ExplicitContentLevel -> this.description
            is Guild.MFALevel             -> when (this) {
                Guild.MFALevel.NONE            -> "None"
                Guild.MFALevel.TWO_FACTOR_AUTH -> "Two-Factor Authentication"
                Guild.MFALevel.UNKNOWN         -> "Unknown"
                else                           -> "Unknown"
            }
            is Member                     -> this.asMention
            is Guild.VerificationLevel    -> when (this) {
                Guild.VerificationLevel.NONE      -> "None"
                Guild.VerificationLevel.LOW       -> "Low"
                Guild.VerificationLevel.MEDIUM    -> "Medium"
                Guild.VerificationLevel.HIGH      -> "High"
                Guild.VerificationLevel.VERY_HIGH -> "Very High"
                Guild.VerificationLevel.UNKNOWN   -> "Unknown"
                else                              -> "Unknown"
            }
            is Guild.NotificationLevel    -> when (this) {
                Guild.NotificationLevel.ALL_MESSAGES  -> "All Messages"
                Guild.NotificationLevel.MENTIONS_ONLY -> "Mentions Only"
                Guild.NotificationLevel.UNKNOWN       -> "Unknown"
                else                                  -> "Unknown"
            }
            is Guild.BoostTier            -> when (this) {
                Guild.BoostTier.NONE    -> "None"
                Guild.BoostTier.TIER_1  -> "Tier 1"
                Guild.BoostTier.TIER_2  -> "Tier 2"
                Guild.BoostTier.TIER_3  -> "Tier 3"
                Guild.BoostTier.UNKNOWN -> "Unknown"
                else                    -> "Unknown"
            }
            is Color                      -> "RGB: $red, $green, $blue"
            is Locale                     -> this.displayName
            is Set<*>                     -> this.joinToString()
            else                          -> this.toString()
        }
    }
    
    private val <T> UpdateEvent<*, T>.valueString: Pair<String, String>
        get() {
            return when (this) {
                is TextChannelUpdateSlowmodeEvent -> "$oldSlowmode seconds" to "$newSlowmode seconds"
                is VoiceChannelUpdateBitrateEvent -> "$oldBitrate khz" to "$newBitrate khz"
                is GuildUpdateBoostCountEvent     -> "$oldBoostCount boosts" to "$newBoostCount boosts"
                
                is GuildUpdateBannerEvent         -> {
                    (oldBannerUrl?.run { "[old icon]($this)" } ?: "No Banner") to (newBannerUrl?.run { "[new icon]($this)" } ?: "No Banner")
                }
                
                is GuildUpdateIconEvent           -> {
                    (oldIconUrl?.run { "[old icon]($this)" } ?: "No Icon") to (newIconUrl?.run { "[new icon]($this)" } ?: "No Icon")
                }
                
                is GuildUpdateFeaturesEvent       -> oldFeatures.joinToString() to newFeatures.joinToString()
                is GuildUpdateVanityCodeEvent     -> (oldVanityUrl ?: "No Vanity Code") to (newVanityUrl ?: "No Vanity Code")
                
                is RoleUpdateColorEvent           -> oldColor.humanString() to oldValue.humanString()
                is RoleUpdatePermissionsEvent     -> oldPermissions.joinToString { it.getName() } to newPermissions.joinToString { it.getName() }
                
                is EmoteUpdateRolesEvent          -> oldRoles.joinToString { it.name } to newRoles.joinToString { it.name }
                
                else                              -> newValue.humanString() to oldValue.humanString()
            }
        }
}