package com.r0adkll.deckbuilder.arch.data.databasev2.dao


import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.r0adkll.deckbuilder.arch.data.databasev2.entities.AttackEntity
import com.r0adkll.deckbuilder.arch.data.databasev2.entities.CardEntity
import com.r0adkll.deckbuilder.arch.data.databasev2.relations.CardWithAttacks
import io.reactivex.Single


@Dao
abstract class CardDao {

    @Transaction @Query("SELECT * FROM cards WHERE id IN(:ids)")
    abstract fun getCards(ids: List<String>): Single<List<CardWithAttacks>>

    @RawQuery
    abstract fun searchCards(query: SupportSQLiteQuery): Single<List<CardWithAttacks>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertCards(cards: List<CardEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertCard(card: CardEntity): Long

    @Insert
    abstract fun insertAttacks(attacks: List<AttackEntity>)

    @Query("DELETE FROM cards")
    abstract fun clear()


    @Transaction
    open fun insertCardsWithAttacks(cards: List<CardWithAttacks>) {
        cards.forEach {
            insertCardWithAttacks(it)
        }
    }

    private fun insertCardWithAttacks(card: CardWithAttacks) {
        if (insertCard(card.card) > 0L) {
            insertAttacks(card.attacks)
        }
    }
}