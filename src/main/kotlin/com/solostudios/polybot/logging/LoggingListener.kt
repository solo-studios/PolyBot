/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file LoggingListener.kt is part of PolyhedralBot
 * Last modified on 09-07-2021 03:32 p.m.
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

package com.solostudios.polybot.logging

import com.solostudios.polybot.PolyBot
import net.dv8tion.jda.api.events.channel.category.CategoryCreateEvent
import net.dv8tion.jda.api.events.channel.category.CategoryDeleteEvent
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdateNameEvent
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdatePositionEvent
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateNSFWEvent
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateNameEvent
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateNewsEvent
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateParentEvent
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdatePositionEvent
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateSlowmodeEvent
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateTopicEvent
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateBitrateEvent
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateNameEvent
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateParentEvent
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdatePositionEvent
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateUserLimitEvent
import net.dv8tion.jda.api.events.emote.EmoteAddedEvent
import net.dv8tion.jda.api.events.emote.EmoteRemovedEvent
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateNameEvent
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateRolesEvent
import net.dv8tion.jda.api.events.guild.GuildAvailableEvent
import net.dv8tion.jda.api.events.guild.GuildBanEvent
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent
import net.dv8tion.jda.api.events.guild.GuildUnavailableEvent
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent
import net.dv8tion.jda.api.events.guild.UnavailableGuildJoinedEvent
import net.dv8tion.jda.api.events.guild.UnavailableGuildLeaveEvent
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdatePendingEvent
import net.dv8tion.jda.api.events.guild.override.PermissionOverrideCreateEvent
import net.dv8tion.jda.api.events.guild.override.PermissionOverrideDeleteEvent
import net.dv8tion.jda.api.events.guild.override.PermissionOverrideUpdateEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateAfkChannelEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateAfkTimeoutEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBannerEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostCountEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostTierEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateCommunityUpdatesChannelEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateDescriptionEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateExplicitContentLevelEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateFeaturesEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateIconEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateLocaleEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateMFALevelEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateMaxMembersEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateMaxPresencesEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNotificationLevelEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateOwnerEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateRegionEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateRulesChannelEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateSplashEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateSystemChannelEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateVanityCodeEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateVerificationLevelEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceDeafenEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildDeafenEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildMuteEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMuteEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceStreamEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceSuppressEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageEmbedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent
import net.dv8tion.jda.api.events.role.RoleCreateEvent
import net.dv8tion.jda.api.events.role.RoleDeleteEvent
import net.dv8tion.jda.api.events.role.update.RoleUpdateColorEvent
import net.dv8tion.jda.api.events.role.update.RoleUpdateHoistedEvent
import net.dv8tion.jda.api.events.role.update.RoleUpdateMentionableEvent
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent
import net.dv8tion.jda.api.events.role.update.RoleUpdatePermissionsEvent
import net.dv8tion.jda.api.events.role.update.RoleUpdatePositionEvent
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.kotlin.getLogger

@Suppress("RedundantOverride")
class LoggingListener(val bot: PolyBot) : ListenerAdapter() {
    private val logger by getLogger()
    
    override fun onUserUpdateName(event: UserUpdateNameEvent) {
        
    }
    
    override fun onGuildMessageUpdate(event: GuildMessageUpdateEvent) {
        
    }
    
    override fun onGuildMessageDelete(event: GuildMessageDeleteEvent) {
        
    }
    
    override fun onGuildMessageEmbed(event: GuildMessageEmbedEvent) {
        
    }
    
    override fun onPermissionOverrideDelete(event: PermissionOverrideDeleteEvent) {
        
    }
    
    override fun onPermissionOverrideUpdate(event: PermissionOverrideUpdateEvent) {
        
    }
    
    override fun onPermissionOverrideCreate(event: PermissionOverrideCreateEvent) {
        
    }
    
    override fun onTextChannelDelete(event: TextChannelDeleteEvent) {
        
    }
    
    override fun onTextChannelUpdateName(event: TextChannelUpdateNameEvent) {
        
    }
    
    override fun onTextChannelUpdateTopic(event: TextChannelUpdateTopicEvent) {
        
    }
    
    override fun onTextChannelUpdatePosition(event: TextChannelUpdatePositionEvent) {
        
    }
    
    override fun onTextChannelUpdateNSFW(event: TextChannelUpdateNSFWEvent) {
        
    }
    
    override fun onTextChannelUpdateParent(event: TextChannelUpdateParentEvent) {
        
    }
    
    override fun onTextChannelUpdateSlowmode(event: TextChannelUpdateSlowmodeEvent) {
        
    }
    
    override fun onTextChannelUpdateNews(event: TextChannelUpdateNewsEvent) {
        
    }
    
    override fun onTextChannelCreate(event: TextChannelCreateEvent) {
        
    }
    
    override fun onVoiceChannelDelete(event: VoiceChannelDeleteEvent) {
        
    }
    
    override fun onVoiceChannelUpdateName(event: VoiceChannelUpdateNameEvent) {
        
    }
    
    override fun onVoiceChannelUpdatePosition(event: VoiceChannelUpdatePositionEvent) {
        
    }
    
    override fun onVoiceChannelUpdateUserLimit(event: VoiceChannelUpdateUserLimitEvent) {
        
    }
    
    override fun onVoiceChannelUpdateBitrate(event: VoiceChannelUpdateBitrateEvent) {
        
    }
    
    override fun onVoiceChannelUpdateParent(event: VoiceChannelUpdateParentEvent) {
        
    }
    
    override fun onVoiceChannelCreate(event: VoiceChannelCreateEvent) {
        
    }
    
    override fun onCategoryDelete(event: CategoryDeleteEvent) {
        
    }
    
    override fun onCategoryUpdateName(event: CategoryUpdateNameEvent) {
        
    }
    
    override fun onCategoryUpdatePosition(event: CategoryUpdatePositionEvent) {
        
    }
    
    override fun onCategoryCreate(event: CategoryCreateEvent) {
        
    }
    
    override fun onGuildJoin(event: GuildJoinEvent) {
        
    }
    
    override fun onGuildLeave(event: GuildLeaveEvent) {
        
    }
    
    override fun onGuildAvailable(event: GuildAvailableEvent) {
        
    }
    
    override fun onGuildUnavailable(event: GuildUnavailableEvent) {
        
    }
    
    override fun onUnavailableGuildJoined(event: UnavailableGuildJoinedEvent) {
        
    }
    
    override fun onUnavailableGuildLeave(event: UnavailableGuildLeaveEvent) {
        
    }
    
    override fun onGuildBan(event: GuildBanEvent) {
        
    }
    
    override fun onGuildUnban(event: GuildUnbanEvent) {
        
    }
    
    override fun onGuildMemberRemove(event: GuildMemberRemoveEvent) {
        
    }
    
    override fun onGuildUpdateAfkChannel(event: GuildUpdateAfkChannelEvent) {
        
    }
    
    override fun onGuildUpdateSystemChannel(event: GuildUpdateSystemChannelEvent) {
        
    }
    
    override fun onGuildUpdateRulesChannel(event: GuildUpdateRulesChannelEvent) {
        
    }
    
    override fun onGuildUpdateCommunityUpdatesChannel(event: GuildUpdateCommunityUpdatesChannelEvent) {
        
    }
    
    override fun onGuildUpdateAfkTimeout(event: GuildUpdateAfkTimeoutEvent) {
        
    }
    
    override fun onGuildUpdateExplicitContentLevel(event: GuildUpdateExplicitContentLevelEvent) {
        
    }
    
    override fun onGuildUpdateIcon(event: GuildUpdateIconEvent) {
        
    }
    
    override fun onGuildUpdateMFALevel(event: GuildUpdateMFALevelEvent) {
        
    }
    
    override fun onGuildUpdateName(event: GuildUpdateNameEvent) {
        
    }
    
    override fun onGuildUpdateNotificationLevel(event: GuildUpdateNotificationLevelEvent) {
        
    }
    
    override fun onGuildUpdateOwner(event: GuildUpdateOwnerEvent) {
        
    }
    
    override fun onGuildUpdateRegion(event: GuildUpdateRegionEvent) {
        
    }
    
    override fun onGuildUpdateSplash(event: GuildUpdateSplashEvent) {
        
    }
    
    override fun onGuildUpdateVerificationLevel(event: GuildUpdateVerificationLevelEvent) {
        
    }
    
    override fun onGuildUpdateLocale(event: GuildUpdateLocaleEvent) {
        
    }
    
    override fun onGuildUpdateFeatures(event: GuildUpdateFeaturesEvent) {
        
    }
    
    override fun onGuildUpdateVanityCode(event: GuildUpdateVanityCodeEvent) {
        
    }
    
    override fun onGuildUpdateBanner(event: GuildUpdateBannerEvent) {
        
    }
    
    override fun onGuildUpdateDescription(event: GuildUpdateDescriptionEvent) {
        
    }
    
    override fun onGuildUpdateBoostTier(event: GuildUpdateBoostTierEvent) {
        
    }
    
    override fun onGuildUpdateBoostCount(event: GuildUpdateBoostCountEvent) {
        
    }
    
    override fun onGuildUpdateMaxMembers(event: GuildUpdateMaxMembersEvent) {
        
    }
    
    override fun onGuildUpdateMaxPresences(event: GuildUpdateMaxPresencesEvent) {
        
    }
    
    override fun onGuildInviteCreate(event: GuildInviteCreateEvent) {
        
    }
    
    override fun onGuildInviteDelete(event: GuildInviteDeleteEvent) {
        
    }
    
    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        
    }
    
    override fun onGuildMemberRoleAdd(event: GuildMemberRoleAddEvent) {
        
    }
    
    override fun onGuildMemberRoleRemove(event: GuildMemberRoleRemoveEvent) {
        
    }
    
    override fun onGuildMemberUpdate(event: GuildMemberUpdateEvent) {
        
    }
    
    override fun onGuildMemberUpdateNickname(event: GuildMemberUpdateNicknameEvent) {
        
    }
    
    override fun onGuildMemberUpdateBoostTime(event: GuildMemberUpdateBoostTimeEvent) {
        
    }
    
    override fun onGuildMemberUpdatePending(event: GuildMemberUpdatePendingEvent) {
        
    }
    
    override fun onGuildVoiceJoin(event: GuildVoiceJoinEvent) {
        
    }
    
    override fun onGuildVoiceMove(event: GuildVoiceMoveEvent) {
        
    }
    
    override fun onGuildVoiceLeave(event: GuildVoiceLeaveEvent) {
        
    }
    
    override fun onGuildVoiceMute(event: GuildVoiceMuteEvent) {
        
    }
    
    override fun onGuildVoiceDeafen(event: GuildVoiceDeafenEvent) {
        
    }
    
    override fun onGuildVoiceGuildMute(event: GuildVoiceGuildMuteEvent) {
        
    }
    
    override fun onGuildVoiceGuildDeafen(event: GuildVoiceGuildDeafenEvent) {
        
    }
    
    override fun onGuildVoiceSuppress(event: GuildVoiceSuppressEvent) {
        
    }
    
    override fun onGuildVoiceStream(event: GuildVoiceStreamEvent) {
        
    }
    
    override fun onRoleCreate(event: RoleCreateEvent) {
        
    }
    
    override fun onRoleDelete(event: RoleDeleteEvent) {
        
    }
    
    override fun onRoleUpdateColor(event: RoleUpdateColorEvent) {
        
    }
    
    override fun onRoleUpdateHoisted(event: RoleUpdateHoistedEvent) {
        
    }
    
    override fun onRoleUpdateMentionable(event: RoleUpdateMentionableEvent) {
        
    }
    
    override fun onRoleUpdateName(event: RoleUpdateNameEvent) {
        
    }
    
    override fun onRoleUpdatePermissions(event: RoleUpdatePermissionsEvent) {
        
    }
    
    override fun onRoleUpdatePosition(event: RoleUpdatePositionEvent) {
        
    }
    
    override fun onEmoteAdded(event: EmoteAddedEvent) {
        
    }
    
    override fun onEmoteRemoved(event: EmoteRemovedEvent) {
        
    }
    
    override fun onEmoteUpdateName(event: EmoteUpdateNameEvent) {
        
    }
    
    override fun onEmoteUpdateRoles(event: EmoteUpdateRolesEvent) {
        
    }
}