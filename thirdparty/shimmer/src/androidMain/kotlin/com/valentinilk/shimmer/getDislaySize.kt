package com.valentinilk.shimmer

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity

@Composable
actual fun getDisplaySize(): Size {
  return with (LocalDensity.current) {
    Size(
      width = LocalConfiguration.current.screenWidthDp.toDp().toPx(),
      height = LocalConfiguration.current.screenHeightDp.toDp().toPx(),
    )
  }
}
