package app.deckbox.common.settings

import app.deckbox.core.di.AppScope
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings
import me.tatarka.inject.annotations.Provides
import platform.Foundation.NSUserDefaults

actual interface PreferencesPlatformComponent {

  @AppScope
  @Provides
  fun provideSettings(delegate: NSUserDefaults): ObservableSettings = NSUserDefaultsSettings(delegate)
}
