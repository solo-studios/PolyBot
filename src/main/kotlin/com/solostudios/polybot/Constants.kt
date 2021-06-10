/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Constants.kt is part of PolyhedralBot
 * Last modified on 09-06-2021 07:35 p.m.
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

import org.intellij.lang.annotations.Language

@Suppress("RegExpRedundantEscape", "MemberVisibilityCanBePrivate", "RegExpUnnecessaryNonCapturingGroup")
object Constants {
    
    @Language("RegExp") // why are there so many discord domains.... bruh.
    private const val discordDomainRegex =
        """(?:(?:https|http):\/\/)?(?:(?:ptb|canary|staging)\.)?(?:discord\.(?:com?|gg)|discordapp\.com|watchanimeattheoffice\.com|dis\.gd)"""
    
    @Language("RegExp")
    private const val messageLinkRegexString =
        """${discordDomainRegex}\/channels\/(?<guild>\d{15,21})\/(?<channel>\d{15,21})\/(?<message>\d{15,21})\/?"""
    
    @Language("RegExp")
    private const val inviteRegexString = """${discordDomainRegex}(?:\/invite)?\/(?<invite>[a-zA-Z\d-]+)"""
    
    @Language("RegExp")
    private const val discordVanityDomainRegex =
        """(?:${discordDomainRegex}|(?:https:\/\/|http:\/\/)?(?:dsc\.gg|invite\.gg|discordvanity\.com|discord\.(?:plus|link|io|me|li|st)))"""
    
    @Language("RegExp")
    const val allInviteRegexString =
        """(?:${inviteRegexString}|${discordVanityDomainRegex}\/[\w\.\~\:\/\?\#\[\]\@\!\$\&\'\(\)\*\+\,\;\%\=\-]+)"""
    
    
    @JvmField
    val messageLinkRegex = Regex(messageLinkRegexString)
    
    @JvmField
    val inviteRegex = Regex(inviteRegexString)
    
    @JvmField
    val allInviteRegex = Regex(allInviteRegexString)
    
    const val botVersionMajor = "@versionMajor@"
    const val botVersionMinor = "@versionMinor@"
    const val botVersionPatch = "@versionRevision@"
    const val botGitHash = "@gitHash@"
    val botVersion: String = if (isDevBuild()) "dev" else "$botVersionMajor.$botVersionMinor.$botVersionPatch+$botGitHash"
    
    fun isDevBuild() = botVersionMajor.startsWith("@")
}