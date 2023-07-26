// Copyright 2023, Christopher Banes and the Tivi project contributors
// SPDX-License-Identifier: Apache-2.0

package app.deckbox.shared.di

import app.deckbox.core.di.ActivityScope
import app.deckbox.shared.DeckBoxUiViewController
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import platform.UIKit.UIViewController

@ActivityScope
@Component
abstract class HomeUiControllerComponent(
  @Component val applicationComponent: IosApplicationComponent,
) : UiComponent {
    abstract val uiViewControllerFactory: () -> UIViewController

    @Provides
    @ActivityScope
    fun uiViewController(bind: DeckBoxUiViewController): UIViewController = bind()

    companion object
}
