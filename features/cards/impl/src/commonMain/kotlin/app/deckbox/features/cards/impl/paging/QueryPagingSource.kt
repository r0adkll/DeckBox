package app.deckbox.features.cards.impl.paging

import androidx.paging.PagingSource
import app.cash.sqldelight.Query
import app.deckbox.core.logging.bark
import kotlin.properties.Delegates

abstract class QueryPagingSource<Key : Any, RowType : Any, DatabaseType : Any> :
  PagingSource<Key, RowType>(),
  Query.Listener {

  protected var currentQuery: Query<DatabaseType>? by Delegates.observable(null) { _, old, new ->
    bark { "Current Query Changed($this), $old" }
    old?.removeListener(this)
    new?.addListener(this)
  }

  init {
    registerInvalidatedCallback {
      currentQuery?.removeListener(this)
      currentQuery = null
    }
  }

  final override fun queryResultsChanged() = invalidate()
}
