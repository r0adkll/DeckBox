package app.deckbox.ui.browse

import app.deckbox.core.di.ActivityScope
import app.deckbox.core.di.MergeActivityScope
import com.r0adkll.kotlininject.merge.annotations.ContributesTo
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

@ContributesTo(MergeActivityScope::class)
interface BrowseComponent {

  @ActivityScope
  @[IntoSet Provides]
  fun bindBrowseUiFactory(factory: BrowseUiFactory): Ui.Factory = factory

  @ActivityScope
  @[IntoSet Provides]
  fun bindBrowsePresenterFactory(factory: BrowsePresenterFactory): Presenter.Factory = factory
}
