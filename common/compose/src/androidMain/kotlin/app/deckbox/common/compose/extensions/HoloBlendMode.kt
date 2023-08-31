package app.deckbox.common.compose.extensions

import android.os.Build
import androidx.compose.ui.graphics.BlendMode

actual val HoloBlendMode: BlendMode
  get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    BlendMode.ColorDodge
  } else {
    BlendMode.Overlay
  }
