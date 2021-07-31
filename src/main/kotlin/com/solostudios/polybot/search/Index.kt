/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Index.kt is part of PolyhedralBot
 * Last modified on 31-07-2021 02:24 a.m.
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

import com.solostudios.polybot.config.search.SearchLocation
import com.solostudios.polybot.service.ShutdownService
import com.solostudios.polybot.util.shortFormat
import java.time.ZoneId
import java.util.Locale
import java.util.TimeZone
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.document.Document
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.Query
import org.apache.lucene.store.Directory
import org.slf4j.kotlin.getLogger
import org.slf4j.kotlin.info
import kotlin.time.Duration
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

abstract class Index(analyzer: Analyzer, cacheDirectory: Directory) : ShutdownService() {
    private val logger by getLogger()
    
    private val indexWriterConfig = IndexWriterConfig(analyzer).setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND)
    private val indexWriter = IndexWriter(cacheDirectory, indexWriterConfig)
    private var indexReader: DirectoryReader = DirectoryReader.open(indexWriter)
        get() {
            val reader: DirectoryReader? = DirectoryReader.openIfChanged(field, indexWriter)
            if (reader != null) {
                field.close()
                field = reader
            }
            
            return field
        }
    private var indexSearcher = IndexSearcher(indexReader)
        get() {
            return if (indexReader == field.indexReader)
                field
            else {
                field = IndexSearcher(indexReader)
                field
            }
        }
    private val queryParserHelper = StandardQueryParser(analyzer).also {
        it.allowLeadingWildcard = true
        it.locale = Locale.US
        it.timeZone = TimeZone.getTimeZone(ZoneId.of("America/New_York"))
    }
    
    fun search(searchQuery: String, maxResults: Int): List<Document> {
        val (results: List<Document>, duration: Duration) = measureTimedValue {
            return@measureTimedValue indexSearcher.search(searchQuery.query(), maxResults).scoreDocs.map { indexSearcher.doc(it.doc) }
        }
        
        logger.info { "Search query $searchQuery took ${duration.shortFormat()} to execute, returning ${results.size} results." }
        
        return results
    }
    
    fun count(searchQuery: String): Int {
        val (results: Int, duration: Duration) = measureTimedValue {
            return@measureTimedValue indexSearcher.count(searchQuery.query())
        }
        
        logger.info { "Count query $searchQuery took ${duration.shortFormat()} to execute, returning a count of $results results." }
        
        return results
    }
    
    fun updateIndex() {
        val duration = measureTime {
            updateIndex(indexWriter)
        }
        
        logger.info { "Took ${duration.shortFormat()} to update the ${searchLocation.name} index." }
    }
    
    protected abstract val searchLocation: SearchLocation
    
    abstract fun updateIndex(writer: IndexWriter)
    
    override fun shutdown() {
        indexReader.close()
        indexWriter.commit()
        indexWriter.close()
        
        super.shutdown()
    }
    
    private fun String.query(): Query = queryParserHelper.parse(this, "body")
}
