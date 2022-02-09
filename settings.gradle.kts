/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file settings.gradle.kts is part of PolyhedralBot
 * Last modified on 09-02-2022 12:04 p.m.
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
import org.gradle.api.initialization.dsl.VersionCatalogBuilder.AliasBuilder
import org.gradle.api.initialization.dsl.VersionCatalogBuilder.LibraryAliasBuilder
import org.gradle.api.initialization.dsl.VersionCatalogBuilder.PluginAliasBuilder

enableFeaturePreview("VERSION_CATALOGS")

rootProject.name = "PolyhedralBot"

pluginManagement {
    plugins {
        id("org.ajoberstar.grgit") version "4.1.1"
        id("com.github.johnrengelman.shadow") version "7.1.2"
        id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.1"
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include(":core")
include(":common")
include(":plugin-framework")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            // Kotlin
            val kotlinVersion = "1.6.10"
            version("kotlin", kotlinVersion)
            alias("kotlin-stdlib") library ("org.jetbrains.kotlin" to "kotlin-stdlib") versionRef "kotlin"
            alias("kotlin-stdlib-jdk7") library ("org.jetbrains.kotlin" to "kotlin-stdlib-jdk7") versionRef "kotlin"
            alias("kotlin-stdlib-jdk8") library ("org.jetbrains.kotlin" to "kotlin-stdlib-jdk8") versionRef "kotlin"
            alias("kotlin-reflect") library ("org.jetbrains.kotlin" to "kotlin-reflect") versionRef "kotlin"
            bundle(
                    "kotlin",
                    listOf("kotlin-stdlib", "kotlin-stdlib-jdk7", "kotlin-stdlib-jdk8", "kotlin-reflect")
                  )
    
            pluginManagement {
                plugins {
                    kotlin("jvm") version kotlinVersion
                    kotlin("plugin.noarg") version kotlinVersion
                    kotlin("plugin.serialization") version kotlinVersion
                }
            }
    
            alias("kotlin-jvm") plugin "org.jetbrains.kotlin.jvm" versionRef "kotlin"
            alias("kotlin-noarg") plugin "org.jetbrains.kotlin.plugin.noarg" versionRef "kotlin"
            alias("kotlin-serialization") plugin "org.jetbrains.kotlin.plugin.serialization" versionRef "kotlin"
            
            alias("kotlin-script-runtime") library ("org.jetbrains.kotlin" to "kotlin-script-runtime") versionRef "kotlin"
            alias("kotlin-compiler-embeddable") library ("org.jetbrains.kotlin" to "kotlin-compiler-embeddable") versionRef "kotlin"
            alias("kotlin-scripting-compiler-embeddable") library ("org.jetbrains.kotlin" to "kotlin-scripting-compiler-embeddable") versionRef "kotlin"
            bundle(
                    "kotlin-scripting",
                    listOf("kotlin-script-runtime", "kotlin-compiler-embeddable", "kotlin-scripting-compiler-embeddable")
                  )
            
            // Kotlin Serialization
            version("kotlinx-serialization", "1.3.2")
            alias("kotlinx-serialization-core") library ("org.jetbrains.kotlinx" to "kotlinx-serialization-core") versionRef "kotlinx-serialization"
            alias("kotlinx-serialization-json") library ("org.jetbrains.kotlinx" to "kotlinx-serialization-json") versionRef "kotlinx-serialization"
            
            bundle("kotlinx-serialization", listOf("kotlinx-serialization-core", "kotlinx-serialization-json"))
            
            // Kotlin Coroutines
            version("kotlinx-coroutines", "1.6.0-native-mt")
            alias("kotlinx-coroutines-core") library ("org.jetbrains.kotlinx" to "kotlinx-coroutines-core") versionRef "kotlinx-coroutines"
            alias("kotlinx-coroutines-jdk8") library ("org.jetbrains.kotlinx" to "kotlinx-coroutines-jdk8") versionRef "kotlinx-coroutines"
            // alias("kotlinx-coroutines-debug") library ("org.jetbrains.kotlinx" to "kotlinx-coroutines-debug") versionRef "kotlinx-coroutines"
            bundle(
                    "kotlinx-coroutines",
                    listOf(
                            "kotlinx-coroutines-core",
                            "kotlinx-coroutines-jdk8",
                            // "kotlinx-coroutines-debug",
                          )
                  )
            
            alias("kotlinx-datetime") library ("org.jetbrains.kotlinx" to "kotlinx-datetime") version "0.3.2"
            
            // Kotlin UUID Support
            version("kotlinx-uuid", "0.0.12")
            alias("kotlinx-uuid") library ("app.softwork" to "kotlinx-uuid-core") versionRef "kotlinx-uuid"
            // Exposed UUID support
            alias("exposed-uuid") library ("app.softwork" to "kotlinx-uuid-exposed") versionRef "kotlinx-uuid"
    
            // Kotlin CLI library
            alias("clikt") library ("com.github.ajalt.clikt" to "clikt") version "3.4.0"
    
            // Kodein Dependency Injection
            alias("kodein") library ("org.kodein.di" to "kodein-di") version "7.10.0"
    
            // Konf
            alias("konf") library ("com.uchuhimo" to "konf") version "1.1.2"
    
            // Utility annotations
            alias("jetbrains-annotations") library ("org.jetbrains" to "annotations") version "22.0.0"
    
            // JDA
            alias("jda") library ("net.dv8tion" to "JDA") version "4.3.0_313"
    
            // Discord webhooks
            alias("discord-webhooks") library ("club.minnced" to "discord-webhooks") version "0.7.5"
    
            // JDA Kotlin extensions
            alias("jda-ktx") library ("com.github.minndevelopment" to "jda-ktx") version "0.7.0"
            
            // JDA utilities
            version("jda-utilities", "3.1.0")
            alias("jda-utilities-commons") library ("com.jagrosh" to "jda-utilities-commons") versionRef "jda-utilities"
            alias("jda-utilities-menu") library ("com.jagrosh" to "jda-utilities-menu") versionRef "jda-utilities"
            bundle("jda-utilities", listOf("jda-utilities-commons", "jda-utilities-menu"))
            
            // Cloud (Command handler)
            version("cloud", "1.6.1")
            alias("cloud-core") library ("cloud.commandframework" to "cloud-core") versionRef "cloud"
            alias("cloud-annotations") library ("cloud.commandframework" to "cloud-annotations") versionRef "cloud"
            alias("cloud-jda") library ("cloud.commandframework" to "cloud-jda") versionRef "cloud"
            alias("cloud-kotlin-extensions") library ("cloud.commandframework" to "cloud-kotlin-extensions") versionRef "cloud"
            alias("cloud-kotlin-coroutines") library ("cloud.commandframework" to "cloud-kotlin-coroutines") versionRef "cloud"
            alias("cloud-kotlin-coroutines-annotations") library ("cloud.commandframework" to "cloud-kotlin-coroutines-annotations") versionRef "cloud"
            alias("cloud-services") library ("cloud.commandframework" to "cloud-services") versionRef "cloud"
            bundle(
                    "cloud",
                    listOf(
                            "cloud-core",
                            "cloud-annotations",
                            "cloud-jda",
                            "cloud-kotlin-extensions",
                            "cloud-kotlin-coroutines",
                            "cloud-kotlin-coroutines-annotations",
                            "cloud-services",
                          )
                  )
            
            // Kryo fast object serialization
            alias("kryo") library ("com.esotericsoftware" to "kryo") version "5.2.1"
            
            // Reflections
            alias("reflections") library ("org.reflections" to "reflections") version "0.10.2"
            
            // SLF4J
            alias("slf4j") library ("org.slf4j" to "slf4j-api") version "1.7.33"
            alias("slf4k") library ("ca.solo-studios" to "slf4k") version "0.4.6" // SLF4J extension library
            alias("logback") library ("ch.qos.logback" to "logback-classic") version "1.2.10"
            
            // Kotlin HTTP api
            version("fuel", "2.3.1")
            alias("fuel-core") library ("com.github.kittinunf.fuel" to "fuel") versionRef "fuel"
            alias("fuel-coroutines") library ("com.github.kittinunf.fuel" to "fuel-coroutines") versionRef "fuel"
            alias("fuel-jackson") library ("com.github.kittinunf.fuel" to "fuel-jackson") versionRef "fuel"
            // alias("fuel-reactor") library ("com.github.kittinunf.fuel" to "fuel-reactor") version "$fuelVersion" // Use Reactor??
            bundle(
                    "fuel",
                    listOf(
                            "fuel-core",
                            "fuel-coroutines",
                            "fuel-jackson",
                            // "fuel-reactor",
                          )
                  )
            
            // Jackson (JSON object serialization/deserialization)
            version("jackson", "2.13.1")
            alias("jackson-core") library ("com.fasterxml.jackson.core" to "jackson-core") versionRef "jackson"
            alias("jackson-module-kotlin") library ("com.fasterxml.jackson.module" to "jackson-module-kotlin") versionRef "jackson"
            alias("jackson-annotations") library ("com.fasterxml.jackson.core" to "jackson-annotations") versionRef "jackson"
            alias("jackson-databind") library ("com.fasterxml.jackson.core" to "jackson-databind") versionRef "jackson"
            alias("jackson-dataformat-hocon") library ("org.honton.chas.hocon" to "jackson-dataformat-hocon") version "1.1.1" // HOCON support for Jackson
            bundle(
                    "jackson",
                    listOf(
                            "jackson-core",
                            "jackson-module-kotlin",
                            "jackson-annotations",
                            "jackson-databind",
                            "jackson-dataformat-hocon",
                          )
                  )
            
            // Persistent cache
            alias("ehcache") library ("org.ehcache" to "ehcache") version "3.9.9"
            
            // Guava
            alias("guava") library ("com.google.guava" to "guava") version "31.0.1-jre"
            
            // Hikari (SQL Connection Pooling)
            alias("hikari") library ("com.zaxxer" to "HikariCP") version "5.0.1"
            // SQLite
            // alias("sqlite-jdbc") library ("org.xerial" to "sqlite-jdbc") version "3.36.0.2"
            // MariaDB
            alias("mariadb") library ("org.mariadb.jdbc" to "mariadb-java-client") version "2.7.4"
            alias("postgresql") library ("org.postgresql" to "postgresql") version "42.3.1"
            
            // Make using SQL not the most excrutiating shit ever and actually bearable to use
            version("exposed", "0.37.3")
            alias("exposed-core") library ("org.jetbrains.exposed" to "exposed-core") versionRef "exposed"
            alias("exposed-dao") library ("org.jetbrains.exposed" to "exposed-dao") versionRef "exposed"
            alias("exposed-jdbc") library ("org.jetbrains.exposed" to "exposed-jdbc") versionRef "exposed"
            alias("exposed-java-time") library ("org.jetbrains.exposed" to "exposed-java-time") versionRef "exposed"
            bundle(
                    "exposed",
                    listOf(
                            "exposed-core",
                            "exposed-dao",
                            "exposed-jdbc",
                            "exposed-java-time",
                            "exposed-uuid"
                          )
                  )
            // Exposed Migrations
            alias("exposed-migrations") library ("gay.solonovamax" to "exposed-migrations") version "4.0.1"
            
            // Apache Lucene search engine
            version("lucene", "9.0.0")
            alias("lucene-core") library ("org.apache.lucene" to "lucene-core") versionRef "lucene"
            // alias("lucene-memory") library ("org.apache.lucene" to "lucene-memory") versionRef "lucene"
            // alias("lucene-suggest") library ("org.apache.lucene" to "lucene-suggest") versionRef "lucene"
            alias("lucene-queryparser") library ("org.apache.lucene" to "lucene-queryparser") versionRef "lucene"
            alias("lucene-analysis-common") library ("org.apache.lucene" to "lucene-analysis-common") versionRef "lucene"
            bundle(
                    "lucene",
                    listOf(
                            "lucene-core",
                            // "lucene-memory",
                            // "lucene-suggest",
                            "lucene-queryparser",
                            "lucene-analysis-common",
                          )
                  )
            
            // Markdown library
            alias("jetbrains-markdown") library ("org.jetbrains" to "markdown") version "0.2.4"
            
            // Git
            alias("jgit") library ("org.eclipse.jgit" to "org.eclipse.jgit") version "6.0.0.202111291000-r"
            
            // Used for fast random number generators
            alias("dsiutils") library ("it.unimi.dsi" to "dsiutils") version "2.6.17"
            
            alias("commons-compress") library ("org.apache.commons" to "commons-compress") version "1.21"
            alias("commons-io") library ("org.apache.commons" to "commons-io") version "1.3.2"
            
            // Testing (JUnit 5)
            version("junit", "5.8.2")
            alias("junit-jupiter-api") library ("org.junit.jupiter" to "junit-jupiter-api") versionRef "junit"
            alias("junit-jupiter-engine") library ("org.junit.jupiter" to "junit-jupiter-engine") versionRef "junit"
            alias("junit-jupiter-params") library ("org.junit.jupiter" to "junit-jupiter-params") versionRef "junit"
            bundle(
                    "junit",
                    listOf(
                            "junit-jupiter-api",
                            "junit-jupiter-engine",
                            "junit-jupiter-params",
                          )
                  )
            
        }
    }
}

infix fun VersionCatalogBuilder.alias(alias: String): AliasBuilder = this.alias(alias)
infix fun AliasBuilder.library(groupName: Pair<String, String>): LibraryAliasBuilder = this.to(groupName.first, groupName.second)
infix fun LibraryAliasBuilder.versionRef(versionRef: String): Unit = this.versionRef(versionRef)
infix fun LibraryAliasBuilder.version(version: String): Unit = this.version(version)
infix fun LibraryAliasBuilder.version(versionSpec: (MutableVersionConstraint).() -> Unit): Unit = this.version(versionSpec)
infix fun PluginAliasBuilder.versionRef(versionRef: String): Unit = this.versionRef(versionRef)
infix fun PluginAliasBuilder.version(version: String): Unit = this.version(version)
infix fun PluginAliasBuilder.version(versionSpec: (MutableVersionConstraint).() -> Unit): Unit = this.version(versionSpec)
infix fun AliasBuilder.plugin(pluginId: String): PluginAliasBuilder = toPluginId(pluginId)