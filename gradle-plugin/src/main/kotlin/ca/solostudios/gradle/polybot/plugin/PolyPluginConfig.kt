/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyPluginConfig.kt is part of PolyBot
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

import javax.inject.Inject
import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.Optional
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.newInstance
import org.gradle.kotlin.dsl.property

public open class PolyPluginConfig @Inject constructor(objectFactory: ObjectFactory) {
    @Input
    public val id: Property<String?> = objectFactory.property()
    
    @Input
    public val group: Property<String?> = objectFactory.property()
    
    @Input
    public val version: Property<String> = objectFactory.property<String>().convention("0.1.0") as Property<String>
    
    @Nested
    public val dependencies: PolyDependencyConfig = objectFactory.newInstance()
    
    @Input
    public val entrypoints: ListProperty<String> = objectFactory.listProperty()
    
    @Input
    @Optional
    public val license: Property<String?> = objectFactory.property<String?>().convention("CUSTOM")
    
    @Input
    @Optional
    public val displayName: Property<String?> = objectFactory.property()
    
    @Input
    @Optional
    public val description: Property<String?> = objectFactory.property()
    
    @Nested
    public val links: PolyLinksConfig = objectFactory.newInstance()
    
    public fun id(id: String) {
        this.id.set(id)
    }
    
    public fun group(group: String) {
        this.group.set(group)
    }
    
    public fun version(version: String) {
        this.version.set(version)
    }
    
    public fun entrypoints(entrypoints: List<String>) {
        this.entrypoints.set(entrypoints)
    }
    
    public fun entrypoints(entrypointsBuilder: MutableList<String>.() -> Unit) {
        this.entrypoints.set(buildList(entrypointsBuilder))
    }
    
    public fun entrypoint(entrypoint: String) {
        this.entrypoints.add(entrypoint)
    }
    
    public fun license(license: String?) {
        this.license.set(license)
    }
    
    public fun displayName(displayName: String?) {
        this.displayName.set(displayName)
    }
    
    public fun description(description: String?) {
        this.description.set(description)
    }
    
    public fun links(action: Action<PolyLinksConfig>) {
        action.execute(links)
    }
}
