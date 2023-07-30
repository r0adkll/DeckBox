package app.deckbox.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import app.deckbox.DeckBoxDatabase
import app.deckbox.core.di.AppScope
import me.tatarka.inject.annotations.Provides

actual interface SqlDelightDatabasePlatformComponent {

  @AppScope
  @Provides
  fun provideNativeSqlDriver(): SqlDriver = NativeSqliteDriver(DeckBoxDatabase.Schema, "deckbox.db")
}
