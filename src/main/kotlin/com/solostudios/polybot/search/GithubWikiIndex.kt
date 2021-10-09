/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file GithubWikiIndex.kt is part of PolyhedralBot
 * Last modified on 09-10-2021 06:06 p.m.
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
import com.github.kittinunf.fuel.coroutines.awaitByteArray
import com.solostudios.polybot.PolyBot
import com.solostudios.polybot.config.search.GithubWikiSearchLocation
import com.solostudios.polybot.util.MarkdownHeaderVisitor
import com.solostudios.polybot.util.get
import java.io.InputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.compress.archivers.ArchiveEntry
import org.apache.commons.compress.archivers.ArchiveInputStream
import org.apache.commons.compress.archivers.ArchiveStreamFactory
import org.apache.commons.compress.compressors.CompressorException
import org.apache.commons.compress.compressors.CompressorStreamFactory
import org.apache.commons.io.FilenameUtils
import org.apache.lucene.analysis.en.EnglishAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.StoredField
import org.apache.lucene.document.TextField
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.store.Directory
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser
import org.slf4j.kotlin.*


class GithubWikiIndex(
        val bot: PolyBot,
        override val searchLocation: GithubWikiSearchLocation,
        cacheDirectory: Directory,
                     ) : Index<GithubWikiResult>(EnglishAnalyzer(),
                                                 cacheDirectory,
                                                 mapOf(
                                                         HUMAN_NAME to 25.0f,
                                                         HEADERS to 15.0f,
                                                         BODY to 1.0f,
                                                      )) {
    private val logger by getLogger()
    private val fuel = FuelManager()
    
    private val githubWiki = "https://github.com/${searchLocation.repoOwner}/${searchLocation.repoName}/wiki"
    private val githubWikiRepo = "https://github.com/${searchLocation.repoOwner}/${searchLocation.wikiRepo}/archive/master"
    private val githubWikiTarball = "$githubWikiRepo.tar.gz"
    private val githubWikiZipball = "$githubWikiRepo.zip"
    
    override suspend fun updateIndex(writer: IndexWriter) {
        val bytes = fuel.get(githubWikiTarball).awaitByteArray()
        
        val input = bytes.inputStream().archiveStream()
        
        var entry: ArchiveEntry?
        while (input.nextEntry.also { entry = it } != null) {
            val name = FilenameUtils.getBaseName(entry?.name)
            val extension = FilenameUtils.getExtension(entry?.name)
            
            when {
                !input.canReadEntryData(entry)             -> continue
                entry?.isDirectory == true                 -> continue
                !extension.equals("md", ignoreCase = true) -> continue
                name.startsWith("_")                       -> continue
                
                else                                       -> {
                    val humanName = name.replace('-', ' ').replace('_', ' ')
                    
                    logger.info { "Creating index for $name.$extension" }
                    
                    val text = input.bufferedReader().readText()
                    val document = Document()
                    val markdown = markdownParser.buildMarkdownTreeFromString(text)
                    val visitor = MarkdownHeaderVisitor().apply { visitNode(markdown) }
                    val headers = visitor.headers.joinToString(separator = "\n") { text[it.first, it.last] }
                    
                    document.add(StoredField(NAME, name))
                    
                    document.add(TextField(HUMAN_NAME, humanName, Field.Store.YES))
                    document.add(TextField(BODY, text, Field.Store.NO))
                    document.add(TextField(HEADERS, headers, Field.Store.NO))
                    
                    
                    @Suppress("BlockingMethodInNonBlockingContext")
                    withContext(Dispatchers.IO) { writer.addDocument(document) }
                }
            }
        }
    }
    
    
    override fun mapDocument(document: Document): GithubWikiResult = GithubWikiResult(document[HUMAN_NAME], document[NAME], githubWiki)
    
    companion object {
        const val NAME = "name"
        const val BODY = "body"
        const val HEADERS = "headers"
        
        const val HUMAN_NAME = "human_name"
        private val markdownFlavour = CommonMarkFlavourDescriptor()
        
        private val markdownParser = MarkdownParser(markdownFlavour)
        private val archiveStreamFactory = ArchiveStreamFactory()
        private val compressorStreamFactory = CompressorStreamFactory()
    }
    
    private fun InputStream.archiveStream(): ArchiveInputStream {
        val buffered = this.buffered()
        val stream = try {
            compressorStreamFactory.createCompressorInputStream(buffered)
        } catch (e: CompressorException) {
            buffered
        }
        
        return archiveStreamFactory.createArchiveInputStream(stream.buffered())
    }
    
}

data class GithubWikiResult(val humanName: String, val name: String, val baseUrl: String) : Result {
    override val body: String
        get() = humanName
    override val title: String
        get() = url
    override val simple: String
        get() = "<$url>"
    
    val url: String
        get() = "$baseUrl/$name"
}
