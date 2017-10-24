package com.r0adkll.deckbuilder.arch.ui.features.search.filter


import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.arch.ui.features.search.filter.adapter.Item
import io.pokemontcg.model.SubType
import io.pokemontcg.model.Type


class FilterIntentions {

    val typeClicks: Relay<Pair<String, Type>> = PublishRelay.create()
    val attributeClicks: Relay<SubType> = PublishRelay.create()
    val optionClicks: Relay<Pair<String, Any>> = PublishRelay.create()
    val viewMoreClicks: Relay<Unit> = PublishRelay.create()
    val valueRangeChanges: Relay<Pair<String, Item.ValueRange.Value>> = PublishRelay.create()
}