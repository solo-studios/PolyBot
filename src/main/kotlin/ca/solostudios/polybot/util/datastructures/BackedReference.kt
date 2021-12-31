/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file BackedReference.kt is part of PolyhedralBot
 * Last modified on 31-12-2021 01:38 p.m.
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

package ca.solostudios.polybot.util.datastructures

import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

open class BackedReference<T, V>(
        private var backingProperty: V,
        private val refresh: (V) -> T,
        private val getBackingProperty: (T) -> V,
                                ) {
    private var reference: WeakReference<T>? = null
    
    operator fun getValue(thisRef: Any, property: KProperty<*>): T {
        val referent = reference?.get()
        
        return if (referent != null) {
            referent
        } else {
            val newReferent = refresh(backingProperty)
            reference = WeakReference(newReferent)
            
            newReferent
        }
    }
    
    operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        backingProperty = getBackingProperty(value)
        reference = WeakReference(value)
    }
}
