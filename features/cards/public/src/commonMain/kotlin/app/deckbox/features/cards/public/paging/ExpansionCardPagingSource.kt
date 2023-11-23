package app.deckbox.features.cards.public.paging

import androidx.paging.PagingSource
import app.deckbox.core.model.Card
import app.deckbox.core.model.Expansion

abstract class ExpansionCardPagingSource : PagingSource<Int, Card>()

interface ExpansionCardPagingSourceFactory {
  fun create(expansion: Expansion): ExpansionCardPagingSource
}
