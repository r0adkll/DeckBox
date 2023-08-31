package app.deckbox.ui.decks.builder.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import app.deckbox.core.model.Card
import app.deckbox.core.model.Evolution
import app.deckbox.core.model.Stacked
import app.deckbox.core.model.SuperType

@Stable
sealed interface CardUiModel {
  val id: String
  val size: Int

  data class SectionHeader(
    val superType: SuperType,
    val title: @Composable () -> String,
    val count: Int,
  ) : CardUiModel {
    override val id: String
      get() = superType.hashCode().toString()

    override val size: Int = 0
  }

  data class EvolutionLine(
    val evolution: Evolution,
  ) : CardUiModel {
    override val id: String
      get() = evolution.hashCode().toString()

    override val size: Int
      get() = evolution.count
  }

  data class Single(
    val card: Stacked<Card>,
  ) : CardUiModel {
    override val id: String
      get() = card.card.id
    override val size: Int
      get() = card.count
  }
}
