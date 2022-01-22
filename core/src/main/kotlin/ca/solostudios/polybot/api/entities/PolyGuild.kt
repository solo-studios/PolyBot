/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyGuild.kt is part of PolyhedralBot
 * Last modified on 22-01-2022 05:19 p.m.
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

package ca.solostudios.polybot.api.entities

import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration

public interface PolyGuild : PolySnowflakeEntity {
    public val loaded: Boolean
    
    public val memberCount: Int
    
    public val name: String
    
    public val iconId: String?
    
    public val iconUrl: String?
    
    public val features: Set<String>
    
    public val splashId: String?
    
    public val splashUrl: String?
    
    public val vanityCode: String?
    
    public val vanityUrl: String?
    
    public val description: String?
    
    public val bannerId: String?
    
    public val bannerUrl: String?
    
    public val boostTier: BoostTier
    
    public val boostCount: Int
    
    public val boosters: Flow<PolyMember>
    
    public val maxBitrate: Int
    
    public val maxFileSize: Long
    
    public val maxMembers: Int
    
    public val afkChannel: PolyPrivateChannel?
    
    public val systemChannel: PolyTextChannel?
    
    public val communityUpdatesChannel: PolyTextChannel?
    
    public val owner: PolyMember?
    
    public val afkTimeout: Duration
    
    public val selfMember: PolyMember
    
    public enum class BoostTier(
            public val id: Int,
            public val maxBitrate: Int,
            public val maxEmotes: Int,
                               ) {
        NONE(0, 96000, 50),
        TIER_1(1, 128000, 100),
        TIER_2(2, 256000, 150),
        TIER_3(3, 384000, 250),
        
        UNKNOWN(-1, Int.MAX_VALUE, Int.MAX_VALUE);
        
        public companion object {
            public fun fromId(id: Int): BoostTier {
                for (tier in values())
                    if (tier.id == id)
                        return tier
                
                return UNKNOWN
            }
        }
    }
}