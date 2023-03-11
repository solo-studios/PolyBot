/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyPluginManagerImplTest.kt is part of PolyBot
 * Last modified on 10-03-2023 03:29 p.m.
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
import ca.solostudios.polybot.api.plugin.PolyPlugin
import ca.solostudios.polybot.api.plugin.finder.PluginCandidateFinder
import ca.solostudios.polybot.api.plugin.info.PluginInfo
import ca.solostudios.polybot.api.plugin.loader.PolyClassLoader
import ca.solostudios.polybot.api.util.ext.pluginContainer
import ca.solostudios.polybot.impl.plugin.PolyPluginManagerImpl
import ca.solostudios.strata.kotlin.toVersion
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import java.nio.file.Files
import java.nio.file.Path
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.io.TempDir
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.writeBytes
import kotlin.io.path.writeText
import kotlin.test.assertEquals

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
    
    
        val jarCandidateFinder = PluginCandidateFinder {
            listOf(jarFile)
        }
        val pluginManager = PolyPluginManagerImpl(polybot, listOf(jarCandidateFinder))
    
        assertDoesNotThrow {
            runBlocking {
                pluginManager.loadPlugins()
            }
        }
    
        assertEquals(1, pluginManager.plugins.size, "Only 1 plugin should be loaded")
        val pluginContainer = pluginManager.pluginContainer<PolyPlugin>("ca.solo-studios.test", "test")
        assertEquals(
                PluginInfo(group = "ca.solo-studios.test", id = "test", version = "0.0.0".toVersion()),
                pluginContainer.info,
                "Plugin info should be known"
                    )
        assertEquals(0, pluginContainer.entrypoints.size, "Should have no entrypoints")
    }
    
    @Test
    fun `test load plugin class from jar`() {
        val jarFile = tempDir.resolve("test.jar")
        jarFile.openJar().use {
            it.putNextEntry(JarEntry(PluginInfo.PLUGIN_INFO_FILE))
            it.write(
                    """
                        {
                            "group": "ca.solo-studios.test",
                            "id": "test",
                            "version": "0.0.0",
                            "entrypoints": [
                                "ca.solostudios.polybot.plugin.TestPlugin"
                            ]
                        }
                   """.trimIndent().toByteArray()
                    )
            it.closeEntry()
            val className = "ca/solostudios/polybot/plugin/TestPlugin.class"
            it.putNextEntry(JarEntry(className))
            val classBytes = javaClass.classLoader.getResourceAsStream(className)!!.readBytes()
            it.write(classBytes)
            it.closeEntry()
        }
        
        
        val jarCandidateFinder = PluginCandidateFinder {
            listOf(jarFile)
        }
        val pluginManager = PolyPluginManagerImpl(polybot, listOf(jarCandidateFinder))
        
        assertDoesNotThrow {
            runBlocking {
                pluginManager.loadPlugins()
            }
        }
        
        assertEquals(1, pluginManager.plugins.size, "Only 1 plugin should be loaded")
        val pluginContainer = pluginManager.pluginContainer<PolyPlugin>("ca.solo-studios.test", "test")
        assertEquals(
                PluginInfo(
                        group = "ca.solo-studios.test",
                        id = "test",
                        version = "0.0.0".toVersion(),
                        entrypoints = listOf("ca.solostudios.polybot.plugin.TestPlugin"),
                          ),
                pluginContainer.info,
                "Plugin info should be known"
                    )
        assertEquals(1, pluginContainer.entrypoints.size, "Should have 1 entrypoint")
        assertEquals(TestPlugin::class, pluginContainer.entrypoints.first()::class, "Loaded plugin entrypoint should have known class")
    }
    
    @Test
    fun `test load unknown plugin class from jar`() {
        val jarFile = tempDir.resolve("test.jar")
        jarFile.openJar().use {
            it.putNextEntry(JarEntry(PluginInfo.PLUGIN_INFO_FILE))
            it.write(
                    """
                        {
                            "group": "ca.solo-studios.test",
                            "id": "test",
                            "version": "0.0.0",
                            "entrypoints": [
                                "ca.solostudios.polybot.plugin.FakeTestPlugin"
                            ]
                        }
                   """.trimIndent().toByteArray()
                    )
            it.closeEntry()
        }
        
        
        val jarCandidateFinder = PluginCandidateFinder {
            listOf(jarFile)
        }
        val pluginManager = PolyPluginManagerImpl(polybot, listOf(jarCandidateFinder))
        
        assertThrows<IllegalStateException> {
            runBlocking {
                pluginManager.loadPlugins()
            }
        }
        
        assertEquals(0, pluginManager.plugins.size, "No plugins should be loaded")
    }
    
    @Test
    fun `test resolve candidate from directory`() {
        val pluginInfo = tempDir.resolve(PluginInfo.PLUGIN_INFO_FILE)
        pluginInfo.writeText(
                """
                    {
                        "group": "ca.solo-studios.test",
                        "id": "test",
                        "version": "0.0.0"
                    }
                """.trimIndent()
                            )
        
        
        val dirCandidateFinder = PluginCandidateFinder {
            listOf(tempDir)
        }
        val pluginManager = PolyPluginManagerImpl(polybot, listOf(dirCandidateFinder))
        
        assertDoesNotThrow {
            runBlocking {
                pluginManager.loadPlugins()
            }
        }
        
        assertEquals(1, pluginManager.plugins.size, "Only 1 plugin should be loaded")
        val pluginContainer = pluginManager.pluginContainer<PolyPlugin>("ca.solo-studios.test", "test")
        assertEquals(
                PluginInfo(group = "ca.solo-studios.test", id = "test", version = "0.0.0".toVersion()),
                pluginContainer.info,
                "Plugin info should be known"
                    )
        assertEquals(0, pluginContainer.entrypoints.size, "Should have no entrypoints")
    }
    
    @Test
    fun `test load plugin class from directory`() {
        val pluginInfo = tempDir.resolve(PluginInfo.PLUGIN_INFO_FILE)
        pluginInfo.writeText(
                """
                    {
                        "group": "ca.solo-studios.test",
                        "id": "test",
                        "version": "0.0.0",
                        "entrypoints": [
                            "ca.solostudios.polybot.plugin.TestPlugin"
                        ]
                    }
                """.trimIndent()
                            )
        
        val className = "ca/solostudios/polybot/plugin/TestPlugin.class"
        val classBytes = javaClass.classLoader.getResourceAsStream(className)!!.readBytes()
        val classFile = tempDir.resolve(className)
        classFile.parent.createDirectories()
        classFile.createFile()
        classFile.writeBytes(classBytes)
        
        
        val dirCandidateFinder = PluginCandidateFinder {
            listOf(tempDir)
        }
        val pluginManager = PolyPluginManagerImpl(polybot, listOf(dirCandidateFinder))
        
        assertDoesNotThrow {
            runBlocking {
                pluginManager.loadPlugins()
            }
        }
        
        assertEquals(1, pluginManager.plugins.size, "Only 1 plugin should be loaded")
        val pluginContainer = pluginManager.pluginContainer<PolyPlugin>("ca.solo-studios.test", "test")
        assertEquals(
                PluginInfo(
                        group = "ca.solo-studios.test",
                        id = "test",
                        version = "0.0.0".toVersion(),
                        entrypoints = listOf("ca.solostudios.polybot.plugin.TestPlugin"),
                          ),
                pluginContainer.info,
                "Plugin info should be known"
                    )
        assertEquals(1, pluginContainer.entrypoints.size, "Should have 1 entrypoint")
        assertEquals(TestPlugin::class, pluginContainer.entrypoints.first()::class, "Loaded plugin entrypoint should have known class")
    }
    
    @Test
    fun `test load unknown plugin class from directory`() {
        val pluginInfo = tempDir.resolve(PluginInfo.PLUGIN_INFO_FILE)
        pluginInfo.writeText(
                """
                    {
                        "group": "ca.solo-studios.test",
                        "id": "test",
                        "version": "0.0.0",
                        "entrypoints": [
                            "ca.solostudios.polybot.plugin.FakeTestPlugin"
                        ]
                    }
                """.trimIndent()
                            )
        
        
        val dirCandidateFinder = PluginCandidateFinder {
            listOf(tempDir)
        }
        val pluginManager = PolyPluginManagerImpl(polybot, listOf(dirCandidateFinder))
        
        assertThrows<IllegalStateException> {
            runBlocking {
                pluginManager.loadPlugins()
            }
        }
        
        assertEquals(0, pluginManager.plugins.size, "No plugins should be loaded")
    }
    
    
    private fun Path.openJar(): JarOutputStream {
        return JarOutputStream(Files.newOutputStream(this))
    }
}