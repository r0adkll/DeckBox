package app.deckbox.android.di

import android.app.Application
import android.content.Context
import app.deckbox.android.DeckBoxApplication
import app.deckbox.core.app.ApplicationInfo
import app.deckbox.core.app.Flavor
import app.deckbox.core.di.AppScope
import app.deckbox.core.di.MergeAppScope
import app.deckbox.shared.di.SharedAppComponent
import com.r0adkll.kotlininject.merge.annotations.MergeComponent
import kotlininject.merge.app.deckbox.android.di.MergedAndroidAppComponent
import me.tatarka.inject.annotations.Provides
import org.jetbrains.compose.components.resources.BuildConfig

@AppScope
@MergeComponent(MergeAppScope::class)
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

  companion object
}

fun AndroidAppComponent.Companion.from(context: Context): MergedAndroidAppComponent {
  return (context.applicationContext as DeckBoxApplication).component
}
