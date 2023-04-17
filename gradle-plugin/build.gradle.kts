/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file build.gradle.kts is part of PolyBot
 * Last modified on 17-04-2023 12:14 p.m.
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
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    
    alias(libs.plugins.kotlin.ksp)
    
    `java-gradle-plugin`
    
    alias(libs.plugins.dokka)
    
    // alias(libs.plugins.)
    id("com.gradle.plugin-publish") version "1.1.0"
}

repositories {
    maven("https://maven.solo-studios.ca/releases")
    
    mavenCentral()
}

version = "0"

kotlin {
    jvmToolchain(11)
    
    explicitApi()
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

val functionalTest: SourceSet by sourceSets.creating

publishing {
    repositories {
        mavenLocal()
    }
}

gradlePlugin {
    plugins {
        create("PolyBotGradlePlugin") {
            id = "ca.solo-studios.polybot-gradle"
            implementationClass = "ca.solostudios.gradle.polybot.PolyBotGradlePlugin"
        }
    }
    
    testSourceSets(functionalTest)
}

dependencies {
    // Gradle Api
    compileOnly(gradleApi())
    compileOnly(gradleKotlinDsl())
    
    // Kotlin
    implementation(libs.bundles.kotlin.base)
    // Kotlin Serialization
    implementation(libs.bundles.kotlinx.serialization.base)
    // Kotlin Coroutines
    implementation(libs.bundles.kotlinx.coroutines.core)
    
    // Jetbrains Annotations
    implementation(libs.jetbrains.annotations)
    
    // SLF4J
    implementation(libs.slf4j)
    // SLF4J extension library
    implementation(libs.slf4k)
    
    // Strata
    implementation(libs.bundles.strata)
    
    functionalTestImplementation(project)
    
    // Testing (JUnit 5)
    functionalTestImplementation(libs.bundles.junit)
    
    functionalTestImplementation(kotlin("test"))
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

/*-----------------------*
 | BEGIN Utility Methods |
 *-----------------------*/

fun DependencyHandler.functionalTestImplementation(dependencyNotation: Any): Dependency? =
        add("functionalTestImplementation", dependencyNotation)

/*---------------------*
 | END Utility Methods |
 *---------------------*/