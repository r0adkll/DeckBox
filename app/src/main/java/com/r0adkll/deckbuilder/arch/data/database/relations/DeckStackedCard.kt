package com.r0adkll.deckbuilder.arch.data.database.relations


class DeckStackedCard(
        var deckId: Long,
        count: Int,
        card: CardWithAttacks
) : StackedCard(count, card)