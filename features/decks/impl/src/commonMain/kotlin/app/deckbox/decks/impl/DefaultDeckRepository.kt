package app.deckbox.decks.impl

import app.deckbox.core.di.AppScope
import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.model.Deck
import app.deckbox.core.model.Legalities
import app.deckbox.core.model.Legality
import app.deckbox.features.decks.api.DeckRepository
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlin.random.Random
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.tatarka.inject.annotations.Inject

@AppScope
@ContributesBinding(MergeAppScope::class)
@Inject
class DefaultDeckRepository : DeckRepository {

  private val decks = MutableStateFlow(StarterDecks)

  override fun observeDecks(): Flow<List<Deck>> {
    return decks.asStateFlow()
  }

  override fun deleteDeck(deck: Deck) {
    decks.value = decks.value.filter { it.id != deck.id }
  }

  override fun duplicateDeck(deck: Deck) {
    decks.value += deck.copy(id = "${Random.nextInt(Int.MAX_VALUE)}")
  }

  companion object {
    private val StarterDecks = listOf(
      Deck(
        id = "0",
        name = "Example Deck 1",
        description = "This is a sample description that will represent a " +
          "custom description a user writes on their deck.",
        collectionMode = false,
        tags = listOf(
          "Meta",
          "Test",
          "#Torb",
        ),
        cardImages = listOf(
          "https://images.pokemontcg.io/sv2/237.png",
          "https://images.pokemontcg.io/sv2/279.png",
          "https://images.pokemontcg.io/sv2/276.png",
          "https://images.pokemontcg.io/sv2/277.png",
          "https://images.pokemontcg.io/sv2/271.png",
        ),
        legalities = Legalities(
          standard = Legality.LEGAL,
          expanded = Legality.LEGAL,
          unlimited = Legality.LEGAL,
        ),
        errors = emptyList(),
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.UTC),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.UTC),
      ),
      Deck(
        id = "1",
        name = "Example Deck 2",
        description = "This is a sample description that will represent a " +
          "custom description a user writes on their deck.",
        collectionMode = false,
        tags = listOf(
          "Meta",
          "Test",
          "#FireNation",
        ),
        cardImages = listOf(
          "https://images.pokemontcg.io/sv2/237.png",
          "https://images.pokemontcg.io/sv2/279.png",
          "https://images.pokemontcg.io/sv2/276.png",
          "https://images.pokemontcg.io/sv2/277.png",
          "https://images.pokemontcg.io/sv2/271.png",
        ),
        legalities = Legalities(
          standard = Legality.ILLEGAL,
          expanded = Legality.LEGAL,
          unlimited = Legality.LEGAL,
        ),
        errors = emptyList(),
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.UTC),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.UTC),
      ),
      Deck(
        id = "2",
        name = "Example Deck 3",
        description = "This is a sample description that will represent a " +
          "custom description a user writes on their deck.",
        collectionMode = false,
        tags = listOf(
          "Meta",
          "Test",
          "#Bacon",
        ),
        cardImages = listOf(
          "https://images.pokemontcg.io/sv2/237.png",
          "https://images.pokemontcg.io/sv2/279.png",
          "https://images.pokemontcg.io/sv2/276.png",
          "https://images.pokemontcg.io/sv2/277.png",
          "https://images.pokemontcg.io/sv2/271.png",
        ),

        legalities = Legalities(
          standard = Legality.ILLEGAL,
          expanded = Legality.ILLEGAL,
          unlimited = Legality.LEGAL,
        ),
        errors = emptyList(),
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.UTC),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.UTC),
      ),
    )
  }
}
