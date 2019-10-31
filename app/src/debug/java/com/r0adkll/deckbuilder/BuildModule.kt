package com.r0adkll.deckbuilder

import com.r0adkll.deckbuilder.internal.AppDelegate
import com.r0adkll.deckbuilder.internal.di.scopes.AppScope
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
class BuildModule {

    @Provides @AppScope
    @IntoSet
    fun provideDebugAppDelegate(): AppDelegate = DebugAppDelegate()
}
