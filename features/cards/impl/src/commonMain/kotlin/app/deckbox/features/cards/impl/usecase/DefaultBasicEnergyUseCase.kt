package app.deckbox.features.cards.impl.usecase

import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.model.Card
import app.deckbox.core.model.ExpansionId
import app.deckbox.core.model.SearchFilter
import app.deckbox.core.model.SuperType
import app.deckbox.features.cards.public.CardRepository
import app.deckbox.features.cards.public.model.CardQuery
import app.deckbox.features.cards.public.usecase.BasicEnergyUseCase
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import me.tatarka.inject.annotations.Inject

@ContributesBinding(MergeAppScope::class)
@Inject
class DefaultBasicEnergyUseCase(
  private val cardRepository: CardRepository,
) : BasicEnergyUseCase {

  override suspend fun execute(expansionId: ExpansionId): Result<List<Card>> {
    // Form a query for just basic energy for a given expansion
    try {
      val cards = cardRepository.getCards(
        CardQuery(
          filter = SearchFilter(
            expansions = setOf(expansionId),
            superTypes = setOf(SuperType.ENERGY),
            subTypes = setOf("Basic"),
          ),
        ),
      )

      return if (cards.isEmpty()) {
        Result.failure(BasicEnergyUseCase.Errors.NoEnergyCardsFound)
      } else {
        Result.success(cards)
      }
    } catch (e: Exception) {
      return Result.failure(BasicEnergyUseCase.Errors.Unknown(e))
    }
  }
}
