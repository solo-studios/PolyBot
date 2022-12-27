/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file ServiceManagerShutdownException.kt is part of PolyBot
 * Last modified on 27-12-2022 01:31 p.m.
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

package ca.solostudios.polybot.common.service

/**
 * Service manager startup exception
 *
 * @constructor Constructs a new runtime exception with the specified detail message.
 * The cause is not initialized, and may subsequently be initialized by a
 * call to [initCause].
 *
 * @param message The detail message. The detail message is saved for
 * later retrieval by the [Throwable.message] method.
 *
 * @param suppressed The list of suppressed exceptions to be added.
 */
public class ServiceManagerShutdownException(
        message: String? = null,
        suppressed: List<Exception> = listOf(),
                                            ) : ServiceManagerLifecycleException(message, suppressed) {
    
    public companion object {
        private const val serialVersionUID: Long = 8835357980251276628L
    }
}