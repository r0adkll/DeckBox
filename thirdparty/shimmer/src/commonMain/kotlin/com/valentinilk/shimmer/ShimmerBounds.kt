package com.valentinilk.shimmer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size

@Composable
expect fun getDisplaySize(): Size

@Composable
internal fun rememberShimmerBounds(
  shimmerBounds: ShimmerBounds,
): Rect? {
  val displaySize = getDisplaySize()
  return remember(shimmerBounds, displaySize) {
    when (shimmerBounds) {
      ShimmerBounds.Window -> Rect(
        0f,
        0f,
        displaySize.width,
        displaySize.height,
      )

      ShimmerBounds.Custom -> Rect.Zero
      ShimmerBounds.View -> null
    }
  }
}

sealed class ShimmerBounds {
  object Custom : ShimmerBounds()
  object View : ShimmerBounds()
  object Window : ShimmerBounds()
}
