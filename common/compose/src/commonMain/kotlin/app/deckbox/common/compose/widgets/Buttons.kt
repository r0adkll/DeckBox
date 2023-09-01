package app.deckbox.common.compose.widgets

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier

@Composable
fun FilledTonalIconButton(
  onClick: () -> Unit,
  icon: @Composable () -> Unit,
  label: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
) {
  FilledTonalButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
  ) {
    CompositionLocalProvider(
      LocalIconSize provides ButtonDefaults.IconSize,
    ) {
      icon()
    }
    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
    label()
  }
}

@Composable
fun OutlinedIconButton(
  onClick: () -> Unit,
  icon: @Composable () -> Unit,
  label: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
) {
  OutlinedButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
  ) {
    CompositionLocalProvider(
      LocalIconSize provides ButtonDefaults.IconSize,
    ) {
      icon()
    }
    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
    label()
  }
}

@Composable
fun TextIconButton(
  onClick: () -> Unit,
  icon: @Composable () -> Unit,
  label: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
) {
  TextButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
  ) {
    CompositionLocalProvider(
      LocalIconSize provides ButtonDefaults.IconSize,
    ) {
      icon()
    }
    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
    label()
  }
}
