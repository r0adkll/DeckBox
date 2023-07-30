package app.deckbox.db

import app.deckbox.DeckBoxDatabase
import app.deckbox.core.di.AppScope
import app.deckbox.core.di.MergeAppScope
import com.r0adkll.kotlininject.merge.annotations.ContributesTo
import me.tatarka.inject.annotations.Provides

expect interface SqlDelightDatabasePlatformComponent

@ContributesTo(MergeAppScope::class)
interface DatabaseComponent : SqlDelightDatabasePlatformComponent {
  @AppScope
  @Provides
  fun provideSqlDelightDatabase(
    factory: DatabaseFactory,
  ): DeckBoxDatabase = factory.build()
}
