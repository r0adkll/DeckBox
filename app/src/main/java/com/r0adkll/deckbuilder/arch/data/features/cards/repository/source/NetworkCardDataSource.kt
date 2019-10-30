package com.r0adkll.deckbuilder.arch.data.features.cards.repository.source

import com.r0adkll.deckbuilder.arch.data.features.cards.cache.CardCache
import com.r0adkll.deckbuilder.arch.data.mappings.FilterMapper
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.SearchField
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.expansions.repository.ExpansionRepository
import com.r0adkll.deckbuilder.arch.domain.features.remote.Remote
import com.r0adkll.deckbuilder.util.AppSchedulers
import com.r0adkll.deckbuilder.util.extensions.plus
import io.pokemontcg.Pokemon
import io.pokemontcg.model.Card
import io.pokemontcg.model.SuperType
import io.pokemontcg.requests.CardQueryBuilder
import io.reactivex.Observable
import kotlin.math.exp

@Suppress("UNCHECKED_CAST")
class NetworkCardDataSource(
        val api: Pokemon,
        val expansionRepository: ExpansionRepository,
        val cache: CardCache,
        val remote: Remote,
        val schedulers: AppSchedulers
) : CardDataSource {

    override fun findByExpansion(setCode: String): Observable<List<PokemonCard>> {
        return expansionRepository.getExpansions() + searchNetwork(null, "",
                Filter(expansions = listOf(Expansion(setCode)))
        )
    }

    override fun search(type: SuperType?, query: String, filter: Filter?): Observable<List<PokemonCard>> {
        return expansionRepository.getExpansions() + searchNetwork(type, query, filter)
    }

    override fun find(ids: List<String>): Observable<List<PokemonCard>> {
        return expansionRepository.getExpansions() + findNetwork(ids)
    }

    private fun findNetwork(ids: List<String>): Observable<List<Card>> {
        return api.card()
                .where {
                    id = ids.joinToString("|")
                    pageSize = 1000
                }
                .observeAll()
                .doOnNext { cache.putCards(it) }
                .subscribeOn(schedulers.network)
    }

    private fun searchNetwork(type: SuperType?, query: String, filter: Filter?): Observable<List<Card>> {
        val request = filter?.let {
            FilterMapper.to(it)
        } ?: CardQueryBuilder()

        if (type != null && type != SuperType.UNKNOWN) {
            request.supertype = type.displayName
        }

        if (query.isNotBlank()) {

            // Apply the search proxies, if exists, to the query
            val proxies = remote.searchProxies
            val adjustedQuery = proxies?.apply(query) ?: query

            // Set search field accordingly
            when(filter?.field ?: SearchField.NAME) {
                SearchField.NAME -> request.name = adjustedQuery
                SearchField.TEXT -> request.text = adjustedQuery
                SearchField.ABILITY_NAME -> request.abilityName = adjustedQuery
                SearchField.ABILITY_TEXT -> request.abilityText = adjustedQuery
                SearchField.ATTACK_NAME -> request.attackName = adjustedQuery
                SearchField.ATTACK_TEXT -> request.attackText = adjustedQuery
            }
        }

        return api.card()
                .where(request)
                .observeAll()
                .doOnNext { cache.putCards(it) }
                .subscribeOn(schedulers.network)
    }
}
