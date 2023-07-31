package app.deckbox.common.settings

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import app.deckbox.core.di.AppScope
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.SharedPreferencesSettings
import me.tatarka.inject.annotations.Provides

actual interface PreferencesPlatformComponent {

  @AppScope
  @Provides
  fun provideSettings(delegate: AppSharedPreferences): ObservableSettings {
    return SharedPreferencesSettings(delegate)
  }

  @AppScope
  @Provides
  fun provideAppPreferences(
    context: Application,
  ): AppSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
}

typealias AppSharedPreferences = SharedPreferences
