package com.r0adkll.deckbuilder.arch.ui.features.overview.di

interface OverviewableComponent {

    fun plus(module: OverviewModule): OverviewComponent
}
