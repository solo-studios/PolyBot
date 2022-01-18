/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file build.gradle.kts is part of PolyhedralBot
 * Last modified on 18-01-2022 11:13 a.m.
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
    id("org.ajoberstar.grgit") version "4.1.1"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.1"
}

var mainClassName: String by application.mainClass
mainClassName = "ca.solostudios.polybot.cli.Launcher"
group = "ca.solostudios.polybot"
val versionObj = Version("0", "3", "3")
version = versionObj

repositories {
    mavenCentral()
    
    maven { // Incendo (Cloud) Snapshots
        name = "incendo-snapshots"
        url = uri("https://repo.incendo.org/content/repositories/snapshots")
    }
    
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

configurations.all {
    // Check for updates every build
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}

dependencies {
    
    val kotlinVersion = findProperty("KOTLIN_VERSION") as String
    val kotlinxSerializationVersion = findProperty("KOTLINX_SERIALIZATION_VERSION") as String
    val kotlinxCoroutinesVersion = findProperty("KOTLINX_COROUTINES_VERSION") as String
    val kotlinxUuidVersion = findProperty("KOTLINX_UUID_VERSION") as String
    val cliKtVersion = findProperty("CLIKT_VERSION") as String
    val kodeinDiVersion = findProperty("KODEIN_DI_VERSION") as String
    val jetbrainsAnnotationVersion = findProperty("JETBRAINS_ANNOTATIONS_VERSION") as String
    val jdaVersion = findProperty("JDA_VERSION") as String
    val discordWebhooksVersion = findProperty("DISCORD_WEBHOOKS_VERSION") as String
    val jdaKtxVersion = findProperty("JDA_KTX_VERSION") as String
    val jdaUtilitiesVersion = findProperty("JDA_UTILITIES_VERSION") as String
    val cloudVersion = findProperty("CLOUD_VERSION") as String
    val kryoVersion = findProperty("KRYO_VERSION") as String
    val reflectionsVersion = findProperty("REFLECTIONS_VERSION") as String
    val slf4jVersion = findProperty("SLF4J_VERSION") as String
    val slf4kVersion = findProperty("SLF4K_VERSION") as String
    val logbackVersion = findProperty("LOGBACK_VERSION") as String
    val fuelVersion = findProperty("FUEL_VERSION") as String
    val jacksonVersion = findProperty("JACKSON_VERSION") as String
    val jacksonHoconVersion = findProperty("JACKSON_HOCON_VERSION") as String
    val ehcacheVersion = findProperty("EHCACHE_VERSION") as String
    val guavaVersion = findProperty("GUAVA_VERSION") as String
    val hikariVersion = findProperty("HIKARI_VERSION") as String
    val mariadbVersion = findProperty("MARIADB_VERSION") as String
    val postgresqlVersion = findProperty("POSTGRESQL_VERSION") as String
    // val sqliteVersion = findProperty("SQLITE_VERSION") as String
    val exposedVersion = findProperty("EXPOSED_VERSION") as String
    val exposedMigrationsVersion = findProperty("EXPOSED_MIGRATIONS_VERSION") as String
    val luceneVersion = findProperty("LUCENE_VERSION") as String
    val intellijMarkdownVersion = findProperty("INTELLIJ_MARKDOWN_VERSION") as String
    val jGitVersion = findProperty("JGIT_VERSION") as String
    val commonsCompressVersion = findProperty("COMMONS_COMPRESS_VERSION") as String
    val commonsIOVersion = findProperty("COMMONS_IO_VERSION") as String
    
    
    // Kotlin
    implementation(kotlin("stdlib", kotlinVersion))
    implementation(kotlin("reflect", kotlinVersion)) // Reflection stuff
    implementation(kotlin("script-runtime", kotlinVersion)) // For executing scripts at runtime
    implementation(kotlin("script-util", kotlinVersion))
    implementation(kotlin("compiler-embeddable", kotlinVersion))
    implementation(kotlin("scripting-compiler-embeddable", kotlinVersion))
    
    // Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$kotlinxCoroutinesVersion")
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-debug:$KOTLINX_COROUTINES_VERSION")
    
    // Kotlin UUID support
    implementation("app.softwork:kotlinx-uuid-core:$kotlinxUuidVersion")
    
    // Kotlin CLI library
    implementation("com.github.ajalt.clikt:clikt:$cliKtVersion")
    
    // Kodein Dependency Injection
    implementation("org.kodein.di:kodein-di:$kodeinDiVersion")
    
    // Utility annotations
    implementation("org.jetbrains:annotations:$jetbrainsAnnotationVersion")
    
    // JDA
    implementation("net.dv8tion:JDA:$jdaVersion") {
        exclude(module = "opus-java")
    }
    // Discord webhooks
    implementation("club.minnced:discord-webhooks:$discordWebhooksVersion")
    // JDA Kotlin extensions
    implementation("com.github.minndevelopment:jda-ktx:$jdaKtxVersion")
    // JDA utilities
    implementation("com.jagrosh:jda-utilities-commons:$jdaUtilitiesVersion")
    implementation("com.jagrosh:jda-utilities-menu:$jdaUtilitiesVersion")
    
    // Cloud (Command handler)
    implementation("cloud.commandframework:cloud-core:$cloudVersion")
    implementation("cloud.commandframework:cloud-annotations:$cloudVersion") // Annotation parser
    implementation("cloud.commandframework:cloud-jda:$cloudVersion") // JDA impl
    implementation("cloud.commandframework:cloud-kotlin-extensions:$cloudVersion") // Kotlin extensions
    implementation("cloud.commandframework:cloud-kotlin-coroutines:$cloudVersion") // Kotlin extensions (coroutines)
    implementation("cloud.commandframework:cloud-kotlin-coroutines-annotations:$cloudVersion") // Kotlin extensions (coroutine annotations)
    implementation("cloud.commandframework:cloud-services:$cloudVersion") // Kotlin extensions
    
    // Kryo fast object serialization
    implementation("com.esotericsoftware:kryo:$kryoVersion")
    
    // Reflections
    implementation("org.reflections:reflections:$reflectionsVersion")
    
    // SLF4J
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("ca.solo-studios:slf4k:$slf4kVersion") // SLF4J extension library
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    
    // Kotlin HTTP api
    implementation("com.github.kittinunf.fuel:fuel:$fuelVersion")
    implementation("com.github.kittinunf.fuel:fuel-coroutines:$fuelVersion")
    implementation("com.github.kittinunf.fuel:fuel-jackson:$fuelVersion")
    // implementation("com.github.kittinunf.fuel:fuel-reactor:$fuelVersion") // Use Reactor??
    
    // Jackson (JSON object serialization/deserialization)
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("org.honton.chas.hocon:jackson-dataformat-hocon:$jacksonHoconVersion") // HOCON support for Jackson
    
    // Persistent cache
    implementation("org.ehcache:ehcache:$ehcacheVersion")
    
    // Guava
    implementation("com.google.guava:guava:$guavaVersion")
    
    // Hikari (SQL Connection Pooling)
    implementation("com.zaxxer:HikariCP:$hikariVersion")
    // SQLite
    // implementation("org.xerial:sqlite-jdbc:$sqliteVersion")
    // MariaDB
    implementation("org.mariadb.jdbc:mariadb-java-client:$mariadbVersion")
    implementation("org.postgresql:postgresql:$postgresqlVersion")
    
    // Make using SQL not the most excrutiating shit ever and actually bearable to use
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    // Exposed Migrations
    implementation("gay.solonovamax:exposed-migrations:$exposedMigrationsVersion")
    // Exposed UUID support
    implementation("app.softwork:kotlinx-uuid-exposed:$kotlinxUuidVersion")
    
    // Apache Lucene search engine
    implementation("org.apache.lucene:lucene-core:$luceneVersion")
    // implementation("org.apache.lucene:lucene-memory:$luceneVersion")
    // implementation("org.apache.lucene:lucene-suggest:$luceneVersion")
    implementation("org.apache.lucene:lucene-queryparser:$luceneVersion")
    implementation("org.apache.lucene:lucene-analysis-common:$luceneVersion")
    
    // Markdown library
    implementation("org.jetbrains:markdown:$intellijMarkdownVersion")
    
    // Git
    implementation("org.eclipse.jgit:org.eclipse.jgit:$jGitVersion")
    
    // Used for fast random number generators
    implementation("it.unimi.dsi:dsiutils:2.6.17")
    
    implementation("org.apache.commons:commons-compress:$commonsCompressVersion")
    implementation("org.apache.commons:commons-io:$commonsIOVersion")
    
    val jUnitVersion = "5.8.2"
    
    // Testing (JUnit 5)
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-params:$jUnitVersion")
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