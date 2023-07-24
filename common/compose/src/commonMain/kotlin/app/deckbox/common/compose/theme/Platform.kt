package app.deckbox.common.compose.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
internal expect fun colorScheme(
    useDarkColors: Boolean,
    useDynamicColors: Boolean,
): ColorScheme
