package com.r0adkll.deckbuilder.arch.ui.features.filter


import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.FilterAttribute
import com.r0adkll.deckbuilder.arch.ui.features.filter.adapter.Item
import io.pokemontcg.model.Type


class FilterIntentions {

    val typeClicks: Relay<Pair<String, Type>> = PublishRelay.create()
    val attributeClicks: Relay<FilterAttribute> = PublishRelay.create()
    val optionClicks: Relay<Pair<String, Any>> = PublishRelay.create()
    val viewMoreClicks: Relay<Unit> = PublishRelay.create()
    val valueRangeChanges: Relay<Pair<String, Item.ValueRange.Value>> = PublishRelay.create()
    val clearFilter: Relay<Unit> = PublishRelay.create()
}