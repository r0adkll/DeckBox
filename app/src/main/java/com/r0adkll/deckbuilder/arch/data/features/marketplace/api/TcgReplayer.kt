package com.r0adkll.deckbuilder.arch.data.features.marketplace.api

import com.r0adkll.deckbuilder.arch.data.features.marketplace.api.model.ApiListPriceResponse
import com.r0adkll.deckbuilder.arch.data.features.marketplace.api.model.ApiPriceResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TcgReplayer {

    @GET("price/{cardId}")
    fun getPrice(
        @Path("cardId") cardId: String,
        @Query("since") sinceDateTime: String? = null,
        @Query("count") count: Int? = null,
        @Query("subType") subType: String? = null
    ): Observable<ApiPriceResponse>

    @GET("prices")
    fun getPrices(
        @Query("cardIds") cardIds: Set<String>
    ): Observable<ApiListPriceResponse>
}
