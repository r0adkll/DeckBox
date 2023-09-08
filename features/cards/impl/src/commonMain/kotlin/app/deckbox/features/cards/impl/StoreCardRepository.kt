package app.deckbox.features.cards.impl

import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.di.AppScope
import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.logging.bark
import app.deckbox.core.model.Card
import app.deckbox.core.model.Stacked
import app.deckbox.features.cards.impl.db.CardDao
import app.deckbox.features.cards.public.CardRepository
import app.deckbox.features.cards.public.model.CardQuery
import app.deckbox.network.PokemonTcgApi
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.mapNotNull
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.FetcherResult
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.store.store5.impl.extensions.fresh
import org.mobilenativefoundation.store.store5.impl.extensions.get

@AppScope
@Inject
@ContributesBinding(MergeAppScope::class)
class StoreCardRepository(
  api: PokemonTcgApi,
  private val db: CardDao,
  private val dispatcherProvider: DispatcherProvider,
) : CardRepository {

  private val store = StoreBuilder
    .from(
      fetcher = CardFetcher(api),
      sourceOfTruth = CardSourceOfTruth(db),
    )
    .build()

  @OptIn(ExperimentalCoroutinesApi::class)
  private val queryStore = StoreBuilder
    .from(
      fetcher = Fetcher.ofResult { cardQuery: CardQuery -> api.getCards(cardQuery.asQueryOptions()).asFetcherResult() },
      sourceOfTruth = SourceOfTruth.of(
        reader = { query: CardQuery ->
          db.observeByRemoteKey(query.key, query.page)
        },
        writer = { query, response -> db.insert(query, response) },
      ),
    )
    .build()

  override suspend fun getCard(id: String): Card? {
    val key = CardKey.Id(id)
    return store.stream(StoreReadRequest.cached(key, refresh = false))
      .filterNot { it is StoreReadResponse.Loading || it is StoreReadResponse.NoNewData }
      .first()
      .dataOrNull()
      ?.let { (it as? CardResponse.Single)?.card }
  }

  override suspend fun getCards(vararg id: String): List<Card> {
    return getCards(id.toList())
  }

  override suspend fun getCards(ids: List<String>): List<Card> {
    val key = CardKey.Ids(ids)
    return store.stream(
      request = StoreReadRequest.cached(
        key = key,
        refresh = false,
      ),
    )
      .filterNot { it is StoreReadResponse.Loading || it is StoreReadResponse.NoNewData }
      .first()
      .requireData()
      .let { (it as CardResponse.Multiple).cards }
  }

  override suspend fun getCards(query: CardQuery): List<Card> {
    return queryStore.fresh(query)
  }

  override suspend fun favorite(id: String, favorited: Boolean) {
    db.favorite(id, favorited)
  }

  override fun observeFavorites(): Flow<Map<String, Boolean>> {
    return db.observeFavorites()
  }

  override fun observeFavorite(id: String): Flow<Boolean> {
    return db.observeFavorite(id)
  }

  override fun observeCards(ids: List<String>): Flow<List<Card>> {
    return store.stream(
      request = StoreReadRequest.cached(
        key = CardKey.Ids(ids),
        refresh = false,
      ),
    ).mapNotNull { (it.dataOrNull() as? CardResponse.Multiple)?.cards }
  }

  override fun observeCardsForDeck(deckId: String): Flow<List<Stacked<Card>>> {
    return db.observeByDeck(deckId)
  }

  override fun observeCardsForBoosterPack(packId: String): Flow<List<Stacked<Card>>> {
    return db.observeByBoosterPack(packId)
  }

  override fun observeCardsForFavorites(): Flow<List<Card>> {
    return db.observeByFavorites()
  }
}

class CardFetcher(
  private val api: PokemonTcgApi,
) : Fetcher<CardKey, CardResponse> {
  override val name: String = "CardFetcher"

  override fun invoke(key: CardKey): Flow<FetcherResult<CardResponse>> = flow {
    when (key) {
      is CardKey.Id -> api.getCard(key.id)
        .map { CardResponse.Single(it) }
        .asFetcherResult()
        .also { emit(it) }

      is CardKey.Ids -> api.getCards()
        .map { CardResponse.Multiple(it.data) }
        .asFetcherResult()
        .also { emit(it) }
    }
  }

  override val fallback: Fetcher<CardKey, CardResponse>? = null
}

fun <T : Any> Result<T>.asFetcherResult(): FetcherResult<T> = when {
  isSuccess -> FetcherResult.Data(this.getOrThrow())
  else -> this.exceptionOrNull()?.let { FetcherResult.Error.Exception(it) } as? FetcherResult<T>
    ?: FetcherResult.Error.Message("Unable to fetch expansions") as FetcherResult<T>
}

class CardSourceOfTruth(
  private val cardDao: CardDao,
) : SourceOfTruth<CardKey, CardResponse, CardResponse> {
  override fun reader(key: CardKey): Flow<CardResponse?> {
    return when (key) {
      is CardKey.Id ->
        cardDao
          .observe(key.id)
          .map { CardResponse.Single(it) }

      is CardKey.Ids ->
        cardDao
          .observe(key.ids)
          .map { CardResponse.Multiple(it) }
    }
  }

  override suspend fun write(key: CardKey, value: CardResponse) {
    when (value) {
      is CardResponse.Single -> cardDao.insert(value.card)
      is CardResponse.Multiple -> cardDao.insert(value.cards)
    }
  }

  override suspend fun delete(key: CardKey) {
    when (key) {
      is CardKey.Id -> cardDao.delete(key.id)
      is CardKey.Ids -> cardDao.delete(key.ids)
    }
  }

  override suspend fun deleteAll() {
    cardDao.deleteAll()
  }
}

sealed interface CardKey {
  data class Id(val id: String) : CardKey
  data class Ids(val ids: List<String>) : CardKey
}

sealed interface CardResponse {
  data class Single(val card: Card) : CardResponse
  data class Multiple(val cards: List<Card>) : CardResponse
}
