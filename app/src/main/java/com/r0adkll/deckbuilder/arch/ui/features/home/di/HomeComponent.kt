package com.r0adkll.deckbuilder.arch.ui.features.home.di


import com.r0adkll.deckbuilder.arch.ui.features.decks.di.DecksComponent
import com.r0adkll.deckbuilder.arch.ui.features.decks.di.DecksModule
import com.r0adkll.deckbuilder.arch.ui.features.home.HomeActivity
import com.r0adkll.deckbuilder.internal.di.ActivityScope
import dagger.Subcomponent


@ActivityScope
@Subcomponent(modules = arrayOf(HomeModule::class))
interface HomeComponent {

    fun inject(activity: HomeActivity)

    fun plus(module: DecksModule): DecksComponent
}