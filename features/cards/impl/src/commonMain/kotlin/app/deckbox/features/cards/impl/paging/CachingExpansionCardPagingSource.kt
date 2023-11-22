package app.deckbox.features.cards.impl.paging

import androidx.paging.PagingState
import app.deckbox.core.di.AppScope
import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.model.Card
import app.deckbox.core.model.Expansion
import app.deckbox.features.cards.impl.db.CardDao
import app.deckbox.features.cards.public.model.appendValue
import app.deckbox.features.cards.public.model.buildQuery
import app.deckbox.features.cards.public.paging.ExpansionCardPagingSource
import app.deckbox.features.cards.public.paging.ExpansionCardPagingSourceFactory
import app.deckbox.network.PokemonTcgApi
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
@ContributesBinding(MergeAppScope::class)
class CachingExpansionPagingSourceFactory(
  private val api: PokemonTcgApi,
  private val db: CardDao,
) : ExpansionCardPagingSourceFactory {

  override fun create(expansion: Expansion): ExpansionCardPagingSource {
    return CachingExpansionCardPagingSource(api, db, expansion)
  }
}

@Suppress("CAST_NEVER_SUCCEEDS", "UNCHECKED_CAST", "USELESS_CAST", "KotlinRedundantDiagnosticSuppress")
class CachingExpansionCardPagingSource(
  private val api: PokemonTcgApi,
  private val db: CardDao,
  private val expansion: Expansion,
) : ExpansionCardPagingSource() {

  override fun getRefreshKey(state: PagingState<Int, Card>): Int? = null

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Card> {
    // First fetch all expansions from database if this is the
    // first load
    return if (params.key == null || params.key == 0) {
      val databaseCards = db.fetchByExpansion(expansion.id)
      if (databaseCards.size >= expansion.total) {
        LoadResult.Page(
          data = databaseCards.sortedBy { it.number.toIntOrNull() ?: 1 },
          prevKey = null,
          nextKey = null,
        )
      } else {
        loadFromNetwork(params)
      }
    } else {
      loadFromNetwork(params)
    }
  }

  private suspend fun loadFromNetwork(params: LoadParams<Int>): LoadResult<Int, Card> {
    val result = api.getCards(
      buildQuery(
        page = params.key?.plus(1) ?: 1,
      ) {
        appendValue("set.id", expansion.id)
      },
    )
    return if (result.isSuccess) {
      val response = result.getOrThrow()

      // Cache response
      db.insert(response.data)

      LoadResult.Page(
        data = response.data,
        prevKey = null,
        nextKey = response.page
          .takeIf { response.hasMore },
      )
    } else {
      LoadResult.Error(result.exceptionOrNull() ?: Exception("Unable to load cards"))
    }
  }
}
