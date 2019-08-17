package com.r0adkll.deckbuilder.arch.data.database.dao


import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.r0adkll.deckbuilder.arch.data.database.entities.AttackEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.CardEntity
import com.r0adkll.deckbuilder.arch.data.database.relations.CardWithAttacks
import io.reactivex.Single
import io.reactivex.functions.Function


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

    @Suppress("UNCHECKED_CAST")
    open fun getCardsSplit(ids: List<String>): Single<List<CardWithAttacks>> {
        return if (ids.size > 900) {
            val chunkedIds = ids.chunked(900)
            Single.zip(chunkedIds.map { getCards(it) }) {
                it.map { cards -> cards as List<CardWithAttacks> }
                        .flatten()
            }
        } else {
            getCards(ids)
        }
    }

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