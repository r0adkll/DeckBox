package app.deckbox.common.compose.imageloading

import android.app.Application
import app.deckbox.core.di.ActivityScope
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.cache.memory.maxSizePercent
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.option.androidContext
import me.tatarka.inject.annotations.Provides
import okio.Path.Companion.toOkioPath

actual interface PlatformImageLoaderComponent {

  @Provides
  @ActivityScope
  fun imageLoader(application: Application): ImageLoader {
    return ImageLoader {
      logger = DeckBoxImageLoaderLogger
      options {
        androidContext(application)
      }
      components {
        setupDefaultComponents()
      }
      interceptor {
        memoryCacheConfig {
          // Set the max size to 25% of the app's available memory.
          maxSizePercent(application, 0.25)
        }
        diskCacheConfig {
          directory(application.cacheDir.resolve("image_cache").toOkioPath())
          maxSizeBytes(5L * 1024L * 1024L * 1024L) // 5 GB
        }
      }
    }
  }
}
