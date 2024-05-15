package app.deckbox.ui.decks.importer.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle

fun AnnotatedString.Builder.addStyleCardSpec(
  lineOffset: Int,
  line: String,
  countStyle: SpanStyle? = null,
  nameStyle: SpanStyle? = null,
  expansionStyle: SpanStyle? = null,
  numberStyle: SpanStyle? = null,
) {
  val parts = line.split(" ")
  if (parts.size < 3) return // The line is incorrectly formed, abandon

  // Count Component
  var partOffset = lineOffset + parts.first().length
  countStyle?.let { addStyle(it, lineOffset, partOffset) }
  partOffset += 1 // add the ' ' space to the count

  // Name Component
  if (parts.size > 3) {
    val namePart = parts.subList(1, parts.size - 2).joinToString(" ")
    nameStyle?.let { addStyle(it, partOffset, partOffset + namePart.length) }
    partOffset += namePart.length + 1 // Add the name part plus the space
  }

  // Expansion Component
  val expansionPart = parts[parts.size - 2]
  expansionStyle?.let { addStyle(it, partOffset, partOffset + expansionPart.length) }
  partOffset += expansionPart.length + 1

  // Expansion Numer Component
  val expansionNumberPart = parts[parts.size - 1]
  numberStyle?.let { addStyle(it, partOffset, partOffset + expansionNumberPart.length) }
}
