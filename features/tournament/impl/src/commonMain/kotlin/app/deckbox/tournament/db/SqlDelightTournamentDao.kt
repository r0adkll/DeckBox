package app.deckbox.tournament.db

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.deckbox.DeckBoxDatabase
import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.logging.bark
import app.deckbox.sqldelight.Deck_list_card_join
import app.deckbox.tournament.api.model.DeckList
import app.deckbox.tournament.api.model.Participant
import app.deckbox.tournament.api.model.Tournament
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@ContributesBinding(MergeAppScope::class)
@Inject
class SqlDelightTournamentDao(
  private val database: DeckBoxDatabase,
  private val dispatcherProvider: DispatcherProvider,
) : TournamentDao {

  override suspend fun getTournaments(): List<Tournament>? = withContext(dispatcherProvider.databaseRead) {
    database.tournamentQueries.getAll()
      .awaitAsList()
      .map { it.toEntity() }
      .also {
        bark { "DB::getTournaments(): $it" }
      }
      .ifEmpty { null }
  }

  override suspend fun getParticipants(tournamentId: String): List<Participant>? {
    return withContext(dispatcherProvider.databaseRead) {
      database.participantQueries
        .getByTournament(tournamentId)
        .awaitAsList()
        .map { it.toEntity() }
        .ifEmpty { null }
    }
  }

  override suspend fun getDeckList(id: String): DeckList? = withContext(dispatcherProvider.databaseRead) {
    database.transactionWithResult {
      val deckListModel = database.deckListQueries
        .getById(id)
        .executeAsOneOrNull() ?: return@transactionWithResult null

      val cardModels = database.deckListCardJoinQueries
        .getCardsForDeckList(id)
        .executeAsList()

      deckListModel.toEntity(cardModels)
    }
  }

  override suspend fun insertTournaments(tournaments: List<Tournament>) {
    withContext(dispatcherProvider.databaseWrite) {
      database.transaction {
        tournaments.forEach { tournament ->
          database.tournamentQueries.insert(tournament.toModel())
        }
      }
    }
  }

  override suspend fun insertParticipants(tournamentId: String, participants: List<Participant>) {
    withContext(dispatcherProvider.databaseWrite) {
      database.transaction {
        participants.forEach { participant ->
          database.participantQueries.insert(participant.toModel(tournamentId))
        }
      }
    }
  }

  override suspend fun insertDeckList(deckList: DeckList) {
    withContext(dispatcherProvider.databaseWrite) {
      database.transaction {
        // Insert the deck model
        database.deckListQueries.insert(deckList.toModel())

        // Inset the cards
        deckList.cards.forEach { card ->
          database.deckListCardQueries.insert(card.toModel())
          database.deckListCardJoinQueries.insert(
            Deck_list_card_join(
              deckId = deckList.id,
              cardId = card.key,
              count = card.count,
            ),
          )
        }
      }
    }
  }
}
