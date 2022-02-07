/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file build.gradle.kts is part of PolyhedralBot
 * Last modified on 06-02-2022 10:40 p.m.
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

plugins {
    java
    kotlin("jvm")
}

repositories {
    mavenCentral()
    
    maven { // JDA
        name = "dv8tion-repo"
        url = uri("https://m2.dv8tion.net/releases")
    }
}

kotlin {
    explicitApi()
    target {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
}

dependencies {
    // Kotlin
    implementation(libs.kotlin.stdlib)
    
    // Kotlin Coroutines
    implementation(libs.bundles.kotlinx.coroutines)
    
    // Kotlin Datetime
    implementation(libs.kotlinx.datetime)
    
    // Jetbrains Annotations
    implementation(libs.jetbrains.annotations)
    
    // JDA
    implementation(libs.jda)
    // Discord webhooks
    implementation(libs.discord.webhooks)
    // JDA Kotlin extensions
    implementation(libs.jda.ktx)
    // JDA utilities
    implementation(libs.bundles.jda.utilities)
}