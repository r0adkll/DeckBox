package app.deckbox.ui.filter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import app.deckbox.core.model.Expansion
import app.deckbox.core.model.Format
import app.deckbox.core.model.SearchFilter
import app.deckbox.expansions.ExpansionsRepository
import app.deckbox.features.cards.public.RarityStore
import app.deckbox.features.cards.public.SubtypeStore
import app.deckbox.ui.filter.spec.AttackCostFilterSpec
import app.deckbox.ui.filter.spec.AttackDamageFilterSpec
import app.deckbox.ui.filter.spec.AttributeFilterSpec
import app.deckbox.ui.filter.spec.ExpansionsFilterSpec
import app.deckbox.ui.filter.spec.FilterSpec
import app.deckbox.ui.filter.spec.HpFilterSpec
import app.deckbox.ui.filter.spec.RarityFilterSpec
import app.deckbox.ui.filter.spec.RetreatCostFilterSpec
import app.deckbox.ui.filter.spec.TypeFilterSpec
import app.deckbox.ui.filter.state.rememberSearchFilter
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Inject

@Inject
class BrowseFilterPresenter(
  private val expansionsRepository: ExpansionsRepository,
  private val rarityStore: RarityStore,
  private val subtypeStore: SubtypeStore,
) : FilterPresenter(
  specs = listOf(
    TypeFilterSpec(
      title = "Type",
      getter = { types },
      setter = { copy(types = it) },
    ),
    AttributeFilterSpec,
    ExpansionsFilterSpec,
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
  getExpansions = { expansionsRepository.getExpansions() },
  getRarities = { rarityStore.get() },
  getSubtypes = { subtypeStore.get() },
) {

  /**
   * There is a bug where Composable functions inside an interface (or abstract class/superclass)
   * will crash on iOS for "No function found for symbol" due to some linkage error. The workaround is
   * to propagate the function call up to the subclasses and call its super.
   *
   * https://github.com/JetBrains/compose-multiplatform/issues/3318
   */
  @Composable
  override fun present(key: String, initialFilter: SearchFilter): FilterUiState {
    return super.present(key, initialFilter)
  }
}

open class FilterPresenter(
  private val specs: List<FilterSpec>,
  private val getExpansions: suspend () -> List<Expansion>,
  private val getRarities: suspend () -> List<String>,
  private val getSubtypes: suspend () -> List<String>,
) {

  @Composable
  open fun present(
    key: String,
  ): FilterUiState {
    return present(key, SearchFilter())
  }

  @Composable
  open fun present(
    key: String,
    initialFilter: SearchFilter,
  ): FilterUiState {
    val fetchExpansions by rememberUpdatedState(getExpansions)
    val expansions by remember(fetchExpansions) {
      flow { emit(fetchExpansions()) }
    }.collectAsState(emptyList())

    val fetchRarities by rememberUpdatedState(getRarities)
    val rarities by remember(fetchRarities) {
      flow { emit(fetchRarities()) }
    }.collectAsState(emptyList())

    val fetchSubtypes by rememberUpdatedState(getSubtypes)
    val subtypes by remember(fetchSubtypes) {
      flow { emit(fetchSubtypes()) }
    }.collectAsState(emptyList())

    var visibleFormat by remember { mutableStateOf(Format.STANDARD) }

    var filter by rememberSearchFilter(
      key = key,
      initial = initialFilter,
    )

    return FilterUiState(
      specs = specs,
      expansions = expansions,
      rarities = rarities,
      subtypes = subtypes,
      visibleExpansionFormat = visibleFormat,
      filter = filter,
    ) { event ->
      when (event) {
        FilterUiEvent.ClearFilter -> {
          filter = SearchFilter()
        }
        is FilterUiEvent.FilterChange -> {
          filter = event.action.applyToFilter(expansions, filter)
        }
        is FilterUiEvent.ChangeVisibleExpansions -> visibleFormat = event.format
      }
    }
  }
}
