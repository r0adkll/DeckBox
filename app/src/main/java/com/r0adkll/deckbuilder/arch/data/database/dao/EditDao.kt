package com.r0adkll.deckbuilder.arch.data.database.dao

import android.net.Uri
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.r0adkll.deckbuilder.arch.data.database.entities.AttackEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.CardEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.DeckCardJoin
import com.r0adkll.deckbuilder.arch.data.database.mapping.RoomEntityMapper
import com.r0adkll.deckbuilder.arch.data.database.relations.CardWithAttacks
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.util.stack
import io.reactivex.Single

@Dao
abstract class EditDao {

    /*
     * Basic CRUD ops that aren't session specific
     */

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertCard(card: CardEntity): Long

    @Insert
    abstract fun insertAttacks(attacks: List<AttackEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertJoins(joins: List<DeckCardJoin>)

    @Update
    abstract fun updateJoins(joins: List<DeckCardJoin>)

    @Update
    abstract fun updateJoin(join: DeckCardJoin)

    @Delete
    abstract fun deleteJoin(join: DeckCardJoin)

    @Query("SELECT * FROM deck_card_join WHERE deckId = :deckId")
    abstract fun getJoins(deckId: Long): List<DeckCardJoin>

    @Query("SELECT * FROM deck_card_join WHERE deckId = :deckId AND cardId = :cardId")
    abstract fun getJoin(deckId: Long, cardId: String): DeckCardJoin?

    /*
     * Session specific CRUD ops
     */

    @Query("UPDATE decks SET name = :name WHERE uid = :deckId")
    abstract fun updateName(deckId: String, name: String): Single<Int>

    @Query("UPDATE decks SET description = :description WHERE uid = :deckId")
    abstract fun updateDescription(deckId: String, description: String): Single<Int>

    @Query("UPDATE decks SET image = :image WHERE uid = :deckId")
    abstract fun updateImage(deckId: String, image: Uri): Single<Int>

    @Query("UPDATE decks SET collectionOnly = :collectionOnly WHERE uid = :deckId")
    abstract fun updateCollectionOnly(deckId: String, collectionOnly: Boolean): Single<Int>

    /*
     * Compound transaction or helper functions
     */

    @Transaction
    open fun addCards(deckId: String, cards: List<PokemonCard>) {
        val duid = deckId.toLong()
        val stackedCards = cards.stack()

        // Insert cards into cache
        val cardsWithAttacks = cards.map { RoomEntityMapper.to(it) }
        insertCardsWithAttacks(cardsWithAttacks)

        val existingJoins = getJoins(duid)

        val joinsToInsert = stackedCards.filter { stackedCard ->
            existingJoins.none { it.cardId == stackedCard.card.id }
        }.map {
            DeckCardJoin(duid, it.card.id, it.count)
        }

        val joinsToUpdate = existingJoins.filter { join ->
            stackedCards.any { it.card.id == join.cardId }
        }.map { join ->
            val cardToUpdate = stackedCards.find { it.card.id == join.cardId }
            cardToUpdate?.let {
                join.count += it.count
                join
            } ?: join
        }

        // Insert/Update joins
        insertJoins(joinsToInsert)
        updateJoins(joinsToUpdate)
    }

    @Transaction
    open fun removeCard(deckId: String, card: PokemonCard) {
        val duid = deckId.toLong()

        // insert card into cache, if not already there
        insertCardsWithAttacks(listOf(RoomEntityMapper.to(card)))

        val join = getJoin(duid, card.id)
        if (join != null) {
            join.count--
            if (join.count <= 0) {
                deleteJoin(join)
            } else {
                updateJoin(join)
            }
        }
    }

    private fun insertCardsWithAttacks(cards: List<CardWithAttacks>) {
        cards.forEach { card ->
            if (insertCard(card.card) > 0L) {
                insertAttacks(card.attacks)
            }
        }
    }
}
