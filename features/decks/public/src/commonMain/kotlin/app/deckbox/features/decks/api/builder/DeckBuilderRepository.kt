package app.deckbox.features.decks.api.builder

import app.deckbox.core.model.Deck
import kotlinx.coroutines.flow.Flow

interface DeckBuilderRepository {

  fun createSession(): String

  /**
   * Observe a deck editing session for a given sessionId. If null
   * is passed then a new session will be created.
   *
   * TODO: Not sure if I wanna use the [Deck] object here as the domain model
   *  for updating the current deck information
   *
   * @param deckId the id of the deck that is being actively edited
   * @return a [Flow] of the deck you are editing
   */
  fun observeSession(deckId: String): Flow<Deck>

  fun editName(deckId: String, name: String)
  fun editDescription(deckId: String, description: String)
  fun addTag(deckId: String, tag: String)
  fun removeTag(deckId: String, tag: String)
  fun incrementCard(deckId: String, cardId: String, amount: Int = 1)
  fun decrementCard(deckId: String, cardId: String, amount: Int = 1)
  fun removeCard(deckId: String, cardId: String)
  fun addBoosterPack(deckId: String, boosterPackId: String)
}
