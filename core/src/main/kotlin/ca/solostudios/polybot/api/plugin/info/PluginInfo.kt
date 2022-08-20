/*
 * PolyBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2022-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PluginInfo.kt is part of PolyBot
 * Last modified on 30-07-2022 07:10 p.m.
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

package ca.solostudios.polybot.api.plugin.info

import ca.solostudios.polybot.api.plugin.info.PluginInfo.Companion.PLUGIN_GROUP_REGEX
import ca.solostudios.polybot.api.plugin.info.PluginInfo.Companion.PLUGIN_ID_REGEX
import ca.solostudios.polybot.impl.serializers.StrataVersionSerializer
import ca.solostudios.strata.version.Version
import kotlinx.serialization.Serializable

@Serializable
public data class PluginInfo(
        /**
         * The group this plugin belongs to.
         *
         * Note: This *must* match [PLUGIN_GROUP_REGEX].
         *
         * @see PLUGIN_GROUP_REGEX
         */
        public val group: String,
        
        /**
         * The id of the plugin.
         *
         * Note: This *must* match [PLUGIN_ID_REGEX].
         *
         * @see PLUGIN_ID_REGEX
         */
        public val id: String,
        
        /**
         * The plugin's version.
         *
         * `null` if the plugin has no provided version.
         */
        @Serializable(with = StrataVersionSerializer::class)
        public val version: Version? = null,
        
        /**
         * A list of entrypoints for this plugin
         */
        public val entrypoints: List<String> = emptyList(),
        
        /**
         * A list of plugins that this plugin depends on
         */
        public val depends: List<PluginDependency> = emptyList(),
        
        /**
         * A list of plugins that this plugin conflicts with
         */
        public val breaks: List<PluginDependency> = emptyList(),
        
        /**
         * The human-readable name of this plugin.
         *
         * Defaults to `null`.
         */
        public val name: String? = null,
        
        /**
         * A description of this plugin.
         *
         * Defaults to `null`.
         */
        public val description: String? = null,
        
        /**
         * A key-value map of contributors and their roles in the creation of the plugin.
         *
         * The key represents the contributor's name, and the value is the contributor's role.
         */
        public val contributors: List<PluginContributor> = emptyList(),
        
        /**
         * A key-value map of the contact info for this plugin.
         *
         * Typical keys include, but are not limited to:
         * - `issues`
         * - `source`
         * - `homepage`
         * - `irc`
         * - `discord`
         */
        public val contact: Map<String, String> = emptyMap(),
        
        /**
         * The license this plugin is under.
         *
         * Defaults to `ARR`
         */
        public val license: String = "ARR",
        
        /**
         * The paths of any nested jar files within this jar.
         *
         * All the jars listed here will be extracted from the jar and added to the plugin's classpath.
         */
        public val nestedJars: List<PluginNesterJar> = emptyList(),
        
        /**
         * Any extra data you wish to store.
         *
         * This extra data may or may not be used by other plugins.
         */
        public val extra: Map<String, String> = emptyMap(),
                            ) {
    
    init {
        require(id.matches(PLUGIN_ID_REGEX)) { "The plugin id \"$id\" is not valid. The id must match the pattern: /$PLUGIN_ID_REGEX/" }
        require(group.matches(PLUGIN_GROUP_REGEX)) { "The group for plugin $id is not valid. The group must match the pattern: /$PLUGIN_GROUP_REGEX/" }
        
        if (name != null)
            require(name.isNotBlank()) { "The name for plugin $id is not valid. The name must not be blank. If you don't wish to specify a name, set it to null." }
        
        if (description != null)
            require(description.isNotBlank()) { "The description for plugin $id is not valid. The description must not be blank. If you don't wish to specify a description, set it to null." }
        
        require(license.isNotBlank()) { "The license for plugin $id is not valid. The license must not be blank." }
        
    }
    
    public companion object {
        /**
         * The pattern the plugin id must match.
         *
         * It must meet the following criteria:
         * - begins with a lowercase character (`a`-`z`).
         * - may only contain lowercase characters (`a`-`z`), numbers (`0`-`9`), dashes (`-`), and underscores (`_`).
         * - ends with a lowercase character (`a`-`z`) or number(`0`-`9`).
         * - is between 3 and 64 characters long.
         *
         * Pattern:
         * ```regex
         * ^[a-z][a-z\d\-_]{1,62}[a-z\d]$
         * ```
         */
        public val PLUGIN_ID_REGEX: Regex = Regex("""^[a-z][a-z\d\-_]{1,62}[a-z\d]$""")
        
        /**
         * The pattern the plugin group must match
         *
         * It must meet the following criteria:
         * - path elements separated by a period (`.`).
         * - all path elements begin with a lowercase character (`a`-`z`).
         * - path elements may contain lowercase characters (`a`-`z`), numbers (`0`-`9`), underscores (`_`), and dashes (`-`).
         * - path elements must not end in an underscore or a dash.
         * - path elements must contain at least 1 character.
         *
         * Pattern:
         * ```regex
         * ^[a-z](?:[a-z\d_\-]*[a-z\d])?(?:\.[a-z](?:[a-z\d_\-]*[a-z\d])?)*$
         * ```
         *
         * Examples:
         * ```
         * com.has-dash.abcd
         * com.nodash.abcd
         * a.b.c.d
         * com.has_underscore.abcd
         * com.has.number.seven7
         * ```
         */
        public val PLUGIN_GROUP_REGEX: Regex = Regex("""^[a-z](?:[a-z\d_\-]*[a-z\d])?(?:\.[a-z](?:[a-z\d_\-]*[a-z\d])?)*$""")
        
        public const val PLUGIN_INFO_FILE: String = "polybot.plugin.json"
    }
}