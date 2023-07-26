package app.deckbox.ui.expansions

import app.deckbox.core.di.ActivityScope
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface ExpansionsComponent {

  @[ActivityScope IntoSet Provides]
  fun bindExpansionsUiFactory(factory: ExpansionsUiFactory): Ui.Factory = factory

  @[ActivityScope IntoSet Provides]
  fun bindExpansionsPresenterFactory(factory: ExpansionsPresenterFactory): Presenter.Factory = factory
}
