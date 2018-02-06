package com.r0adkll.deckbuilder.arch.data.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.r0adkll.deckbuilder.arch.data.room.dao.CardDao
import com.r0adkll.deckbuilder.arch.data.room.entities.AttackEntity
import com.r0adkll.deckbuilder.arch.data.room.entities.CardEntity


@Database(entities = [
    (CardEntity::class),
    (AttackEntity::class)
], version = 1)
abstract class CardDatabase : RoomDatabase() {

    abstract fun cards(): CardDao
}