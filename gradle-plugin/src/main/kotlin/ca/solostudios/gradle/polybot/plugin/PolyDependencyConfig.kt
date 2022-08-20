/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyDependencyConfig.kt is part of PolyBot
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

package ca.solostudios.gradle.polybot.plugin

import ca.solostudios.gradle.polybot.serialization.StrataVersionRangeSerializer
import ca.solostudios.strata.kotlin.toVersionRange
import ca.solostudios.strata.version.VersionRange
import javax.inject.Inject
import kotlinx.serialization.Serializable
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import org.gradle.kotlin.dsl.listProperty

public open class PolyDependencyConfig @Inject constructor(objectFactory: ObjectFactory) {
    @Input
    public val breaks: ListProperty<PolyPluginDependency> = objectFactory.listProperty()
    
    @Input
    public val depends: ListProperty<PolyPluginDependency> = objectFactory.listProperty()
    
    public fun depends(dependency: String) {
        depends.add(dependency.toDependency())
    }
    
    public fun breaks(dependency: String) {
        breaks.add(dependency.toDependency())
    }
    
    internal fun String.toDependency(): PolyPluginDependency {
        val strings = split(':')
        
        if (strings.size != 3) {
            throw IllegalArgumentException("Dependency string '$this' cannot be parsed. Must have a group, id, and version, all separated by colons (':')")
        }
        
        return PolyPluginDependency(strings[0], strings[1], strings[2].toVersionRange())
    }
    
    @Serializable
    public data class PolyPluginDependency(
            public val group: String,
            public val id: String,
            @Serializable(with = StrataVersionRangeSerializer::class)
            public val versionRange: VersionRange,
                                          )
}