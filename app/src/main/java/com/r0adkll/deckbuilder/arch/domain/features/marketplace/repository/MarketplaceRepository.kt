package com.r0adkll.deckbuilder.arch.domain.features.marketplace.repository

import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Price
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.PriceCondition
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.PriceRarity
import io.reactivex.Observable

/**
 * Repository to access marketplace data for showing pricing data
 */
interface MarketplaceRepository {

    /**
     * Get the list of available pricing rarities in the current marketplace
     * @return an observable of the list of marketplace rarities
     */
    fun getRarities(): Observable<List<PriceRarity>>

    /**
     * Get the list of available card conditions in the current marketplace
     * @return an observable of the list of available card conditions
     */
    fun getConditions(): Observable<List<PriceCondition>>

    /**
     * Get the pricing information for a single card
     * @param cardId the api card id to get the pricing information of
     * @param rarity the optional rarity of the card to get (i.e. 1st edition, reverse holo, normal)
     * @return an observable of the price for the given [cardId] and [rarity], if suplied
     */
    fun getPrice(cardId: String, rarity: String? = null): Observable<Price>

    /**
     * Get the price history for a single card/rarity
     * @param cardId the api card id to get the pricing information of
     * @param rarity the optional rarity of the card to get (i.e. 1st edition, reverse holo, normal)
     * @return tan observable of the price history for this card
     */
    fun getPriceHistory(cardId: String, rarity: String? = null): Observable<List<Price>>

    /**
     * Get a list of prices for multiple card ids
     * @param cardIds the list of card ids to get the pricing information for
     * @return an observable of the map of prices for all of the [cardIds], if their price could be found
     */
    fun getPrices(cardIds: List<String>): Observable<Map<String, Price>>
}
