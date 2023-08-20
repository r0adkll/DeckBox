package app.deckbox.ui.filter.spec

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.deckbox.core.model.Expansion
import app.deckbox.core.model.SearchFilter
import app.deckbox.core.model.Type
import app.deckbox.ui.filter.FilterUiEvent
import app.deckbox.ui.filter.FilterUiEvent.FilterChange
import app.deckbox.ui.filter.FilterUiState
import app.deckbox.ui.filter.spec.TypeFilterAction.AddType
import app.deckbox.ui.filter.spec.TypeFilterAction.RemoveType
import app.deckbox.ui.filter.widgets.TypePicker

class TypeFilterSpec(
  override val title: String,
  private val getter: SearchFilter.() -> Set<Type>,
  private val setter: SearchFilter.(Set<Type>) -> SearchFilter,
) : FilterSpec() {

  override fun LazyListScope.buildContent(
    uiState: FilterUiState,
    actionEmitter: (FilterUiEvent) -> Unit,
  ) {
    item {
      TypePicker(
        values = getter(uiState.filter),
        onSelected = { type ->
          actionEmitter(FilterChange(AddType(type, getter, setter)))
        },
        onUnselected = { type ->
          actionEmitter(FilterChange(RemoveType(type, getter, setter)))
        },
        modifier = Modifier.padding(
          start = 16.dp,
          end = 16.dp,
          bottom = 8.dp,
        ),
      )
    }
  }
}

sealed interface TypeFilterAction : FilterAction {

  data class AddType(
    val type: Type,
    val getter: SearchFilter.() -> Set<Type>,
    val setter: SearchFilter.(Set<Type>) -> SearchFilter,
  ) : TypeFilterAction {
    override fun applyToFilter(
      expansions: List<Expansion>,
      filter: SearchFilter,
    ): SearchFilter {
      return filter.setter(getter(filter).plus(type))
    }
  }

  data class RemoveType(
    val type: Type,
    val getter: SearchFilter.() -> Set<Type>,
    val setter: SearchFilter.(Set<Type>) -> SearchFilter,
  ) : TypeFilterAction {
    override fun applyToFilter(
      expansions: List<Expansion>,
      filter: SearchFilter,
    ): SearchFilter {
      return filter.setter(getter(filter).minus(type))
    }
  }
}
