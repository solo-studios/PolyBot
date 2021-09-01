/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file GithubWikiIndex.kt is part of PolyhedralBot
 * Last modified on 01-09-2021 06:18 p.m.
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

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.coroutines.awaitUnit
import com.solostudios.polybot.PolyBot
import com.solostudios.polybot.config.search.GithubWikiSearchLocation
import java.io.File
import java.util.zip.ZipFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.store.Directory

class GithubWikiIndex(
        val bot: PolyBot,
        override val searchLocation: GithubWikiSearchLocation,
        cacheDirectory: Directory,
                     ) : Index(StandardAnalyzer(), cacheDirectory) {
    private val fuel = FuelManager()
    
    private val githubWiki = "https://github.com/${searchLocation.repoOwner}/${searchLocation.repoName}/archive/master"
    private val githubWikiTarball = "$githubWiki.tar.gz"
    private val githubWikiZip = "$githubWiki.zip"
    
    override suspend fun updateIndex(writer: IndexWriter) {
        @Suppress("BlockingMethodInNonBlockingContext")
        withContext(Dispatchers.IO) {
            val temp = File.createTempFile("github_wiki", "zip")
            
            fuel.download(githubWikiTarball).fileDestination { _, _ -> temp }.awaitUnit()
            
            
            ZipFile(temp).use { zip ->
                zip.entries().asSequence().forEach { entry ->
                    zip.getInputStream(entry).use { input ->
                    
                    }
                }
            }
            
        }
    }
    
}