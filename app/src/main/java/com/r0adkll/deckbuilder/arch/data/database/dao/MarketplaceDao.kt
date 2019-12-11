package com.r0adkll.deckbuilder.arch.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.r0adkll.deckbuilder.arch.data.database.entities.PriceEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.ProductEntity
import com.r0adkll.deckbuilder.arch.data.database.mapping.mapToEntity
import com.r0adkll.deckbuilder.arch.data.database.relations.ProductWithPrices
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Product
import io.reactivex.Single

@Dao
abstract class MarketplaceDao {

    @Transaction
    @Query("SELECT * FROM marketplace_products WHERE cardId = :cardId")
    abstract fun getProductWithPrices(cardId: String): Single<ProductWithPrices>

    @Transaction
    @Query("""
        SELECT *
        FROM marketplace_products
        WHERE marketplace_products.cardId IN(:cardIds) 
    """)
    abstract fun getLatestPriceForProducts(cardIds: Set<String>): Single<List<ProductWithPrices>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertProduct(product: ProductEntity): Long

    @Insert
    abstract fun insertPrices(prices: List<PriceEntity>): List<Long>

    @Query("DELETE FROM marketplace_products")
    abstract fun deleteAll()

    @Transaction
    open fun insertProducts(products: List<Product>) {
        for (product in products) {
            val entity = product.mapToEntity()
            insertProduct(entity)
            val priceEntities = product.prices.map {
                it.mapToEntity(entity.cardId)
            }
            insertPrices(priceEntities)
        }
    }
}
