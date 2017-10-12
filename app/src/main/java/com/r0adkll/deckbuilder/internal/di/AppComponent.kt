package com.r0adkll.deckbuilder.internal.di


import com.r0adkll.deckbuilder.BuildModule
import com.r0adkll.deckbuilder.DeckApp
import dagger.Component


@AppScope
@Component(modules = arrayOf(
        AppModule::class,
        BuildModule::class
))
interface AppComponent {

    fun inject(app: DeckApp)
}