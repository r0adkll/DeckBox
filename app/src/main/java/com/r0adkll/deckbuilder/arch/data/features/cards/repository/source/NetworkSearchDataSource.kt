package com.r0adkll.deckbuilder.arch.data.features.cards.repository.source


import com.r0adkll.deckbuilder.arch.data.remote.Remote
import com.r0adkll.deckbuilder.arch.data.features.expansions.ExpansionDataSource
import com.r0adkll.deckbuilder.arch.data.mappings.CardMapper
import com.r0adkll.deckbuilder.arch.data.mappings.FilterMapper
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.SearchField
import com.r0adkll.deckbuilder.util.Schedulers
import io.pokemontcg.Pokemon
import io.pokemontcg.model.Card
import io.pokemontcg.model.SuperType
import io.pokemontcg.requests.CardQueryBuilder
import io.reactivex.Observable


@Suppress("UNCHECKED_CAST")
class NetworkSearchDataSource(
        val api: Pokemon,
        val source: ExpansionDataSource,
        val remote: Remote,
        val schedulers: Schedulers
) : SearchDataSource {

    override fun search(type: SuperType?, query: String, filter: Filter?): Observable<List<PokemonCard>> {
        return Observable.combineLatestDelayError(listOf(source.getExpansions(), searchNetwork(type, query, filter)), { t: Array<out Any> ->
            val expansions = t[0] as List<Expansion>
            val cards = t[1] as List<Card>
            cards.map { CardMapper.to(it, expansions) }
        }).onErrorResumeNext(Observable.just(emptyList()))
    }


    override fun find(ids: List<String>): Observable<List<PokemonCard>> {
        return Observable.combineLatestDelayError(listOf(source.getExpansions(), findNetwork(ids)), { t: Array<out Any> ->
            val expansions = t[0] as List<Expansion>
            val cards = t[1] as List<Card>
            cards.map { CardMapper.to(it, expansions) }
        }).onErrorReturnItem(emptyList())
    }


    private fun findNetwork(ids: List<String>): Observable<List<Card>> {
        return api.card()
                .where {
                    id = ids.joinToString("|")
                }
                .observeAll()
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
                .subscribeOn(schedulers.network)
    }
}