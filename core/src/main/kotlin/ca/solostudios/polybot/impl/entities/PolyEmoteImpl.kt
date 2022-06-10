/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyEmoteImpl.kt is part of PolyBot
 * Last modified on 10-06-2022 11:32 a.m.
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
 * POLYBOT IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ca.solostudios.polybot.impl.entities

import ca.solostudios.polybot.api.PolyBot
import ca.solostudios.polybot.api.entities.PolyEmote
import ca.solostudios.polybot.api.entities.PolyGuild
import ca.solostudios.polybot.api.entities.PolyRole
import ca.solostudios.polybot.api.entities.Snowflake
import ca.solostudios.polybot.api.entities.Snowflake.Companion.snowflake
import ca.solostudios.polybot.api.util.ext.poly
import dev.minn.jda.ktx.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import net.dv8tion.jda.api.entities.Emote

internal class PolyEmoteImpl(
        override val bot: PolyBot,
        override val jdaEmote: Emote,
                            ) : PolyEmote {
    override val snowflake: Snowflake by lazy { jdaEmote.idLong.snowflake() }
    
    override val guild: PolyGuild?
        get() = jdaEmote.guild?.poly(bot)
    override val roles: Flow<PolyRole>
        get() = jdaEmote.roles.asFlow().map { it.poly(bot) }
    override val providesRoles: Boolean
        get() = jdaEmote.canProvideRoles()
    override val name: String
        get() = jdaEmote.name
    override val isManaged: Boolean
        get() = jdaEmote.isManaged
    override val isAvailable: Boolean
        get() = jdaEmote.isAvailable
    override val isAnimated: Boolean
        get() = jdaEmote.isAnimated
    override val imageUrl: String
        get() = jdaEmote.imageUrl
    
    override suspend fun delete(reason: String?) {
        jdaEmote.delete()
                .await()
    }
    
    override val asMention: String
        get() = jdaEmote.asMention
    
    override fun toString(): String = asMention
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PolyEmoteImpl) return false
        
        if (jdaEmote != other.jdaEmote) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        return jdaEmote.hashCode()
    }
}