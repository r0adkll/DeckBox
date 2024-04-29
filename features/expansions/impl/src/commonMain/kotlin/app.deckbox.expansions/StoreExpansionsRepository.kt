package app.deckbox.expansions

import app.deckbox.core.di.AppScope
import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.logging.bark
import app.deckbox.core.model.Expansion
import app.deckbox.expansions.db.ExpansionsDao
import app.deckbox.network.PokemonTcgApi
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.FetcherResult
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.impl.extensions.get

@AppScope
@Inject
@ContributesBinding(MergeAppScope::class)
class StoreExpansionsRepository(
  expansionsDao: ExpansionsDao,
  api: PokemonTcgApi,
) : ExpansionsRepository {

  private val store = StoreBuilder
    .from(
      fetcher = ExpansionFetcher(api),
      sourceOfTruth = ExpansionSourceOfTruth(expansionsDao),
    )
    .build()

  override suspend fun getExpansion(id: String): Expansion {
    val response = store.get(ExpansionKey.ById(id)) as ExpansionResponse.Single
    return response.expansion
  }

  override suspend fun getExpansions(): List<Expansion> {
    val response = store.get(ExpansionKey.All) as ExpansionResponse.All
    return response.expansions
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  override fun observeExpansions(): Flow<List<Expansion>> {
    return store.stream(StoreReadRequest.cached(ExpansionKey.All, refresh = true))
      .flatMapLatest {
        bark { "${it.origin}, data: ${it.dataOrNull()}" }
        val data = it.dataOrNull()
        if (data != null) {
          flowOf((data as ExpansionResponse.All).expansions)
        } else {
          emptyFlow()
        }
      }
  }
}

class ExpansionFetcher(
  private val api: PokemonTcgApi,
) : Fetcher<ExpansionKey, ExpansionResponse> {
  override val name: String = "ExpansionFetcher"

  override fun invoke(key: ExpansionKey): Flow<FetcherResult<ExpansionResponse>> = flow {
    when (key) {
      is ExpansionKey.All -> api.getExpansions()
        .map { ExpansionResponse.All(it) }
        .asFetcherResult()
        .also { emit(it) }
      is ExpansionKey.ById -> api.getExpansion(key.id)
        .map { ExpansionResponse.Single(it) }
        .asFetcherResult()
        .also { emit(it) }
    }
  }

  override val fallback: Fetcher<ExpansionKey, ExpansionResponse>? = null
}

class ExpansionSourceOfTruth(
  private val expansionsDao: ExpansionsDao,
) : SourceOfTruth<ExpansionKey, ExpansionResponse, ExpansionResponse> {
  override suspend fun delete(key: ExpansionKey) {
    when (key) {
      ExpansionKey.All -> expansionsDao.deleteExpansions()
      is ExpansionKey.ById -> expansionsDao.deleteExpansion(key.id)
    }
  }

  override suspend fun deleteAll() {
    expansionsDao.deleteExpansions()
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  override fun reader(key: ExpansionKey): Flow<ExpansionResponse?> {
    return when (key) {
      ExpansionKey.All -> expansionsDao.observeExpansions()
        .map { ExpansionResponse.All(it) }
        .mapLatest {
          if (it.expansions.isEmpty()) null else it
        }
      is ExpansionKey.ById -> expansionsDao.observeExpansion(key.id)
        .onEach { bark { "Expansion Found($key): $it" } }
        .map { ExpansionResponse.Single(it) }
    }
  }

  override suspend fun write(key: ExpansionKey, value: ExpansionResponse) {
    when (value) {
      is ExpansionResponse.All -> expansionsDao.insertExpansions(value.expansions)
      is ExpansionResponse.Single -> expansionsDao.insertExpansions(listOf(value.expansion))
    }
  }
}

fun <T : Any> Result<T>.asFetcherResult(): FetcherResult<T> = when {
  isSuccess -> FetcherResult.Data(this.getOrThrow())
  else -> this.exceptionOrNull()?.let { FetcherResult.Error.Exception(it) } as? FetcherResult<T>
    ?: FetcherResult.Error.Message("Unable to fetch expansions") as FetcherResult<T>
}

sealed interface ExpansionKey {
  object All : ExpansionKey
  class ById(val id: String) : ExpansionKey
}

sealed interface ExpansionResponse {
  data class All(val expansions: List<Expansion>) : ExpansionResponse
  data class Single(val expansion: Expansion) : ExpansionResponse
}
