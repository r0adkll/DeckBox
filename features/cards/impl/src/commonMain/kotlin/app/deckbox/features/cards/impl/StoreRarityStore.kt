package app.deckbox.features.cards.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.deckbox.DeckBoxDatabase
import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.di.MergeAppScope
import app.deckbox.features.cards.public.RarityStore
import app.deckbox.network.PokemonTcgApi
import app.deckbox.sqldelight.Rarities
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.impl.extensions.get

@ContributesBinding(MergeAppScope::class)
@Inject
class StoreRarityStore(
  private val pokemonTcgApi: PokemonTcgApi,
  private val db: DeckBoxDatabase,
  private val dispatcherProvider: DispatcherProvider,
) : RarityStore {

  @OptIn(ExperimentalCoroutinesApi::class)
  private val rarityStore = StoreBuilder
    .from(
      fetcher = Fetcher.ofResult { pokemonTcgApi.getRarities().asFetcherResult() },
      sourceOfTruth = SourceOfTruth.of(
        reader = {
          db.rarityQueries.getAll()
            .asFlow()
            .mapToList(dispatcherProvider.databaseRead)
            .mapLatest {
              it.ifEmpty { null }
            }
        },
        writer = { _, rarities ->
          db.transaction {
            rarities.map { Rarities(it) }.forEach {
              db.rarityQueries.insert(it)
            }
          }
        },
        deleteAll = { db.rarityQueries.deleteAll() },
      ),
    )
    .build()

  override suspend fun get(): List<String> {
    return rarityStore.get(Unit)
  }
}
