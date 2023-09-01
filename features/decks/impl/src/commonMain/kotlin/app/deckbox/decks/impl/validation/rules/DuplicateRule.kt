package app.deckbox.decks.impl.validation.rules

import app.deckbox.core.model.Card
import app.deckbox.core.model.Stacked
import app.deckbox.core.model.SuperType
import app.deckbox.features.decks.api.validation.Validation
import app.deckbox.decks.impl.validation.invalid
import app.deckbox.decks.impl.validation.success

object DuplicateRule : Rule {
  override val name: String get() = "duplicate-rule"

  override fun check(cards: List<Stacked<Card>>): Validation {
    val grouped = cards.groupBy { it.card.name }
    val hasViolation = grouped.any { (_, value) ->
      val totalCount = value.sumOf { it.count }
      totalCount > MAX_COUNT &&
        !(value.first().card.supertype == SuperType.ENERGY && value.first().isBasic())
    }
    return if (hasViolation) {
      invalid("You can only have 4 cards of the same name")
    } else {
      success()
    }
  }

  fun Stacked<Card>.isBasic(): Boolean {
    return card.subtypes.contains("Basic") ||
      (card.name.contains("Basic", ignoreCase = true) && card.name.contains("Energy", ignoreCase = true))
  }

  private const val MAX_COUNT = 4
}
