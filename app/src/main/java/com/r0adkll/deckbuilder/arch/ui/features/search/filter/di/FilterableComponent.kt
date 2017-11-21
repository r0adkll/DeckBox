package com.r0adkll.deckbuilder.arch.ui.features.search.filter.di


interface FilterableComponent {

    fun plus(module: FilterModule): FilterComponent
}