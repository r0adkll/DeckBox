package app.deckbox.tournament

import app.deckbox.core.di.MergeAppScope
import app.deckbox.tournament.api.TournamentRepository
import app.deckbox.tournament.api.model.DeckList
import app.deckbox.tournament.api.model.Participant
import app.deckbox.tournament.api.model.Tournament
import app.deckbox.tournament.db.TournamentDao
import app.deckbox.tournament.limitless.TournamentDataSource
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.FetcherResult
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse

@Inject
@ContributesBinding(MergeAppScope::class)
class StoreTournamentRepository(
  private val dataSource: TournamentDataSource,
  private val db: TournamentDao,
) : TournamentRepository {

  private val tournamentsStore = StoreBuilder.from(
    fetcher = Fetcher.ofResult { dataSource.getTournaments().asFetcherResult() },
    sourceOfTruth = SourceOfTruth.of(
      nonFlowReader = { db.getTournaments() },
      writer = { _, tournaments -> db.insertTournaments(tournaments) },
    ),
  ).build()

  private val participantsStore = StoreBuilder.from(
    fetcher = Fetcher.ofResult { tournamentId: String ->
      dataSource.getParticipants(tournamentId).asFetcherResult()
    },
    sourceOfTruth = SourceOfTruth.of(
      nonFlowReader = { tournamentId -> db.getParticipants(tournamentId) },
      writer = { tournamentId, participants -> db.insertParticipants(tournamentId, participants) },
    ),
  ).build()

  private val deckListStore = StoreBuilder.from(
    fetcher = Fetcher.ofResult { deckListId: String -> dataSource.getDeckList(deckListId).asFetcherResult() },
    sourceOfTruth = SourceOfTruth.of(
      nonFlowReader = { deckListId -> db.getDeckList(deckListId) },
      writer = { _, deckList -> db.insertDeckList(deckList) },
    ),
  ).build()

  override suspend fun getTournaments(): Result<List<Tournament>> {
    return tournamentsStore.stream(StoreReadRequest.cached(Unit, false))
      .filterNot { it is StoreReadResponse.Loading || it is StoreReadResponse.NoNewData }
      .first()
      .asResult()
  }

  override suspend fun getParticipants(tournamentId: String): Result<List<Participant>> {
    return participantsStore.stream(StoreReadRequest.cached(tournamentId, false))
      .filterNot { it is StoreReadResponse.Loading || it is StoreReadResponse.NoNewData }
      .first()
      .asResult()
  }

  override suspend fun getDeckList(deckListId: String): Result<DeckList> {
    return deckListStore.stream(StoreReadRequest.cached(deckListId, false))
      .filterNot { it is StoreReadResponse.Loading || it is StoreReadResponse.NoNewData }
      .first()
      .asResult()
  }

  private fun <T : Any> Result<T>.asFetcherResult(): FetcherResult<T> {
    return if (isSuccess) {
      FetcherResult.Data(getOrThrow())
    } else {
      FetcherResult.Error.Exception(exceptionOrNull()!!)
    }
  }

  private fun <T : Any> StoreReadResponse<T>.asResult(): Result<T> {
    return dataOrNull()
      ?.let { Result.success(it) }
      ?: Result.failure(IOException(errorMessageOrNull() ?: "Something went wrong"))
  }
}
