package app.deckbox.tournament.usecase

import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.logging.bark
import app.deckbox.core.model.Card
import app.deckbox.core.model.Stacked
import app.deckbox.core.model.stack
import app.deckbox.expansions.ExpansionsRepository
import app.deckbox.features.cards.public.CardRepository
import app.deckbox.tournament.api.model.DeckList
import app.deckbox.tournament.api.usecase.FetchDeckListCardsUseCase
import app.deckbox.tournament.api.usecase.FetchDeckListCardsUseCase.Errors
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import me.tatarka.inject.annotations.Inject

@ContributesBinding(MergeAppScope::class)
@Inject
class DefaultFetchDeckListCardsUseCase(
  private val expansionRepository: ExpansionsRepository,
  private val cardRepository: CardRepository,
) : FetchDeckListCardsUseCase {

  override suspend fun execute(deckList: DeckList): Result<List<Stacked<Card>>> {
    // First we need to collect the list of expansions used in this deck list so that we
    // can accurately form the list of cardIds to request
    val expansionSetCodes = deckList.cards.map { it.setCode }.toSet()
    val expansions = expansionRepository.getExpansions(expansionSetCodes)

    // Validate that we have an expansion for each requested set code
    val mappedExpansions = expansionSetCodes.mapNotNull { setCode ->
      expansions.find { expansion -> expansion.ptcgoCode == setCode }
    }.associateBy { it.ptcgoCode }

    bark {
      "Mapped Expansions(\n${mappedExpansions.entries.joinToString(", \n") { "  [${it.key}] = ${it.value}" }}\n)"
    }

    // If we didn't then we should fail this fetch b/c it can't succeed
    val didFetchAllExpansions = mappedExpansions.size == expansionSetCodes.size
    if (!didFetchAllExpansions) return Result.failure(Errors.ExpansionFetchError)

    // Now iterate through the list of decklist cards and form their API ids
    val cardIds = deckList.cards.map { card ->
      val expansion = mappedExpansions[card.setCode]!!
      "${expansion.id}-${card.number}"
    }

    bark { "Fetching cards(${cardIds.size}): $cardIds" }

    // Now fetch the list of hydrated cards from the API for the given list of ids
    val cards = cardRepository.getCards(cardIds)

    bark { "Cards Fetched! $cards" }

    // Validate that we fetched the correct amount of cards
    if (cards.size != deckList.cards.size) {
      // Find the missing cards
      val missingCards = cardIds.filter { id ->
        cards.none { it.id == id }
      }
      bark { "Missing Cards: $missingCards" }
      return Result.failure(Errors.CardsFetchError)
    }

    // Now stack the cards with the supplied 'count' information from the deck list meta-data
    val indexedCards = deckList.cards.associateBy { it.key }
    val stackedCards = cards.map { card ->
      val meta = indexedCards[card.metaKey] ?: return Result.failure(Errors.CardsMappingError)
      card.stack(count = meta.count)
    }

    return Result.success(stackedCards)
  }

  private val Card.metaKey: String get() = "${expansion.ptcgoCode}-$number"
}
