package app.deckbox.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import app.deckbox.DeckboxDatabase

actual class DriverFactory {
  actual fun createDriver(): SqlDriver {
    return NativeSqliteDriver(DeckboxDatabase.Schema, "deckbox.db")
  }
}
