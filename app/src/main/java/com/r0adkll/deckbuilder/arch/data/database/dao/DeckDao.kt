package com.r0adkll.deckbuilder.arch.data.database.dao

import android.net.Uri
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.r0adkll.deckbuilder.arch.data.database.entities.AttackEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.CardEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.DeckCardJoin
import com.r0adkll.deckbuilder.arch.data.database.entities.DeckEntity
import com.r0adkll.deckbuilder.arch.data.database.mapping.RoomEntityMapper
import com.r0adkll.deckbuilder.arch.data.database.relations.CardWithAttacks
import com.r0adkll.deckbuilder.arch.data.database.relations.DeckStackedCard
import com.r0adkll.deckbuilder.arch.data.database.relations.StackedCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import com.r0adkll.deckbuilder.util.stack
import io.reactivex.Flowable

@Dao
abstract class DeckDao {

    @Query("SELECT * FROM decks")
    abstract fun getDecks(): Flowable<List<DeckEntity>>

    @Query("SELECT * FROM decks WHERE uid = :deckId")
    abstract fun getDeck(deckId: Long): Flowable<DeckEntity>

    @Query("SELECT * FROM decks WHERE name = :name")
    abstract fun getDeck(name: String): DeckEntity?

    @Transaction
    @Query("""
        SELECT * FROM deck_card_join 
        INNER JOIN cards ON deck_card_join.cardId = cards.id 
        WHERE deck_card_join.deckId = :deckId
        """
    )
    abstract fun getDeckCards(deckId: Long): Flowable<List<StackedCard>>

    @Transaction
    @Query("SELECT * FROM deck_card_join INNER JOIN cards ON deck_card_join.cardId = cards.id")
    abstract fun getDeckCards(): Flowable<List<DeckStackedCard>>

    @Query("UPDATE decks SET name = :name, description = :description, image = :image WHERE uid = :deckId")
    abstract fun updateDeck(deckId: Long, name: String, description: String?, image: Uri?)

    @Query("DELETE FROM decks WHERE uid = :deckId")
    abstract fun deleteDeck(deckId: Long)

    @Query("DELETE FROM deck_card_join WHERE deckId = :deckId")
    abstract fun deleteDeckJoins(deckId: Long)

    @Query("DELETE FROM decks")
    abstract fun deleteAll()

    @Insert
    abstract fun insertDeck(deck: DeckEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertCard(card: CardEntity): Long

    @Insert
    abstract fun insertAttacks(attacks: List<AttackEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertJoins(joins: List<DeckCardJoin>)

    @Transaction
    open fun insertDeckWithCards(
        id: Long?,
        cards: List<PokemonCard>,
        name: String,
        description: String?,
        image: DeckImage?,
        collectionOnly: Boolean
    ): DeckEntity {
        // Insert Cards
        val cardsWithAttacks = cards.map { RoomEntityMapper.to(it) }
        insertCardsWithAttacks(cardsWithAttacks)

        // Insert or Update deck
        val deck: DeckEntity?
        if (id != null) {
            updateDeck(id, name, description, image?.uri)
            deck = DeckEntity(id, name, description, image?.uri, collectionOnly, System.currentTimeMillis())
            deleteDeckJoins(id)
        } else {
            deck = DeckEntity(0L, name, description, image?.uri, collectionOnly, System.currentTimeMillis())
            deck.uid = insertDeck(deck)
        }

        // Insert/Update joins
        val joins = cards.stack().map { DeckCardJoin(deck.uid, it.card.id, it.count) }
        insertJoins(joins)

        return deck
    }

    @Transaction
    open fun duplicateDeck(deck: Deck) {
        duplicate(deck)
    }

    private fun duplicate(deck: Deck) {
        val existing = getDeck(deck.name)
        if (existing == null) {
            val entity = DeckEntity(
                0L,
                deck.name,
                deck.description,
                deck.image?.uri,
                deck.collectionOnly,
                System.currentTimeMillis()
            )
            val id = insertDeck(entity)
            val joins = deck.cards.stack().map { DeckCardJoin(id, it.card.id, it.count) }
            insertJoins(joins)
        } else {
            val regex = DUPLICATE_REGEX.toRegex()
            val count = regex.findAll(deck.name).lastOrNull()?.let {
                val count = it.value.replace("(", "").replace(")", "").trim().toIntOrNull() ?: 1
                count + 1
            } ?: 1

            val cleanName = deck.name.replace(regex, "").trim()
            val newName = "$cleanName ($count)"
            duplicate(Deck(
                "",
                newName,
                deck.description,
                deck.image,
                deck.collectionOnly,
                deck.cards,
                false,
                deck.timestamp
            ))
        }
    }

    private fun insertCardsWithAttacks(cards: List<CardWithAttacks>) {
        cards.forEach {
            insertCardWithAttacks(it)
        }
    }

    private fun insertCardWithAttacks(card: CardWithAttacks) {
        if (insertCard(card.card) > 0L) {
            insertAttacks(card.attacks)
        }
    }

    companion object {

        private const val DUPLICATE_REGEX = "\\(\\d+\\)"
    }
}
