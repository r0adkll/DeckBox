package app.deckbox.ui.expansions

import app.deckbox.core.di.ActivityScope
import app.deckbox.core.di.MergeActivityScope
import com.r0adkll.kotlininject.merge.annotations.ContributesTo
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

@ContributesTo(MergeActivityScope::class)
interface ExpansionsComponent {

  @ActivityScope
  @[IntoSet Provides]
  fun bindExpansionsUiFactory(factory: ExpansionsUiFactory): Ui.Factory = factory

  @ActivityScope
  @[IntoSet Provides]
  fun bindExpansionsPresenterFactory(factory: ExpansionsPresenterFactory): Presenter.Factory = factory
}
