package app.deckbox.features.decks.api.validation

import app.deckbox.core.model.Card
import app.deckbox.core.model.Stacked

interface DeckValidator {

  suspend fun validate(cards: List<Stacked<Card>>): DeckValidation
}
