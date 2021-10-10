/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file CacheManager.kt is part of PolyhedralBot
 * Last modified on 09-10-2021 09:58 p.m.
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

package com.solostudios.polybot.cache

import com.solostudios.polybot.PolyBot
import com.solostudios.polybot.util.ShutdownService
import com.solostudios.polybot.util.cacheConfigBuilder
import com.solostudios.polybot.util.getCache
import com.solostudios.polybot.util.withKotlinValueSerializer
import org.ehcache.PersistentCacheManager
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import org.ehcache.config.units.EntryUnit
import org.ehcache.config.units.MemoryUnit

@Suppress("MemberVisibilityCanBePrivate")
class CacheManager(val bot: PolyBot) : ShutdownService() {
    val cacheManager: PersistentCacheManager = CacheManagerBuilder.newCacheManagerBuilder()
            .with(CacheManagerBuilder.persistence(bot.getCacheDirectory("ehcache").toFile()))
            .withCache("messages", cacheConfigBuilder<Long, CachedMessage>(ResourcePoolsBuilder.newResourcePoolsBuilder()
                                                                                   .heap(10_000L, EntryUnit.ENTRIES)
                                                                                   .offheap(500L, MemoryUnit.MB)
                                                                                   .disk(1L, MemoryUnit.GB, true))
                    // .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofDays(56L)))
                    .withKotlinValueSerializer())
            .build(true)
    
    // init {
    //     cacheManager.init()
    // }
    
    val messageCache = MessageCache(this, cacheManager.getCache("messages"))
    
    override fun serviceShutdown() {
        cacheManager.close()
    }
}