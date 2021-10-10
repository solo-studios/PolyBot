/*
 * PolyhedralBot - A Discord bot for the Polyhedral Development discord server
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file BanEntity.kt is part of PolyhedralBot
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

package ca.solostudios.polybot.entities.data.db.entities

/*
object BanTable : KotlinxUUIDTable("BAN_DATA") {
    val guild = long("guild").index()
    val member = long("member")
    val reason = text("reason")
    val moderator = long("moderator")
    val time = datetime("time").index()
}

class BanEntity(id: EntityID<UUID>) : KotlinxUUIDEntity(id) {
    var guild by BanTable.guild
    var reason: String by BanTable.reason
    var member by BanTable.member
    var moderator by BanTable.moderator
    var time: LocalDateTime by BanTable.time
    
    companion object : KotlinxUUIDEntityClass<BanEntity>(BanTable)
}
*/
