package app.deckbox.ui.filter

import androidx.compose.runtime.Stable
import app.deckbox.core.model.Expansion
import app.deckbox.core.model.Format
import app.deckbox.core.model.SearchFilter
import app.deckbox.core.model.SuperType
import app.deckbox.ui.filter.spec.Attribute
import app.deckbox.ui.filter.spec.FilterAction
import app.deckbox.ui.filter.spec.FilterSpec
import com.slack.circuit.runtime.CircuitUiState

@Stable
data class FilterUiState(
  val specs: List<FilterSpec>,
  val filter: SearchFilter,
  val expansions: List<Expansion>,
  val visibleExpansionFormat: Format,
  val subtypes: List<String>,
  val rarities: List<String>,
  val eventSink: (FilterUiEvent) -> Unit,
) {

  val attributes: Set<Attribute> = setOf(
    Attribute.Supertype(SuperType.POKEMON),
    Attribute.Supertype(SuperType.TRAINER),
    Attribute.Supertype(SuperType.ENERGY),
    *subtypes.map { Attribute.Subtype(it) }.toTypedArray(),
  )

  val selectedAttributes: Set<Attribute> = setOf(
    *filter.superTypes.map { Attribute.Supertype(it) }.toTypedArray(),
    *filter.subTypes.map { Attribute.Subtype(it) }.toTypedArray(),
  )
}

sealed interface FilterUiEvent {
  object ClearFilter : FilterUiEvent
  class FilterChange(val action: FilterAction) : FilterUiEvent
  class ChangeVisibleExpansions(val format: Format) : FilterUiEvent
}
