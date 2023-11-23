package app.deckbox.features.cards.impl.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.deckbox.DeckBoxDatabase
import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.di.AppScope
import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.logging.bark
import app.deckbox.core.model.Card
import app.deckbox.features.cards.impl.db.CardDao
import app.deckbox.features.cards.public.model.CardQuery
import app.deckbox.features.cards.public.paging.CardPagingSourceFactory
import app.deckbox.sqldelight.Cards
import app.deckbox.sqldelight.RemoteKey
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
@ContributesBinding(MergeAppScope::class)
class DatabaseCardPagingSourceFactory(
  private val sourceFactory: (CardQuery) -> DatabaseCardPagingSource,
) : CardPagingSourceFactory {

  override fun create(query: CardQuery): PagingSource<Int, Card> {
    return sourceFactory(query)
  }
}

@Suppress("CAST_NEVER_SUCCEEDS", "UNCHECKED_CAST","USELESS_CAST", "KotlinRedundantDiagnosticSuppress")
@Inject
class DatabaseCardPagingSource(
  @Assisted private val query: CardQuery,
  private val database: DeckBoxDatabase,
  private val cardDao: CardDao,
  private val dispatcherProvider: DispatcherProvider,
) : QueryPagingSource<Int, Card, Cards>() {

  override fun getRefreshKey(state: PagingState<Int, Card>): Int? = null

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Card> {
    return withContext(dispatcherProvider.databaseRead) {
      try {
        val loadKey = params.key ?: DefaultPageKey
        // Attempt to find the local remote key for this query
        val remoteKey = database.remoteKeyQueries
          .getForQueryAndKey(
            query = query.key,
            key = loadKey
          )
          .executeAsOneOrNull()

        bark { "Database Card Page: Params(${params.key}), RemoteKey($remoteKey)" }

        // If we don't have a local remote key, or the local key's count is 0,
        // then just return empty success, and let the mediator load from network
        if (remoteKey?.count == 0) {
          return@withContext LoadResult.Page(
            data = emptyList<Card>(),
            prevKey = null,
            nextKey = null,
          )
        }

        // Okay, we have a key, which means we should have cards, so lets attempt to load them
        val cards = remoteKey?.let {
          cardDao.fetchByRemoteKey(it.id) {
            currentQuery = it
          }.sortedByDescending { it.number.toIntOrNull() }
        } ?: cardDao.fetchByRemoteKey(
          query = query.key,
          key = loadKey,
          onQuery = { currentQuery = it }
        ).sortedByDescending { it.number.toIntOrNull() }

        // If RemoteKey is null, then we need to wait on the mediator to load
        // and invalidate this
        if (remoteKey != null) {
          // Validate that the # of cards matches the expected account, or return Invalid
          if (cards.size != remoteKey.count) {
            this@DatabaseCardPagingSource.bark {
              "Uh-oh, local database returned an unexpected count of cards, " +
                "${cards.size} != ${remoteKey.count}"
            }
          }

          LoadResult.Page(
            data = cards,
            prevKey = null,
            nextKey = remoteKey.nextKey,
            itemsBefore = remoteKey.itemsBefore,
            itemsAfter = remoteKey.itemsAfter,
          )
        } else {
          bark { "RemoteKey is null for (${query.key}, loadKey = $loadKey)" }
          LoadResult.Page(
            data = emptyList(),
            prevKey = null,
            nextKey = null,
          )
        }
      } catch (t: Throwable) {
        bark(throwable = t) { "Error fetching page from database" }
        LoadResult.Error(t)
      }
    }
  }

  private val RemoteKey.itemsBefore: Int
    get() = (key - 1) * this@DatabaseCardPagingSource.query.pageSize

  private val RemoteKey.itemsAfter: Int
    get() = totalCount - (itemsBefore + count)
}

internal const val DefaultPageKey = 1
