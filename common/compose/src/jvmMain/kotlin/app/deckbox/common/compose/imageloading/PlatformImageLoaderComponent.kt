package app.deckbox.common.compose.imageloading

import app.deckbox.core.di.AppScope
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.cache.memory.maxSizePercent
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.intercept.bitmapMemoryCacheConfig
import com.seiko.imageloader.intercept.imageMemoryCacheConfig
import com.seiko.imageloader.intercept.painterMemoryCacheConfig
import java.io.File
import me.tatarka.inject.annotations.Provides
import okio.Path.Companion.toOkioPath

actual interface PlatformImageLoaderComponent {

  @Provides
  @AppScope
  fun imageLoader(): ImageLoader {
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
          directory(getCacheDir().toOkioPath().resolve("image_cache"))
          maxSizeBytes(50L * 1024L * 1024L * 1024L) // 50 GB
        }
      }
    }
  }

  enum class OperatingSystem {
    Windows, Linux, MacOS, Unknown
  }

  private val currentOperatingSystem: OperatingSystem
    get() {
      val operSys = System.getProperty("os.name").lowercase()
      return if (operSys.contains("win")) {
        OperatingSystem.Windows
      } else if (operSys.contains("nix") || operSys.contains("nux") ||
        operSys.contains("aix")
      ) {
        OperatingSystem.Linux
      } else if (operSys.contains("mac")) {
        OperatingSystem.MacOS
      } else {
        OperatingSystem.Unknown
      }
    }

  private fun getCacheDir() = when (currentOperatingSystem) {
    OperatingSystem.Windows -> File(System.getenv("AppData"), "DeckBox/cache")
    OperatingSystem.Linux -> File(System.getProperty("user.home"), ".cache/DeckBox")
    OperatingSystem.MacOS -> File(System.getProperty("user.home"), "Library/Caches/DeckBox")
    else -> throw IllegalStateException("Unsupported operating system")
  }
}
