package app.deckbox.db

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import app.deckbox.DeckBoxDatabase
import app.deckbox.core.model.SuperType
import app.deckbox.sqldelight.Attacks
import app.deckbox.sqldelight.Booster_pack_join
import app.deckbox.sqldelight.Booster_packs
import app.deckbox.sqldelight.CardMarketPrices
import app.deckbox.sqldelight.Cards
import app.deckbox.sqldelight.Deck_card_join
import app.deckbox.sqldelight.Decks
import app.deckbox.sqldelight.Expansions
import app.deckbox.sqldelight.RemoteKey
import app.deckbox.sqldelight.TcgPlayerPrices
import me.tatarka.inject.annotations.Inject

@Inject
class DatabaseFactory(
  private val driver: SqlDriver,
) {
  fun build(): DeckBoxDatabase = DeckBoxDatabase(
    driver = driver,
    cardsAdapter = Cards.Adapter(
      supertypeAdapter = EnumColumnAdapter<SuperType>(),
      subtypesAdapter = StringListAdapter,
      nationalPokedexNumbersAdapter = IntListAdapter,
      hpAdapter = IntColumnAdapter,
      typesAdapter = TypeListAdapter,
      evolvesToAdapter = StringListAdapter,
      rulesAdapter = StringListAdapter,
      retreatCostAdapter = TypeListAdapter,
      convertedRetreatCostAdapter = IntColumnAdapter,
      legalitiesExpandedAdapter = EnumColumnAdapter(),
      legalitiesStandardAdapter = EnumColumnAdapter(),
      legalitiesUnlimitedAdapter = EnumColumnAdapter(),
      resistancesAdapter = CardEffectListAdapter,
      weaknessesAdapter = CardEffectListAdapter,
    ),
    expansionsAdapter = Expansions.Adapter(
      totalAdapter = IntColumnAdapter,
      printedTotalAdapter = IntColumnAdapter,
      releaseDateAdapter = LocalDateAdapter,
      updatedAtAdapter = LocalDateTimeAdapter,
      legalitiesUnlimitedAdapter = EnumColumnAdapter(),
      legalitiesStandardAdapter = EnumColumnAdapter(),
      legalitiesExpandedAdapter = EnumColumnAdapter(),
    ),
    attacksAdapter = Attacks.Adapter(
      costAdapter = TypeListAdapter,
      convertedEnergyCostAdapter = IntColumnAdapter,
    ),
    tcgPlayerPricesAdapter = TcgPlayerPrices.Adapter(
      updatedAtAdapter = LocalDateAdapter,
    ),
    cardMarketPricesAdapter = CardMarketPrices.Adapter(
      updatedAtAdapter = LocalDateAdapter,
    ),
    decksAdapter = Decks.Adapter(
      tagsAdapter = StringSetAdapter,
      createdAtAdapter = LocalDateTimeAdapter,
      updatedAtAdapter = LocalDateTimeAdapter,
    ),
    deck_card_joinAdapter = Deck_card_join.Adapter(
      countAdapter = IntColumnAdapter,
    ),
    booster_packsAdapter = Booster_packs.Adapter(
      createdAtAdapter = LocalDateTimeAdapter,
      updatedAtAdapter = LocalDateTimeAdapter,
    ),
    booster_pack_joinAdapter = Booster_pack_join.Adapter(
      countAdapter = IntColumnAdapter,
    ),
    RemoteKeyAdapter = RemoteKey.Adapter(
      keyAdapter = IntColumnAdapter,
      countAdapter = IntColumnAdapter,
      totalCountAdapter = IntColumnAdapter,
      nextKeyAdapter = IntColumnAdapter,
    ),
  )
}
