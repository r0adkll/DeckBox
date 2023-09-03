package app.deckbox.core.coroutines

sealed interface LoadState<Data> {
  data object Loading : LoadState<Nothing>
  class Loaded<Data>(val data: Data) : LoadState<Data>
  data object Error : LoadState<Nothing>
}
