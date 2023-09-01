package app.deckbox.decks.impl.validation.rules

import app.deckbox.core.model.Card
import app.deckbox.core.model.Stacked
import app.deckbox.decks.impl.validation.invalid
import app.deckbox.decks.impl.validation.success
import app.deckbox.features.decks.api.validation.Validation

object SizeRule : Rule {
  override val name: String = "size-rule"

  override fun check(cards: List<Stacked<Card>>): Validation {
    return if (cards.sumOf { it.count } == MAX_SIZE) {
      success()
    } else {
      invalid("A deck must contain exactly 60 cards")
    }
  }

  private const val MAX_SIZE = 60
}
