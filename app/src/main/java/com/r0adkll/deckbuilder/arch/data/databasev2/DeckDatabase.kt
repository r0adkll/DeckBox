package com.r0adkll.deckbuilder.arch.data.databasev2

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.r0adkll.deckbuilder.arch.data.databasev2.converter.UriConverter
import com.r0adkll.deckbuilder.arch.data.databasev2.dao.CardDao
import com.r0adkll.deckbuilder.arch.data.databasev2.dao.SessionDao
import com.r0adkll.deckbuilder.arch.data.databasev2.entities.*


@Database(
        version = 1,
        entities = [
            AttackEntity::class,
            CardEntity::class,
            SessionCardJoin::class,
            SessionChangeEntity::class,
            SessionEntity::class
        ]
)
@TypeConverters(UriConverter::class)
abstract class DeckDatabase : RoomDatabase() {

    abstract fun cards(): CardDao
    abstract fun sessions(): SessionDao
}