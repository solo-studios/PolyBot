/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file build.gradle.kts is part of PolyhedralBot
 * Last modified on 01-09-2021 06:17 p.m.
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

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val KOTLIN_VERSION: String by project
val KOTLINX_SERIALIZATION_VERSION: String by project
val KOTLINX_COROUTINES_VERSION: String by project
val JETBRAINS_ANNOTATIONS_VERSION: String by project
val JDA_VERSION: String by project
val DISCORD_WEBHOOKS: String by project
val JDA_KTX_VERSION: String by project
val JDA_UTILITIES_VERSION: String by project
val CLOUD_VERSION: String by project
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
val SQLITE_VERSION: String by project
val EXPOSED_VERSION: String by project
val LUCENE_VERSION: String by project
val INTELLIJ_MARKDOWN_VERSION: String by project
val JGIT_VERSION: String by project
val GITHUB_API_VERSION: String by project
val XCHART_VERSION: String by project


plugins {
    java
    application
    kotlin("jvm")
    kotlin("plugin.noarg")
    kotlin("plugin.serialization")
    id("org.ajoberstar.grgit") version "4.0.2"
    //    id("ca.cutterslade.analyze")
}

group = "com.solostudios.polyhedralbot"
val versionObj = Version("0", "0", "0")
version = versionObj

repositories {
    mavenCentral()
    
    maven { // Incendo (Cloud)
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

application {
    mainClass.set("com.solostudios.polybot.LauncherKt")
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect")) // Reflection stuff
    implementation(kotlin("script-runtime")) // For executing scripts at runtime
    implementation(kotlin("script-util"))
    implementation(kotlin("compiler-embeddable"))
    implementation(kotlin("scripting-compiler-embeddable"))
    // Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$KOTLINX_SERIALIZATION_VERSION")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$KOTLINX_SERIALIZATION_VERSION")
    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$KOTLINX_COROUTINES_VERSION")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$KOTLINX_COROUTINES_VERSION")
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-debug:$KOTLINX_COROUTINES_VERSION")
    
    // Utility annotations
    implementation("org.jetbrains:annotations:$JETBRAINS_ANNOTATIONS_VERSION")
    
    // JDA
    implementation("net.dv8tion:JDA:$JDA_VERSION") {
        exclude(module = "opus-java")
    }
    // Discord webhooks
    implementation("club.minnced:discord-webhooks:$DISCORD_WEBHOOKS")
    // JDA Kotlin extensions
    implementation("com.github.solonovamax:jda-ktx:${JDA_KTX_VERSION}")
    // JDA utilities
    implementation("com.jagrosh:jda-utilities-commons:$JDA_UTILITIES_VERSION")
    implementation("com.jagrosh:jda-utilities-menu:$JDA_UTILITIES_VERSION")
    
    // Cloud (Command handler)
    implementation("cloud.commandframework:cloud-core:$CLOUD_VERSION")
    implementation("cloud.commandframework:cloud-annotations:$CLOUD_VERSION") // Annotation parser
    implementation("cloud.commandframework:cloud-jda:$CLOUD_VERSION") // JDA impl
    implementation("cloud.commandframework:cloud-kotlin-extensions:$CLOUD_VERSION") // Kotlin extensions
    
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
    
    // SQLite
    implementation("org.xerial:sqlite-jdbc:$SQLITE_VERSION") // TODO: 8/30/21 Support more SQL types
    // Make using SQL not the most excrutiating shit ever and actually bearable to use
    implementation("org.jetbrains.exposed:exposed-core:$EXPOSED_VERSION")
    implementation("org.jetbrains.exposed:exposed-dao:$EXPOSED_VERSION")
    implementation("org.jetbrains.exposed:exposed-jdbc:$EXPOSED_VERSION")
    implementation("org.jetbrains.exposed:exposed-java-time:$EXPOSED_VERSION")
    
    // Apache Lucene search engine
    implementation("org.apache.lucene:lucene-core:$LUCENE_VERSION")
    implementation("org.apache.lucene:lucene-memory:$LUCENE_VERSION")
    implementation("org.apache.lucene:lucene-suggest:$LUCENE_VERSION")
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
    
    // Testing (JUnit 5)
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-params:5.7.2")
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
            apiVersion = "1.5"
            languageVersion = "1.5"
            freeCompilerArgs += "-Xopt-in=kotlin.time.ExperimentalTime"
        }
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
            "$major.$minor.$patch+${env["BUILD_NUMBER"]}"
    }
}

fun getGitHash(): String = grgit.head().abbreviatedId

val env: Map<String, String>
    get() = System.getenv()

