/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file LuceneCommands.kt is part of PolyhedralBot
 * Last modified on 18-09-2021 06:52 p.m.
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

package com.solostudios.polybot.commands

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.specifier.Greedy
import com.solostudios.polybot.PolyBot
import com.solostudios.polybot.cloud.PolyCommands
import com.solostudios.polybot.cloud.permission.annotations.JDAUserPermission
import com.solostudios.polybot.entities.PolyMessage
import com.solostudios.polybot.util.get
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.visitors.RecursiveVisitor
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser
import org.slf4j.kotlin.*

class LuceneCommands(bot: PolyBot) : PolyCommands(bot) {
    private val logger by getLogger()
    
    @CommandMethod("lucene <markdown>")
    @JDAUserPermission(ownerOnly = true)
    fun lucene(message: PolyMessage,
               @Greedy
               @Argument("markdown")
               markdown: String) {
        val flavour = CommonMarkFlavourDescriptor()
        val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(markdown)
        
        val visitor = HeaderVisitor()
        
        visitor.visitNode(parsedTree)
        
        visitor.headers.forEach {
            logger.info { "Header: ${markdown[it.first, it.last].trim()}" }
        }
        
        // message.reply(writer.writeValueAsString(parsedTree)).mentionRepliedUser(false).queue()
    }
    
    private class HeaderVisitor : RecursiveVisitor() {
        val headers = mutableListOf<IntRange>()
        
        override fun visitNode(node: ASTNode) {
            if (node.type == MarkdownElementTypes.ATX_1 || node.type == MarkdownElementTypes.ATX_2 ||
                node.type == MarkdownElementTypes.ATX_3 || node.type == MarkdownElementTypes.ATX_4 ||
                node.type == MarkdownElementTypes.ATX_5 || node.type == MarkdownElementTypes.ATX_6) {
                val content = node.children.find { astNode -> astNode.type == MarkdownTokenTypes.ATX_CONTENT }
                
                if (content != null) {
                    headers += content.startOffset .. content.endOffset
                }
            } else {
                super.visitNode(node)
            }
        }
    }
}