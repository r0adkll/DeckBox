package app.deckbox.features.cards.public

import app.deckbox.core.model.Card
import app.deckbox.core.model.Expansion

interface ExpansionCardRepository {

  suspend fun getCards(expansion: Expansion): List<Card>
}
