package com.r0adkll.deckbuilder.arch.ui.features.marketplace

import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Price
import com.robinhood.spark.SparkAdapter

class ProductSparkAdapter(
    private val prices: List<Price>
) : SparkAdapter() {

    private val baseLine = prices.maxByOrNull { it.updatedAt }?.market?.toFloat()

    override fun getY(index: Int): Float {
        return prices[index].market?.toFloat() ?: 0f
    }

    override fun getItem(index: Int): Any {
        return prices[index]
    }

    override fun getCount(): Int {
        return prices.size
    }

    override fun hasBaseLine(): Boolean {
        return baseLine != null
    }

    override fun getBaseLine(): Float {
        return baseLine ?: -1f
    }

    override fun getX(index: Int): Float {
        return prices[index].updatedAt.toFloat()
    }
}
