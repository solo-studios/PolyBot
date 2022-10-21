/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file RequiredConfigProperty.kt is part of PolyBot
 * Last modified on 21-10-2022 02:21 p.m.
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

package ca.solostudios.polybot.api.service.config.property

import ca.solostudios.polybot.api.service.config.ServiceConfig
import ca.solostudios.polybot.api.service.config.ServiceConfigHolder
import kotlin.reflect.KProperty

public class RequiredConfigProperty<T>(
        public override val config: ServiceConfig
                                      ) : ConfigProperty<T> {
    private val holder: ServiceConfigHolder
        get() = config.configHolder
    
    override fun getValue(thisRef: ServiceConfig, property: KProperty<*>): T {
        if (thisRef != config)
            error("thisRef must be equal to config")
        
        return if (this in holder)
            holder[this]
        else error("Could not find property for $property in config, but is required.")
    }
    
    override fun setValue(thisRef: ServiceConfig, property: KProperty<*>, value: T) {
        if (thisRef != config)
            error("thisRef must be equal to config")
        
        holder[this] = value
    }
}