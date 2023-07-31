package app.deckbox.common.settings

import app.deckbox.core.di.MergeAppScope
import com.r0adkll.kotlininject.merge.annotations.ContributesTo
import me.tatarka.inject.annotations.Provides

expect interface PreferencesPlatformComponent

@ContributesTo(MergeAppScope::class)
interface SettingsComponent : PreferencesPlatformComponent {

  val DeckBoxSettingsImpl.bind: DeckBoxSettings
    @Provides get() = this
}
