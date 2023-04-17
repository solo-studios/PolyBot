/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2023-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file build.gradle.kts is part of PolyBot
 * Last modified on 16-04-2023 03:13 p.m.
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

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    
    alias(libs.plugins.kotlin.ksp)
    
    alias(libs.plugins.dokka)
    
    application
    
    id("ca.solo-studios.polybot-gradle") version "0"
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
    
    target {
        compilations.configureEach {
            kotlinOptions {
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

dependencies {
    ksp(project(":annotation-processor"))
    implementation(project(":core"))
}