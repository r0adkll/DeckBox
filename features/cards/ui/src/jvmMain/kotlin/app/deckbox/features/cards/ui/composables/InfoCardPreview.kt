package app.deckbox.features.cards.ui.composables

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.theme.DeckBoxTheme
import app.deckbox.core.model.Card
import app.deckbox.core.model.Expansion
import app.deckbox.core.model.SuperType
import app.deckbox.core.model.Type
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Preview
@Composable
fun InfoCardPreview() {

  DeckBoxTheme {

    InfoCard(
      name = "Charizard EX",
      modifier = Modifier.padding(32.dp),
      card = Card(
        id = "someid",
        name = "Charizard EX",
        image = Card.Image("", ""),
        supertype = SuperType.POKEMON,
        subtypes = emptyList(),
        level = null,
        hp = 280,
        types = listOf(
          Type.FIRE,
        ),
        evolvesFrom = null,
        evolvesTo = null,
        rules = null,
        ancientTrait = null,
        abilities = null,
        attacks = null,
        weaknesses = null,
        resistances = null,
        retreatCost = listOf(Type.COLORLESS, Type.COLORLESS, Type.COLORLESS),
        convertedRetreatCost = 3,
        number = "101",
        expansion = Expansion(
          id = "set1",
          name = "Obsidian Flames",
          releaseDate = LocalDate(2000, 1, 1),
          total = 500,
          printedTotal = 500,
          series = "Scarlet & Violet",
          legalities = null,
          ptcgoCode = null,
          updatedAt = LocalDateTime(2000, 1, 1, 12, 0, 0, 0),
          images = Expansion.Images(
            symbol = "",
            logo = "",
          ),
        ),
        artist = "r0adkll",
        rarity = "Double Rare",
        flavorText = null,
        nationalPokedexNumbers = null,
        legalities = null,
        tcgPlayer = null,
        cardMarket = null,
      ),
    )
  }
}
