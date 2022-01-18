/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file build.gradle.kts is part of PolyhedralBot
 * Last modified on 18-01-2022 01:38 p.m.
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

@file:Suppress("SuspiciousCollectionReassignment", "PropertyName")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.plugins.ide.idea.model.IdeaProject
import org.jetbrains.gradle.ext.ActionDelegationConfig
import org.jetbrains.gradle.ext.CodeStyleConfig
import org.jetbrains.gradle.ext.CopyrightConfiguration
import org.jetbrains.gradle.ext.EncodingConfiguration
import org.jetbrains.gradle.ext.GroovyCompilerConfiguration
import org.jetbrains.gradle.ext.IdeaCompilerConfiguration
import org.jetbrains.gradle.ext.ProjectSettings
import org.jetbrains.gradle.ext.RunConfiguration
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea
    java
    application
    distribution
    kotlin("jvm")
    kotlin("plugin.noarg")
    kotlin("plugin.serialization")
    id("org.ajoberstar.grgit")
    id("com.github.johnrengelman.shadow")
    id("org.jetbrains.gradle.plugin.idea-ext")
}

var mainClassName: String by application.mainClass
mainClassName = "ca.solostudios.polybot.cli.Launcher"
group = "ca.solostudios.polybot"
val versionObj = Version("0", "3", "5")
version = versionObj

repositories {
    mavenCentral()
    
    // maven { // Incendo (Cloud) Snapshots
    //     name = "incendo-snapshots"
    //     url = uri("https://repo.incendo.org/content/repositories/snapshots")
    // }
    
    maven { // JDA
        name = "dv8tion-repo"
        url = uri("https://m2.dv8tion.net/releases")
    }
    
    maven {
        name = "jitpack"
        url = uri("https://jitpack.io/")
    }
    
    @Suppress("DEPRECATION")
    jcenter {
        content {
            // includeModuleByRegex("com\\.jagrosh", "jda-*")
            includeGroup("com.jagrosh")
        }
    }
}

dependencies {
    // Kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect) // Reflection stuff
    implementation(libs.bundles.kotlin.scripting) // For executing scripts at runtime
    
    // Kotlin Serialization
    implementation(libs.bundles.kotlinx.serialization)
    
    // Kotlin Coroutines
    implementation(libs.bundles.kotlinx.coroutines)
    
    // Kotlin UUID support
    implementation(libs.kotlinx.uuid)
    
    // Kotlin CLI library
    implementation(libs.clikt)
    
    // Kodein Dependency Injection
    implementation(libs.kodein)
    
    // Utility annotations
    implementation(libs.jetbrains.annotations)
    
    // JDA
    implementation(libs.jda)
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
    
    // Persistent cache
    implementation(libs.ehcache)
    
    // Guava
    implementation(libs.guava)
    
    // Hikari (SQL Connection Pooling)
    implementation(libs.hikari)
    // SQLite
    // implementation(libs.sqlite)
    // MariaDB
    implementation(libs.mariadb)
    // PostreSQL
    implementation(libs.postgresql)
    
    // Make using SQL not the most excrutiating shit ever and actually bearable to use
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
    implementation(libs.dsiutils)
    
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
    getByName<JavaExec>("run") {
        args = listOf(
                "run"
                     )
    }
    getByName<Test>("test") {
        useJUnitPlatform()
    }
    
    processResources {
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
    
    withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_11.toString()
            apiVersion = "1.6"
            languageVersion = "1.6"
            freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
        }
    }
    
    withType<ShadowJar> {
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
    
    withType<Jar> {
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
    
    getByName<Tar>("distTar") {
        compression = Compression.GZIP
        archiveFileName.set("PolyhedralBot-dist.tar.gz")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

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
        get() = grgit.head().id
    
    override fun toString(): String {
        return if (localBuild) // Only use git hash if it's a local build.
            "$major.$minor.$patch-local+${getGitHash()}"
        else
            "$major.$minor.$patch+$buildNumber"
    }
}

fun getGitHash(): String = grgit.head().abbreviatedId

val env: Map<String, String>
    get() = System.getenv()