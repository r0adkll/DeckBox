package com.r0adkll.deckbuilder.arch.data.features.cards.repository.source

import com.r0adkll.deckbuilder.arch.data.features.cards.cache.CardCache
import com.r0adkll.deckbuilder.arch.data.mappings.FilterMapper
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.SearchField
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.expansions.repository.ExpansionRepository
import com.r0adkll.deckbuilder.arch.domain.features.remote.Remote
import com.r0adkll.deckbuilder.internal.di.scopes.AppScope
import com.r0adkll.deckbuilder.util.AppSchedulers
import com.r0adkll.deckbuilder.util.extensions.plus
import io.pokemontcg.Pokemon
import io.pokemontcg.model.Card
import io.pokemontcg.model.SuperType
import io.pokemontcg.requests.CardQueryBuilder
import io.reactivex.Observable
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
@AppScope
class NetworkCardDataSource @Inject constructor(
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
                pageSize = MAX_PAGE_SIZE
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
            val adjustedQuery = remote.searchProxies
                ?.apply(query)
                ?: query

            request.applySearchField(adjustedQuery, filter)
        }

        return api.card()
            .where(request)
            .observeAll()
            .doOnNext { cache.putCards(it) }
            .subscribeOn(schedulers.network)
    }

    fun CardQueryBuilder.applySearchField(query: String, filter: Filter?) {
        when (filter?.field ?: SearchField.NAME) {
            SearchField.NAME -> this.name = query
            SearchField.TEXT -> this.text = query
            SearchField.ABILITY_NAME -> this.abilityName = query
            SearchField.ABILITY_TEXT -> this.abilityText = query
            SearchField.ATTACK_NAME -> this.attackName = query
            SearchField.ATTACK_TEXT -> this.attackText = query
        }
    }

    companion object {
        private const val MAX_PAGE_SIZE = 1000
    }
}
