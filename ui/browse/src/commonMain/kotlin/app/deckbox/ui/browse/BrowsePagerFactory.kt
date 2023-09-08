package app.deckbox.ui.browse

import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.deckbox.core.model.Card
import app.deckbox.features.cards.public.model.MAX_PAGE_SIZE
import app.deckbox.features.cards.public.paging.CardPagingSource
import app.deckbox.features.cards.public.paging.CardRemoteMediator

/**
 * Extracting this so we don't have to stare at ide errors when editing presenter
 */
@OptIn(ExperimentalPagingApi::class)
internal fun createPager(
  pagingSourceFactory: () -> CardPagingSource,
  remoteMediatorFactory: () -> CardRemoteMediator,
): Pager<Int, Card> {
  return Pager<Int, Card>(
    config = PagingConfig(
      pageSize = MAX_PAGE_SIZE,
    ),
    initialKey = 1,
    remoteMediator = remoteMediatorFactory(),
    pagingSourceFactory = pagingSourceFactory,
  )
}
