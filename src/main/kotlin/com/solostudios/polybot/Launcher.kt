/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Launcher.kt is part of PolyhedralBot
 * Last modified on 14-06-2021 07:27 p.m.
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

package com.solostudios.polybot

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.jasonclawson.jackson.dataformat.hocon.HoconFactory
import com.solostudios.polybot.config.PolyConfig
import java.io.File
import org.slf4j.kotlin.getLogger
import kotlin.system.exitProcess

private val logger by getLogger()

fun main(args: Array<String>) {
    
    val config = readConfig("polybot.conf")
    
    PolyBot(config)
}

private fun readConfig(fileName: String) = readConfig(File(fileName))

private fun readConfig(file: File): PolyConfig {
    if (!file.exists()) {
        createConfigFileAndExit(file)
    }
    val mapper = ObjectMapper(HoconFactory())
            .registerKotlinModule()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true)
            .configure(JsonParser.Feature.IGNORE_UNDEFINED, true)
    
    return mapper.readValue(file)
}

private fun createConfigFileAndExit(file: File) {
    logger.error("File '${file.name}' not present. Writing default config to '${file.name}'.\n" +
                         "Please edit this file to include the bot token + all other fields.")
    
    val stream = PolyBot::class.java.getResourceAsStream("/default.conf")
    
    if (stream != null)
        stream.transferTo(file.outputStream())
    else {
        logger.error("Could not write default config as it was not found.")
        exitProcess(-2)
    }
    
    logger.info("Wrote default config to '${file.name}'. Please edit the file before running again.")
    
    exitProcess(1)
}