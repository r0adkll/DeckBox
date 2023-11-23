package app.deckbox.features.cards.public.paging

import androidx.paging.PagingSource
import app.deckbox.core.model.Card
import app.deckbox.features.cards.public.model.CardQuery

typealias CardPagingSource = PagingSource<Int, Card>

interface CardPagingSourceFactory {
  fun create(query: CardQuery): PagingSource<Int, Card>
}
