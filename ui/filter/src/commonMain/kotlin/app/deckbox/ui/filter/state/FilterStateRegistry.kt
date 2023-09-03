package app.deckbox.ui.filter.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import app.deckbox.core.model.SearchFilter

val LocalFilterStateRegistry = compositionLocalOf { FilterStateRegistry() }

class FilterStateRegistry {

  private val states = mutableMapOf<String, SearchFilter>()

  fun getState(key: String, defaultValue: SearchFilter = SearchFilter()): SearchFilter {
    return states[key] ?: run {
      states[key] = defaultValue
      defaultValue
    }
  }

  fun updateState(key: String, state: SearchFilter) {
    states[key] = state
  }
}

@Composable
fun rememberSearchFilter(
  key: String,
  initial: SearchFilter = SearchFilter(),
) : MutableState<SearchFilter> {
  val registry = LocalFilterStateRegistry.current

  val state = remember(key) { mutableStateOf(registry.getState(key, initial)) }
  LaunchedEffect(state.value) {
    registry.updateState(key, state.value)
  }

  return state
}
