package app.deckbox.core.coroutines

sealed interface LoadState<Data> {
  object Loading : LoadState<Nothing>
  class Loaded<Data>(val data: Data) : LoadState<Data>
  class Error(val message: String) : LoadState<Nothing>
}
