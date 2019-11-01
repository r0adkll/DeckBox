package com.r0adkll.deckbuilder.arch.data.database

import com.r0adkll.deckbuilder.arch.data.database.util.*
import io.pokemontcg.model.SubType
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class QueryTest {

    @Test
    fun testQuerySingleCondition() {
        val result = Query.select("cards").where("id" eq 100).get()
        result shouldBeEqualTo "SELECT * FROM cards WHERE id = 100"
    }

    @Test
    fun testQueryJoinSingleCondition() {
        val result = Query.select("cards")
            .join("attacks").on("cards.id" eq "attacks.cardId")
            .where("attacks.name" like "%scratch%")
            .get()
        result shouldBeEqualTo "SELECT * FROM cards INNER JOIN attacks ON cards.id = \"attacks.cardId\" " +
            "WHERE attacks.name LIKE \"%scratch%\""
    }

    @Test
    fun testQueryMultiCondition() {
        val result = Query.select("cards")
            .where("hp" gt 100)
            .and("subtype" eq SubType.BASIC.displayName)
            .or("subtype" eq SubType.GX.displayName)
            .get()
        result shouldBeEqualTo "SELECT * FROM cards WHERE hp > 100 AND subtype = \"Basic\" OR subtype = \"GX\""
    }

    @Test
    fun testQueryMultiSubCondition() {
        val result = Query.select("cards")
            .where("hp" lt 100)
            .and(("subtype" eq SubType.GX.displayName) or ("subtype" eq SubType.STAGE_2.displayName))
            .get()
        result shouldBeEqualTo "SELECT * FROM cards WHERE hp < 100 AND (subtype = \"GX\" OR subtype = \"Stage 2\")"
    }

    @Test
    fun testQueryTerminalCondition() {
        val result = Query.select("cards")
            .where("name" eq "Charizard")
            .and("text".notNull())
            .get()
        result shouldBeEqualTo "SELECT * FROM cards WHERE name = \"Charizard\" AND text IS NOT NULL"
    }
}
