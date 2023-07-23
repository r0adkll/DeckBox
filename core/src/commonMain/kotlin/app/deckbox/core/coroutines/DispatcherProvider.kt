package app.deckbox.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class DispatcherProvider(
  val main: CoroutineDispatcher = Dispatchers.Main,
  val io: CoroutineDispatcher = Dispatchers.IO,
  val computation: CoroutineDispatcher = Dispatchers.Default,
)
