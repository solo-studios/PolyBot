/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file MarkdownHeaderVisitor.kt is part of PolyhedralBot
 * Last modified on 09-10-2021 10:58 p.m.
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

package ca.solostudios.polybot.util

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.visitors.RecursiveVisitor

class MarkdownHeaderVisitor : RecursiveVisitor() {
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