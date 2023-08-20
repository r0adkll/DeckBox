package app.deckbox.ui.filter.spec

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.deckbox.core.model.Expansion
import app.deckbox.core.model.SearchFilter
import app.deckbox.core.model.SuperType
import app.deckbox.ui.filter.FilterUiEvent
import app.deckbox.ui.filter.FilterUiEvent.FilterChange
import app.deckbox.ui.filter.FilterUiState
import app.deckbox.ui.filter.spec.AttributeFilterAction.AddAttribute
import app.deckbox.ui.filter.spec.AttributeFilterAction.RemoveAttribute
import app.deckbox.ui.filter.widgets.AttributePicker

object AttributeFilterSpec : FilterSpec() {

  override val title: String = "Attributes"

  override fun LazyListScope.buildContent(
    uiState: FilterUiState,
    actionEmitter: (FilterUiEvent) -> Unit,
  ) {
    item {
      AttributePicker(
        values = uiState.attributes,
        selected = uiState.selectedAttributes,
        onSelected = {
          actionEmitter(FilterChange(AddAttribute(it)))
        },
        onUnselected = {
          actionEmitter(FilterChange(RemoveAttribute(it)))
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

sealed class Attribute(val displayText: String) {
  data class Supertype(val superType: SuperType) : Attribute(superType.displayName)
  data class Subtype(val subType: String) : Attribute(subType)
}

sealed interface AttributeFilterAction : FilterAction {

  data class AddAttribute(val attribute: Attribute) : AttributeFilterAction {
    override fun applyToFilter(
      expansions: List<Expansion>,
      filter: SearchFilter,
    ): SearchFilter {
      return when (attribute) {
        is Attribute.Subtype -> filter.copy(subTypes = filter.subTypes.plus(attribute.subType))
        is Attribute.Supertype -> filter.copy(superTypes = filter.superTypes.plus(attribute.superType))
      }
    }
  }

  data class RemoveAttribute(val attribute: Attribute) : AttributeFilterAction {
    override fun applyToFilter(
      expansions: List<Expansion>,
      filter: SearchFilter,
    ): SearchFilter {
      return when (attribute) {
        is Attribute.Subtype -> filter.copy(subTypes = filter.subTypes.minus(attribute.subType))
        is Attribute.Supertype -> filter.copy(superTypes = filter.superTypes.minus(attribute.superType))
      }
    }
  }
}
