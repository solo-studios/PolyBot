/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file settings.gradle.kts is part of PolyhedralBot
 * Last modified on 14-02-2022 09:42 a.m.
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
import org.gradle.api.initialization.dsl.VersionCatalogBuilder.LibraryAliasBuilder
import org.gradle.api.initialization.dsl.VersionCatalogBuilder.PluginAliasBuilder

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
            library("kotlin-stdlib", "org.jetbrains.kotlin", "kotlin-stdlib") versionRef "kotlin"
            library("kotlin-stdlib-jdk7", "org.jetbrains.kotlin", "kotlin-stdlib-jdk7") versionRef "kotlin"
            library("kotlin-stdlib-jdk8", "org.jetbrains.kotlin", "kotlin-stdlib-jdk8") versionRef "kotlin"
            library("kotlin-reflect", "org.jetbrains.kotlin", "kotlin-reflect") versionRef "kotlin"
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
    
            plugin("kotlin-jvm", "org.jetbrains.kotlin.jvm") versionRef "kotlin"
            plugin("kotlin-noarg", "org.jetbrains.kotlin.plugin.noarg") versionRef "kotlin"
            plugin("kotlin-serialization", "org.jetbrains.kotlin.plugin.serialization") versionRef "kotlin"
    
            library("kotlin-script-runtime", "org.jetbrains.kotlin", "kotlin-script-runtime") versionRef "kotlin"
            library("kotlin-compiler", "org.jetbrains.kotlin", "kotlin-compiler-embeddable") versionRef "kotlin"
            library("kotlin-scripting-compiler", "org.jetbrains.kotlin", "kotlin-scripting-compiler-embeddable") versionRef "kotlin"
            bundle(
                    "kotlin-scripting",
                    listOf("kotlin-script-runtime", "kotlin-compiler", "kotlin-scripting-compiler")
                  )
    
            // Kotlin Serialization
            version("kotlinx-serialization", "1.3.2")
            library("kotlinx-serialization-core", "org.jetbrains.kotlinx", "kotlinx-serialization-core") versionRef "kotlinx-serialization"
            library("kotlinx-serialization-json", "org.jetbrains.kotlinx", "kotlinx-serialization-json") versionRef "kotlinx-serialization"
    
            bundle("kotlinx-serialization", listOf("kotlinx-serialization-core", "kotlinx-serialization-json"))
    
            // Kotlin Coroutines
            version("kotlinx-coroutines", "1.6.0-native-mt")
            library("kotlinx-coroutines-core", "org.jetbrains.kotlinx", "kotlinx-coroutines-core") versionRef "kotlinx-coroutines"
            library("kotlinx-coroutines-jdk8", "org.jetbrains.kotlinx", "kotlinx-coroutines-jdk8") versionRef "kotlinx-coroutines"
            // library("kotlinx-coroutines-debug", "org.jetbrains.kotlinx", "kotlinx-coroutines-debug") versionRef "kotlinx-coroutines"
            bundle(
                    "kotlinx-coroutines",
                    listOf(
                            "kotlinx-coroutines-core",
                            "kotlinx-coroutines-jdk8",
                            // "kotlinx-coroutines-debug",
                          )
                  )
    
            library("kotlinx-datetime", "org.jetbrains.kotlinx", "kotlinx-datetime") version "0.3.2"
    
            // Kotlin UUID Support
            version("kotlinx-uuid", "0.0.12")
            library("kotlinx-uuid", "app.softwork", "kotlinx-uuid-core") versionRef "kotlinx-uuid"
            // Exposed UUID support
            library("exposed-uuid", "app.softwork", "kotlinx-uuid-exposed") versionRef "kotlinx-uuid"
    
            // Kotlin CLI library
            library("clikt", "com.github.ajalt.clikt", "clikt") version "3.4.0"
    
            // Kodein Dependency Injection
            library("kodein", "org.kodein.di", "kodein-di") version "7.10.0"
    
            // Konf
            library("konf", "com.uchuhimo", "konf") version "1.1.2"
    
            // Utility annotations
            library("jetbrains-annotations", "org.jetbrains", "annotations") version "22.0.0"
    
            // JDA
            library("jda", "net.dv8tion", "JDA") version "4.3.0_313"
    
            // Discord webhooks
            library("discord-webhooks", "club.minnced", "discord-webhooks") version "0.7.5"
    
            // JDA Kotlin extensions
            library("jda-ktx", "com.github.minndevelopment", "jda-ktx") version "0.7.0"
    
            // JDA utilities
            version("jda-utilities", "3.1.0")
            library("jda-utilities-commons", "com.jagrosh", "jda-utilities-commons") versionRef "jda-utilities"
            library("jda-utilities-menu", "com.jagrosh", "jda-utilities-menu") versionRef "jda-utilities"
            bundle("jda-utilities", listOf("jda-utilities-commons", "jda-utilities-menu"))
    
            // Cloud (Command handler)
            version("cloud", "1.6.1")
            library("cloud-core", "cloud.commandframework", "cloud-core") versionRef "cloud"
            library("cloud-annotations", "cloud.commandframework", "cloud-annotations") versionRef "cloud"
            library("cloud-jda", "cloud.commandframework", "cloud-jda") versionRef "cloud"
            library("cloud-kotlin-extensions", "cloud.commandframework", "cloud-kotlin-extensions") versionRef "cloud"
            library("cloud-kotlin-coroutines", "cloud.commandframework", "cloud-kotlin-coroutines") versionRef "cloud"
            library(
                    "cloud-kotlin-coroutines-annotations",
                    "cloud.commandframework",
                    "cloud-kotlin-coroutines-annotations"
                   ) versionRef "cloud"
            library("cloud-services", "cloud.commandframework", "cloud-services") versionRef "cloud"
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
            library("kryo", "com.esotericsoftware", "kryo") version "5.2.1"
    
            // Reflections
            library("reflections", "org.reflections", "reflections") version "0.10.2"
    
            // SLF4J
            library("slf4j", "org.slf4j", "slf4j-api") version "1.7.33"
            library("slf4k", "ca.solo-studios", "slf4k") version "0.4.6" // SLF4J extension library
            library("logback", "ch.qos.logback", "logback-classic") version "1.2.10"
    
            // Kotlin HTTP api
            version("fuel", "2.3.1")
            library("fuel-core", "com.github.kittinunf.fuel", "fuel") versionRef "fuel"
            library("fuel-coroutines", "com.github.kittinunf.fuel", "fuel-coroutines") versionRef "fuel"
            library("fuel-jackson", "com.github.kittinunf.fuel", "fuel-jackson") versionRef "fuel"
            // library("fuel-reactor", "com.github.kittinunf.fuel", "fuel-reactor") version "$fuelVersion" // Use Reactor??
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
            library("jackson-core", "com.fasterxml.jackson.core", "jackson-core") versionRef "jackson"
            library("jackson-module-kotlin", "com.fasterxml.jackson.module", "jackson-module-kotlin") versionRef "jackson"
            library("jackson-annotations", "com.fasterxml.jackson.core", "jackson-annotations") versionRef "jackson"
            library("jackson-databind", "com.fasterxml.jackson.core", "jackson-databind") versionRef "jackson"
            // HOCON support for Jackson
            library("jackson-dataformat-hocon", "org.honton.chas.hocon", "jackson-dataformat-hocon") version "1.1.1"
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
            library("ehcache", "org.ehcache", "ehcache") version "3.9.9"
    
            // Guava
            library("guava", "com.google.guava", "guava") version "31.0.1-jre"
    
            // Hikari (SQL Connection Pooling)
            library("hikari", "com.zaxxer", "HikariCP") version "5.0.1"
            // SQLite
            // library("sqlite-jdbc", "org.xerial", "sqlite-jdbc") version "3.36.0.2"
            // MariaDB
            library("mariadb", "org.mariadb.jdbc", "mariadb-java-client") version "2.7.4"
            library("postgresql", "org.postgresql", "postgresql") version "42.3.1"
    
            // Make using SQL not the most excrutiating shit ever and actually bearable to use
            version("exposed", "0.37.3")
            library("exposed-core", "org.jetbrains.exposed", "exposed-core") versionRef "exposed"
            library("exposed-dao", "org.jetbrains.exposed", "exposed-dao") versionRef "exposed"
            library("exposed-jdbc", "org.jetbrains.exposed", "exposed-jdbc") versionRef "exposed"
            library("exposed-java-time", "org.jetbrains.exposed", "exposed-java-time") versionRef "exposed"
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
            library("exposed-migrations", "gay.solonovamax", "exposed-migrations") version "4.0.1"
    
            // Apache Lucene search engine
            version("lucene", "9.0.0")
            library("lucene-core", "org.apache.lucene", "lucene-core") versionRef "lucene"
            // library("lucene-memory", "org.apache.lucene", "lucene-memory") versionRef "lucene"
            // library("lucene-suggest", "org.apache.lucene", "lucene-suggest") versionRef "lucene"
            library("lucene-queryparser", "org.apache.lucene", "lucene-queryparser") versionRef "lucene"
            library("lucene-analysis-common", "org.apache.lucene", "lucene-analysis-common") versionRef "lucene"
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
            library("jetbrains-markdown", "org.jetbrains", "markdown") version "0.2.4"
    
            // Git
            library("jgit", "org.eclipse.jgit", "org.eclipse.jgit") version "6.0.0.202111291000-r"
    
            // Used for fast random number generators
            library("dsiutils", "it.unimi.dsi", "dsiutils") version "2.6.17"
    
            library("commons-compress", "org.apache.commons", "commons-compress") version "1.21"
            library("commons-io", "org.apache.commons", "commons-io") version "1.3.2"
    
            // Testing (JUnit 5)
            version("junit", "5.8.2")
            library("junit-jupiter-api", "org.junit.jupiter", "junit-jupiter-api") versionRef "junit"
            library("junit-jupiter-engine", "org.junit.jupiter", "junit-jupiter-engine") versionRef "junit"
            library("junit-jupiter-params", "org.junit.jupiter", "junit-jupiter-params") versionRef "junit"
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

infix fun LibraryAliasBuilder.versionRef(versionRef: String): Unit = this.versionRef(versionRef)
infix fun LibraryAliasBuilder.version(version: String): Unit = this.version(version)
infix fun LibraryAliasBuilder.version(versionSpec: (MutableVersionConstraint).() -> Unit): Unit = this.version(versionSpec)
infix fun PluginAliasBuilder.versionRef(versionRef: String): Unit = this.versionRef(versionRef)
infix fun PluginAliasBuilder.version(version: String): Unit = this.version(version)
infix fun PluginAliasBuilder.version(versionSpec: (MutableVersionConstraint).() -> Unit): Unit = this.version(versionSpec)