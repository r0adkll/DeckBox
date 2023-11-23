package app.deckbox.features.cards.public.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import app.deckbox.core.model.Card
import app.deckbox.features.cards.public.model.CardQuery

@OptIn(ExperimentalPagingApi::class)
abstract class CardRemoteMediator : RemoteMediator<Int, Card>()

interface CardRemoteMediatorFactory {
  fun create(query: CardQuery): CardRemoteMediator
}
