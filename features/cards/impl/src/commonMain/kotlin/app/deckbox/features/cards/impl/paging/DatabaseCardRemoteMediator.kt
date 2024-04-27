package app.deckbox.features.cards.impl.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import app.deckbox.DeckBoxDatabase
import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.di.AppScope
import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.logging.LogPriority.ERROR
import app.deckbox.core.logging.bark
import app.deckbox.core.model.Card
import app.deckbox.features.cards.impl.db.CardDao
import app.deckbox.features.cards.public.model.CardQuery
import app.deckbox.features.cards.public.paging.CardRemoteMediator
import app.deckbox.features.cards.public.paging.CardRemoteMediatorFactory
import app.deckbox.network.PokemonTcgApi
import app.deckbox.sqldelight.RemoteKeyCardJoin
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
@ContributesBinding(MergeAppScope::class)
class DatabaseCardRemoteMediatorFactory(
  private val mediatorCreator: (query: CardQuery) -> DatabaseCardRemoteMediator,
) : CardRemoteMediatorFactory {

  override fun create(query: CardQuery): CardRemoteMediator {
    return mediatorCreator(query)
  }
}

@Suppress("CAST_NEVER_SUCCEEDS", "USELESS_CAST", "KotlinRedundantDiagnosticSuppress")
@Inject
@OptIn(ExperimentalPagingApi::class)
class DatabaseCardRemoteMediator(
  @Assisted private val query: CardQuery,
  private val database: DeckBoxDatabase,
  private val cardDao: CardDao,
  private val api: PokemonTcgApi,
  private val dispatcherProvider: DispatcherProvider,
) : CardRemoteMediator() {

  override suspend fun load(
    loadType: LoadType,
    state: PagingState<Int, Card>,
  ): MediatorResult {
    return try {
      val loadKey = when (loadType) {
        LoadType.REFRESH -> null
        LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
        LoadType.APPEND -> {
          val latestKey = database.remoteKeyQueries
            .getLatest(query.key)
            .executeAsOne()

          if (latestKey.nextKey == null) {
            return MediatorResult.Success(
              endOfPaginationReached = true,
            )
          }

          latestKey.nextKey
        }
        else -> null
      } ?: DefaultPageKey

      bark { "Mediator Loading($loadType) for $loadKey, query = ${query.key}" }

      // Load from network
      val result = withContext(dispatcherProvider.io) {
        api.getCards(
          query.copy(
            page = loadKey,
          ).asQueryOptions(),
        )
      }

      if (result.isSuccess) {
        val response = result.getOrThrow()

        withContext(dispatcherProvider.databaseWrite) {
          // Insert a new remote key
          val remoteKeyId = database.remoteKeyQueries.transactionWithResult {
            database.remoteKeyQueries.insert(
              query = query.key,
              key = loadKey,
              count = response.count,
              totalCount = response.totalCount,
              nextKey = loadKey.plus(1)
                .takeIf { response.hasMore },
            )

            database.remoteKeyQueries
              .lastInsertRowId()
              .executeAsOne()
          }

          // Okay now insert all the cards
          bark { "Inserting ${response.count} cards" }
          cardDao.insert(response.data)

          database.transaction {
            // Now insert all the relations
            bark { "Inserting query relations for Id($remoteKeyId)" }
            response.data.forEach { card ->
              database.remoteKeyQueries.insertRemoteKeyCard(
                RemoteKeyCardJoin(
                  remoteKeyId = remoteKeyId,
                  cardId = card.id,
                ),
              )
            }
          }
        }

        MediatorResult.Success(
          endOfPaginationReached = !response.hasMore,
        )
      } else {
        MediatorResult.Error(result.exceptionOrNull() ?: Exception("Network error"))
      }
    } catch (t: Throwable) {
      bark(ERROR, throwable = t) { "Mediator Error" }
      MediatorResult.Error(t)
    }
  }
}
