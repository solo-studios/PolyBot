/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyBotPluginFunctionalTest.kt is part of PolyBot
 * Last modified on 20-08-2022 05:43 p.m.
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

import ca.solostudios.gradle.polybot.PolyBotGradlePlugin
import java.io.File
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import kotlin.test.assertNotNull


class PolyBotPluginFunctionalTest {
    @TempDir
    lateinit var testProjectDir: File
    
    @Test
    fun test() {
        val buildFile = testProjectDir.resolve("build.gradle.kts")
        buildFile.writeText(
                """
            plugins {
                id("ca.solo-studios.polybot-gradle")
            }
            
            polybot {
                plugin("testing") {
                    group("ca.solo-studios.test")
                    license("MIT")
                    displayName("Test Plugin")
                    description("Test")
                    version("1.0.0")
                    
                    entrypoints {
                        add("test1")
                        add("test2")
                        add("test3")
                    }
                    println(this)
                }
            }
        """.trimIndent()
                           )
        
        val result = GradleRunner.create()
                .withProjectDir(testProjectDir)
                .withPluginClasspath()
                .build()
        
        println("output: ${result.output}")
        // println("result: ${result.}")
    }
    
    @Test
    fun testPluginApplied() {
        val project = ProjectBuilder.builder()
                .withProjectDir(testProjectDir)
                .build()
        
        project.pluginManager.apply(PolyBotGradlePlugin::class.java)
        
        assertNotNull(project.plugins.getPlugin(PolyBotGradlePlugin::class.java))
    }
}