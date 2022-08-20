/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file UrlExtensions.kt is part of PolyBot
 * Last modified on 20-08-2022 05:43 p.m.
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

package ca.solostudios.polybot.impl.util

import java.net.JarURLConnection
import java.net.URL
import java.nio.file.Path
import kotlin.io.path.toPath

/**
 * Resolves the code source from a local path.
 *
 * This implementation is from
 * [UrlUtil](https://github.com/FabricMC/fabric-loader/blob/d713b2a801ad142394ac0eb1d1eaa452fc783021/src/main/java/net/fabricmc/loader/impl/util/UrlUtil.java#L32-L50),
 * as part of [FabricMC/fabric-loader](https://github.com/FabricMC/fabric-loader/).
 *
 * This is a direct kotlin port of that code.
 *
 * The following code is under [Apache 2.0](https://github.com/FabricMC/fabric-loader/blob/d713b2a801ad142394ac0eb1d1eaa452fc783021/LICENSE).
 *
 * @param localPath The local path from which the code source is being resolved.
 * @return The resolved path.
 */
public fun URL.resolveCodeSource(localPath: String): Path {
    val connection = openConnection()
    return if (connection is JarURLConnection) {
        connection.jarFileURL.toURI().toPath()
    } else {
        val path = path
        if (path.endsWith(localPath)) {
            URL(protocol, host, port, path.substring(0, path.length - localPath.length)).toURI().toPath()
        } else {
            error("Could not figure out code source for file '$localPath' in URL '$this'!")
        }
    }
}