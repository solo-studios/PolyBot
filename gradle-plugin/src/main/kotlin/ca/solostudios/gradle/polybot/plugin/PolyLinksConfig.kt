/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyLinksConfig.kt is part of PolyBot
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

import java.net.URL
import javax.inject.Inject
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.setValue

public open class PolyLinksConfig @Inject constructor(objectFactory: ObjectFactory) {
    @get:Input
    @get:Optional
    public val homepageProperty: Property<URL?> = objectFactory.property()
    
    public var homepage: URL? by homepageProperty
    
    @get:Input
    @get:Optional
    public val sourceProperty: Property<URL?> = objectFactory.property()
    
    public var source: URL? by sourceProperty
    
    @get:Input
    @get:Optional
    public val issuesProperty: Property<URL?> = objectFactory.property()
    
    public var issues: URL? by issuesProperty
    
    @get:Input
    @get:Optional
    public val licenseProperty: Property<URL?> = objectFactory.property()
    
    public var license: URL? by licenseProperty
    // @get:Input
    // @get:Optional
    // public var license: URL? by objectFactory.property()
    
    override fun toString(): String {
        return "PolyLinksConfig(homepage='${homepageProperty.orNull}', source='${sourceProperty.orNull}', issues='${issuesProperty.orNull}', license='${licenseProperty.orNull}')"
    }
}