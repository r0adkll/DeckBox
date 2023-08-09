package app.deckbox.decks.impl

import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.model.Deck
import app.deckbox.features.decks.public.DeckRepository
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.tatarka.inject.annotations.Inject

@ContributesBinding(MergeAppScope::class)
@Inject
class DefaultDeckRepository : DeckRepository {

  override fun observeDecks(): Flow<List<Deck>> {
    return flowOf(
      listOf(
        Deck(
          id = "0",
          name = "Example Deck 1",
          description = "This is a sample description that will represent a custom description a user writes on their deck.",
          collectionMode = false,
          tags = listOf(
            "Standard",
            "Meta",
            "Test",
          ),
          cardImages = listOf(
            "https://images.pokemontcg.io/sv2/237.png",
            "https://images.pokemontcg.io/sv2/279.png",
            "https://images.pokemontcg.io/sv2/276.png",
            "https://images.pokemontcg.io/sv2/277.png",
            "https://images.pokemontcg.io/sv2/271.png",
          ),
          errors = emptyList(),
          createdAt = Clock.System.now().toLocalDateTime(TimeZone.UTC),
          updatedAt = Clock.System.now().toLocalDateTime(TimeZone.UTC),
        ),
        Deck(
          id = "1",
          name = "Example Deck 2",
          description = "This is a sample description that will represent a custom description a user writes on their deck.",
          collectionMode = false,
          tags = listOf(
            "Standard",
            "Meta",
            "Test",
          ),
          cardImages = listOf(
            "https://images.pokemontcg.io/sv2/237.png",
            "https://images.pokemontcg.io/sv2/279.png",
            "https://images.pokemontcg.io/sv2/276.png",
            "https://images.pokemontcg.io/sv2/277.png",
            "https://images.pokemontcg.io/sv2/271.png",
          ),
          errors = emptyList(),
          createdAt = Clock.System.now().toLocalDateTime(TimeZone.UTC),
          updatedAt = Clock.System.now().toLocalDateTime(TimeZone.UTC),
        ),
        Deck(
          id = "2",
          name = "Example Deck 3",
          description = "This is a sample description that will represent a custom description a user writes on their deck.",
          collectionMode = false,
          tags = listOf(
            "Standard",
            "Meta",
            "Test",
          ),
          cardImages = listOf(
            "https://images.pokemontcg.io/sv2/237.png",
            "https://images.pokemontcg.io/sv2/279.png",
            "https://images.pokemontcg.io/sv2/276.png",
            "https://images.pokemontcg.io/sv2/277.png",
            "https://images.pokemontcg.io/sv2/271.png",
          ),
          errors = emptyList(),
          createdAt = Clock.System.now().toLocalDateTime(TimeZone.UTC),
          updatedAt = Clock.System.now().toLocalDateTime(TimeZone.UTC),
        ),
      ),
    )
  }
}
