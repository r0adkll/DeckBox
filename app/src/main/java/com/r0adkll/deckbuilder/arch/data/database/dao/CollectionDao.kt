package com.r0adkll.deckbuilder.arch.data.database.dao

import androidx.room.*
import com.r0adkll.deckbuilder.arch.data.database.entities.CollectionCountEntity
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import io.reactivex.Flowable
import io.reactivex.Single


@Dao
abstract class CollectionDao {

    @Query("SELECT * FROM collection")
    abstract fun observeAll(): Flowable<List<CollectionCountEntity>>

    @Query("SELECT * FROM collection WHERE cardId = :id")
    abstract fun getCount(id: String): Flowable<CollectionCountEntity>

    @Query("SELECT * FROM collection WHERE cardId IN(:ids)")
    abstract fun getCounts(ids: List<String>): Flowable<List<CollectionCountEntity>>

    @Query("SELECT * FROM collection WHERE `set` = :set")
    abstract fun getCountForSet(set: String): Flowable<List<CollectionCountEntity>>

    @Query("SELECT * FROM collection WHERE series = :series")
    abstract fun getCountForSeries(series: String): Flowable<List<CollectionCountEntity>>


    @Query("SELECT * FROM collection WHERE cardId = :id")
    abstract fun count(id: String): CollectionCountEntity?

    @Update
    abstract fun updateCount(count: CollectionCountEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCount(count: CollectionCountEntity): Long


    @Transaction
    fun incrementCount(card: PokemonCard) {
        val count = count(card.id)
        if (count != null) {
            count.count += 1
            updateCount(count)
        } else if (card.expansion != null){
            val newCount = CollectionCountEntity(
                    card.id,
                    1,
                    card.expansion.code,
                    card.expansion.series
            )
            insertCount(newCount)
        }
    }

    @Transaction
    fun decrementCount(card: PokemonCard) {
        val count = count(card.id)
        if (count != null && count.count > 0) {
            count.count -= 1
            updateCount(count)
        }
    }
}