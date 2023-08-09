package app.deckbox.features.decks.public

import app.deckbox.core.model.Deck
import kotlinx.coroutines.flow.Flow

interface DeckRepository {

  fun observeDecks(): Flow<List<Deck>>
}
