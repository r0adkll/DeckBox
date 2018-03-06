package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.di


import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.DeckImagePickerFragment
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.DeckImageUi
import com.r0adkll.deckbuilder.internal.di.scopes.FragmentScope
import dagger.Module
import dagger.Provides


@Module
class DeckImageModule(val fragment: DeckImagePickerFragment) {


    @Provides @FragmentScope
    fun provideUi(): DeckImageUi = fragment


    @Provides @FragmentScope
    fun provideIntentions(): DeckImageUi.Intentions = fragment
}