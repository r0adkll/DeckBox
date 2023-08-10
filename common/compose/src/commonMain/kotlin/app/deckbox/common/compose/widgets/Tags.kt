package app.deckbox.common.compose.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.widgets.TagStyle.Filled
import app.deckbox.common.compose.widgets.TagStyle.Outline
import app.deckbox.core.extensions.fluentIf

enum class TagStyle {
  Filled,
  Outline,
}

data class Tag(
  val text: String,
  val style: TagStyle = Filled,
)

@Composable
fun Tags(
  tags: List<String>,
  modifier: Modifier = Modifier,
) {
  TagGroup(
    tags = tags.map { Tag(it) },
    modifier = modifier
      .padding(16.dp),
  )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagGroup(
  tags: List<Tag>,
  modifier: Modifier = Modifier,
) {
  FlowRow(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    tags.forEach { tag ->
      TagChip(
        style = tag.style,
        modifier = Modifier.padding(top = 8.dp),
      ) {
        Text(tag.text)
      }
    }
  }
}

@Composable
fun TagChip(
  style: TagStyle,
  modifier: Modifier = Modifier,
  shape: Shape = RoundedCornerShape(8.dp),
  content: @Composable BoxScope.() -> Unit,
) {
  val color = when (style) {
    Filled -> MaterialTheme.colorScheme.secondaryContainer
    Outline -> MaterialTheme.colorScheme.surface
  }

  Box(
    modifier = modifier
      .background(
        color = color,
        shape = shape,
      )
      .fluentIf(style == Outline) {
        border(
          width = 1.dp,
          color = MaterialTheme.colorScheme.onSurface,
          shape = shape,
        )
      }
      .padding(
        horizontal = 16.dp,
        vertical = 6.dp,
      ),
  ) {
    CompositionLocalProvider(
      LocalTextStyle provides MaterialTheme.typography.labelLarge,
    ) {
      content()
    }
  }
}
