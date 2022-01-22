/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Snowflake.kt is part of PolyhedralBot
 * Last modified on 21-01-2022 01:44 p.m.
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

import kotlinx.datetime.Instant

/**
 * snowflake
 *
 * Note: this class has a natural ordering that is inconsistent with [equals],
 * since [compareTo] only compares the first 42 bits of the ULong [value] (comparing the timestamp),
 * whereas [equals] uses all bits of the ULong [value].
 * [compareTo] can return `0` even if [equals] returns `false`,
 * but [equals] only returns `true` if [compareTo] returns `0`.
 *
 * @property id
 * @constructor Create a snowflake with the specified id
 */
public class Snowflake public constructor(public val id: ULong) : Comparable<Snowflake> {
    public val idLong: Long
        get() = id.toLong()
    
    public val idString: String
        get() = id.toString()
    
    public val instant: Instant
        get() = Instant.fromEpochMilliseconds(DISCORD_EPOCH_LONG + (id shr TIMESTAMP_OFFSET).toLong())
    
    public constructor(id: String) : this(id.toULong())
    
    public constructor(instant: Instant) : this(instant.toEpochMilliseconds().minus(DISCORD_EPOCH_LONG).toULong() shl TIMESTAMP_OFFSET)
    
    override fun compareTo(other: Snowflake): Int = millisSinceEpoch.compareTo(other.millisSinceEpoch)
    
    private val millisSinceEpoch: ULong
        get() = id shr TIMESTAMP_OFFSET
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        
        other as SnowflakeEntity
        
        if (id != other.id) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        return id.hashCode()
    }
    
    override fun toString(): String {
        return "PolySnowflake(id=$id)"
    }
    
    public companion object {
        public const val DISCORD_EPOCH_LONG: Long = 1420070400000L
        
        public val DISCORD_EPOCH: Instant = Instant.fromEpochMilliseconds(DISCORD_EPOCH_LONG)
        
        public const val TIMESTAMP_OFFSET: Int = 22
        
        public fun Long.snowflake(): Snowflake = Snowflake(this.toULong())
        
        public fun ULong.snowflake(): Snowflake = Snowflake(this)
    }
}