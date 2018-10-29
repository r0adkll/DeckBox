package com.r0adkll.deckbuilder.internal.di


import android.content.Context
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.internal.di.scopes.AppScope
import com.r0adkll.deckbuilder.util.helper.Connectivity
import com.r0adkll.deckbuilder.util.helper.ConnectivityContext
import dagger.Module
import dagger.Provides


@Module
class AppModule(val app: DeckApp) {

    @Provides @AppScope
    fun provideApplicationContext(): Context = app


    @Provides @AppScope
    fun provideConnectivity(context: Context): Connectivity = ConnectivityContext(context)
}