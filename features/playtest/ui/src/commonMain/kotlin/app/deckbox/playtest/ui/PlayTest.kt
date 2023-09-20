package app.deckbox.playtest.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.deckbox.common.screens.PlayTestScreen
import app.deckbox.core.di.MergeActivityScope
import com.r0adkll.kotlininject.merge.annotations.CircuitInject

@Suppress("UNUSED_PARAMETER")
@CircuitInject(MergeActivityScope::class, PlayTestScreen::class)
@Composable
fun PlayTest(
  state: PlayTestUiState,
  modifier: Modifier = Modifier,
) {
  TODO("Add your screen UI code here")
}
