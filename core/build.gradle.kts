/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file build.gradle.kts is part of PolyBot
 * Last modified on 21-02-2023 06:20 p.m.
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

@file:Suppress("SuspiciousCollectionReassignment", "DSL_SCOPE_VIOLATION")

import kotlin.math.max

plugins {
    java
    
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.noarg)
    alias(libs.plugins.kotlin.serialization)
    
    alias(libs.plugins.kotlin.ksp)
    
    alias(libs.plugins.dokka)
    
    distribution
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
    
    explicitApi()
    target {
        compilations.configureEach {
            kotlinOptions {
                jvmTarget = "11"
                apiVersion = "1.7"
                languageVersion = "1.7"
                freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
            }
        }
    }
}

java {
    withSourcesJar()
}

dependencies {
    // Common Subproject
    // project.projects
    api(project(":common"))
    
    api(libs.ksp.service)
    ksp(libs.ksp.service)
    
    // Kotlin
    api(libs.bundles.kotlin.base)
    api(libs.kotlin.reflect)
    // Kotlin Serialization
    api(libs.bundles.kotlinx.serialization.base)
    
    // Kotlin Coroutines
    api(libs.bundles.kotlinx.coroutines.core)
    // Kotlin Datetime
    api(libs.kotlinx.datetime)
    
    // Kodein Dependency Injection
    api(libs.kodein.di)
    
    // Konf
    api(libs.bundles.konf)
    
    // Jetbrains Annotations
    api(libs.jetbrains.annotations)
    
    // Guava
    api(libs.guava)
    api(libs.guava.kotlin)
    
    // JDA
    api(libs.jda) {
        exclude(module = "opus-java")
    }
    // Discord webhooks
    api(libs.discord.webhooks)
    // JDA Kotlin extensions
    api(libs.jda.ktx)
    // JDA utilities
    api(libs.bundles.jda.utilities)
    
    // Cloud
    api(libs.bundles.cloud)
    
    // SLF4J
    api(libs.slf4j)
    // SLF4J extension library
    api(libs.slf4k)
    
    // Strata
    api(libs.bundles.strata)
    
    implementation(libs.logback)
    
    implementation(libs.dsi.dsiutils)
    
    // Testing
    testApi(kotlin("test"))
    
    testApi(libs.bundles.junit)
    testApi(libs.bundles.kotlinx.coroutines.debugging)
    
    testApi(libs.mockk)
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
    
    withType<Jar>().configureEach {
        from(rootProject.file("LICENSE"))
    }
}