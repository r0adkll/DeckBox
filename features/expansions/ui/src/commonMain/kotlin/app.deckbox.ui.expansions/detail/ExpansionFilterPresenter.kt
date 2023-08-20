package app.deckbox.ui.expansions.detail

import app.deckbox.core.model.Card
import app.deckbox.ui.filter.FilterPresenter
import app.deckbox.ui.filter.spec.AttackCostFilterSpec
import app.deckbox.ui.filter.spec.AttackDamageFilterSpec
import app.deckbox.ui.filter.spec.AttributeFilterSpec
import app.deckbox.ui.filter.spec.HpFilterSpec
import app.deckbox.ui.filter.spec.RarityFilterSpec
import app.deckbox.ui.filter.spec.RetreatCostFilterSpec
import app.deckbox.ui.filter.spec.TypeFilterSpec

class ExpansionFilterPresenter(
  private val cards: List<Card>
) : FilterPresenter(
  specs = listOf(
    TypeFilterSpec(
      title = "Type",
      getter = { types },
      setter = { copy(types = it) },
    ),
    AttributeFilterSpec,
    RarityFilterSpec,
    HpFilterSpec,
    AttackDamageFilterSpec,
    AttackCostFilterSpec,
    RetreatCostFilterSpec,
    TypeFilterSpec(
      title = "Weaknesses",
      getter = { weaknesses },
      setter = { copy(weaknesses = it) },
    ),
    TypeFilterSpec(
      title = "Resistances",
      getter = { resistances },
      setter = { copy(resistances = it) },
    ),
  ),
  getExpansions = { emptyList() },
  getRarities = { cards.mapNotNull { it.rarity }.toSet().toList() },
  getSubtypes = { cards.flatMap { it.subtypes }.toSet().toList() },
)
