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
import io.pokemontcg.ExperimentalPokemonApi
import io.pokemontcg.Pokemon
import io.pokemontcg.allAsObservable
import io.pokemontcg.model.Card
import io.pokemontcg.model.SuperType
import io.pokemontcg.requests.CardQueryBuilder
import io.pokemontcg.requests.query.CardBuilder
import io.pokemontcg.requests.query.StringValue
import io.pokemontcg.requests.query.cardBuilder
import io.reactivex.Observable
import javax.inject.Inject

@ExperimentalPokemonApi
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
    return expansionRepository.getExpansions() + searchNetwork(
      null, "",
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
        query = cardBuilder {
          ids(ids)
        }
        pageSize = MAX_PAGE_SIZE
      }
      .allAsObservable()
      .doOnNext { cache.putCards(it) }
      .subscribeOn(schedulers.network)
  }

  private fun searchNetwork(type: SuperType?, query: String, filter: Filter?): Observable<List<Card>> {
    val actualFilter = if (type != null && type != SuperType.UNKNOWN && filter != null) {
      filter.copy(superType = type)
    } else {
      filter
    }


    val adjustedQuery = if (query.isNotBlank()) {
      remote.searchProxies
        ?.apply(query)
        ?: query
    } else null

    val request = actualFilter?.let {
      FilterMapper.to(it) {
        if (adjustedQuery != null) {
          applySearchField(adjustedQuery, it)
        }
      }
    } ?: CardQueryBuilder().apply {
      this.query = cardBuilder {
        if (type != null && type != SuperType.UNKNOWN) {
          supertype(type)
        }

        if (adjustedQuery != null) {
          applySearchField(adjustedQuery, null)
        }
      }
    }

    return api.card()
      .where(request)
      .allAsObservable()
      .doOnNext { cache.putCards(it) }
      .subscribeOn(schedulers.network)
  }

  private fun CardBuilder.applySearchField(query: String, filter: Filter?) {
    when (filter?.field ?: SearchField.NAME) {
      SearchField.NAME -> name(query)
      SearchField.TEXT -> components += StringValue("flavorText:$query")
      SearchField.ABILITY_NAME -> abilities { name(query) }
      SearchField.ABILITY_TEXT -> abilities { text(query) }
      SearchField.ATTACK_NAME -> attacks { name(query) }
      SearchField.ATTACK_TEXT -> attacks { text(query) }
    }
  }

  companion object {
    private const val MAX_PAGE_SIZE = 1000
  }
}
