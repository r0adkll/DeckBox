package com.valentinilk.shimmer

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import java.awt.Toolkit

@Composable
actual fun getDisplaySize(): Size {
  val screenSize = Toolkit.getDefaultToolkit().screenSize
  val screenHeight = screenSize.height.toFloat()
  val screenWidth = screenSize.width.toFloat()
  return Size(screenWidth, screenHeight)
}
