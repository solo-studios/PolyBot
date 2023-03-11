/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PolyPluginDslExtensions.kt is part of PolyBot
 * Last modified on 03-02-2023 01:24 p.m.
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
import ca.solostudios.polybot.api.plugin.dsl.event.PolyEventDsl
import ca.solostudios.polybot.api.plugin.dsl.service.PolyServiceDsl
import ca.solostudios.polybot.api.service.PolyService
import ca.solostudios.polybot.api.service.config.ServiceConfig
import ca.solostudios.polybot.api.service.config.ServiceConfigHolder
import cloud.commandframework.annotations.AnnotationAccessor
import cloud.commandframework.annotations.injection.ParameterInjector
import cloud.commandframework.context.CommandContext
import org.kodein.di.DI
import kotlin.reflect.KClass

public inline fun <reified E : Exception> PolyCommandDsl.exceptionHandler(
        noinline handler: (event: MessageEvent, exception: E) -> Unit,
                                                                         ) {
    exceptionHandler(E::class, handler)
}

public inline fun <reified T : Any> PolyCommandDsl.injector(injector: ParameterInjector<MessageEvent, T>) {
    injector(T::class, injector)
}

public inline fun <reified T : Any> PolyCommandDsl.injector(
        noinline injector: (context: CommandContext<MessageEvent>,
                            annotationAccessor: AnnotationAccessor) -> T,
                                                           ) {
    injector(T::class, ParameterInjector(injector))
}

public fun <T : Any> PolyCommandDsl.injector(
        clazz: KClass<T>,
        injector: (context: CommandContext<MessageEvent>, annotationAccessor: AnnotationAccessor) -> T,
                                            ) {
    injector(clazz, ParameterInjector(injector))
}

public inline fun <reified T : PolyService<C>, reified C : ServiceConfig> PolyServiceDsl.register(
        noinline serviceProvider: (config: C, di: DI) -> T
                                                                                                 ) {
    return register(T::class, C::class, serviceProvider)
}

public inline fun <reified T : PolyService<C>, reified C : ServiceConfig> PolyServiceDsl.configure(
        noinline configBlock: C.() -> Unit,
                                                                                                  ) {
    return configure(T::class, C::class, configBlock)
}

public inline fun <reified C : ServiceConfig> PolyServiceDsl.configInitializer(
        noinline initializer: (configHolder: ServiceConfigHolder) -> C
                                                                              ) {
    return configInitializer(C::class, initializer)
}

public inline fun <reified E : PolyEvent> PolyEventDsl.listener(eventListener: PolyEventListener<E>) {
    return listener(E::class, eventListener)
}