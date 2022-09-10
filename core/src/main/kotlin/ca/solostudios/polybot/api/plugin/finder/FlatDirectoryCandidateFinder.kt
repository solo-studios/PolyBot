/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file FlatDirectoryCandidateFinder.kt is part of PolyBot
 * Last modified on 10-09-2022 03:20 p.m.
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

package ca.solostudios.polybot.api.plugin.finder

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import org.slf4j.kotlin.*
import kotlin.io.path.absolute
import kotlin.io.path.createDirectory
import kotlin.io.path.exists
import kotlin.io.path.extension
import kotlin.io.path.isDirectory
import kotlin.io.path.isHidden
import kotlin.io.path.isRegularFile
import kotlin.io.path.name
import kotlin.streams.toList

public class FlatDirectoryCandidateFinder(
        private val path: Path
                                         ) : PluginCandidateFinder {
    private val logger by getLogger()
    
    @Throws(IOException::class, RuntimeException::class)
    override fun findCandidates(): List<Path> {
        logger.debug { "Here is the current path ${path.absolute()}" }
        if (!path.exists()) {
            try {
                path.createDirectory()
            } catch (e: IOException) {
                throw RuntimeException("Could not create directory '$path'.")
            }
        }
        
        if (!path.isDirectory()) {
            error("Path '$path' is not a directory. It must be a directory for plugins to resolve properly.")
        }
        
        try {
            return Files.walk(path, 1)
                    .filter {
                        path.isValid()
                    }
                    .toList()
        } catch (e: IOException) {
            throw RuntimeException("Exception while searching path '$path' for plugins.", e)
        }
    }
    
    private fun Path.isValid(): Boolean {
        if (!isRegularFile())
            return false
        
        try {
            if (isHidden())
                return false
        } catch (e: IOException) {
            logger.warn(e) { "Error occurred while checking if the file '$this' is hidden." }
        }
    
        return extension == "jar" && !name.startsWith('.')
    }
}