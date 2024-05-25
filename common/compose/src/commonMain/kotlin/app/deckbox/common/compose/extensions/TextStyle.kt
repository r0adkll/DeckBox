package app.deckbox.common.compose.extensions

import androidx.compose.ui.text.TextStyle

fun TextStyle.alpha(alpha: Float): TextStyle = copy(color = color.copy(alpha = alpha))
