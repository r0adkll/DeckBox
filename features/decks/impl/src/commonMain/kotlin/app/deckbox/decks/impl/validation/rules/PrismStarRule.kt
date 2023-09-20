package app.deckbox.decks.impl.validation.rules

import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.model.Card
import app.deckbox.core.model.Stacked
import app.deckbox.decks.impl.validation.invalid
import app.deckbox.decks.impl.validation.success
import app.deckbox.features.decks.api.validation.Validation
import com.r0adkll.kotlininject.merge.annotations.ContributesMultibinding

@ContributesMultibinding(MergeAppScope::class)
object PrismStarRule : Rule {
  override val name: String = "prism-star-rule"

  override fun check(cards: List<Stacked<Card>>): Validation {
    val prismStarCards = cards.filter { it.card.name.contains(PRISM_SYMBOL) }

    return if (prismStarCards.any { it.count > MAX_COUNT }) {
      invalid("You can’t have more than 1 Prism Star card with the same name in your deck")
    } else {
      success()
    }
  }

  private const val PRISM_SYMBOL = "◇"
  private const val MAX_COUNT = 1
}
