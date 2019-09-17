package com.r0adkll.deckbuilder.arch.data.features.cards.repository.source

import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.remote.Remote
import com.r0adkll.deckbuilder.util.helper.Connectivity
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import io.reactivex.functions.Function
import timber.log.Timber


class DefaultCardDataSource(
        val preferences: AppPreferences,
        val diskSource: CardDataSource,
        val networkSource: CardDataSource,
        val previewSource: CardDataSource,
        val connectivity: Connectivity,
        val remote: Remote
) : CardDataSource {

    override fun search(type: SuperType?, query: String, filter: Filter?): Observable<List<PokemonCard>> {
        val forceDiskSearch = filter?.expansions
                ?.map { it.code }
                ?.let { it.isNotEmpty() && preferences.offlineExpansions.get().containsAll(it) }
                ?: false
        return if (connectivity.isConnected() && !forceDiskSearch) {
            // TODO: Check for search type qualifictions for preview data

            networkSource.search(type, query, filter)
                    .onErrorResumeNext(Function {
                        Timber.e(it, "Error searching for cards")
                        diskSource.search(type, query, filter)
                    })
        } else {
            diskSource.search(type, query, filter)
        }
    }

    override fun find(ids: List<String>): Observable<List<PokemonCard>> {
        return if (connectivity.isConnected()) {
            diskSource.find(ids)
                    .flatMap { diskCards ->
                        val missingIds = ids.filter { id -> diskCards.none { card -> card.id == id } }
                        if (missingIds.isNotEmpty()) {
                            // TODO: Split id's based on preview set configuration

                            networkSource.find(missingIds)
                                    .map { it.plus(diskCards) }
                        } else {
                            Observable.just(diskCards)
                        }
                    }
        } else {
            diskSource.find(ids)
        }
    }
}
