package app.deckbox.common.compose.imageloading

import app.deckbox.core.di.AppScope
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.cache.memory.maxSizePercent
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.intercept.bitmapMemoryCacheConfig
import com.seiko.imageloader.intercept.imageMemoryCacheConfig
import com.seiko.imageloader.intercept.painterMemoryCacheConfig
import kotlinx.cinterop.ExperimentalForeignApi
import me.tatarka.inject.annotations.Provides
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual interface PlatformImageLoaderComponent {

  @OptIn(ExperimentalForeignApi::class)
  @Provides
  @AppScope
  fun imageLoader(): ImageLoader {
    val cacheDir: Path by lazy {
      NSFileManager.defaultManager.URLForDirectory(
        directory = NSCachesDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = true,
        error = null,
      )!!.path.orEmpty().toPath()
    }
    return ImageLoader {
      logger = DeckBoxImageLoaderLogger
      components {
        setupDefaultComponents()
      }
      interceptor {
        bitmapMemoryCacheConfig {
          maxSizePercent(0.25)
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
          directory(cacheDir.resolve("image_cache"))
          maxSizeBytes(5L * 1024L * 1024L * 1024L) // 5GB
        }
      }
    }
  }
}
