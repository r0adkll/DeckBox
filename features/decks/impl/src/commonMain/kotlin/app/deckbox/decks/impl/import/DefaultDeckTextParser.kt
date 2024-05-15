package app.deckbox.decks.impl.import

import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.logging.bark
import app.deckbox.core.model.stack
import app.deckbox.expansions.ExpansionsRepository
import app.deckbox.features.cards.public.CardRepository
import app.deckbox.features.decks.api.import.CardSpec
import app.deckbox.features.decks.api.import.CardSpec.ParseResult
import app.deckbox.features.decks.api.import.CardSpec.Validation
import app.deckbox.features.decks.api.import.DeckSpec
import app.deckbox.features.decks.api.import.DeckTextParser
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

// TODO: This is very hard-coded and prone to breaking. We should find a more robust way of identifying basic energy
//  card sets. OR add robust alerting/messaging so that when this breaks we can patch accordingly
private const val DefaultEnergySetCode = "SVE"

/**
 * Deck lists will tend to add these as a form of basic energy in the lists:
 *
 * `4 Fire Energy 2`
 *
 * which is not easily parcelable. This is a constant to identify these and then replace them with a set code
 * that can be associated with the desired basic energy card.
 *
 * @see DefaultEnergySetCode
 */
private const val GenericEnergySetCode = "Energy"

@ContributesBinding(MergeAppScope::class)
@Inject
class DefaultDeckTextParser(
  private val cardRepository: CardRepository,
  private val expansionRepository: ExpansionsRepository,
  private val dispatcherProvider: DispatcherProvider,
) : DeckTextParser {

  override suspend fun parse(text: String): DeckSpec = withContext(dispatcherProvider.computation) {
    if (text.isEmpty()) return@withContext DeckSpec(text, DeckSpec.Content.Empty)

    val lines = text.split(NewlineRegex)

    // Parse the lines to card specs
    var cardSpecs = parseCardSpecs(lines)

    // Collect and reconnoiter setCodes to actual expansions
    val expansionSetCodes = cardSpecs.mapNotNull {
      (it.result as? ParseResult.Success)?.setCode
    }.toSet()

    val expansions = try {
      expansionRepository.getExpansions(expansionSetCodes)
    } catch (e: Exception) {
      bark { "Error loading expansions: $expansionSetCodes" }
      null
    }

    // Validate that we have an expansion for each requested set code
    val mappedExpansions = expansionSetCodes.mapNotNull { setCode ->
      expansions?.find { expansion -> (expansion.ptcgoCode ?: expansion.id).equals(setCode, true) }
    }.associateBy { it.ptcgoCode ?: it.id }

    cardSpecs = cardSpecs
      .map { spec ->
        val parsedSetCode = (spec.result as? ParseResult.Success)?.setCode ?: return@map spec
        val expansion = mappedExpansions[parsedSetCode]

        if (expansion == null) {
          spec.copy(validation = Validation.Failure.InvalidSetCode(parsedSetCode))
        } else {
          spec
        }
      }

    // Now iterate through the list of decklist cards and form their API ids
    val cardIds = cardSpecs
      .filterNot { it.validation is Validation.Failure }
      .mapNotNull { it.result as? ParseResult.Success }
      .map { card ->
        val expansion = mappedExpansions[card.setCode]!! // Validated earlier
        "${expansion.id}-${card.setNumber}"
      }

    // Now fetch the list of hydrated cards from the API for the given list of ids
    val cards = cardRepository.getCards(cardIds)

    // Map the fetched cards as validated information against the specs
    cardSpecs = cardSpecs
      .map { spec ->
        if (spec.validation is Validation.Failure) return@map spec
        val cardResult = spec.result as? ParseResult.Success ?: return@map spec
        val expansion = mappedExpansions[cardResult.setCode]!! // Validated earlier
        val parsedCardId = "${expansion.id}-${cardResult.setNumber}"
        cards.find { it.id == parsedCardId }
          ?.let { card ->
            spec.copy(
              validation = Validation.Success(
                card = card.stack(cardResult.count),
              ),
            )
          } ?: spec.copy(
          validation = Validation.Failure.InvalidNumber(cardResult.setCode, cardResult.setNumber),
        )
      }

    return@withContext DeckSpec(
      originalText = text,
      content = DeckSpec.Content.Parsed(cardSpecs),
    )
  }

  private fun parseCardSpecs(lines: List<String>): List<CardSpec> {
    return lines
      .map { line ->
        val parts = line.trim().split(" ")
        if (parts.size >= MIN_PARTS) {
          val count = parts.countPart()
          val setNumber = parts.setNumberPart()
          val setCode = parts.setCodePart()
          val name = parts.namePart()

          CardSpec(
            line = line,
            result = when {
              count == null -> ParseResult.Error.MissingCount
              else -> ParseResult.Success(count, name, setCode, setNumber)
            },
          )
        } else {
          CardSpec(
            line = line,
            result = ParseResult.Error.Invalid,
          )
        }.also {
          bark { "Line($line) => $it" }
        }
      }
  }

  private fun List<String>.countPart(): Int? = get(0).toIntOrNull()
  private fun List<String>.setNumberPart(): String = get(size - 1)
  private fun List<String>.setCodePart(): String = get(size - 2).let {
    if (it == GenericEnergySetCode) {
      DefaultEnergySetCode
    } else {
      it
    }
  }

  private fun List<String>.namePart(): String? {
    // If the line is 3 parts, then the assumption is that here is no name
    if (size <= 3) return null
    return subList(1, size - 2).joinToString(separator = " ")
  }

  companion object {
    private val NewlineRegex = "\\r?\\n".toRegex()
    private const val MIN_PARTS = 3
  }
}
