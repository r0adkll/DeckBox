package app.deckbox.tournament

import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.di.MergeAppScope
import app.deckbox.tournament.api.TournamentRepository
import app.deckbox.tournament.api.model.DeckList
import app.deckbox.tournament.api.model.Participant
import app.deckbox.tournament.api.model.Tournament
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import me.tatarka.inject.annotations.Inject

@Inject
@ContributesBinding(MergeAppScope::class)
class CachingTournamentRepository(
  private val dispatcherProvider: DispatcherProvider,
) : TournamentRepository {

  override suspend fun getTournaments(): List<Tournament> {
    TODO("Not yet implemented")
  }

  override suspend fun getParticipants(tournament: Tournament): List<Participant> {
    TODO("Not yet implemented")
  }

  override suspend fun getDeckList(tournament: Tournament, participant: Participant): DeckList {
    TODO("Not yet implemented")
  }
}
