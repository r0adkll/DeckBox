package com.r0adkll.deckbuilder.arch.ui.features.marketplace

import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Product
import com.robinhood.spark.SparkAdapter

class ProductSparkAdapter(
        private val products: List<Product>
) : SparkAdapter() {

    private val baseLine = products.maxBy { it.recordedAt }?.marketPrice?.toFloat()

    override fun getY(index: Int): Float {
        return products[index].marketPrice?.toFloat() ?: 0f
    }

    override fun getItem(index: Int): Any {
        return products[index]
    }

    override fun getCount(): Int {
        return products.size
    }

    override fun hasBaseLine(): Boolean {
        return baseLine != null
    }

    override fun getBaseLine(): Float {
        return baseLine ?: -1f
    }

    override fun getX(index: Int): Float {
        return products[index].recordedAt.toFloat()
    }
}
