package com.r0adkll.deckbuilder.arch.data.databasev2

import io.pokemontcg.model.SubType
import org.amshove.kluent.`should equal`
import org.junit.Assert.*
import org.junit.Test

class QueryTest {

    @Test
    fun testQuerySingleCondition() {
        val result = Query.select("cards").where("id" eq 100).get()
        result `should equal` "SELECT * FROM cards WHERE id = 100"
    }

    @Test
    fun testQueryJoinSingleCondition() {
        val result = Query.select("cards")
                .join("attacks").on("cards.id" eq "attacks.cardId")
                .where("attacks.name" like "%scratch%")
                .get()
        result `should equal` "SELECT * FROM cards INNER JOIN attacks ON cards.id = attacks.cardId WHERE attacks.name LIKE %scratch%"
    }

    @Test
    fun testQueryMultiCondition() {
        val result = Query.select("cards")
                .where("hp" gt 100)
                .and("subtype" eq SubType.BASIC.displayName)
                .or("subtype" eq SubType.GX.displayName)
                .get()
        result `should equal` "SELECT * FROM cards WHERE hp > 100 AND subtype = Basic OR subtype = GX"
    }


}