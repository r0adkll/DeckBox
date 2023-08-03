package app.deckbox.network

data class PagedResponse<Data>(
  val data: List<Data>,
  val page: Int,
  val pageSize: Int,
  val count: Int,
  val totalCount: Int,
) {
  // TODO: Not sure how these values return in multi-page scenarios
  val hasMore: Boolean
    get() = count < totalCount
}
