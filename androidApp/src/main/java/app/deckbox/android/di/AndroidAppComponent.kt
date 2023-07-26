package app.deckbox.android.di

import android.app.Application
import android.content.Context
import app.deckbox.android.DeckBoxApplication
import app.deckbox.core.app.ApplicationInfo
import app.deckbox.core.app.Flavor
import app.deckbox.core.di.AppScope
import app.deckbox.shared.di.SharedAppComponent
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import org.jetbrains.compose.components.resources.BuildConfig

@AppScope
@Component
abstract class AndroidAppComponent(
  @get:Provides val application: Application,
) : SharedAppComponent {

  @Suppress("DEPRECATION")
  @AppScope
  @Provides
  fun provideApplicationInfo(application: Application): ApplicationInfo {
    val packageInfo = application.packageManager.getPackageInfo(application.packageName, 0)

    return ApplicationInfo(
      packageName = application.packageName,
      debugBuild = BuildConfig.DEBUG,
      flavor = Flavor.Standard,
      versionName = packageInfo.versionName,
      versionCode = packageInfo.versionCode,
    )
  }

  companion object {
    fun from(context: Context): AndroidAppComponent {
      return (context.applicationContext as DeckBoxApplication).component
    }
  }
}
