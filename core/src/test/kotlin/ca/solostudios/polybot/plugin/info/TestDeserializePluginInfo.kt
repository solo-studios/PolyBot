/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file TestDeserializePluginInfo.kt is part of PolyBot
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

package ca.solostudios.polybot.plugin.info

import ca.solostudios.polybot.api.plugin.info.PluginContributor
import ca.solostudios.polybot.api.plugin.info.PluginDependency
import ca.solostudios.polybot.api.plugin.info.PluginInfo
import ca.solostudios.polybot.api.plugin.info.PluginNesterJar
import ca.solostudios.strata.kotlin.toVersion
import ca.solostudios.strata.kotlin.toVersionRange
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TestDeserializePluginInfo {
    @Test
    fun `test plugin info deserialization fails when version is invalid`() {
        val pluginInfoString = this::class.java.getResource("/plugin-info/invalid/invalid-version.json")!!.openStream()
                .bufferedReader()
                .readText()
    
        assertThrows<IllegalArgumentException>("Plugin info at location /plugin-info/invalid/invalid-json.json was parsed but should have failed the blank name check.") {
            Json.decodeFromString<PluginInfo>(pluginInfoString)
        }
    }
    
    @Test
    fun `test plugin info deserialization fails when name is blank`() {
        val pluginInfoString = this::class.java.getResource("/plugin-info/invalid/blank-name.json")!!.openStream()
                .bufferedReader()
                .readText()
    
        assertThrows<IllegalArgumentException>("Plugin info at location /plugin-info/invalid/blank-name.json was parsed but should have failed the blank name check.") {
            Json.decodeFromString<PluginInfo>(pluginInfoString)
        }
    }
    
    @Test
    fun `test plugin info deserialization fails when description is blank`() {
        val pluginInfoString = this::class.java.getResource("/plugin-info/invalid/blank-description.json")!!.openStream()
                .bufferedReader()
                .readText()
    
        assertThrows<IllegalArgumentException>("Plugin info at location /plugin-info/invalid/blank-description.json was parsed but should have failed the blank description check.") {
            Json.decodeFromString<PluginInfo>(pluginInfoString)
        }
    }
    
    @Test
    fun `test plugin info deserialization fails when license is blank`() {
        val pluginInfoString = this::class.java.getResource("/plugin-info/invalid/blank-license.json")!!.openStream()
                .bufferedReader()
                .readText()
    
        assertThrows<IllegalArgumentException>("Plugin info at location /plugin-info/invalid/blank-license.json was parsed but should have failed the blank description check.") {
            Json.decodeFromString<PluginInfo>(pluginInfoString)
        }
    }
    
    @Test
    fun `test plugin info deserialization fails when group is invalid`() {
        val pluginInfoString = this::class.java.getResource("/plugin-info/invalid/invalid-group.json")!!.openStream()
                .bufferedReader()
                .readText()
    
        assertThrows<IllegalArgumentException>("Plugin info at location /plugin-info/invalid/invalid-group.json was parsed but should have failed the blank description check.") {
            Json.decodeFromString<PluginInfo>(pluginInfoString)
        }
    }
    
    @Test
    fun `test plugin info deserialization fails when id is invalid`() {
        val pluginInfoString = this::class.java.getResource("/plugin-info/invalid/invalid-id.json")!!.openStream()
                .bufferedReader()
                .readText()
    
        assertThrows<IllegalArgumentException>("Plugin info at location /plugin-info/invalid/invalid-id.json was parsed but should have failed the blank description check.") {
            Json.decodeFromString<PluginInfo>(pluginInfoString)
        }
    }
    
    @Test
    fun `test plugin info deserialization missing name`() {
        val pluginInfoString = this::class.java.getResource("/plugin-info/valid/missing-name.json")!!.openStream()
                .bufferedReader()
                .readText()
        
        val pluginInfo = assertDoesNotThrow("Plugin info at location /plugin-info/valid/missing-name.json could not parsed.") {
            Json.decodeFromString<PluginInfo>(pluginInfoString)
        }
        
        assertNull(pluginInfo.name)
    }
    
    @Test
    fun `test plugin info deserialization missing description`() {
        val pluginInfoString = this::class.java.getResource("/plugin-info/valid/missing-description.json")!!.openStream()
                .bufferedReader()
                .readText()
        
        val pluginInfo = assertDoesNotThrow("Plugin info at location /plugin-info/valid/missing-description.json could not parsed.") {
            Json.decodeFromString<PluginInfo>(pluginInfoString)
        }
        
        assertNull(pluginInfo.description)
    }
    
    @Test
    fun `test plugin info deserialization missing contributors`() {
        val pluginInfoString = this::class.java.getResource("/plugin-info/valid/missing-contributors.json")!!.openStream()
                .bufferedReader()
                .readText()
        
        val pluginInfo = assertDoesNotThrow("Plugin info at location /plugin-info/valid/missing-contributors.json could not parsed.") {
            Json.decodeFromString<PluginInfo>(pluginInfoString)
        }
        
        assertEquals(listOf(), pluginInfo.contributors)
    }
    
    @Test
    fun `test plugin info deserialization missing nested jars`() {
        val pluginInfoString = this::class.java.getResource("/plugin-info/valid/missing-nested-jars.json")!!.openStream()
                .bufferedReader()
                .readText()
        
        val pluginInfo = assertDoesNotThrow("Plugin info at location /plugin-info/valid/missing-nester-jars.json could not parsed.") {
            Json.decodeFromString<PluginInfo>(pluginInfoString)
        }
        
        assertEquals(listOf(), pluginInfo.nestedJars)
    }
    
    @Test
    fun `test plugin info deserialization missing license`() {
        val pluginInfoString = this::class.java.getResource("/plugin-info/valid/missing-license.json")!!.openStream()
                .bufferedReader()
                .readText()
        
        val pluginInfo = assertDoesNotThrow("Plugin info at location /plugin-info/valid/missing-license.json could not parsed.") {
            Json.decodeFromString<PluginInfo>(pluginInfoString)
        }
        
        assertEquals("ARR", pluginInfo.license)
    }
    
    @Test
    fun `test plugin info deserialization missing breaks`() {
        val pluginInfoString = this::class.java.getResource("/plugin-info/valid/missing-breaks.json")!!.openStream()
                .bufferedReader()
                .readText()
        
        val pluginInfo = assertDoesNotThrow("Plugin info at location /plugin-info/valid/missing-breaks.json could not parsed.") {
            Json.decodeFromString<PluginInfo>(pluginInfoString)
        }
        
        assertEquals(listOf(), pluginInfo.breaks)
    }
    
    @Test
    fun `test plugin info deserialization missing depends`() {
        val pluginInfoString = this::class.java.getResource("/plugin-info/valid/missing-depends.json")!!.openStream()
                .bufferedReader()
                .readText()
        
        val pluginInfo = assertDoesNotThrow("Plugin info at location /plugin-info/valid/missing-depends.json could not parsed.") {
            Json.decodeFromString<PluginInfo>(pluginInfoString)
        }
        
        assertEquals(listOf(), pluginInfo.depends)
    }
    
    @Test
    fun `test plugin info deserialization missing entrypoints`() {
        val pluginInfoString = this::class.java.getResource("/plugin-info/valid/missing-entrypoints.json")!!.openStream()
                .bufferedReader()
                .readText()
        
        val pluginInfo = assertDoesNotThrow("Plugin info at location /plugin-info/valid/missing-entrypoints.json could not parsed.") {
            Json.decodeFromString<PluginInfo>(pluginInfoString)
        }
        
        assertEquals(listOf(), pluginInfo.entrypoints)
    }
    
    @Test
    fun `test valid plugin info deserialization`() {
        val pluginInfoString = this::class.java.getResource("/plugin-info/valid/polybot.plugin.json")!!.openStream()
                .bufferedReader()
                .readText()
        
        val pluginInfo = assertDoesNotThrow("Plugin info at location /plugin-info/valid/polybot.plugin.json could not be parsed.") {
            Json.decodeFromString<PluginInfo>(pluginInfoString)
        }
        
        assertEquals("ca.solo-studios.test", pluginInfo.group)
        assertEquals("valid-plugin", pluginInfo.id)
        assertEquals("1.2.3".toVersion(), pluginInfo.version)
        assertEquals(listOf("ca.solostudios.test.ValidPluginEntrypoint"), pluginInfo.entrypoints)
        assertEquals(
                listOf(
                        PluginDependency("ca.solo-studios.test", "test-dep-one", "5.6.7".toVersionRange()),
                        PluginDependency("ca.solo-studios.test", "test-dep-two", "8.9.+".toVersionRange())
                      ),
                pluginInfo.depends
                    )
        assertEquals(
                listOf(
                        PluginDependency("ca.solo-studios.test", "test-breaks-one", "+".toVersionRange())
                      ),
                pluginInfo.breaks
                    )
        assertEquals("Test Plugin", pluginInfo.name)
        assertEquals("Here is a test description", pluginInfo.description)
        assertEquals("MIT", pluginInfo.license)
        assertEquals(
                listOf(
                        PluginNesterJar("/here/is/the/first.jar"),
                        PluginNesterJar("/here/is/the/first.jar")
                      ),
                pluginInfo.nestedJars
                    )
        assertEquals(
                listOf(
                        PluginContributor("contributor one", "role one"),
                        PluginContributor("contributor two", "role two"),
                        PluginContributor("contributor three", "role three")
                      ),
                pluginInfo.contributors
                    )
    }
}