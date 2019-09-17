package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.di


import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.DeckImagePickerFragment
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.DeckImageRenderer
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.DeckImageUi
import com.r0adkll.deckbuilder.internal.di.scopes.FragmentScope
import com.r0adkll.deckbuilder.util.AppSchedulers
import dagger.Module
import dagger.Provides


@Module
class DeckImageModule(val fragment: DeckImagePickerFragment) {

    @Provides @FragmentScope
    fun provideUi(): DeckImageUi = fragment


    @Provides @FragmentScope
    fun provideIntentions(): DeckImageUi.Intentions = fragment


    @Provides @FragmentScope
    fun provideActions(): DeckImageUi.Actions = fragment


    @Provides @FragmentScope
    fun provideRenderer(
            actions: DeckImageUi.Actions,
            schedulers: AppSchedulers
    ) : DeckImageRenderer = DeckImageRenderer(actions, schedulers.main, schedulers.comp)
}
