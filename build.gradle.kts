import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.4.31"
}

group = "com.solostudios.polyhedralbot"
version = "1.0.0"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib"))
    
    // JDA
    implementation("net.dv8tion:JDA:4.2.+")
    
    // JDA utilities
    
    implementation("com.jagrosh:jda-utilities-commons:3.0.5")
    implementation("com.jagrosh:jda-utilities-command:3.0.5")
    implementation("com.jagrosh:jda-utilities-doc:3.0.5") // figure out what this is
    implementation("com.jagrosh:jda-utilities-menu:3.0.5")
    
    // Cloud
    implementation("cloud.commandframework:cloud-core:1.4.0")
    implementation("cloud.commandframework:cloud-annotations:1.4.0")
    implementation("cloud.commandframework:cloud-jda:1.4.0")
    implementation("cloud.commandframework:cloud-kotlin-extensions:1.4.0")
    
    // Reflections
    implementation("org.reflections:reflections:0.9.12")
    
    // Logging
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    
    // other stuff (remove me)
    //    implementation("com.jagrosh:EasySQL:0.3")
    //    implementation("club.minnced:discord-webhooks:0.1.8")
    //    implementation("com.typesafe:config:1.3.2")
    
    // Kotlin HTTP api
    implementation("com.github.kittinunf.fuel:fuel:2.3.1")
    implementation("com.github.kittinunf.fuel:fuel-coroutines:2.3.1")
    implementation("com.github.kittinunf.fuel:fuel-jackson:2.3.1")
    //    implementation("com.github.kittinunf.fuel:fuel-reactor:2.3.1") // Use Reactor??
    
    // Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.12.3")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.12.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.3")
    
    // Guava
    implementation("com.google.guava:guava:30.1.1-jre")
    
    // SQLite
    implementation("org.xerial:sqlite-jdbc:3.32.3.2")
    
    // Make using SQL actually bearable to use
    implementation("org.jetbrains.exposed:exposed-core:+")
    implementation("org.jetbrains.exposed:exposed-dao:+")
    implementation("org.jetbrains.exposed:exposed-jdbc:+")
    implementation("org.jetbrains.exposed:exposed-java-time:+")
    
    // Testing (switch to JUnit 5)
    //    testImplementation("junit:junit:4.13.1")
    //    testImplementation("org.hamcrest:hamcrest-core:1.3")
    
    // Testing (JUnit 5)
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-params:5.7.0")
    
    // idk
    //    implementation(kotlin("script-runtime"))
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

/*
if anything, I want to just go back to being friends with
- kohi
- jesse (juno)/aurora
- leohra
for my other friends, yes I liked them, but I think I'd be able to live without them (no offense to them).. I just *REALLY* want to
 */


