package app.deckbox.common.compose.imageloading

import android.app.Application
import app.deckbox.core.di.AppScope
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.cache.memory.maxSizePercent
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.intercept.bitmapMemoryCacheConfig
import com.seiko.imageloader.intercept.imageMemoryCacheConfig
import com.seiko.imageloader.intercept.painterMemoryCacheConfig
import com.seiko.imageloader.option.androidContext
import me.tatarka.inject.annotations.Provides
import okio.Path.Companion.toOkioPath

actual interface PlatformImageLoaderComponent {

  @Provides
  @AppScope
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
        bitmapMemoryCacheConfig {
          maxSizePercent(application, 0.25)
        }
        // cache 50 image
        imageMemoryCacheConfig {
          maxSize(50)
        }
        // cache 50 painter
        painterMemoryCacheConfig {
          maxSize(50)
        }
        diskCacheConfig {
          directory(application.cacheDir.resolve("image_cache").toOkioPath())
          maxSizeBytes(5L * 1024L * 1024L * 1024L) // 5 GB
        }
      }
    }
  }
}
