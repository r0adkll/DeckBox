package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.di

import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.DeckImagePickerFragment
import com.r0adkll.deckbuilder.internal.di.scopes.FragmentScope
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [(DeckImageModule::class)])
interface DeckImageComponent {

    fun inject(activity: DeckImagePickerFragment)
}
