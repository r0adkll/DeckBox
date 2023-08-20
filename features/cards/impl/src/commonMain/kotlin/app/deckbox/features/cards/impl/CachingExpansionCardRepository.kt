package app.deckbox.features.cards.impl

import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.model.Card
import app.deckbox.core.model.Expansion
import app.deckbox.features.cards.impl.db.CardDao
import app.deckbox.features.cards.public.ExpansionCardRepository
import app.deckbox.features.cards.public.model.appendValue
import app.deckbox.features.cards.public.model.buildQuery
import app.deckbox.network.PokemonTcgApi
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.FetcherResult
import org.mobilenativefoundation.store.store5.MemoryPolicy
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.impl.extensions.get

@ContributesBinding(MergeAppScope::class)
@Inject
class CachingExpansionCardRepository(
  private val api: PokemonTcgApi,
  private val db: CardDao,
) : ExpansionCardRepository {

  private val store = StoreBuilder
    .from<Expansion, List<Card>, List<Card>>(
      fetcher = Fetcher.ofResult { expansion -> fetchCards(expansion.id) },
      sourceOfTruth = SourceOfTruth.of(
        reader = { key ->
          db.observeByExpansion(key.id)
            .map {
              if (it.size >= key.total) {
                it
              } else {
                null
              }
            }
        },
        writer = { _, local -> db.insert(local) },
      ),
    )
    .cachePolicy(
      MemoryPolicy.builder<Any, Any>()
        .setMaxSize(300)
        .build(),
    )
    .build()

  override suspend fun getCards(expansion: Expansion): List<Card> {
    return store.get(expansion)
  }

  /**
   * Keep fetching cards until there are no more left.
   */
  private suspend fun fetchCards(expansionId: String): FetcherResult<List<Card>> {
    val cards = mutableListOf<Card>()

    var page = 1
    var hasNext = false

    do {
      val response = api.getCards(
        buildQuery(
          page = page,
        ) {
          appendValue("set.id", expansionId)
        },
      )

      if (response.isSuccess) {
        response.getOrNull()?.let { pagedResponse ->
          cards += pagedResponse.data
          if (pagedResponse.hasMore) {
            page++
            hasNext = true
          } else {
            hasNext = false
          }
        } ?: return FetcherResult.Error.Message("Unable to fetch cards for $expansionId")
      } else {
        return response.exceptionOrNull()?.let {
          FetcherResult.Error.Exception(it)
        } ?: FetcherResult.Error.Message("Unable to fetch cards for $expansionId")
      }
    } while (hasNext)

    return FetcherResult.Data(cards, origin = "API")
  }
}
