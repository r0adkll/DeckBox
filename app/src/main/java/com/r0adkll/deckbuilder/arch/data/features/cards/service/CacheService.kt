package com.r0adkll.deckbuilder.arch.data.features.cards.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.arch.data.room.CardDatabase
import com.r0adkll.deckbuilder.arch.data.room.mapping.EntityMapper
import io.pokemontcg.Pokemon
import io.pokemontcg.model.Card
import timber.log.Timber
import javax.inject.Inject


class CacheService : IntentService("DeckBox-Cache-Service") {

    @Inject lateinit var api: Pokemon
    @Inject lateinit var db: CardDatabase


    override fun onCreate() {
        super.onCreate()
        DeckApp.component.inject(this)
    }


    override fun onHandleIntent(intent: Intent?) {
        var page = 1
        var count = 0

        var cardModels = getPage()
        while (cardModels.size == PAGE_SIZE) {

            // Map to DB entities
            val (cards, attacks) = EntityMapper.to(cardModels)

            // Store into database
            db.cards().insertCards(cards, attacks)
            count += cards.size

            Timber.i("Page of cards inserted into database: ${cards.size} cards")

            // Get next page
            page++
            cardModels = getPage(page)
        }

        Timber.i("$count cards over $page pages inserted into database")
    }


    private fun getPage(pageNumber: Int = 1): List<Card> {
        return api.card()
                .where {
                    page = pageNumber
                    pageSize = PAGE_SIZE
                }
                .all()
    }


    companion object {
        private const val PAGE_SIZE = 1000

        fun start(context: Context) {
            val intent = Intent(context, CacheService::class.java)
            context.startService(intent)
        }
    }
}