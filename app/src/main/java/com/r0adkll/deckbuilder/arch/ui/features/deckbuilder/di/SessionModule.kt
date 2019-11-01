package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di

import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Module
class SessionModule(val sessionId: Long) {

    @Provides
    @SessionId
    fun provideSessionId(): Long = sessionId
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SessionId
