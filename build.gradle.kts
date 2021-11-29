/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file build.gradle.kts is part of PolyhedralBot
 * Last modified on 29-11-2021 12:45 p.m.
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
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val KOTLIN_VERSION: String by project
val KOTLINX_SERIALIZATION_VERSION: String by project
val KOTLINX_COROUTINES_VERSION: String by project
val KOTLINX_UUID_VERSION: String by project
val JETBRAINS_ANNOTATIONS_VERSION: String by project
val JDA_VERSION: String by project
val DISCORD_WEBHOOKS_VERSION: String by project
val JDA_KTX_VERSION: String by project
val JDA_UTILITIES_VERSION: String by project
val CLOUD_VERSION: String by project
val CLOUD_KT_EXTENSIONS_VERSION: String by project
val KRYO_VERSION: String by project
val REFLECTIONS_VERSION: String by project
val SLF4J_VERSION: String by project
val SLF4K_VERSION: String by project
val LOGBACK_VERSION: String by project
val FUEL_VERSION: String by project
val JACKSON_VERSION: String by project
val JACKSON_HOCON_VERSION: String by project
val CACHE_4K_VERSION: String by project
val EHCACHE_VERSION: String by project
val GUAVA_VERSION: String by project
val HIKARI_VERSION: String by project
val MARIADB_VERSION: String by project
val POSTGRESQL_VERSION: String by project
// val SQLITE_VERSION: String by project
val EXPOSED_VERSION: String by project
val EXPOSED_MIGRATIONS_VERSION: String by project
val LUCENE_VERSION: String by project
val INTELLIJ_MARKDOWN_VERSION: String by project
val JGIT_VERSION: String by project
val GITHUB_API_VERSION: String by project
val XCHART_VERSION: String by project
val COMMONS_COMPRESS_VERSION: String by project
val COMMONS_IO_VERSION: String by project


plugins {
    java
    application
    distribution
    kotlin("jvm")
    kotlin("plugin.noarg")
    kotlin("plugin.serialization")
    id("org.ajoberstar.grgit") version "4.0.2"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    //    id("ca.cutterslade.analyze")
}

var mainClassName: String by application.mainClass
mainClassName = "ca.solostudios.polybot.Launcher"
group = "ca.solostudios.polybot"
val versionObj = Version("0", "1", "0")
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
    
    maven {
        name = "ajoberstar-backup"
        url = uri("https://ajoberstar.org/bintray-backup/")
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
    // Kotlin
    implementation(kotlin("stdlib", KOTLIN_VERSION))
    implementation(kotlin("reflect", KOTLIN_VERSION)) // Reflection stuff
    implementation(kotlin("script-runtime", KOTLIN_VERSION)) // For executing scripts at runtime
    implementation(kotlin("script-util", KOTLIN_VERSION))
    implementation(kotlin("compiler-embeddable", KOTLIN_VERSION))
    implementation(kotlin("scripting-compiler-embeddable", KOTLIN_VERSION))
    // Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$KOTLINX_SERIALIZATION_VERSION")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$KOTLINX_SERIALIZATION_VERSION")
    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$KOTLINX_COROUTINES_VERSION")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$KOTLINX_COROUTINES_VERSION")
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-debug:$KOTLINX_COROUTINES_VERSION")
    
    implementation("app.softwork:kotlinx-uuid-core:$KOTLINX_UUID_VERSION")
    implementation("app.softwork:kotlinx-uuid-exposed:$KOTLINX_UUID_VERSION")
    
    // Utility annotations
    implementation("org.jetbrains:annotations:$JETBRAINS_ANNOTATIONS_VERSION")
    
    // JDA
    implementation("net.dv8tion:JDA:$JDA_VERSION") {
        exclude(module = "opus-java")
    }
    // Discord webhooks
    implementation("club.minnced:discord-webhooks:$DISCORD_WEBHOOKS_VERSION")
    // JDA Kotlin extensions
    implementation("com.github.solonovamax:jda-ktx:${JDA_KTX_VERSION}")
    // JDA utilities
    implementation("com.jagrosh:jda-utilities-commons:$JDA_UTILITIES_VERSION")
    implementation("com.jagrosh:jda-utilities-menu:$JDA_UTILITIES_VERSION")
    
    // Cloud (Command handler)
    implementation("cloud.commandframework:cloud-core:$CLOUD_VERSION") { isChanging = true }
    implementation("cloud.commandframework:cloud-annotations:$CLOUD_VERSION") { isChanging = true } // Annotation parser
    implementation("cloud.commandframework:cloud-jda:$CLOUD_VERSION") { isChanging = true } // JDA impl
    implementation("cloud.commandframework:cloud-kotlin-extensions:$CLOUD_KT_EXTENSIONS_VERSION") { isChanging = true } // Kotlin extensions
    implementation("cloud.commandframework:cloud-services:$CLOUD_VERSION") { isChanging = true } // Kotlin extensions
    
    // Kryo fast object serialization
    implementation("com.esotericsoftware:kryo:$KRYO_VERSION")
    
    // Reflections
    implementation("org.reflections:reflections:$REFLECTIONS_VERSION")
    
    // SLF4J
    implementation("org.slf4j:slf4j-api:$SLF4J_VERSION")
    implementation("ca.solo-studios:slf4k:$SLF4K_VERSION") // SLF4J extension library
    implementation("ch.qos.logback:logback-classic:$LOGBACK_VERSION")
    
    // Kotlin HTTP api
    implementation("com.github.kittinunf.fuel:fuel:$FUEL_VERSION")
    implementation("com.github.kittinunf.fuel:fuel-coroutines:$FUEL_VERSION")
    implementation("com.github.kittinunf.fuel:fuel-jackson:$FUEL_VERSION")
    // implementation("com.github.kittinunf.fuel:fuel-reactor:$FUEL_VERSION") // Use Reactor??
    
    // Jackson (JSON object serialization/deserialization)
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$JACKSON_VERSION")
    implementation("com.fasterxml.jackson.core:jackson-core:$JACKSON_VERSION")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$JACKSON_VERSION")
    implementation("com.fasterxml.jackson.core:jackson-databind:$JACKSON_VERSION")
    implementation("org.honton.chas.hocon:jackson-dataformat-hocon:$JACKSON_HOCON_VERSION") // HOCON support for Jackson
    
    // Kotlin Cache utility
    implementation("io.github.reactivecircus.cache4k:cache4k:$CACHE_4K_VERSION")
    // Persistent cache
    implementation("org.ehcache:ehcache:$EHCACHE_VERSION")
    
    // Guava
    implementation("com.google.guava:guava:$GUAVA_VERSION")
    
    // Hikari (SQL Connection Pooling)
    implementation("com.zaxxer:HikariCP:$HIKARI_VERSION")
    // SQLite
    // implementation("org.xerial:sqlite-jdbc:$SQLITE_VERSION")
    // MariaDB
    implementation("org.mariadb.jdbc:mariadb-java-client:$MARIADB_VERSION")
    implementation("org.postgresql:postgresql:$POSTGRESQL_VERSION")
    // Make using SQL not the most excrutiating shit ever and actually bearable to use
    implementation("org.jetbrains.exposed:exposed-core:$EXPOSED_VERSION")
    implementation("org.jetbrains.exposed:exposed-dao:$EXPOSED_VERSION")
    implementation("org.jetbrains.exposed:exposed-jdbc:$EXPOSED_VERSION")
    implementation("org.jetbrains.exposed:exposed-java-time:$EXPOSED_VERSION")
    // Exposed Migrations
    implementation("gay.solonovamax:exposed-migrations:$EXPOSED_MIGRATIONS_VERSION")
    
    // Apache Lucene search engine
    implementation("org.apache.lucene:lucene-core:$LUCENE_VERSION")
    // implementation("org.apache.lucene:lucene-memory:$LUCENE_VERSION")
    // implementation("org.apache.lucene:lucene-suggest:$LUCENE_VERSION")
    implementation("org.apache.lucene:lucene-queryparser:$LUCENE_VERSION")
    implementation("org.apache.lucene:lucene-analyzers-common:$LUCENE_VERSION")
    
    // Markdown library
    implementation("org.jetbrains:markdown:$INTELLIJ_MARKDOWN_VERSION")
    
    // Git
    implementation("org.eclipse.jgit:org.eclipse.jgit:$JGIT_VERSION")
    
    // Github API
    implementation("org.kohsuke:github-api:$GITHUB_API_VERSION")
    
    // Chart drawing ??
    implementation("org.knowm.xchart:xchart:$XCHART_VERSION")
    
    // Xo
    implementation("it.unimi.dsi:dsiutils:2.6.17")
    
    implementation("org.apache.commons:commons-compress:$COMMONS_COMPRESS_VERSION")
    implementation("org.apache.commons:commons-io:$COMMONS_IO_VERSION")
    
    // Testing (JUnit 5)
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    @Suppress("GradlePackageUpdate")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    @Suppress("GradlePackageUpdate")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-params")
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
            // freeCompilerArgs += "-Xopt-in=kotlin.time.ExperimentalTime"
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

/**
 * Version class that does version stuff.
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