package app.deckbox.db

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import app.deckbox.DeckboxDatabase
import app.deckbox.core.model.SuperType
import app.deckbox.sqldelight.Cards

expect class DriverFactory {
  fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): DeckboxDatabase {
  val driver = driverFactory.createDriver()

  val database = DeckboxDatabase(
    driver = driver,
    cardsAdapter = Cards.Adapter(
      supertypeAdapter = EnumColumnAdapter<SuperType>(),
      subtypesAdapter = StringListAdapter,
      nationalPokedexNumbersAdapter = IntListAdapter,
      typesAdapter = StringListAdapter,
      evolvesToAdapter = StringListAdapter,
      rulesAdapter = StringListAdapter,
    ),
  )

  // Do more work with the database (see below).
  return database
}
