/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file SearchManager.kt is part of PolyhedralBot
 * Last modified on 30-07-2021 09:20 p.m.
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

package com.solostudios.polybot.search

import com.solostudios.polybot.PolyBot
import com.solostudios.polybot.config.search.GithubWikiSearchLocation
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.store.NRTCachingDirectory
import org.slf4j.kotlin.error
import org.slf4j.kotlin.getLogger
import kotlin.io.path.Path


@Suppress("MemberVisibilityCanBePrivate")
class SearchManager(val bot: PolyBot) {
    private val logger by getLogger()
    
    val searchIndex: Map<String, Index>
    
    val defaultIndex: Index
    
    init {
        val searchConfig = bot.config.searchConfig
        val indexes = mutableMapOf<String, Index>()
        var default: Index? = null
        
        for (location in searchConfig.searchLocations) {
            when (location) {
                is GithubWikiSearchLocation -> {
                    val diskCache = FSDirectory.open(Path(searchConfig.searchCache, location.name))
                    val diskRamCache = NRTCachingDirectory(diskCache, 5.0, 60.0)
                    
                    val index = GithubWikiIndex(location, diskRamCache)
                    indexes[location.name] = index
                    
                    if (location.name == searchConfig.default)
                        default = index
                }
                
                else                        -> {
                    logger.error { "Search location ${location.name} not a known subtype" }
                }
            }
        }
        
        searchIndex = indexes
        defaultIndex = default ?: throw NullPointerException("Could not find default index value.")
    }
}