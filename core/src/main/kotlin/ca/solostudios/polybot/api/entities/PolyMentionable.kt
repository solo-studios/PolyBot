/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyMentionable.kt is part of PolyhedralBot
 * Last modified on 07-02-2022 01:12 a.m.
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

/**
 * Marks an entity as mentionable.
 *
 * A mentionable entity is one that can be formatted as a string, which can be resolved to the entity.
 *
 * An example of this is:
 * - @user mentions on github.
 *   A user can be formatted as `"@" + the_username`, and it will resolve to a mention for the user.
 */
public interface PolyMentionable {
    /**
     * Formats this entity as a resolvable mention, and returns the corresponding string.
     */
    public val asMention: String
    
    /**
     * Formats this entity as a resolvable mention, and returns the corresponding string.
     *
     * @return This entity, formatted as a mention
     * @see asMention
     */
    public override fun toString(): String
}