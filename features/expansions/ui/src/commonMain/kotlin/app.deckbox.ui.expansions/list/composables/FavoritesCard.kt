package app.deckbox.ui.expansions.list.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.widgets.CardHeader
import cafe.adriel.lyricist.LocalStrings

@Composable
fun FavoritesCard(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val shape = RoundedCornerShape(12.dp)
  Surface(
    modifier = modifier
      .clip(shape)
      .clickable(onClick = onClick),
    color = MaterialTheme.colorScheme.tertiaryContainer,
    shape = shape,
  ) {
    CardHeader(
      title = { Text(LocalStrings.current.favorites) },
      subtitle = {},
      leading = {
        Box(
          modifier = Modifier,
          contentAlignment = Alignment.Center,
        ) {
          Icon(
            Icons.Rounded.Favorite,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary,
          )
        }
      },
    )
  }
}
