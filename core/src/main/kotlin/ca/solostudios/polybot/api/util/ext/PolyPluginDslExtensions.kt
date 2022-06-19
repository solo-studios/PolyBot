/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyPluginDslExtensions.kt is part of PolyBot
 * Last modified on 10-06-2022 12:41 p.m.
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

package ca.solostudios.polybot.api.util.ext

import ca.solostudios.polybot.api.cloud.event.MessageEvent
import ca.solostudios.polybot.api.event.PolyEvent
import ca.solostudios.polybot.api.event.PolyEventListener
import ca.solostudios.polybot.api.plugin.dsl.command.PolyCommandDsl
import ca.solostudios.polybot.api.plugin.dsl.command.PolyParameterInjectorDsl
import ca.solostudios.polybot.api.plugin.dsl.event.PolyEventDsl
import ca.solostudios.polybot.api.plugin.dsl.service.PolyServiceDsl
import ca.solostudios.polybot.api.service.PolyService
import cloud.commandframework.annotations.AnnotationAccessor
import cloud.commandframework.annotations.injection.ParameterInjector
import cloud.commandframework.context.CommandContext
import kotlin.reflect.KClass

public inline fun <reified E : Exception> PolyCommandDsl.exceptionHandler(noinline handler: (event: MessageEvent, exception: E) -> Unit) {
    exceptionHandler(E::class, handler)
}

public inline fun <reified T : Any> PolyParameterInjectorDsl.injector(injector: ParameterInjector<MessageEvent, T>) {
    injector(T::class, injector)
}

public inline fun <reified T : Any> PolyParameterInjectorDsl.injector(
        noinline injector: (context: CommandContext<MessageEvent>, annotationAccessor: AnnotationAccessor) -> T,
                                                                     ) {
    injector(T::class, ParameterInjector(injector))
}

public fun <T : Any> PolyParameterInjectorDsl.injector(
        clazz: KClass<T>,
        injector: (context: CommandContext<MessageEvent>, annotationAccessor: AnnotationAccessor) -> T,
                                                      ) {
    injector(clazz, ParameterInjector(injector))
}

public inline fun <reified T : PolyService> PolyServiceDsl.register(service: T) {
    return register(T::class, service)
}

public inline fun <reified T : PolyService> PolyServiceDsl.configure(noinline block: T.() -> Unit) {
    return configure(T::class, block)
}

public inline fun <reified E : PolyEvent> PolyEventDsl.listener(eventListener: PolyEventListener<E>) {
    return listener(E::class, eventListener)
}