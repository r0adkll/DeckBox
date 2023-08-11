package app.deckbox.common.compose.navigation

import androidx.compose.runtime.staticCompositionLocalOf

sealed interface DetailNavigation {
  object None : DetailNavigation
  object Active : DetailNavigation
}

/**
 * Composition local that screens can use to determine if they are in a panel detail
 * CircuitContent and should thus change its UI treatments accordingly
 */
val LocalDetailNavigation = staticCompositionLocalOf<DetailNavigation> {
  DetailNavigation.None
}
