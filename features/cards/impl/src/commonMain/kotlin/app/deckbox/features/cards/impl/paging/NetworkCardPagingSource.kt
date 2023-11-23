package app.deckbox.features.cards.impl.paging

import androidx.paging.PagingState
import app.deckbox.core.di.AppScope
import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.model.Card
import app.deckbox.features.cards.impl.db.CardDao
import app.deckbox.features.cards.public.model.CardQuery
import app.deckbox.features.cards.public.paging.CardPagingSource
import app.deckbox.features.cards.public.paging.CardPagingSourceFactory
import app.deckbox.network.PokemonTcgApi
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
//@ContributesBinding(MergeAppScope::class)
class NetworkCardPagingSourceFactory(
  private val api: PokemonTcgApi,
  private val db: CardDao,
) : CardPagingSourceFactory {

  override fun create(query: CardQuery): CardPagingSource {
    return NetworkCardPagingSource(api, db, query)
  }
}

@Suppress("CAST_NEVER_SUCCEEDS", "USELESS_CAST", "KotlinRedundantDiagnosticSuppress")
class NetworkCardPagingSource(
  private val api: PokemonTcgApi,
  private val db: CardDao,
  private val query: CardQuery,
) : CardPagingSource() {

  override fun getRefreshKey(state: PagingState<Int, Card>): Int? = null

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Card> {
    val result = api.getCards(
      query.copy(page = params.key ?: 1)
        .asQueryOptions()
    )
    return if (result.isSuccess) {
      val response = result.getOrThrow()

      // Cache response
      db.insert(response.data)

      LoadResult.Page(
        data = response.data,
        prevKey = null,
        nextKey = response.page
          .plus(1)
          .takeIf { response.hasMore },
      )
    } else {
      LoadResult.Error(result.exceptionOrNull() ?: Exception("Unable to load cards"))
    }
  }
}
