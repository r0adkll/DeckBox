package app.deckbox.db

import android.app.Application
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.deckbox.DeckBoxDatabase
import app.deckbox.core.di.AppScope
import me.tatarka.inject.annotations.Provides

actual interface SqlDelightDatabasePlatformComponent {

  @AppScope
  @Provides
  fun provideAndroidSqlDriver(
    application: Application,
  ): SqlDriver = AndroidSqliteDriver(
    schema = DeckBoxDatabase.Schema,
    context = application,
    name = "deckbox.db",
    callback = object : AndroidSqliteDriver.Callback(DeckBoxDatabase.Schema) {
      override fun onConfigure(db: SupportSQLiteDatabase) {
        db.enableWriteAheadLogging()
        db.setForeignKeyConstraintsEnabled(true)
      }
    },
  )
}
