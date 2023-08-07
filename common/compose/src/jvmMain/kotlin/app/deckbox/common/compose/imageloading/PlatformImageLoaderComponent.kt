package app.deckbox.common.compose.imageloading

import app.deckbox.core.di.ActivityScope
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.setupDefaultComponents
import java.io.File
import me.tatarka.inject.annotations.Provides
import okio.Path.Companion.toOkioPath

actual interface PlatformImageLoaderComponent {

  @Provides
  @ActivityScope
  fun imageLoader(): ImageLoader {
    return ImageLoader {
      components {
        setupDefaultComponents()
      }
      interceptor {
        memoryCacheConfig {
          maxSizeBytes(1 * 1024 * 1024 * 1024) // 1GB
        }
        diskCacheConfig {
          directory(getCacheDir().toOkioPath().resolve("image_cache"))
          maxSizeBytes(50L/*giga*/ * 1024L/*mega*/ * 1024L/*kilo*/ * 1024L/*byte*/) // 50 GB
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
