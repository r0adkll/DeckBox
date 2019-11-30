package com.r0adkll.deckbuilder.arch.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.r0adkll.deckbuilder.arch.data.database.entities.PriceEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.ProductEntity
import com.r0adkll.deckbuilder.arch.data.database.mapping.mapToEntity
import com.r0adkll.deckbuilder.arch.data.database.relations.PriceWithProduct
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
        SELECT *, MAX(marketplace_prices.updatedAt) 
        FROM marketplace_prices 
        INNER JOIN marketplace_products ON marketplace_prices.productId = marketplace_products.id 
        WHERE marketplace_products.cardId IN(:cardIds) 
        GROUP BY marketplace_prices.productId
    """)
    abstract fun getLatestPriceForProducts(cardIds: Set<String>): Single<List<PriceWithProduct>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertProduct(product: ProductEntity): Long

    @Insert
    abstract fun insertPrices(prices: List<PriceEntity>): List<Long>

    @Query("DELETE FROM marketplace_products")
    abstract fun deleteAll()

    @Transaction
    fun insertProducts(products: List<Product>) {
        for (product in products) {
            val entity = product.mapToEntity()
            val productId = insertProduct(entity)
            val priceEntities = product.prices.map {
                it.mapToEntity(productId)
            }
            insertPrices(priceEntities)
        }
    }
}
