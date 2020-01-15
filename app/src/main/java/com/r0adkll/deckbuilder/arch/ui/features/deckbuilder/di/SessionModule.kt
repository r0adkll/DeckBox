package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di

import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Module
class SessionModule(val editId: String) {

    @Provides
    @EditId
    fun provideEditId(): String = editId
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class EditId
