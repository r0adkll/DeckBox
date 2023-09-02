package app.deckbox.features.boosterpacks.ui.list.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Deck
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import app.deckbox.common.compose.icons.outline.BoosterPack
import app.deckbox.common.compose.icons.rounded.AddDeck
import app.deckbox.common.compose.icons.rounded.Duplicate
import app.deckbox.common.compose.icons.rounded.Experiment
import app.deckbox.common.compose.widgets.CardHeader
import app.deckbox.common.compose.widgets.DeleteConfirmation
import app.deckbox.common.compose.widgets.OutlinedIconButton
import app.deckbox.common.compose.widgets.SizedIcon
import app.deckbox.common.compose.widgets.TonalIcon
import app.deckbox.core.extensions.ifNullOrBlank
import app.deckbox.core.extensions.readableFormat
import app.deckbox.core.model.BoosterPack
import cafe.adriel.lyricist.LocalStrings
import com.seiko.imageloader.rememberImagePainter

@Composable
internal fun BoosterPackItem(
  pack: BoosterPack,
  onClick: () -> Unit,
  onDelete: () -> Unit,
  onDuplicate: () -> Unit,
  modifier: Modifier = Modifier,
  interactionSource: InteractionSource = remember { MutableInteractionSource() },
) {
  Card(
    modifier = modifier
      .clickable(onClick = onClick),
  ) {
    CardHeader(
      leading = {
        TonalIcon(
          Icons.Outlined.BoosterPack,
          contentDescription = null,
          containerColor = MaterialTheme.colorScheme.secondaryContainer,
          modifier = Modifier.padding(top = 4.dp),
        )
      },
      title = {
        Text(
          text = pack.name.ifNullOrBlank { LocalStrings.current.deckDefaultNoName },
          style = MaterialTheme.typography.titleLarge,
        )
      },
      subtitle = {
        Text(LocalStrings.current.deckLastUpdated(pack.updatedAt.readableFormat))
      },
    )

    ImageSlice(pack.cardImages)

    Actions(
      onDelete = onDelete,
      onDuplicate = onDuplicate,
      interactionSource = interactionSource,
    )
  }
}

@Composable
private fun ImageSlice(
  images: List<String>,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .padding(
        horizontal = 16.dp,
        vertical = 4.dp,
      )
      .clip(RoundedCornerShape(16.dp))
      .horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    images.forEach { url ->
      val painter = key(url) { rememberImagePainter(url) }

      val width = 90.dp
      val height = 150.dp
      Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .width(width)
          .height(height)
          .clip(RoundedCornerShape(16.dp)),
      )
    }
  }
}

@Composable
private fun Actions(
  modifier: Modifier = Modifier,
  onDelete: () -> Unit,
  onDuplicate: () -> Unit,
  interactionSource: InteractionSource = remember { MutableInteractionSource() },
) {
  var showDeleteConfirmation by remember { mutableStateOf(false) }

  val isDragged by interactionSource.collectIsDraggedAsState()
  LaunchedEffect(isDragged, showDeleteConfirmation) {
    if (showDeleteConfirmation && isDragged) {
      showDeleteConfirmation = false
    }
  }

  Box(modifier = modifier) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(
          horizontal = 16.dp,
          vertical = 16.dp,
        ),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {

      OutlinedIconButton(
        onClick = { },
        colors = ButtonDefaults.outlinedButtonColors(
          contentColor = MaterialTheme.colorScheme.secondary,
        ),
        border = BorderStroke(
          width = 1.dp,
          color = MaterialTheme.colorScheme.secondary,
        ),
        icon = {
          SizedIcon(
            Icons.Rounded.AddDeck,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
          )
        },
        label = { Text("Add to deck") },
      )

      Spacer(Modifier.weight(1f))

      IconButton(
        onClick = onDuplicate,
      ) {
        Icon(
          Icons.Rounded.Duplicate,
          contentDescription = LocalStrings.current.deckActionDuplicateButtonContentDescription,
        )
      }

      IconButton(
        onClick = { showDeleteConfirmation = true },
      ) {
        Icon(Icons.Rounded.Delete, contentDescription = LocalStrings.current.deckActionDeleteButtonContentDescription)
      }
    }

    DeleteConfirmation(
      visible = showDeleteConfirmation,
      onDelete = onDelete,
      onCancel = { showDeleteConfirmation = false },
      modifier = Modifier.zIndex(1f),
    )
  }
}
