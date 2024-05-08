package app.deckbox.tournament

import app.deckbox.core.di.MergeAppScope
import app.deckbox.tournament.api.TournamentRepository
import app.deckbox.tournament.api.model.DeckList
import app.deckbox.tournament.api.model.Participant
import app.deckbox.tournament.api.model.Tournament
import app.deckbox.tournament.limitless.TournamentDataSource
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import me.tatarka.inject.annotations.Inject

// TODO: Caching stuff
@Inject
@ContributesBinding(MergeAppScope::class)
class CachingTournamentRepository(
  private val dataSource: TournamentDataSource,
) : TournamentRepository {

  override suspend fun getTournaments(): Result<List<Tournament>> {
    return dataSource.getTournaments()
  }

  override suspend fun getParticipants(tournamentId: String): Result<List<Participant>> {
    return dataSource.getParticipants(tournamentId)
  }

  override suspend fun getDeckList(deckListId: String): Result<DeckList> {
    return dataSource.getDeckList(deckListId)
  }
}
