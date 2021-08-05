/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Constants.kt is part of PolyhedralBot
 * Last modified on 04-08-2021 08:23 p.m.
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

@file:Suppress("unused")

package com.solostudios.polybot

import com.solostudios.polybot.Constants.InternalConstants.discordVanityDomainRegex
import com.solostudios.polybot.Constants.InternalConstants.inviteRegexString
import com.solostudios.polybot.Constants.InternalConstants.messageLinkRegexString
import org.intellij.lang.annotations.Language

@Suppress("MemberVisibilityCanBePrivate", "RegExpUnnecessaryNonCapturingGroup", "RegExpRedundantEscape")
object Constants {
    
    const val logEmbedColour = 0x00FF00
    
    const val defaultUsername = "Unknown User#0000"
    private const val defaultAvatarId = 0
    const val defaultAvatarUrl = "https://cdn.discordapp.com/embed/avatars/$defaultAvatarId.png"
    
    const val configFile = "polybot.conf"
    
    const val defaultConfig = "/default.conf"
    
    @Language("RegExp")
    const val allInviteRegexString = """(?:$inviteRegexString|$discordVanityDomainRegex\/[a-zA-Z\d.~:/?#@!$&'()*+,;%=\[\]\-]+)"""
    
    
    @JvmField
    val messageLinkRegex = Regex(messageLinkRegexString, RegexOption.IGNORE_CASE)
    
    @JvmField
    val inviteRegex = Regex(inviteRegexString)
    
    @JvmField
    val allInviteRegex = Regex(allInviteRegexString)
    
    const val botVersionMajor = "@versionMajor@"
    const val botVersionMinor = "@versionMinor@"
    const val botVersionPatch = "@versionRevision@"
    const val botGitHash = "@gitHash@"
    val botVersion: String = if (isDevBuild()) "$botGitHash-dev" else "$botVersionMajor.$botVersionMinor.$botVersionPatch+$botGitHash"
    
    fun isDevBuild() = botVersionMajor.startsWith('@')
    
    private object InternalConstants {
        @Language("RegExp")
        const val protocol = "(?:https?:\\/\\/)"
        
        @Language("RegExp")
        const val domainPrefix = "$protocol?(?:[^\\s]*\\.)?"
        
        @Language("RegExp") // why are there so many discord domains.... bruh.
        const val discordDomainRegex = """$domainPrefix(?:discord(?:\.com|\.gg|\.co|app\.com)|watchanimeattheoffice\.com|dis\.gd)"""
        
        @Language("RegExp")
        const val messageLinkRegexString = "$discordDomainRegex\\/channels\\/" +
                "(?<guild>\\d+)\\/" +   // Guild ID
                "(?<channel>\\d+)\\/" + // Channel ID
                "(?<message>\\d+)\\/" + // Message ID
                "?(?:\\?\\S*|#\\S*)?"       // Useless bullshit
        
        @Language("RegExp")
        const val inviteRegexString = "$discordDomainRegex(?:\\/invite)?\\/(?<invite>[a-z0-9-]+)(?:\\?\\S*)?(?:#\\S*)?"
        
        @Language("RegExp")
        const val discordVanityDomainRegex = """$domainPrefix(?:dsc\.gg|invite\.gg|discordvanity\.com|discord\.(?:plus|link|io|me|li|st))"""
    }
}