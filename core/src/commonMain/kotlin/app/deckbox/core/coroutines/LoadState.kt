package app.deckbox.core.coroutines

sealed interface LoadState<Data> {
  val dataOrNull: Data? get() = (this as? Loaded<Data>)?.data

  data object Loading : LoadState<Nothing>
  class Loaded<Data>(val data: Data) : LoadState<Data>
  data object Error : LoadState<Nothing>
}

@Suppress("UNCHECKED_CAST")
inline fun <Data, Result> LoadState<Data>.map(mapper: (Data) -> Result): LoadState<Result> {
  return when (this) {
    is LoadState.Loaded -> LoadState.Loaded(mapper(data))
    LoadState.Error -> LoadState.Error as LoadState<Result>
    LoadState.Loading -> LoadState.Loading as LoadState<Result>
  }
}

@Suppress("UNCHECKED_CAST")
inline fun <Data, Result> LoadState<Data>.mapResult(mapper: (Data) -> kotlin.Result<Result>): LoadState<Result> {
  return when (this) {
    is LoadState.Loaded -> {
      val mappingResult = mapper(data)
      if (mappingResult.isSuccess) {
        LoadState.Loaded(mappingResult.getOrThrow())
      } else {
        LoadState.Error as LoadState<Result>
      }
    }
    LoadState.Error -> LoadState.Error as LoadState<Result>
    LoadState.Loading -> LoadState.Loading as LoadState<Result>
  }
}
