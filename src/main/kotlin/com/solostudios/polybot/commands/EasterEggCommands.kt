/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file EasterEggCommands.kt is part of PolyhedralBot
 * Last modified on 22-08-2021 02:28 a.m.
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

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.specifier.Greedy
import com.solostudios.polybot.PolyBot
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.utils.MarkdownSanitizer
import org.slf4j.kotlin.getLogger
import kotlin.random.Random

class EasterEggCommands(val bot: PolyBot) {
    private val logger by getLogger()
    
    @CommandMethod("gay|crimes")
    fun crimes(message: Message) {
        message.reply("Be gay, do crimes.").mentionRepliedUser(false).queue()
    }
    
    @CommandMethod("uwu|uwuify|owo|owoify <text>")
    fun uwuify(message: Message,
               @Greedy
               @Argument("text")
               text: String) {
        val uwuText = UwU.transformTextToUwU(text)
        
        logger.info(uwuText.trim())
        
        message.reply(uwuText.takeIf { it.length <= 4000 } ?: uwuText.substring(0, 4000))
                .mentionRepliedUser(false)
                .queue()
    }
    
    /**
     * This code has been mostly derived from [owoify-js](https://github.com/mohan-cao/owoify-js),
     * as well as [Uwuifier-node](https://github.com/Schotsl/Uwuifier-node)
     *
     * @constructor Create empty Uw u
     */
    private object UwU {
        const val faceChance = 0.05
        const val actionChance = 0.075
        const val stuttersChance = 0.1
        const val commonReplacementChance = 0.95
        
        val uwuRandom = Random(Random.nextLong())
        val faces: List<String> = listOf("(・\\`ω´・)", ";;w;;", "OwO", "UwU", ">w<", "^w^", "ÚwÚ", "^-^", ":3", "x3", "owo", ">w<", ">.<",
                                         ">.>", "^w^", "(^\\_^)", "^\\_^", ".-.", ".\\_.", "(>'-')>", "xD", ":P", "X\\_X", ">\\_<", "O\\_o",
                                         "o\\_O", "(\\* ^ ω ^)", "(⌒ω⌒)", "ヽ(\\*・ω・)ﾉ", "(o´∀\\`o)", "(o･ω･o)", "＼(＾▽＾)／", "≧◡≦",
                                         "(✿◠‿◠)")
        val exclamations: List<String> = listOf("!?", "?!!", "?!?1", "!!11", "?!?!")
        val actions: List<String> = listOf("\\*blushes\\*", "\\*whispers to self\\*", "\\*cries\\*", "\\*screams\\*", "\\*sweats\\*",
                                           "\\*twerks\\*", "\\*runs away\\*", "\\*screeches\\*", "\\*walks away\\*", "\\*sees bulge\\*",
                                           "\\*looks at you\\*", "\\*notices buldge\\*", "\\*starts twerking\\*", "\\*huggles tightly\\*",
                                           "\\*boops your nose\\*", "\\*hides behind chair\\*", "\\*looks around anxiously\\*",
                                           "\\*jumps at you\\*")
        
        val uwuRegexes: Map<Regex, String> = mapOf(
                "[rl]" to "w",
                "[RL]" to "W",
                "n([aeiou])" to "ny\$1",
                "N([aeiou])" to "Ny\$1",
                "N([AEIOU])" to "NY\$1",
                "ove" to "uv",
                "OVE" to "UV",
                "\\b((?:ha|hah|heh|hehe)+)\\b" to "\$1 xD",
                "\\b([Tt])he\\b" to "\$1eh",
                "\\bYou\\b" to "U",
                "\\byou\\b" to "u",
                "\\bUou'? re ?" to "Ur",
                "\\byou'?re?" to "ur",
                "([Oo])ver" to "\$1wor",
                "([Ww])orse" to "\$1ose",
                "([Mm])om" to "\$1wom",
                "([Ff])uc" to "\$1wuc",
                "([Pp])le" to "\$1we",
                "([DdFfGgHhJjPpQqRrSsTtXxYyZz])le" to "\$1wal",
                "([Pp])oi" to "\$1woi",
                "ver" to "wer",
                "Ver" to "Wer",
                "([Ff])i" to "\$1wi",
                "FI" to "FWI",
                "[vw]le" to "wal",
                "ew" to "uwu",
                "([Hh])ey" to "\$1ay",
                "[({<\\]]" to "｡･:\\\\*:･ﾟ★｡･:\\\\*:･ﾟ☆",
                "[)}>\\]]" to "☆ﾟ･:\\\\*:･｡★ﾟ･:\\\\*:･｡").mapKeys { it.key.toRegex() }
                .mapValues {
                    MarkdownSanitizer.sanitize(it.value, MarkdownSanitizer.SanitizationStrategy.ESCAPE)
                }
        
        val characterORegex = "[oO]".toRegex()
        val periodRegex = "[.,](?![0-9])".toRegex()
        val exclamationRegex = "[?!]+".toRegex()
        
        fun transformTextToUwU(text: String): String {
            var uwuString = text.replace(characterORegex) {
                if (uwuRandom.nextFloat() <= 0.05)
                    if (it.value == "O")
                        "OWO"
                    else
                        "owo"
                else
                    return@replace it.value
            }
            
            for (uwuRegex in uwuRegexes) {
                if (uwuRandom.nextFloat() <= commonReplacementChance)
                    uwuString = uwuString.replace(uwuRegex.key, uwuRegex.value)
            }
            
            uwuString = uwuString.split(" ").map {
                it.replace(periodRegex, " " + Regex.escapeReplacement(faces.random(uwuRandom)))
            }.joinToString(separator = " ") {
                if (uwuRandom.nextFloat() <= faceChance)
                    it + " " + faces.random(uwuRandom)
                else if (uwuRandom.nextFloat() <= actionChance)
                    it + " " + actions.random(uwuRandom)
                else if (uwuRandom.nextFloat() <= stuttersChance)
                    (it.first() + "-").repeat(uwuRandom.nextInt(3)) + it
                else
                    it
            }.replace(exclamationRegex, exclamations.random(uwuRandom))
            
            return uwuString
        }
    }
}