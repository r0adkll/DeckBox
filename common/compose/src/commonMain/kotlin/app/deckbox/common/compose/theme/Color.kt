package app.deckbox.common.compose.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import app.deckbox.core.model.Type

val md_theme_light_primary = Color(0xFF006494)
val md_theme_light_onPrimary = Color(0xFFffffff)
val md_theme_light_primaryContainer = Color(0xFFc8e6ff)
val md_theme_light_onPrimaryContainer = Color(0xFF001e31)
val md_theme_light_secondary = Color(0xFF795900)
val md_theme_light_onSecondary = Color(0xFFffffff)
val md_theme_light_secondaryContainer = Color(0xFFffdf99)
val md_theme_light_onSecondaryContainer = Color(0xFF261a00)
val md_theme_light_tertiary = Color(0xFF65597b)
val md_theme_light_onTertiary = Color(0xFFffffff)
val md_theme_light_tertiaryContainer = Color(0xFFecdcff)
val md_theme_light_onTertiaryContainer = Color(0xFF201634)
val md_theme_light_error = Color(0xFFba1b1b)
val md_theme_light_errorContainer = Color(0xFFffdad4)
val md_theme_light_onError = Color(0xFFffffff)
val md_theme_light_onErrorContainer = Color(0xFF410001)
val md_theme_light_background = Color(0xFFfcfcff)
val md_theme_light_onBackground = Color(0xFF1a1c1e)
val md_theme_light_surface = Color(0xFFfcfcff)
val md_theme_light_onSurface = Color(0xFF1a1c1e)
val md_theme_light_surfaceVariant = Color(0xFFdee3ea)
val md_theme_light_onSurfaceVariant = Color(0xFF41474d)
val md_theme_light_outline = Color(0xFF72787e)
val md_theme_light_inverseOnSurface = Color(0xFFf0f0f3)
val md_theme_light_inverseSurface = Color(0xFF2f3032)
val md_theme_light_inversePrimary = Color(0xFF8bceff)
val md_theme_light_shadow = Color(0xFF000000)

val DeckBoxLightColors = lightColorScheme(
  primary = md_theme_light_primary,
  onPrimary = md_theme_light_onPrimary,
  primaryContainer = md_theme_light_primaryContainer,
  onPrimaryContainer = md_theme_light_onPrimaryContainer,
  secondary = md_theme_light_secondary,
  onSecondary = md_theme_light_onSecondary,
  secondaryContainer = md_theme_light_secondaryContainer,
  onSecondaryContainer = md_theme_light_onSecondaryContainer,
  tertiary = md_theme_light_tertiary,
  onTertiary = md_theme_light_onTertiary,
  tertiaryContainer = md_theme_light_tertiaryContainer,
  onTertiaryContainer = md_theme_light_onTertiaryContainer,
  error = md_theme_light_error,
  errorContainer = md_theme_light_errorContainer,
  onError = md_theme_light_onError,
  onErrorContainer = md_theme_light_onErrorContainer,
  background = md_theme_light_background,
  onBackground = md_theme_light_onBackground,
  surface = md_theme_light_surface,
  onSurface = md_theme_light_onSurface,
  surfaceVariant = md_theme_light_surfaceVariant,
  onSurfaceVariant = md_theme_light_onSurfaceVariant,
  outline = md_theme_light_outline,
  inverseOnSurface = md_theme_light_inverseOnSurface,
  inverseSurface = md_theme_light_inverseSurface,
  inversePrimary = md_theme_light_inversePrimary,
//  shadow = md_theme_light_shadow,
)

val md_theme_dark_primary = Color(0xFF8bceff)
val md_theme_dark_onPrimary = Color(0xFF003450)
val md_theme_dark_primaryContainer = Color(0xFF004b71)
val md_theme_dark_onPrimaryContainer = Color(0xFFc8e6ff)
val md_theme_dark_secondary = Color(0xFFfabd00)
val md_theme_dark_onSecondary = Color(0xFF402d00)
val md_theme_dark_secondaryContainer = Color(0xFF5c4300)
val md_theme_dark_onSecondaryContainer = Color(0xFFffdf99)
val md_theme_dark_tertiary = Color(0xFFcfbfe8)
val md_theme_dark_onTertiary = Color(0xFF362b4b)
val md_theme_dark_tertiaryContainer = Color(0xFF4d4162)
val md_theme_dark_onTertiaryContainer = Color(0xFFecdcff)
val md_theme_dark_error = Color(0xFFffb4a9)
val md_theme_dark_errorContainer = Color(0xFF930006)
val md_theme_dark_onError = Color(0xFF680003)
val md_theme_dark_onErrorContainer = Color(0xFFffdad4)
val md_theme_dark_background = Color(0xFF1a1c1e)
val md_theme_dark_onBackground = Color(0xFFe2e2e5)
val md_theme_dark_surface = Color(0xFF1a1c1e)
val md_theme_dark_onSurface = Color(0xFFe2e2e5)
val md_theme_dark_surfaceVariant = Color(0xFF41474d)
val md_theme_dark_onSurfaceVariant = Color(0xFFc1c7ce)
val md_theme_dark_outline = Color(0xFF8b9198)
val md_theme_dark_inverseOnSurface = Color(0xFF1a1c1e)
val md_theme_dark_inverseSurface = Color(0xFFe2e2e5)
val md_theme_dark_inversePrimary = Color(0xFF006494)
val md_theme_dark_shadow = Color(0xFF000000)

val DeckBoxDarkColors = darkColorScheme(
  primary = md_theme_dark_primary,
  onPrimary = md_theme_dark_onPrimary,
  primaryContainer = md_theme_dark_primaryContainer,
  onPrimaryContainer = md_theme_dark_onPrimaryContainer,
  secondary = md_theme_dark_secondary,
  onSecondary = md_theme_dark_onSecondary,
  secondaryContainer = md_theme_dark_secondaryContainer,
  onSecondaryContainer = md_theme_dark_onSecondaryContainer,
  tertiary = md_theme_dark_tertiary,
  onTertiary = md_theme_dark_onTertiary,
  tertiaryContainer = md_theme_dark_tertiaryContainer,
  onTertiaryContainer = md_theme_dark_onTertiaryContainer,
  error = md_theme_dark_error,
  errorContainer = md_theme_dark_errorContainer,
  onError = md_theme_dark_onError,
  onErrorContainer = md_theme_dark_onErrorContainer,
  background = md_theme_dark_background,
  onBackground = md_theme_dark_onBackground,
  surface = md_theme_dark_surface,
  onSurface = md_theme_dark_onSurface,
  surfaceVariant = md_theme_dark_surfaceVariant,
  onSurfaceVariant = md_theme_dark_onSurfaceVariant,
  outline = md_theme_dark_outline,
  inverseOnSurface = md_theme_dark_inverseOnSurface,
  inverseSurface = md_theme_dark_inverseSurface,
  inversePrimary = md_theme_dark_inversePrimary,
  // shadow = md_theme_dark_shadow,
)

val seed = Color(0xFF03a9f4)
val error = Color(0xFFba1b1b)

object PokemonTypeColor {
  val Colorless = Color(0xFFE7E3D4)
  val Fire = Color(0xFFE11119)
  val Grass = Color(0xFF1D8520)
  val Water = Color(0xFF0F61C0)
  val Lightning = Color(0xFFF6DD0A)
  val Fighting = Color(0xFFCD2C13)
  val Psychic = Color(0xFF67267C)
  val Metal = Color(0xFF767D86)
  val Dragon = Color(0xFF947718)
  val Fairy = Color(0xFFFC5897)
  val Darkness = Color(0xFF122640)

  fun Type.toColor(): Color = when (this) {
    Type.COLORLESS -> Colorless
    Type.DARKNESS -> Darkness
    Type.DRAGON -> Dragon
    Type.FAIRY -> Fairy
    Type.FIGHTING -> Fighting
    Type.FIRE -> Fire
    Type.GRASS -> Grass
    Type.LIGHTNING -> Lightning
    Type.METAL -> Metal
    Type.PSYCHIC -> Psychic
    Type.WATER -> Water
    Type.UNKNOWN -> Colorless
  }

  @Composable
  fun Type.toContentColor(isSelected: Boolean = false): Color = when (this) {
    Type.COLORLESS -> if (isSelected) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.outline
    else -> if (isSelected) Color.White else toColor()
  }

  fun Type.toBackgroundColor(): Color = when (this) {
    Type.LIGHTNING -> Lightning.copy(alpha = 0.12f)
    Type.UNKNOWN,
    Type.COLORLESS,
    -> Colorless.copy(alpha = 0.20f)
    else -> toColor().copy(alpha = 0.20f)
  }
}
