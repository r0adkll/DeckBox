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
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    modifier = modifier,
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
  val colors = when (style) {
    Filled -> TagDefaults.filledColors()
    Outline -> TagDefaults.outlinedColors()
  }

  Box(
    modifier = modifier
      .background(
        color = colors.containerColor,
        shape = shape,
      )
      .fluentIf(style == Outline) {
        border(
          width = 1.dp,
          color = colors.borderColor,
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
      LocalContentColor provides colors.contentColor,
    ) {
      content()
    }
  }
}

@Immutable
class TagColors internal constructor(
  val containerColor: Color,
  val contentColor: Color,
  val borderColor: Color,
)

object TagDefaults {

  @Composable
  fun filledColors(
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
    borderColor: Color = Color.Unspecified,
  ) = TagColors(containerColor, contentColor, borderColor)

  @Composable
  fun outlinedColors(
    containerColor: Color = Color.Transparent,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
    borderColor: Color = MaterialTheme.colorScheme.outline,
  ) = TagColors(containerColor, contentColor, borderColor)
}
