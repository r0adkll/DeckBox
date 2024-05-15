package app.deckbox.decks.impl.validation.rules

import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.model.Card
import app.deckbox.core.model.Stacked
import app.deckbox.decks.impl.validation.invalid
import app.deckbox.decks.impl.validation.success
import app.deckbox.features.decks.api.validation.Validation
import com.r0adkll.kotlininject.merge.annotations.ContributesMultibinding

@ContributesMultibinding(MergeAppScope::class)
object AceSpecRule : Rule {
  override val name: String get() = "ace-spec-rule"

  override fun check(cards: List<Stacked<Card>>): Validation {
    val numRadiantPokemon = cards.sumOf {
      if (it.card.subtypes.contains(SUBTYPE_ACESPEC)) {
        it.count
      } else {
        0
      }
    }

    return if (numRadiantPokemon > ACE_SPEC_LIMIT) {
      invalid("You can't have more than 1 Ace Spec Trainer in your deck")
    } else {
      success()
    }
  }
}

private const val ACE_SPEC_LIMIT = 1
private const val SUBTYPE_ACESPEC = "ACE SPEC"
