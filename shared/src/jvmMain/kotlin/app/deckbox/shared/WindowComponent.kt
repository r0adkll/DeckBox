// Copyright 2023, Christopher Banes and the Tivi project contributors
// SPDX-License-Identifier: Apache-2.0

package app.deckbox.shared

import app.deckbox.core.di.ActivityScope
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.di.MergeAppScope
import app.deckbox.shared.di.UiComponent
import app.deckbox.shared.root.DeckBoxContentWithInsets
import com.r0adkll.kotlininject.merge.annotations.ContributesSubcomponent

@ActivityScope
@ContributesSubcomponent(
  scope = MergeActivityScope::class,
  parentScope = MergeAppScope::class,
)
abstract class WindowComponent : UiComponent {
  abstract val deckBoxContent: DeckBoxContentWithInsets

  companion object
}
