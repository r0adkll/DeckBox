package app.deckbox.features.decks.api.import

import app.deckbox.core.model.Card
import app.deckbox.core.model.Stacked

interface DeckTextParser {

  /**
   * Parse a string formatted deck list and return the list of stacked cards, or error
   * if unable to parse
   */
  suspend fun parse(text: String): DeckSpec

  sealed class Errors(message: String? = null, cause: Throwable? = null) : Exception(message, cause) {
    data class MissingExpansions(val setCodes: List<String>) : Errors("Missing [$setCodes] expansions")
    data class MissingCards(val cardIds: List<String>) : Errors("Missing [$cardIds] cards")
    data object CardMappingError : Errors()
  }
}

/**
 * The resulting parsed (or otherwise) spec for an entire deck list text block.
 * @param originalText the original text that was parsed
 * @param content the parsed result, or failure states if unable to be parsed.
 */
data class DeckSpec(
  val originalText: String = "",
  val content: Content = Content.Empty,
) {

  val canImport: Boolean
    get() {
      if (content !is Content.Parsed) return false
      return content.cards.none { it.validation is CardSpec.Validation.Failure }
    }

  val cards: List<Stacked<Card>>
    get() = (content as? Content.Parsed)
      ?.cards
      ?.mapNotNull {
        (it.validation as? CardSpec.Validation.Success)?.card
      } ?: emptyList()

  sealed interface Content {
    data object Empty : Content

    data class Parsed(
      val cards: List<CardSpec>,
    ) : Content

    data object Invalid : Content
  }
}

/**
 * The parsed spec for each line in a deck list that details the card information
 * in the deck. This information must be loaded and validated to be imported into the user's
 * list of decks.
 *
 * @param line the original text parsed into this spec
 * @param result the result of parsing the [line] into a spec
 * @param validation the result of loading the spec information from our data source
 */
data class CardSpec(
  val line: String,
  val result: ParseResult,
  val validation: Validation = Validation.None,
) {

  sealed interface ParseResult {
    data class Success(
      val count: Int,
      val name: String?,
      val setCode: String,
      val setNumber: String,
    ) : ParseResult {

      val key: String
        get() = "$setCode-$setNumber"
    }

    sealed interface Error : ParseResult {
      data object MissingCount : Error
      data object Invalid : Error
    }
  }

  sealed interface Validation {
    data object None : Validation

    data class Success(
      val card: Stacked<Card>,
    ) : Validation

    sealed interface Failure : Validation {
      data class InvalidSetCode(val setCode: String) : Failure
      data class InvalidNumber(val setCode: String, val setNumber: String) : Failure
    }
  }
}
