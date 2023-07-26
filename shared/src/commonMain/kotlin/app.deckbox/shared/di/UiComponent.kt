// Copyright 2023, Christopher Banes and the Tivi project contributors
// SPDX-License-Identifier: Apache-2.0

package app.deckbox.shared.di

import app.deckbox.core.di.ActivityScope
import app.deckbox.ui.browse.BrowseComponent
import app.deckbox.ui.decks.DecksComponent
import app.deckbox.ui.expansions.ExpansionsComponent
import com.slack.circuit.foundation.CircuitConfig
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.Provides

interface UiComponent :
  BrowseComponent,
  DecksComponent,
  ExpansionsComponent {

//    @Provides
//    @ActivityScope
//    fun provideLyricist(): TiviStrings {
//        return Lyricist(
//            defaultLanguageTag = Locales.EN,
//            translations = Strings,
//        ).strings
//    }

  @Provides
  @ActivityScope
  fun provideCircuitConfig(
    uiFactories: Set<Ui.Factory>,
    presenterFactories: Set<Presenter.Factory>,
  ): CircuitConfig = CircuitConfig.Builder()
    .addUiFactories(uiFactories)
    .addPresenterFactories(presenterFactories)
    .build()
}
