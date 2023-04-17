/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file CommandProcessor.kt is part of PolyBot
 * Last modified on 17-04-2023 12:14 p.m.
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

package ca.solostudios.polybot.ksp.processor

import ca.solostudios.polybot.api.commands.PolyCommand
import ca.solostudios.polybot.api.plugin.info.PluginCommands
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import java.io.IOException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class CommandProcessor(environment: SymbolProcessorEnvironment) : SymbolProcessor {
    private val codeGenerator = environment.codeGenerator
    private val logger = environment.logger
    
    private val verbose = environment.options["polybot.processor.verbose"].toBoolean()
    
    val commands: MutableList<KSClassDeclaration> = mutableListOf()
    
    override fun process(resolver: Resolver): List<KSAnnotated> {
        commands += resolver.getSymbolsWithAnnotation(COMMAND_ANNOTATION_NAME)
                .filterIsInstance<KSClassDeclaration>()
                .filter { it.qualifiedName != null && it.containingFile != null }
                .toList()
        
        return emptyList()
    }
    
    override fun finish() {
        val dependencies = Dependencies(true, *commands.mapNotNull { it.containingFile }.toTypedArray())
    
        val commandClasses = commands.mapNotNull {
            if (it.qualifiedName == null) {
                logger.warn("A class was annotated with @Command, but the qualified name cannot be resolved. Package: ${it.packageName.asString()}, Name:${it.simpleName.asString()}")
                null
            } else {
                it.qualifiedName!!.asString()
            }
        }
    
        val pluginCommands = PluginCommands(commandClasses)
    
        try {
            codeGenerator.createNewFile(dependencies = dependencies, packageName = "", fileName = PluginCommands.PLUGIN_COMMAND_INFO_FILE, extensionName = "")
                    .bufferedWriter().use { writer ->
                        writer.append(Json.encodeToString(pluginCommands))
                        // for (cmd in commands)
                        //     writer.appendLine("packageName: ${cmd.packageName.asString()}, simpleName: ${cmd.simpleName.asString()}, qualifiedName: ${cmd.qualifiedName?.asString()}")
                    }
        } catch (e: IOException) {
            logger.warn("Unable to write to ${PluginCommands.PLUGIN_COMMAND_INFO_FILE}: $e")
        }
    }
    
    private fun KSPLogger.verbose(message: String) {
        if (verbose)
            logging(message)
    }
    
    // private fun KSClassDeclaration.toBinaryName(): String = toClassName().reflectionName()
    
    // private fun KSClassDeclaration.toClassName(): ClassName {
    //     require(!isLocal()) {
    //         "Local and anonymous classes are not supported."
    //     }
    //
    //     val pkgName = packageName.asString()
    //     val typesString = qualifiedName!!.asString().removePrefix("$pkgName.")
    //
    //     val simpleNames = typesString.split(".")
    //     return ClassName(pkgName, simpleNames)
    // }
    
    
    companion object {
        val COMMAND_ANNOTATION_NAME = PolyCommand::class.qualifiedName!!
    }
}