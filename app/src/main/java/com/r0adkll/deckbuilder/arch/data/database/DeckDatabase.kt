package com.r0adkll.deckbuilder.arch.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.r0adkll.deckbuilder.arch.data.database.converter.UriConverter
import com.r0adkll.deckbuilder.arch.data.database.dao.CardDao
import com.r0adkll.deckbuilder.arch.data.database.dao.DeckDao
import com.r0adkll.deckbuilder.arch.data.database.dao.SessionDao
import com.r0adkll.deckbuilder.arch.data.database.entities.*


@Database(
        version = 1,
        entities = [
            DeckEntity::class,
            DeckCardJoin::class,
            AttackEntity::class,
            CardEntity::class,
            SessionCardJoin::class,
            SessionChangeEntity::class,
            SessionEntity::class
        ]
)
@TypeConverters(UriConverter::class)
abstract class DeckDatabase : RoomDatabase() {

    abstract fun decks(): DeckDao
    abstract fun cards(): CardDao
    abstract fun sessions(): SessionDao
}