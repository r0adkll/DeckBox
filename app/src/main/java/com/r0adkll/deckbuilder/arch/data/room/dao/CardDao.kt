package com.r0adkll.deckbuilder.arch.data.room.dao


import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.r0adkll.deckbuilder.arch.data.room.entities.AttackEntity
import com.r0adkll.deckbuilder.arch.data.room.entities.CardEntity


@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCards(cards: List<CardEntity>, attacks: List<AttackEntity>)


    @Query("DELETE FROM cards")
    fun deleteAll()
}