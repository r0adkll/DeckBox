package app.deckbox.ui.decks

import app.deckbox.core.di.ActivityScope
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface DecksComponent {

  @[ActivityScope IntoSet Provides]
  fun bindDecksUiFactory(factory: DecksUiFactory): Ui.Factory = factory

  @[ActivityScope IntoSet Provides]
  fun bindDecksPresenterFactory(factory: DecksPresenterFactory): Presenter.Factory = factory
}
