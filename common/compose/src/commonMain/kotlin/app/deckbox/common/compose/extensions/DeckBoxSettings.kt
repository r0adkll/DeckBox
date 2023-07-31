package app.deckbox.common.compose.extensions

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import app.deckbox.common.settings.DeckBoxSettings

@Composable
fun DeckBoxSettings.shouldUseDarkColors(): Boolean {
  val themePreference = remember { observeTheme() }.collectAsState(initial = theme)
  return when (themePreference.value) {
    DeckBoxSettings.Theme.LIGHT -> false
    DeckBoxSettings.Theme.DARK -> true
    else -> isSystemInDarkTheme()
  }
}

@Composable
fun DeckBoxSettings.shouldUseDynamicColors(): Boolean {
  return remember { observeUseDynamicColors() }
    .collectAsState(initial = useDynamicColors)
    .value
}
