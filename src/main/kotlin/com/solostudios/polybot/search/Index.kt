/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Index.kt is part of PolyhedralBot
 * Last modified on 30-07-2021 10:09 p.m.
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
import java.io.Closeable
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

abstract class Index(analyzer: Analyzer, cacheDirectory: Directory) : Closeable {
    val queryParserHelper = StandardQueryParser(analyzer).also {
        it.allowLeadingWildcard = true
        it.locale = Locale.US
        it.timeZone = TimeZone.getTimeZone(ZoneId.of("America/New_York"))
    }
    
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
    
    protected abstract val searchLocation: SearchLocation
    
    fun search(searchQuery: String, maxResults: Int): List<Document> {
        return indexSearcher.search(searchQuery.query(), maxResults).scoreDocs.map { indexSearcher.doc(it.doc) }
    }
    
    fun count(searchQuery: String): Int = indexSearcher.count(searchQuery.query())
    
    fun updateIndex() {
        updateIndex(indexWriter)
    }
    
    abstract fun updateIndex(writer: IndexWriter)
    
    override fun close() {
        indexReader.close()
        indexWriter.commit()
        indexWriter.close()
    }
    
    private fun String.query(): Query = queryParserHelper.parse(this, "body")
}