/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file ServiceInitializer.kt is part of PolyBot
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

package ca.solostudios.polybot.impl.plugin.dsl.service

import ca.solostudios.polybot.api.service.PolyService
import ca.solostudios.polybot.api.service.config.ServiceConfig
import org.kodein.di.DI
import kotlin.reflect.KClass

internal data class ServiceInitializer<T : PolyService<C>, C : ServiceConfig>(
        val serviceClass: KClass<T>,
        val configClass: KClass<C>,
        val initializer: (@UnsafeVariance C, DI) -> T
                                                                             )