// Copyright 2023, Drew Heavner and the Deckbox project contributors
// SPDX-License-Identifier: Apache-2.0

package app.deckbox.shared.di

import app.deckbox.core.di.ActivityScope
import com.slack.circuit.foundation.CircuitConfig
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.Provides

interface UiComponent {

//    @Provides
//    @SingleIn(MergeActivityScope::class)
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
