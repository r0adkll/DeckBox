package app.deckbox.decks.impl.validation.rules

import app.deckbox.core.model.Card
import app.deckbox.core.model.Stacked
import app.deckbox.core.model.SuperType
import app.deckbox.decks.impl.validation.invalid
import app.deckbox.decks.impl.validation.success
import app.deckbox.features.decks.api.validation.Validation

object BasicRule : Rule {
  override val name: String get() = "basic-rule"

  override fun check(cards: List<Stacked<Card>>): Validation {
    val hasBasicCard = cards.any {
      it.card.supertype == SuperType.POKEMON && it.card.evolvesFrom == null
    }

    return if (hasBasicCard) {
      success()
    } else {
      invalid("A deck must contain at least one Basic Pokémon card")
    }
  }
}
