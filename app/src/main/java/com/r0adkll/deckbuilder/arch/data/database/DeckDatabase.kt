package com.r0adkll.deckbuilder.arch.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.r0adkll.deckbuilder.arch.data.database.converter.UriConverter
import com.r0adkll.deckbuilder.arch.data.database.dao.CardDao
import com.r0adkll.deckbuilder.arch.data.database.dao.CollectionDao
import com.r0adkll.deckbuilder.arch.data.database.dao.DeckDao
import com.r0adkll.deckbuilder.arch.data.database.dao.SessionDao
import com.r0adkll.deckbuilder.arch.data.database.entities.AttackEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.CardEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.CollectionCountEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.DeckCardJoin
import com.r0adkll.deckbuilder.arch.data.database.entities.DeckEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.SessionCardJoin
import com.r0adkll.deckbuilder.arch.data.database.entities.SessionChangeEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.SessionEntity

/**
 * The Room database object for storing all local on-device data from card cache
 * to all user data if offline account
 *
 * Changelog
 * ---
 * 1. Initial Version (production)
 * 2. Added collections support (production)
 */
@Database(
    version = 2,
    entities = [
        DeckEntity::class,
        DeckCardJoin::class,
        AttackEntity::class,
        CardEntity::class,
        SessionCardJoin::class,
        SessionChangeEntity::class,
        SessionEntity::class,
        CollectionCountEntity::class
    ]
)
@TypeConverters(UriConverter::class)
abstract class DeckDatabase : RoomDatabase() {

    abstract fun decks(): DeckDao
    abstract fun cards(): CardDao
    abstract fun sessions(): SessionDao
    abstract fun collection(): CollectionDao

    companion object {

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `collection` (
                        `cardId` TEXT NOT NULL, 
                        `count` INTEGER NOT NULL, 
                        `set` TEXT NOT NULL, 
                        `series` TEXT NOT NULL, 
                        PRIMARY KEY(`cardId`)
                    )
                """)
                database.execSQL("ALTER TABLE `decks` ADD COLUMN `collectionOnly` INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE `sessions` ADD COLUMN `originalCollectionOnly` INTEGER")
                database.execSQL("ALTER TABLE `sessions` ADD COLUMN `collectionOnly` INTEGER")
            }
        }
    }
}
