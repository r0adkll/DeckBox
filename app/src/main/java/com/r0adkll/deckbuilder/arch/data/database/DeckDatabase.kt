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
import com.r0adkll.deckbuilder.arch.data.database.dao.MarketplaceDao
import com.r0adkll.deckbuilder.arch.data.database.dao.SessionDao
import com.r0adkll.deckbuilder.arch.data.database.entities.AttackEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.CardEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.CollectionCountEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.DeckCardJoin
import com.r0adkll.deckbuilder.arch.data.database.entities.DeckEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.PriceEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.ProductEntity
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
 * 3. Added marketplace product + prices
 */
@Database(
    version = 3,
    entities = [
        DeckEntity::class,
        DeckCardJoin::class,
        AttackEntity::class,
        CardEntity::class,
        SessionCardJoin::class,
        SessionChangeEntity::class,
        SessionEntity::class,
        CollectionCountEntity::class,
        ProductEntity::class,
        PriceEntity::class
    ]
)
@TypeConverters(UriConverter::class)
abstract class DeckDatabase : RoomDatabase() {

    abstract fun decks(): DeckDao
    abstract fun cards(): CardDao
    abstract fun sessions(): SessionDao
    abstract fun collection(): CollectionDao
    abstract fun marketplace(): MarketplaceDao

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

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `marketplace_products` (
                        `cardId` TEXT NOT NULL, 
                        `setCode` TEXT NOT NULL, 
                        `groupId` INTEGER NOT NULL, 
                        `productId` INTEGER NOT NULL, 
                        `productName` TEXT NOT NULL, 
                        `url` TEXT NOT NULL, 
                        `modifiedOn` INTEGER NOT NULL,
                        PRIMARY KEY(`cardId`)
                    )
                """)
                database.execSQL("""
                    CREATE INDEX IF NOT EXISTS `index_marketplace_products_cardId` ON `marketplace_products` (`cardId`)
                """)
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `marketplace_prices` (
                        `price_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        `rarity` TEXT NOT NULL, 
                        `low` REAL, 
                        `mid` REAL, 
                        `high` REAL, 
                        `market` REAL, 
                        `directLow` REAL, 
                        `updatedAt` INTEGER NOT NULL, 
                        `expiresAt` INTEGER NOT NULL, 
                        `parentId` INTEGER NOT NULL, 
                        FOREIGN KEY(`parentId`) REFERENCES `marketplace_products`(`cardId`) ON UPDATE NO ACTION ON DELETE CASCADE 
                    )
                """)
            }
        }
    }
}
