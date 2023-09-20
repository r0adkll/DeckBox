package app.deckbox.decks.impl.validation

import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.model.Card
import app.deckbox.core.model.Stacked
import app.deckbox.decks.impl.validation.rules.BasicRule
import app.deckbox.decks.impl.validation.rules.DuplicateRule
import app.deckbox.decks.impl.validation.rules.PrismStarRule
import app.deckbox.decks.impl.validation.rules.RadiantRule
import app.deckbox.decks.impl.validation.rules.Rule
import app.deckbox.decks.impl.validation.rules.SizeRule
import app.deckbox.features.decks.api.validation.DeckValidation
import app.deckbox.features.decks.api.validation.DeckValidator
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@ContributesBinding(MergeAppScope::class)
@Inject
class RuleDeckValidator(
  private val deckRules: Set<Rule>,
  private val dispatcherProvider: DispatcherProvider,
) : DeckValidator {

  override suspend fun validate(cards: List<Stacked<Card>>): DeckValidation {
    return withContext(dispatcherProvider.computation) {
      DeckValidation(
        isEmpty = cards.isEmpty(),
        deckRules.map { it.check(cards) },
      )
    }
  }
}
