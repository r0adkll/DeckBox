package app.deckbox.network

data class PagedResponse<Data>(
  val data: List<Data>,
  val page: Int,
  val pageSize: Int,
  val count: Int,
  val totalCount: Int,
) {

  val hasMore: Boolean
    get() = ((page - 1) * pageSize + count) < totalCount
}
