package app.deckbox.ui.filter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FilterAltOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import app.deckbox.common.compose.widgets.SearchBarElevation
import app.deckbox.common.compose.widgets.SearchBarHeight
import app.deckbox.common.compose.widgets.SearchBarPadding
import com.moriatsushi.insetsx.statusBars

enum class FilterState {
  VISIBLE,
  HIDDEN,
}

@Composable
fun SearchBarWithFilter(
  filterState: FilterState,
  onClose: () -> Unit,
  onClear: () -> Unit,
  modifier: Modifier = Modifier,
  searchBar: @Composable () -> Unit,
  filterTitle: @Composable () -> Unit,
  filter: @Composable () -> Unit,
) {
  Box(
    modifier = modifier,
  ) {
    searchBar()

    val alpha by animateFloatAsState(
      targetValue = when (filterState) {
        FilterState.VISIBLE -> 1f
        FilterState.HIDDEN -> 0f
      },
    )

    val zIndex by animateFloatAsState(
      targetValue = when (filterState) {
        FilterState.VISIBLE -> 2f
        FilterState.HIDDEN -> 0f
      },
    )

    val shapePercent by animateIntAsState(
      targetValue = when (filterState) {
        FilterState.VISIBLE -> 0
        FilterState.HIDDEN -> 50
      },
    )

    val paddingHorizontal by animateDpAsState(
      targetValue = when (filterState) {
        FilterState.VISIBLE -> 0.dp
        FilterState.HIDDEN -> 16.dp
      },
    )

    val contentPaddingTopPx by animateIntAsState(
      targetValue = when (filterState) {
        FilterState.VISIBLE -> WindowInsets.statusBars.getTop(LocalDensity.current)
        FilterState.HIDDEN -> 0
      },
    )

    val marginPaddingTopPx by animateIntAsState(
      targetValue = when (filterState) {
        FilterState.VISIBLE -> 0
        FilterState.HIDDEN -> WindowInsets.statusBars.getTop(LocalDensity.current)
      },
    )

    FilterTopBar(
      shape = RoundedCornerShape(shapePercent),
      leading = {
        IconButton(
          onClick = onClose,
        ) {
          Icon(Icons.Rounded.Close, contentDescription = null)
        }
      },
      trailing = {
        IconButton(
          onClick = onClear,
        ) {
          Icon(Icons.Rounded.FilterAltOff, contentDescription = null)
        }
      },
      title = filterTitle,
      contentPadding = PaddingValues(top = with(LocalDensity.current) { contentPaddingTopPx.toDp() }),
      modifier = Modifier
        .padding(top = with(LocalDensity.current) { marginPaddingTopPx.toDp() })
        .padding(horizontal = paddingHorizontal)
        .zIndex(zIndex)
        .alpha(alpha),
    )

    AnimatedVisibility(
      visible = filterState == FilterState.VISIBLE,
      enter = expandVertically(
        expandFrom = Alignment.Top,
      ) + fadeIn(),
      exit = shrinkVertically(
        shrinkTowards = Alignment.Top,
      ) + fadeOut(),
      modifier = Modifier
        .windowInsetsPadding(WindowInsets.statusBars)
        .padding(
          top = SearchBarHeight,
        ),
    ) {
      Box(
        modifier = Modifier
          .background(MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)),
      ) {
        filter()
      }
    }
  }
}

@Composable
private fun FilterTopBar(
  shape: Shape,
  contentPadding: PaddingValues,
  modifier: Modifier = Modifier,
  leading: @Composable () -> Unit,
  trailing: @Composable () -> Unit,
  title: @Composable () -> Unit,
) {
  Row(
    modifier = modifier
      .background(
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(SearchBarElevation),
        shape = shape,
      )
      .padding(contentPadding)
      .height(SearchBarHeight),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    CompositionLocalProvider(
      LocalContentColor provides MaterialTheme.colorScheme.onSurface,
    ) {
      leading()
    }
    Spacer(Modifier.width(SearchBarPadding))

    Box(
      modifier = Modifier.weight(1f),
      contentAlignment = Alignment.CenterStart,
    ) {
      CompositionLocalProvider(
        LocalTextStyle provides MaterialTheme.typography.titleLarge,
        LocalContentColor provides MaterialTheme.colorScheme.onSurface,
      ) {
        title()
      }
    }

    Box(
      modifier = Modifier.size(SearchBarHeight),
      contentAlignment = Alignment.Center,
    ) {
      CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant,
      ) {
        trailing()
      }
    }
  }
}
