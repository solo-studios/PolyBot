/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file ehcache.kt is part of PolyhedralBot
 * Last modified on 03-08-2021 01:53 p.m.
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

package com.solostudios.polybot.util

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.ByteBufferInput
import com.esotericsoftware.kryo.io.ByteBufferOutput
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import org.ehcache.Cache
import org.ehcache.CacheManager
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import org.ehcache.spi.serialization.Serializer

inline fun <reified K, reified T> CacheManager.getCache(alias: String): Cache<K, T> =
    getCache(alias, K::class.java, T::class.java)

//inline fun <reified K : Any, V : Any> CacheConfigurationBuilder<K, V>.withKotlinSerializer(clazz: KClass<K>) {
//    return withSerializer(clazz.java, getKotlinSerializer<K>())
//}

inline fun <T, reified V : Any> CacheConfigurationBuilder<T, V>.withKotlinValueSerializer(): CacheConfigurationBuilder<T, V> {
    return withValueSerializer(getKotlinSerializer())
}

inline fun <reified K, reified V> cacheConfigBuilder(resourcePoolsBuilder: ResourcePoolsBuilder): CacheConfigurationBuilder<K, V> {
    return CacheConfigurationBuilder.newCacheConfigurationBuilder(K::class.java, V::class.java, resourcePoolsBuilder)
}

inline fun <reified T> getKotlinSerializer(): Serializer<T> {
    return object : Serializer<T> {
        
        val kryo = Kryo().apply {
            isRegistrationRequired = false
            register(T::class.java)
        }
        
        override fun equals(obj: T, binary: ByteBuffer) = obj == read(binary)
        
        override fun serialize(obj: T): ByteBuffer {
            val byteOutputStream = ByteArrayOutputStream()
            val output = ByteBufferOutput(byteOutputStream)
            kryo.writeClassAndObject(output, obj)
            output.close()
            return output.byteBuffer
        }
        
        override fun read(binary: ByteBuffer): T {
            val input = ByteBufferInput(binary)
            val t = kryo.readClassAndObject(input) as T
            input.close()
            return t
        }
    }
}

operator fun <K, V> Cache<K, V>.set(id: K, value: V) {
    put(id, value)
}
