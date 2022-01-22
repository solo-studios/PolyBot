/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyUser.kt is part of PolyhedralBot
 * Last modified on 21-01-2022 04:50 p.m.
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

import ca.solostudios.polybot.api.PolyObject
import ca.solostudios.polybot.api.data.PolyImage

public interface PolyUser : PolySnowflakeEntity, Mentionable {
    public val name: String
    
    public val discriminator: Int
    
    public val discriminatorString: String
    
    public val asTag: String
    
    public val mutualGuilds: List<PolyGuild>
    
    public val isBot: Boolean
    
    public val isSystem: Boolean
    
    public interface Avatar : PolyObject {
        
        public val user: PolyUser
        
        public val url: String
        
        public val defaultUrl: String
        
        public val isCustom: Boolean
        
        public val isAnimated: Boolean
            get() = url.startsWith("a")
        
        public val format: PolyImage.Format
            get() = when {
                isAnimated -> PolyImage.Format.GIF
                else       -> PolyImage.Format.PNG
            }
        
        public fun getUrl(format: PolyImage.Format): String
    }
}