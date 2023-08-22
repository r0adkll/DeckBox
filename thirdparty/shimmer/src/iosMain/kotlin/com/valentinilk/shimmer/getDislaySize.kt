package com.valentinilk.shimmer

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.UIKit.UIScreen

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun getDisplaySize(): Size {
  return UIScreen.mainScreen.bounds.useContents {
    Size(
      width = size.width.toFloat(),
      height = size.height.toFloat(),
    )
  }
}
