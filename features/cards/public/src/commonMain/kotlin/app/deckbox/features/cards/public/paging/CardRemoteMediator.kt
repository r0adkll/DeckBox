package app.deckbox.features.cards.public.paging

import app.cash.paging.RemoteMediator
import app.deckbox.core.model.Card
import app.deckbox.features.cards.public.model.CardQuery

@OptIn(app.cash.paging.ExperimentalPagingApi::class)
abstract class CardRemoteMediator : RemoteMediator<Int, Card>()

interface CardRemoteMediatorFactory {
  fun create(query: CardQuery): CardRemoteMediator
}
