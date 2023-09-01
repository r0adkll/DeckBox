package app.deckbox.decks.impl.validation.rules

import app.deckbox.core.model.Card
import app.deckbox.core.model.Stacked
import app.deckbox.features.decks.api.validation.Validation

interface Rule {
  val name: String

  /**
   * Check if the list of cards, the current representation of a deck, is valid for this implementation of
   * the rule
   *
   * @param cards the list of cards in a deck to check/validate
   * @return the validation result
   */
  fun check(cards: List<Stacked<Card>>): Validation
}
