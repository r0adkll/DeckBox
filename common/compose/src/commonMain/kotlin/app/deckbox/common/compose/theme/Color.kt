package app.deckbox.common.compose.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import app.deckbox.core.model.Type

val md_theme_light_primary = Color(0xFF006493)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFCAE6FF)
val md_theme_light_onPrimaryContainer = Color(0xFF001E30)
val md_theme_light_secondary = Color(0xFF785900)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFFFDF9E)
val md_theme_light_onSecondaryContainer = Color(0xFF261A00)
val md_theme_light_tertiary = Color(0xFFB50577)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFFFD8E7)
val md_theme_light_onTertiaryContainer = Color(0xFF3D0025)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_outline = Color(0xFF72787E)
val md_theme_light_background = Color(0xFFFCFCFF)
val md_theme_light_onBackground = Color(0xFF1A1C1E)
val md_theme_light_surface = Color(0xFFF9F9FC)
val md_theme_light_onSurface = Color(0xFF1A1C1E)
val md_theme_light_surfaceVariant = Color(0xFFDDE3EA)
val md_theme_light_onSurfaceVariant = Color(0xFF41474D)
val md_theme_light_inverseSurface = Color(0xFF2E3133)
val md_theme_light_inverseOnSurface = Color(0xFFF0F0F3)
val md_theme_light_inversePrimary = Color(0xFF8DCDFF)
val md_theme_light_surfaceTint = Color(0xFF006493)
val md_theme_light_outlineVariant = Color(0xFFC1C7CE)
val md_theme_light_scrim = Color(0xFF000000)

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
  onError = md_theme_light_onError,
  errorContainer = md_theme_light_errorContainer,
  onErrorContainer = md_theme_light_onErrorContainer,
  outline = md_theme_light_outline,
  background = md_theme_light_background,
  onBackground = md_theme_light_onBackground,
  surface = md_theme_light_surface,
  onSurface = md_theme_light_onSurface,
  surfaceVariant = md_theme_light_surfaceVariant,
  onSurfaceVariant = md_theme_light_onSurfaceVariant,
  inverseSurface = md_theme_light_inverseSurface,
  inverseOnSurface = md_theme_light_inverseOnSurface,
  inversePrimary = md_theme_light_inversePrimary,
  surfaceTint = md_theme_light_surfaceTint,
  outlineVariant = md_theme_light_outlineVariant,
  scrim = md_theme_light_scrim,
)

val md_theme_dark_primary = Color(0xFF8DCDFF)
val md_theme_dark_onPrimary = Color(0xFF00344F)
val md_theme_dark_primaryContainer = Color(0xFF004B70)
val md_theme_dark_onPrimaryContainer = Color(0xFFCAE6FF)
val md_theme_dark_secondary = Color(0xFFFABD00)
val md_theme_dark_onSecondary = Color(0xFF3F2E00)
val md_theme_dark_secondaryContainer = Color(0xFF5B4300)
val md_theme_dark_onSecondaryContainer = Color(0xFFFFDF9E)
val md_theme_dark_tertiary = Color(0xFFFFAFD2)
val md_theme_dark_onTertiary = Color(0xFF63003F)
val md_theme_dark_tertiaryContainer = Color(0xFF8B005A)
val md_theme_dark_onTertiaryContainer = Color(0xFFFFD8E7)
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_dark_outline = Color(0xFF8B9198)
val md_theme_dark_background = Color(0xFF1A1C1E)
val md_theme_dark_onBackground = Color(0xFFE2E2E5)
val md_theme_dark_surface = Color(0xFF111416)
val md_theme_dark_onSurface = Color(0xFFC6C6C9)
val md_theme_dark_surfaceVariant = Color(0xFF41474D)
val md_theme_dark_onSurfaceVariant = Color(0xFFC1C7CE)
val md_theme_dark_inverseSurface = Color(0xFFE2E2E5)
val md_theme_dark_inverseOnSurface = Color(0xFF1A1C1E)
val md_theme_dark_inversePrimary = Color(0xFF006493)
val md_theme_dark_surfaceTint = Color(0xFF8DCDFF)
val md_theme_dark_outlineVariant = Color(0xFF41474D)
val md_theme_dark_scrim = Color(0xFF000000)

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
  onError = md_theme_dark_onError,
  errorContainer = md_theme_dark_errorContainer,
  onErrorContainer = md_theme_dark_onErrorContainer,
  outline = md_theme_dark_outline,
  background = md_theme_dark_background,
  onBackground = md_theme_dark_onBackground,
  surface = md_theme_dark_surface,
  onSurface = md_theme_dark_onSurface,
  surfaceVariant = md_theme_dark_surfaceVariant,
  onSurfaceVariant = md_theme_dark_onSurfaceVariant,
  inverseSurface = md_theme_dark_inverseSurface,
  inverseOnSurface = md_theme_dark_inverseOnSurface,
  inversePrimary = md_theme_dark_inversePrimary,
  surfaceTint = md_theme_dark_surfaceTint,
  outlineVariant = md_theme_dark_outlineVariant,
  scrim = md_theme_dark_scrim,
)

val seed = Color(0xFF03a9f4)

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
