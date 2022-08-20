/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyBotGradleExtension.kt is part of PolyBot
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

package ca.solostudios.gradle.polybot

import ca.solostudios.gradle.polybot.plugin.PolyPluginConfig
import javax.inject.Inject
import org.gradle.api.Action
import org.gradle.api.DomainObjectSet
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.domainObjectSet
import org.gradle.kotlin.dsl.newInstance

public open class PolyBotGradleExtension @Inject constructor(private val objectFactory: ObjectFactory) {
    public val plugins: DomainObjectSet<PolyPluginConfig> = objectFactory.domainObjectSet(PolyPluginConfig::class)
    
    public fun plugins(action: Action<in DomainObjectSet<PolyPluginConfig>>) {
        action.execute(plugins)
    }
    
    public fun plugin(id: String, action: Action<in PolyPluginConfig>) {
        val plugin = objectFactory.newInstance(PolyPluginConfig::class)
        plugin.id(id)
        action.execute(plugin)
        
        plugins.add(plugin) // register
    }
    
    
    public fun plugin(action: Action<in PolyPluginConfig>) {
        val plugin = objectFactory.newInstance(PolyPluginConfig::class)
        action.execute(plugin)
        
        plugins.add(plugin) // register
    }
}