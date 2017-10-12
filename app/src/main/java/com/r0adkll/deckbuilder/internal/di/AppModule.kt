package com.r0adkll.deckbuilder.internal.di


import android.content.Context
import com.r0adkll.deckbuilder.DeckApp
import dagger.Module
import dagger.Provides


@Module
class AppModule(val app: DeckApp) {

    @Provides @AppScope
    fun provideApplicationContext(): Context = app
}