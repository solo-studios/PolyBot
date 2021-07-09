/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file DelegatingCollection.kt is part of PolyhedralBot
 * Last modified on 09-07-2021 03:32 p.m.
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

package net.dv8tion.jda

import java.util.function.Predicate

internal class DelegatingCollection<T>(
        private val delegate: MutableCollection<T> = mutableListOf(),
        private val adder: (toAdd: T) -> Unit,
        private val remover: (toRemove: T) -> Unit,
                                      ) : MutableCollection<T> by delegate {
    override fun add(element: T): Boolean {
        adder(element)
        return delegate.add(element)
    }
    
    override fun addAll(elements: Collection<T>): Boolean {
        elements.forEach(adder)
        return delegate.addAll(elements)
    }
    
    override fun clear() {
        delegate.forEach(remover)
        delegate.clear()
    }
    
    override fun remove(element: T): Boolean {
        remover(element)
        return delegate.remove(element)
    }
    
    override fun removeAll(elements: Collection<T>): Boolean {
        elements.forEach(remover)
        return delegate.removeAll(elements)
    }
    
    override fun removeIf(filter: Predicate<in T>): Boolean {
        @Suppress("LABEL_NAME_CLASH")
        return delegate.removeIf { element ->
            if (filter.test(element)) {
                remover(element)
                return@removeIf true
            }
            return@removeIf false
        }
    }
    
    override fun retainAll(elements: Collection<T>): Boolean {
        return delegate.removeIf { element ->
            if (!elements.contains(element)) {
                remover(element)
                return@removeIf true
            }
            return@removeIf false
        }
    }
}