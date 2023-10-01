package app.deckbox.common.compose.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.EditOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.lyricist.LocalStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditingAppBar(
  onExitClick: () -> Unit,
  scrollBehavior: TopAppBarScrollBehavior,
  modifier: Modifier = Modifier,
  containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
) {
  LargeTopAppBar(
    title = {
      Text(LocalStrings.current.builderEditingTitle)
    },
    navigationIcon = {
      IconButton(
        onClick = onExitClick,
      ) {
        Icon(Icons.Rounded.Close, contentDescription = null)
      }
    },
    actions = {
      IconButton(
        onClick = onExitClick,
      ) {
        Icon(
          Icons.Rounded.EditOff,
          contentDescription = null,
        )
      }
    },
    scrollBehavior = scrollBehavior,
    modifier = modifier,
    colors = TopAppBarDefaults.largeTopAppBarColors(
      containerColor = containerColor,
    )
  )
}
