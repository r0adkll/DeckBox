package app.deckbox.common.compose.extensions

import androidx.compose.ui.Modifier

inline fun Modifier.thenIf(
  condition: Boolean,
  whenFalse: Modifier.() -> Modifier = { this },
  whenTrue: Modifier.() -> Modifier,
): Modifier = if (condition) whenTrue() else whenFalse()
