/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file settings.gradle.kts is part of PolyBot
 * Last modified on 10-06-2022 03:01 p.m.
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
import de.fayard.refreshVersions.core.StabilityLevel
import org.gradle.api.initialization.dsl.VersionCatalogBuilder.LibraryAliasBuilder
import org.gradle.api.initialization.dsl.VersionCatalogBuilder.PluginAliasBuilder

rootProject.name = "PolyBot"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("de.fayard.refreshVersions") version "0.40.2"
}

refreshVersions {
    extraArtifactVersionKeyRules(file("versions.rules"))
    
    rejectVersionIf {
        @Suppress("UnstableApiUsage")
        candidate.stabilityLevel != StabilityLevel.Stable
    }
}

include(":core")
include(":common")

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    versionCatalogs {
        create("libs") {
            // Kotlin
            library("kotlin-stdlib", "org.jetbrains.kotlin", "kotlin-stdlib") version "_"
            library("kotlin-stdlib-jdk7", "org.jetbrains.kotlin", "kotlin-stdlib-jdk7") version "_"
            library("kotlin-stdlib-jdk8", "org.jetbrains.kotlin", "kotlin-stdlib-jdk8") version "_"
            library("kotlin-reflect", "org.jetbrains.kotlin", "kotlin-reflect") version "_"
            bundle(
                    "kotlin",
                    listOf("kotlin-stdlib", "kotlin-stdlib-jdk7", "kotlin-stdlib-jdk8", "kotlin-reflect")
                  )
            
            pluginManagement {
                plugins {
                    kotlin("jvm")
                    kotlin("plugin.noarg")
                    kotlin("plugin.serialization")
                }
            }
            
            plugin("kotlin-jvm", "org.jetbrains.kotlin.jvm") version "_"
            plugin("kotlin-noarg", "org.jetbrains.kotlin.plugin.noarg") version "_"
            plugin("kotlin-serialization", "org.jetbrains.kotlin.plugin.serialization") version "_"
            
            library("kotlin-script-runtime", "org.jetbrains.kotlin", "kotlin-script-runtime") version "_"
            library("kotlin-compiler", "org.jetbrains.kotlin", "kotlin-compiler-embeddable") version "_"
            library("kotlin-scripting-compiler", "org.jetbrains.kotlin", "kotlin-scripting-compiler-embeddable") version "_"
            bundle(
                    "kotlin-scripting",
                    listOf("kotlin-script-runtime", "kotlin-compiler", "kotlin-scripting-compiler")
                  )
            
            // Kotlin Serialization
            library("kotlinx-serialization-core", "org.jetbrains.kotlinx", "kotlinx-serialization-core") version "_"
            library("kotlinx-serialization-json", "org.jetbrains.kotlinx", "kotlinx-serialization-json") version "_"
            
            bundle("kotlinx-serialization", listOf("kotlinx-serialization-core", "kotlinx-serialization-json"))
            
            // Kotlin Coroutines
            library("kotlinx-coroutines-core", "org.jetbrains.kotlinx", "kotlinx-coroutines-core") version "_"
            library("kotlinx-coroutines-jdk8", "org.jetbrains.kotlinx", "kotlinx-coroutines-jdk8") version "_"
            // library("kotlinx-coroutines-debug", "org.jetbrains.kotlinx", "kotlinx-coroutines-debug") versionRef "kotlinx-coroutines"
            bundle(
                    "kotlinx-coroutines",
                    listOf(
                            "kotlinx-coroutines-core",
                            "kotlinx-coroutines-jdk8",
                            // "kotlinx-coroutines-debug",
                          )
                  )
            
            library("kotlinx-datetime", "org.jetbrains.kotlinx", "kotlinx-datetime") version "_"
            
            // Kotlin UUID Support
            version("kotlinx-uuid", "0.0.12")
            library("kotlinx-uuid", "app.softwork", "kotlinx-uuid-core") version "_"
            // Exposed UUID support
            library("exposed-uuid", "app.softwork", "kotlinx-uuid-exposed") version "_"
            
            // Kotlin CLI library
            library("clikt", "com.github.ajalt.clikt", "clikt") version "_"
            
            // Kodein Dependency Injection
            library("kodein", "org.kodein.di", "kodein-di") version "_"
            
            // Konf
            library("konf", "com.uchuhimo", "konf") version "_"
            
            // Utility annotations
            library("jetbrains-annotations", "org.jetbrains", "annotations") version "_"
            
            // JDA
            library("jda", "net.dv8tion", "JDA") version "_"
            
            // Discord webhooks
            library("discord-webhooks", "club.minnced", "discord-webhooks") version "_"
            
            // JDA Kotlin extensions
            library("jda-ktx", "com.github.minndevelopment", "jda-ktx") version "_"
            
            // JDA utilities
            library("jda-utilities-commons", "com.jagrosh", "jda-utilities-commons") version "_"
            library("jda-utilities-menu", "com.jagrosh", "jda-utilities-menu") version "_"
            bundle("jda-utilities", listOf("jda-utilities-commons", "jda-utilities-menu"))
            
            // Cloud (Command handler)
            library("cloud-core", "cloud.commandframework", "cloud-core") version "_"
            library("cloud-annotations", "cloud.commandframework", "cloud-annotations") version "_"
            library("cloud-jda", "cloud.commandframework", "cloud-jda") version "_"
            library("cloud-kotlin-extensions", "cloud.commandframework", "cloud-kotlin-extensions") version "_"
            library("cloud-kotlin-coroutines", "cloud.commandframework", "cloud-kotlin-coroutines") version "_"
            library(
                    "cloud-kotlin-coroutines-annotations",
                    "cloud.commandframework",
                    "cloud-kotlin-coroutines-annotations"
                   ) version "_"
            library("cloud-services", "cloud.commandframework", "cloud-services") version "_"
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
            
            // Kryo (fast object serialization)
            library("kryo", "com.esotericsoftware", "kryo") version "_"
            
            // Reflections
            library("reflections", "org.reflections", "reflections") version "_"
            
            // SLF4J
            library("slf4j", "org.slf4j", "slf4j-api") version "_"
            library("slf4k", "ca.solo-studios", "slf4k") version "_" // SLF4J extension library
            library("logback", "ch.qos.logback", "logback-classic") version "_"
            
            // Kotlin HTTP api
            library("fuel-core", "com.github.kittinunf.fuel", "fuel") version "_"
            library("fuel-coroutines", "com.github.kittinunf.fuel", "fuel-coroutines") version "_"
            library("fuel-jackson", "com.github.kittinunf.fuel", "fuel-jackson") version "_"
            // library("fuel-reactor", "com.github.kittinunf.fuel", "fuel-reactor") version "_" // Use Reactor??
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
            library("jackson-core", "com.fasterxml.jackson.core", "jackson-core") version "_"
            library("jackson-module-kotlin", "com.fasterxml.jackson.module", "jackson-module-kotlin") version "_"
            library("jackson-annotations", "com.fasterxml.jackson.core", "jackson-annotations") version "_"
            library("jackson-databind", "com.fasterxml.jackson.core", "jackson-databind") version "_"
            // HOCON support for Jackson
            library("jackson-dataformat-hocon", "org.honton.chas.hocon", "jackson-dataformat-hocon") version "_"
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
            library("ehcache", "org.ehcache", "ehcache") version "_"
            
            // Guava
            library("guava", "com.google.guava", "guava") version "_"
            
            // Hikari (SQL Connection Pooling)
            library("hikari", "com.zaxxer", "HikariCP") version "_"
            // SQLite
            // library("sqlite-jdbc", "org.xerial", "sqlite-jdbc") version "3.36.0.2"
            // MariaDB
            library("mariadb", "org.mariadb.jdbc", "mariadb-java-client") version "_"
            library("postgresql", "org.postgresql", "postgresql") version "_"
            
            // Make using SQL not the most excruciating shit ever and actually bearable to use
            library("exposed-core", "org.jetbrains.exposed", "exposed-core") version "_"
            library("exposed-dao", "org.jetbrains.exposed", "exposed-dao") version "_"
            library("exposed-jdbc", "org.jetbrains.exposed", "exposed-jdbc") version "_"
            library("exposed-java-time", "org.jetbrains.exposed", "exposed-java-time") version "_"
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
            library("exposed-migrations", "gay.solonovamax", "exposed-migrations") version "_"
            
            // Apache Lucene search engine
            library("lucene-core", "org.apache.lucene", "lucene-core") version "_"
            // library("lucene-memory", "org.apache.lucene", "lucene-memory") version "_"
            // library("lucene-suggest", "org.apache.lucene", "lucene-suggest") version "_"
            library("lucene-queryparser", "org.apache.lucene", "lucene-queryparser") version "_"
            library("lucene-analysis-common", "org.apache.lucene", "lucene-analysis-common") version "_"
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
            library("jetbrains-markdown", "org.jetbrains", "markdown") version "_"
            
            // Git
            library("jgit", "org.eclipse.jgit", "org.eclipse.jgit") version "_"
            
            // Used for fast random number generators
            library("dsiutils", "it.unimi.dsi", "dsiutils") version "_"
            
            library("commons-compress", "org.apache.commons", "commons-compress") version "_"
            library("commons-io", "org.apache.commons", "commons-io") version "_"
            
            // Testing (JUnit 5)
            library("junit-jupiter-api", "org.junit.jupiter", "junit-jupiter-api") version "_"
            library("junit-jupiter-engine", "org.junit.jupiter", "junit-jupiter-engine") version "_"
            library("junit-jupiter-params", "org.junit.jupiter", "junit-jupiter-params") version "_"
            bundle(
                    "junit",
                    listOf(
                            "junit-jupiter-api",
                            "junit-jupiter-engine",
                            "junit-jupiter-params",
                          )
                  )
            
            library("strata", "ca.solo-studios", "strata") version "_"
            library("strata-kotlin", "ca.solo-studios", "strata-kotlin") version "_"
            
            bundle("strata", listOf("strata", "strata-kotlin"))
            
            library("ksp-service", "ca.solo-studios", "ksp-service-annotation") version "_"
        }
    }
}

infix fun LibraryAliasBuilder.versionRef(versionRef: String): Unit = this.versionRef(versionRef)
infix fun LibraryAliasBuilder.version(version: String): Unit = this.version(version)
infix fun LibraryAliasBuilder.version(versionSpec: (MutableVersionConstraint).() -> Unit): Unit = this.version(versionSpec)
infix fun PluginAliasBuilder.versionRef(versionRef: String): Unit = this.versionRef(versionRef)
infix fun PluginAliasBuilder.version(version: String): Unit = this.version(version)
infix fun PluginAliasBuilder.version(versionSpec: (MutableVersionConstraint).() -> Unit): Unit = this.version(versionSpec)