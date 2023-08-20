package app.deckbox.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import app.deckbox.DeckBoxDatabase
import app.deckbox.core.di.AppScope
import me.tatarka.inject.annotations.Provides

actual interface SqlDelightDatabasePlatformComponent {

  @AppScope
  @Provides
  fun provideJvmSqlDriver(): SqlDriver {
    val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    DestructiveMigrationSchema.perform(driver)
    DeckBoxDatabase.Schema.create(driver)
    return driver
  }
}
