/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file build.gradle.kts is part of PolyBot
 * Last modified on 17-04-2023 12:47 p.m.
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

@file:Suppress("SuspiciousCollectionReassignment", "PropertyName", "DSL_SCOPE_VIOLATION", "UnstableApiUsage")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.ajoberstar.grgit.Grgit
import org.gradle.plugins.ide.idea.model.IdeaProject
import org.jetbrains.gradle.ext.ActionDelegationConfig
import org.jetbrains.gradle.ext.CodeStyleConfig
import org.jetbrains.gradle.ext.CopyrightConfiguration
import org.jetbrains.gradle.ext.EncodingConfiguration
import org.jetbrains.gradle.ext.GroovyCompilerConfiguration
import org.jetbrains.gradle.ext.IdeaCompilerConfiguration
import org.jetbrains.gradle.ext.ProjectSettings
import org.jetbrains.gradle.ext.RunConfiguration
import kotlin.math.max

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.noarg)
    alias(libs.plugins.kotlin.serialization)
    
    java
    
    application
    distribution
    
    alias(libs.plugins.dokka)
    
    alias(libs.plugins.grgit)
    
    alias(libs.plugins.shadow)
    
    idea
    alias(libs.plugins.idea.ext)
    
    // id("org.jetbrains.kotlinx.binary-compatibility-validator")
}

var mainClassName: String by application.mainClass
mainClassName = "ca.solostudios.polybot.cli.Launcher"

val versionObj = Version("0", "3", "5")

allprojects {
    version = versionObj
    group = "ca.solostudios.polybot"
}

repositories {
    maven("https://maven.solo-studios.ca/releases")
    mavenCentral()
    maven("https://m2.dv8tion.net/releases")
    maven("https://jitpack.io/")
    maven("https://m2.chew.pro/releases")
}

kotlin {
    jvmToolchain(11)
}


java {
    withSourcesJar()
}


dependencies {
    // Kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect) // Reflection stuff
    implementation(libs.bundles.kotlin.scripting) // For executing scripts at runtime
    
    // Kotlin Serialization
    implementation(libs.bundles.kotlinx.serialization.base)
    
    // Kotlin Coroutines
    implementation(libs.bundles.kotlinx.coroutines.core)
    
    // Kotlin UUID support
    implementation(libs.kotlinx.uuid)
    
    // Kotlin CLI library
    implementation(libs.clikt)
    
    // Kodein Dependency Injection
    implementation(libs.kodein.di)
    
    // Utility annotations
    implementation(libs.jetbrains.annotations)
    
    // JDA
    implementation(libs.jda) {
        exclude(module = "opus-java")
    }
    // Discord webhooks
    implementation(libs.discord.webhooks)
    // JDA Kotlin extensions
    implementation(libs.jda.ktx)
    // JDA utilities
    implementation(libs.bundles.jda.utilities)
    
    // Cloud (Command handler)
    implementation(libs.bundles.cloud)
    
    // Kryo fast object serialization
    implementation(libs.kryo)
    
    // Reflections
    implementation(libs.reflections)
    
    // SLF4J
    implementation(libs.slf4j)
    implementation(libs.slf4k) // SLF4J extension library
    implementation(libs.logback)
    
    // Kotlin HTTP api
    implementation(libs.bundles.fuel)
    
    // Jackson (JSON object serialization/deserialization)
    implementation(libs.bundles.jackson)
    implementation(libs.jackson.dataformat.hocon)
    
    // Persistent cache
    implementation(libs.ehcache)
    
    // Guava
    implementation(libs.guava)
    
    // Hikari (SQL Connection Pooling)
    implementation(libs.hikaricp)
    // SQLite
    // implementation(libs.sqlite)
    // MariaDB
    implementation(libs.mariadb)
    // PostreSQL
    implementation(libs.postgresql)
    
    // Make using SQL not the most excruciating shit ever and actually bearable to use
    implementation(libs.bundles.exposed)
    // Exposed Migrations
    implementation(libs.exposed.migrations)
    
    // Apache Lucene search engine
    implementation(libs.bundles.lucene)
    
    // Markdown library
    implementation(libs.jetbrains.markdown)
    
    // Git
    implementation(libs.jgit)
    
    // Used for fast random number generators
    implementation(libs.dsi.dsiutils)
    
    implementation(libs.commons.compress)
    implementation(libs.commons.io)
    
    // Testing (JUnit 5)
    testImplementation(libs.bundles.junit)
}

application {
    applicationDefaultJvmArgs = listOf(
            "--add-opens", "java.base/java.nio=ALL-UNNAMED", // kryo
            "--add-opens", "java.base/java.lang=ALL-UNNAMED", // kryo
            "--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED", // reflectasm
                                      )
}

noArg {
    invokeInitializers = true
    annotation("kotlinx.serialization.Serializable")
}

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
        
        failFast = false
        maxParallelForks = max(Runtime.getRuntime().availableProcessors() - 1, 1)
    }
    
    withType<Javadoc>().configureEach {
        options {
            encoding = "UTF-8"
        }
    }
    
    named<JavaExec>("run") {
        args = listOf(
                "run"
                     )
    }
    
    processResources.configure {
        filesMatching("polybot.properties") {
            expand(
                    "versionMajor" to versionObj.major,
                    "versionMinor" to versionObj.minor,
                    "versionPatch" to versionObj.patch,
                    "buildNumber" to versionObj.buildNumber,
                    "gitHash" to versionObj.gitHash,
                    "localBuild" to versionObj.localBuild.toString(),
                  )
        }
    }
    
    withType<ShadowJar>().configureEach {
        mergeServiceFiles()
        minimize {
            exclude {
                it.moduleGroup == "org.mariadb.jdbc" || it.moduleGroup == "org.postgresql" || it.moduleGroup == "ch.qos.logback" ||
                        it.moduleGroup == "org.jetbrains.kotlinx" || it.moduleGroup == "org.ehcache" ||
                        it.moduleGroup == "org.jetbrains.exposed"
            }
            exclude {
                it.moduleName == "kotlin-reflect"
            }
            exclude {
                it.moduleGroup == "org.apache.lucene" && (it.moduleName == "lucene-core" || it.moduleName == "lucene-sandbox")
            }
        }
    }
    
    withType<Jar>().configureEach {
        from(rootProject.file("LICENSE"))
        manifest {
            attributes(
                    "Main-Class" to mainClassName,
                    "Built-By" to System.getProperty("user.name"),
                    "Built-Jdk" to System.getProperty("java.version"),
                    "Implementation-Title" to project.name,
                    "Implementation-Version" to project.version.toString(),
                    "Add-Opens" to "java.base/java.nio java.base/sun.nio.ch java.base/java.lang",
                      )
        }
    }
    
    named<Tar>("distTar") {
        compression = Compression.GZIP
        archiveFileName = "PolyhedralBot-dist.tar.gz"
    }
}

val grgit: Grgit by lazy { grgitService.service.get().grgit!! }

/**
 * Version class, which does version stuff.
 */
@Suppress("MemberVisibilityCanBePrivate")
class Version(val major: String, val minor: String, val patch: String) {
    val localBuild: Boolean
        get() = env["BUILD_NUMBER"] == null
    
    val buildNumber: String
        get() = env["BUILD_NUMBER"] ?: "0"
    
    val gitHash: String
        get() = grgit.head().id
    
    val shortGitHash: String
        get() = grgit.head().abbreviatedId
    
    override fun toString(): String {
        return if (localBuild) // Only use git hash if it's a local build.
            "$major.$minor.$patch-local+$shortGitHash"
        else
            "$major.$minor.$patch+$buildNumber"
    }
}

val env: Map<String, String>
    get() = System.getenv()


/*-----------------------*
 | BEGIN IntelliJ Config |
 *-----------------------*/

idea {
    project {
        settings {
            withIDEADir {
                val scope = resolve("scopes")
                scope.mkdirs()
                
                val file = scope.resolve("PaginationMenu.xml")
                file.writeText("<component/>")
                
                withIDEAFileXml(file.toRelativeString(this)) {
                    val root = this.asNode()
                    root.attributes()["name"] = "DependencyValidationManager"
                    root.appendNode(
                            "scope", mapOf(
                            "name" to "PaginationMenu",
                            "pattern" to "src[PolyhedralBot.main]:com.solostudios.polybot.util.PaginationMenu"
                                          )
                                   )
                }
            }
            
            copyright {
                profiles {
                    val profileJDAUtilities = create("JDA Utilities") {
                        notice = """
                            Copyright 2016-2018 John Grosh (jagrosh) & Kaidan Gustave (TheMonitorLizard)

                            Licensed under the Apache License, Version 2.0 (the "License");
                            you may not use this file except in compliance with the License.
                            You may obtain a copy of the License at

                                https://www.apache.org/licenses/LICENSE-2.0

                            Unless required by applicable law or agreed to in writing, software
                            distributed under the License is distributed on an "AS IS" BASIS,
                            WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
                            See the License for the specific language governing permissions and
                            limitations under the License.
                        """.trimIndent()
                        keyword = "Copyright"
                    }
                    val profilePolyBot = create("PolyBot") {
                        notice = """
                            ${"$"}project.name - A Discord bot for the Polyhedral Development discord server
                            Copyright (c) ${"$"}originalComment.match("Copyright \(c\) (\d+)", 1, "-")${"$"}today.year solonovamax <solonovamax@12oclockpoint.com>

                            The file ${"$"}file.fileName is part of ${"$"}project.name
                            Last modified on ${"$"}file.lastModified.format('dd-MM-yyyy hh:mm aaa')

                            MIT License

                            Permission is hereby granted, free of charge, to any person obtaining a copy
                            of this software and associated documentation files (the "Software"), to deal
                            in the Software without restriction, including without limitation the rights
                            to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
                            copies of the Software, and to permit persons to whom the Software is
                            furnished to do so, subject to the following conditions:

                            The above copyright notice and this permission notice shall be included in all
                            copies or substantial portions of the Software.

                            ${"$"}project.name.toUpperCase() IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
                            IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
                            FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
                            AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
                            LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
                            OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
                            SOFTWARE.
                        """.trimIndent()
                        keyword = "Copyright"
                        //language=RegExp
                        allowReplaceRegexp = "20[0-9]{2}"
                    }
                    useDefault = profilePolyBot.name
                    
                    scopes = mapOf(
                            "PaginationMenu" to profileJDAUtilities.name,
                            "Project Files" to profilePolyBot.name
                                  )
                }
            }
        }
    }
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

/*---------------------*
 | END IntelliJ Config |
 *---------------------*/

/*-----------------------*
 | BEGIN Utility Methods |
 *-----------------------*/

fun IdeaProject.settings(configuration: ProjectSettings.() -> Unit) {
    (this as ExtensionAware).configure(configuration)
}

fun ProjectSettings.delegateActions(configuration: ActionDelegationConfig.() -> Unit) {
    (this as ExtensionAware).configure(configuration)
}

fun ProjectSettings.taskTriggers(configuration: IdeaCompilerConfiguration.() -> Unit) {
    (this as ExtensionAware).configure(configuration)
}

fun ProjectSettings.compiler(configuration: IdeaCompilerConfiguration.() -> Unit) {
    (this as ExtensionAware).configure(configuration)
}

fun ProjectSettings.groovyCompiler(configuration: GroovyCompilerConfiguration.() -> Unit) {
    (this as ExtensionAware).configure(configuration)
}

fun ProjectSettings.codeStyle(configuration: CodeStyleConfig.() -> Unit) {
    (this as ExtensionAware).configure(configuration)
}

fun ProjectSettings.copyright(configuration: CopyrightConfiguration.() -> Unit) {
    (this as ExtensionAware).configure(configuration)
}

fun ProjectSettings.encodings(configuration: EncodingConfiguration.() -> Unit) {
    (this as ExtensionAware).configure(configuration)
}

fun ProjectSettings.runConfigurations(configuration: PolymorphicDomainObjectContainer<RunConfiguration>.() -> Unit) {
    (this as ExtensionAware).configure<NamedDomainObjectContainer<RunConfiguration>> {
        (this as PolymorphicDomainObjectContainer<RunConfiguration>).apply(configuration)
    }
}

/*---------------------*
 | END Utility Methods |
 *---------------------*/