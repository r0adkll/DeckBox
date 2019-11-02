package com.r0adkll.deckbuilder.arch.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.r0adkll.deckbuilder.arch.data.database.entities.CollectionCountEntity
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
abstract class CollectionDao {

    @Query("SELECT * FROM collection")
    abstract fun getAll(): Single<List<CollectionCountEntity>>

    @Query("SELECT * FROM collection")
    abstract fun observeAll(): Flowable<List<CollectionCountEntity>>

    @Query("SELECT * FROM collection WHERE cardId = :id")
    abstract fun getCount(id: String): Flowable<CollectionCountEntity>

    @Query("SELECT * FROM collection WHERE `set` = :set")
    abstract fun getCountForSet(set: String): Flowable<List<CollectionCountEntity>>

    @Query("SELECT * FROM collection WHERE series = :series")
    abstract fun getCountForSeries(series: String): Flowable<List<CollectionCountEntity>>

    @Query("SELECT * FROM collection WHERE cardId = :id")
    abstract fun count(id: String): CollectionCountEntity?

    @Query("SELECT * FROM collection WHERE `set` = :set")
    abstract fun counts(set: String): List<CollectionCountEntity>

    @Update
    abstract fun updateCount(count: CollectionCountEntity): Int

    @Update
    abstract fun updateCounts(counts: List<CollectionCountEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCount(count: CollectionCountEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCounts(count: List<CollectionCountEntity>)

    @Query("DELETE FROM collection")
    abstract fun deleteAll()

    @Transaction
    open fun incrementCount(card: PokemonCard): CollectionCountEntity? {
        val count = count(card.id)
        return when {
            count != null -> {
                count.count += 1
                updateCount(count)
                count
            }
            card.expansion != null -> {
                val newCount = CollectionCountEntity(
                    card.id,
                    1,
                    card.expansion.code,
                    card.expansion.series
                )
                insertCount(newCount)
                newCount
            }
            else -> null
        }
    }

    @Transaction
    open fun decrementCount(card: PokemonCard): CollectionCountEntity? {
        val count = count(card.id)
        if (count != null && count.count > 0) {
            count.count -= 1
            updateCount(count)
        }
        return count
    }

    @Transaction
    open fun incrementSet(set: String, cards: List<PokemonCard>): List<CollectionCountEntity> {
        val existingCards = counts(set)
        existingCards.forEach { it.count++ }
        updateCounts(existingCards)

        val missingCards = cards.filter { card -> existingCards.none { existing -> existing.cardId == card.id } }
        val newCountEntities = missingCards.map { card ->
            CollectionCountEntity(
                card.id,
                1,
                card.expansion!!.code,
                card.expansion.series
            )
        }
        insertCounts(newCountEntities)

        return existingCards + newCountEntities
    }
}
