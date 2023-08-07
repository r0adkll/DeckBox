package app.deckbox.common.compose

import androidx.compose.runtime.Composable

@Composable
expect fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit)
