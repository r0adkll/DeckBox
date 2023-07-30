package app.deckbox.shared.di

import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.di.AppScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import me.tatarka.inject.annotations.Provides

interface SharedAppComponent : CoreComponent

interface CoreComponent {

  @OptIn(ExperimentalCoroutinesApi::class)
  @AppScope
  @Provides
  fun provideCoroutineDispatchers(): DispatcherProvider = DispatcherProvider(
    io = Dispatchers.IO,
    databaseWrite = Dispatchers.IO.limitedParallelism(1),
    databaseRead = Dispatchers.IO.limitedParallelism(4),
    computation = Dispatchers.Default,
    main = Dispatchers.Main,
  )
}
