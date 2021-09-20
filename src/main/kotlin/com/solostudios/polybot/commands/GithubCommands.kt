/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file GithubCommands.kt is part of PolyhedralBot
 * Last modified on 20-09-2021 01:46 a.m.
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

package com.solostudios.polybot.commands

import com.solostudios.polybot.PolyBot
import com.solostudios.polybot.cloud.PolyCommandContainer
import com.solostudios.polybot.cloud.PolyCommands
import com.solostudios.polybot.entities.PolyMessage
import com.solostudios.polybot.util.dayMonthYearFormatter
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import org.knowm.xchart.BitmapEncoder
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler
import org.kohsuke.github.GitHub
import org.slf4j.kotlin.*

@PolyCommandContainer
class GithubCommands(bot: PolyBot) : PolyCommands(bot) {
    private val logger by org.slf4j.kotlin.getLogger()
    val github: GitHub = GitHub.connectAnonymously()
    
    // @CommandMethod("ghstatus|gh-status|gh status")
    fun ghStatus(message: PolyMessage) {
        val now = LocalDate.now()
        
        val sixMonths = now.minus(Period.ofMonths(6))
        
        val stars = github.getRepository(githubRepo)
                .listStargazers2()
                .withPageSize(50)
                .groupingBy {
                    it.starredAt
                }
                .eachCount()
                .mapKeys {
                    (sixMonths.toEpochDay() - it.key.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toEpochDay()).toInt()
                }
        
        
        val chart = XYChartBuilder()
                .width(600)
                .height(300)
                .title("Start Gazers Over Time")
                .theme(Styler.ChartTheme.GGPlot2)
                .build()
        
        chart.styler.apply {
            isCursorEnabled = false
            isToolTipsEnabled = false
            isZoomEnabled = false
            setyAxisTickLabelsFormattingFunction {
                LocalDate.ofEpochDay(it.toLong() + sixMonths.toEpochDay()).format(dayMonthYearFormatter)
            }
        }
        
        chart.addSeries("Stars", stars.values.toIntArray(), stars.keys.toIntArray())
        
        logger.info { "Made chart" }
        
        val input = PipedInputStream()
        val output = PipedOutputStream(input)
        
        // message.textChannel.sendFile(input, "image.png").queue()
        
        BitmapEncoder.saveBitmap(chart, output, BitmapEncoder.BitmapFormat.PNG)
        logger.info { "Finished" }
    }
    
    companion object {
        const val githubRepo = "PolyhedralDev/Terra" // TODO: 2021-08-25 Don't hardcode this and use item from config file instead
    }
}
