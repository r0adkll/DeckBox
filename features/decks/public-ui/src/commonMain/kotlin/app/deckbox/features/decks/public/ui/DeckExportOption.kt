package app.deckbox.features.decks.public.ui

import androidx.compose.runtime.Composable
import deckbox.features.decks.public_ui.generated.resources.Res
import deckbox.features.decks.public_ui.generated.resources.action_export_image
import deckbox.features.decks.public_ui.generated.resources.action_export_text
import org.jetbrains.compose.resources.stringResource

enum class DeckExportOption(val text: @Composable () -> String) {
  Text({ stringResource(Res.string.action_export_text) }),
  Image({ stringResource(Res.string.action_export_image) }),
}
