package app.deckbox.core.coroutines

sealed interface LoadState<Data> {
  val dataOrNull: Data? get() = (this as? Loaded<Data>)?.data

  data object Loading : LoadState<Nothing>
  class Loaded<Data>(val data: Data) : LoadState<Data>
  data object Error : LoadState<Nothing>
}
