/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file categories.kt is part of PolyhedralBot
 * Last modified on 03-10-2021 06:49 p.m.
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

const val MOD_CATEGORY = "category.mod"
const val UTIL_CATEGORY = "category.util"
const val EASTER_EGG_CATEGORY = "category.easter-egg"
const val GITHUB_CATEGORY = "category.github"
const val BOT_ADMIN_CATEGORY = "category.bot-admin"
const val TAG_CATEGORY = "category.tag"

private val categoryMap = mapOf(
        MOD_CATEGORY to Category("Moderation", listOf("mod", "admin", "administration")),
        UTIL_CATEGORY to Category("Utilities", listOf("util")),
        EASTER_EGG_CATEGORY to Category("Easter Eggs", listOf("surprise", "easter-egg")),
        GITHUB_CATEGORY to Category("GitHub", listOf()),
        BOT_ADMIN_CATEGORY to Category("Bot Administration", listOf("bot", "bot admin", "bot-administration", "bot-admin")),
        TAG_CATEGORY to Category("Tags", listOf())
                               )

fun getCategory(categoryKey: String): Category = categoryMap.getValue(categoryKey)

data class Category(val name: String, val aliases: List<String>)
