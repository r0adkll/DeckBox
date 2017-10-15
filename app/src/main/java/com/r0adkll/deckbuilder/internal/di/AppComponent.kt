package com.r0adkll.deckbuilder.internal.di


import com.r0adkll.deckbuilder.BuildModule
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.arch.data.DataModule
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di.DeckBuilderComponent
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di.DeckBuilderModule
import com.r0adkll.deckbuilder.arch.ui.features.home.di.HomeComponent
import com.r0adkll.deckbuilder.arch.ui.features.home.di.HomeModule
import dagger.Component


@AppScope
@Component(modules = arrayOf(
        AppModule::class,
        BuildModule::class,
        DataModule::class
))
interface AppComponent {

    fun inject(app: DeckApp)

    fun plus(module: HomeModule): HomeComponent
    fun plus(module: DeckBuilderModule): DeckBuilderComponent
}