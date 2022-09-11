/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyPluginManagerImplTest.kt is part of PolyBot
 * Last modified on 10-09-2022 08:50 p.m.
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

package ca.solostudios.polybot.plugin

import ca.solostudios.polybot.api.PolyBot
import ca.solostudios.polybot.api.plugin.finder.PluginCandidateFinder
import ca.solostudios.polybot.api.plugin.info.PluginInfo
import ca.solostudios.polybot.api.plugin.loader.PolyClassLoader
import ca.solostudios.polybot.impl.plugin.PolyPluginManagerImpl
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import java.nio.file.Files
import java.nio.file.Path
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream
import java.util.zip.ZipFile
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.io.TempDir

@ExtendWith(MockKExtension::class)
class PolyPluginManagerImplTest {
    @TempDir
    lateinit var tempDir: Path
    
    @RelaxedMockK
    lateinit var polybot: PolyBot
    
    val classLoader = PolyClassLoader(javaClass.classLoader)
    
    @BeforeEach
    fun beforeTests() {
        every { polybot.classLoader } returns classLoader
    }
    
    @Test
    fun `test resolve candidate from jar`() {
        val jarFile = tempDir.resolve("test.jar")
        jarFile.openJar().use {
            it.putNextEntry(JarEntry(PluginInfo.PLUGIN_INFO_FILE))
            it.write(
                    """
                        {
                            "group": "ca.solo-studios.test",
                            "id": "test",
                            "version": "0.0.0"
                        }
                   """.trimIndent().toByteArray()
                    )
            it.closeEntry()
        }
        
        
        ZipFile(jarFile.toFile()).use {
            println("entries: ${it.entries()}")
            for (entry in it.entries())
                println("entry ${entry.name}: ${it.getInputStream(entry).bufferedReader().readText()}")
        }
        
        val jarCandidateFinder = PluginCandidateFinder {
            listOf(jarFile)
        }
        val pluginManager = PolyPluginManagerImpl(polybot, listOf(jarCandidateFinder))
        
        assertDoesNotThrow {
            runBlocking {
                pluginManager.initPlugins()
            }
        }
    }
    
    private fun Path.openJar(): JarOutputStream {
        return JarOutputStream(Files.newOutputStream(this))
    }
}