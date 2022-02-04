/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyCategory.kt is part of PolyhedralBot
 * Last modified on 03-02-2022 08:28 p.m.
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

import net.dv8tion.jda.api.entities.Category

/**
 * Represents a discord category.
 *
 * This is a wrapper for a [JDA category][Category].
 *
 * Categories are used in guilds to organize channels into groups.
 * A channel may or may not have a parent category.
 *
 * @see PolyGuildChannel
 */
public interface PolyCategory : PolyGuildChannel {
    /**
     * The JDA category that is being wrapped by this entity
     */
    public override val jdaChannel: Category
    
    /**
     * The JDA category that is being wrapped by this entity
     */
    public val jdaCategory: Category
        get() = jdaChannel
    
    /**
     * A list of all guild channels that belong to this category
     *
     * @see PolyGuildChannel
     */
    public val guildChannels: List<PolyGuildChannel>
    
    /**
     * A list of all text channels that belong to this category
     *
     * @see PolyTextChannel
     */
    public val textChannels: List<PolyTextChannel>
    
    /**
     * A list of all voice channels that belong to this category
     *
     * @see PolyVoiceChannel
     */
    public val voiceChannels: List<PolyVoiceChannel>
}