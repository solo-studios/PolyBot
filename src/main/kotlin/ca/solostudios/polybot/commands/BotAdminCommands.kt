/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file BotAdminCommands.kt is part of PolyhedralBot
 * Last modified on 28-10-2021 07:58 p.m.
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

package ca.solostudios.polybot.commands

import ca.solostudios.polybot.ExitCodes
import ca.solostudios.polybot.PolyBot
import ca.solostudios.polybot.cloud.commands.PolyCommandContainer
import ca.solostudios.polybot.cloud.commands.PolyCommands
import ca.solostudios.polybot.cloud.commands.annotations.CommandName
import ca.solostudios.polybot.cloud.commands.annotations.JDAUserPermission
import ca.solostudios.polybot.cloud.commands.annotations.PolyCategory
import ca.solostudios.polybot.entities.PolyMessage
import ca.solostudios.polybot.entities.PolyUser
import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.Hidden
import cloud.commandframework.annotations.specifier.Greedy
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.time.Duration
import javax.script.ScriptEngineManager
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.time.withTimeout
import net.dv8tion.jda.api.JDA
import org.slf4j.kotlin.*
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.EvaluationResult
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.api.ScriptAcceptedLocation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.ScriptEvaluationConfiguration
import kotlin.script.experimental.api.acceptedLocations
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.api.ide
import kotlin.script.experimental.api.providedProperties
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.dependenciesFromClassContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvm.updateClasspath
import kotlin.script.experimental.jvm.util.classpathFromClass
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

@Hidden
@PolyCommandContainer
@PolyCategory(BOT_ADMIN_CATEGORY)
class BotAdminCommands(bot: PolyBot) : PolyCommands(bot) {
    private val logger by getLogger()
    private val engine = ScriptEngineManager().getEngineByName("kotlin")
    private val scriptingHost = BasicJvmScriptingHost()
    
    @CommandName("Shutdown Bot")
    @CommandMethod("shutdown")
    @JDAUserPermission(ownerOnly = true)
    @CommandDescription("Shutdown PolyBot from within discord.\nNOTE: This will **NOT** restart the bot afterwards.")
    suspend fun shutdown(message: PolyMessage, author: PolyUser) {
        message.reply("Shutting down PolyBot")
        logger.info { "Shutdown request was triggered by ${author.tag} (${author.id})" }
        bot.shutdown(ExitCodes.EXIT_CODE_SHUTDOWN)
    }
    
    @CommandName("Restart Bot")
    @CommandMethod("restart")
    @JDAUserPermission(ownerOnly = true)
    @CommandDescription("Restart PolyBot from within discord.\nThe bot process will exit then start up again.")
    suspend fun restart(message: PolyMessage, author: PolyUser) {
        message.reply("Restarting PolyBot")
        logger.info { "Restart request was triggered by ${author.tag} (${author.id})" }
        bot.shutdown(ExitCodes.EXIT_CODE_RESTART)
    }
    
    @CommandName("Update Bot")
    @CommandMethod("update")
    @JDAUserPermission(ownerOnly = true)
    @CommandDescription("Update PolyBot from within discord.\nThe bot process will exit, a new jar will be downloaded, and then the bot will start again.")
    suspend fun update(message: PolyMessage, author: PolyUser) {
        message.reply("Updating PolyBot")
        logger.info { "Update request was triggered by ${author.tag} (${author.id})" }
        bot.shutdown(ExitCodes.EXIT_CODE_UPDATE)
    }
    
    @CommandName("Eval")
    @CommandMethod("eval [code]")
    @JDAUserPermission(ownerOnly = true)
    suspend fun eval(message: PolyMessage,
                     author: PolyUser,
                     @Greedy
                     @Argument(value = "code", description = "Code to evaluate")
                     code: String) {
        assert(author.isOwner) // sanity check
        assert(author.id in bot.config.botConfig.ownerIds)
        
        try {
            val result = withTimeout(Duration.ofSeconds(10)) {
                val (err, out, result) = captureOutAndErr {
                    evalString<KotlinAdminScript>(code) {
                        providedProperties(
                                "user" to author,
                                "message" to message,
                                "logger" to logger,
                                "bot" to bot,
                                "jda" to bot.jda,
                                          )
                    }
                }
    
                // result as ResultWithDiagnostics.Success
    
    
                return@withTimeout buildString {
                    append(result)
        
                    // if (!output.isNullOrEmpty()) {
                    //     val returnValue = result.value.returnValue
                    //     returnValue as ResultValue.Value
                    //    
                    //     returnValue.toString()
                    //     appendLine("\nReturn value:")
                    //     appendLine("```")
                    //     appendLine(output)
                    //     appendLine("```")
                    // }
        
                    if (out.isNotEmpty()) {
                        appendLine("\nConsole output:")
                        appendLine("```")
                        appendLine(out)
                        appendLine("```")
                    }
        
                    if (err.isNotEmpty()) {
                        appendLine("\nErr output:")
                        appendLine("```")
                        appendLine(out)
                        appendLine("```")
                    }
                }
            }
            
            logger.info { "Result of eval: $result" }
    
            message.reply("Evaluation completed successfully.")
    
            if (result.isNotEmpty())
                message.reply(result.takeIf { it.length <= 4000 } ?: result.substring(0, 4000))
        } catch (e: TimeoutCancellationException) {
    
        }
    }
    
    /**
     * From [Jetbrains/kotlin](https://github.com/JetBrains/kotlin/blob/master/libraries/scripting/jvm-host-test/test/kotlin/script/experimental/jvmhost/test/TestScriptDefinitions.kt#L62).
     *
     * @param T
     * @param source
     * @param configure
     * @receiver
     * @return
     */
    private inline fun <reified T : Any> evalString(
            source: String,
            noinline configure: ScriptEvaluationConfiguration.Builder.() -> Unit
                                                   ): ResultWithDiagnostics<EvaluationResult> {
        val actualConfiguration = createJvmCompilationConfigurationFromTemplate<T>()
        return scriptingHost.eval(source.toScriptSource(), actualConfiguration, ScriptEvaluationConfiguration(configure))
    }
    
    @KotlinScript(
            displayName = "PolyScript",
            compilationConfiguration = KotlinAdminScriptCompilationConfiguration::class,
                 )
    abstract class KotlinAdminScript
    
    class KotlinAdminScriptCompilationConfiguration : ScriptCompilationConfiguration(
            {
                defaultImports(DEFAULT_EVAL_IMPORTS)
                
                updateClasspath(classpathFromClass<PolyBot>())
                
                providedProperties(
                        "bot" to PolyBot::class,
                        "logger" to KLogger::class,
                        "jda" to JDA::class,
                        // "guild" to PolyGuild::class,
                        // "member" to PolyMember::class,
                        "user" to PolyUser::class,
                        "message" to PolyMessage::class
                                  )
                
                jvm {
                    dependenciesFromClassContext(contextClass = PolyBot::class, wholeClasspath = true)
                }
                ide {
                    acceptedLocations(ScriptAcceptedLocation.Everywhere)
                }
            }
                                                                                    ) {
        companion object {
            val DEFAULT_EVAL_IMPORTS = listOf(
                    "kotlin.*",
                    "kotlinx.*",
                    "kotlinx.coroutines.*",
                    "ca.solostudios.polybot.*",
                    "ca.solostudios.polybot.util.*",
                    "ca.solostudios.polybot.entities.*",
                    "net.dv8tion.jda.api.*",
                    "org.slf4j.kotlin.*",
                                             )
            
        }
    }
    
    /**
     * Taken from [Jetbrains/kotlin](https://github.com/JetBrains/kotlin/blob/master/libraries/scripting/jvm-host-test/test/kotlin/script/experimental/jvmhost/test/ScriptingHostTest.kt#L519).
     *
     * @param body
     * @receiver
     * @return
     */
    private fun captureOutAndErr(body: () -> ResultWithDiagnostics<EvaluationResult>): Triple<String, String, ResultWithDiagnostics<EvaluationResult>> {
        val outStream = ByteArrayOutputStream()
        val errStream = ByteArrayOutputStream()
        
        val prevOut = System.out
        val prevErr = System.err
        System.setOut(PrintStream(outStream))
        System.setErr(PrintStream(errStream))
        lateinit var res: ResultWithDiagnostics<EvaluationResult>
        try {
            res = body()
        } finally {
            System.out.flush()
            System.err.flush()
            System.setOut(prevOut)
            System.setErr(prevErr)
        }
        
        
        return Triple(outStream.toString().trim(), errStream.toString().trim(), res)
    }
}