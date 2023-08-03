package app.deckbox.ui.expansions.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.deckbox.common.screens.ExpansionDetailScreen
import app.deckbox.core.di.MergeActivityScope
import com.r0adkll.kotlininject.merge.annotations.CircuitInject

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(MergeActivityScope::class, ExpansionDetailScreen::class)
@Composable
internal fun ExpansionDetail(
  state: ExpansionDetailUiState,
  modifier: Modifier = Modifier,
) {
  Scaffold(
    modifier = modifier,
  ) {
    Box(
      modifier = Modifier.padding(it),
      contentAlignment = Alignment.Center
    ) {
      Text("Expansion Detail ID(${state})")
    }
  }
}
