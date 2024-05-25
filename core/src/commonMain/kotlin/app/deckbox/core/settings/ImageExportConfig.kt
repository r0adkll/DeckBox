package app.deckbox.core.settings

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class ImageExportConfig(
  val maxColumns: Int,
  val widthPx: Int,
  val horizontalSpacing: Int,
  val verticalSpacing: Int,
) {

  companion object {
    val DEFAULT = ImageExportConfig(
      maxColumns = 8,
      widthPx = 600,
      horizontalSpacing = 0,
      verticalSpacing = 0,
    )
  }
}

fun ImageExportConfig.asString(): String {
  return Json.encodeToString(this)
}

fun ImageExportConfig.Companion.fromString(s: String): ImageExportConfig {
  return Json.decodeFromString(s)
}
