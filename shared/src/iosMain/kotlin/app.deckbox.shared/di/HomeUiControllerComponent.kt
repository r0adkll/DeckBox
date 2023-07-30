// Copyright 2023, Drew Heavner and the Deckbox project contributors
// SPDX-License-Identifier: Apache-2.0

package app.deckbox.shared.di

import app.deckbox.core.di.ActivityScope
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.di.MergeAppScope
import app.deckbox.shared.DeckBoxUiViewController
import com.r0adkll.kotlininject.merge.annotations.ContributesSubcomponent
import me.tatarka.inject.annotations.Provides
import platform.UIKit.UIViewController

@ActivityScope
@ContributesSubcomponent(
  scope = MergeActivityScope::class,
  parentScope = MergeAppScope::class,
)
abstract class HomeUiControllerComponent : UiComponent {
  abstract val uiViewControllerFactory: () -> UIViewController

  @Provides
  @ActivityScope
  fun uiViewController(bind: DeckBoxUiViewController): UIViewController = bind()

  companion object
}
