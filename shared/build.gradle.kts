import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
  alias(libs.plugins.composeMultiplatform)
  id("app.deckbox.android.library")
  id("app.deckbox.multiplatform")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
  targets.withType<KotlinNativeTarget> {
    binaries.withType<Framework> {
      isStatic = true
      baseName = "shared"
    }
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(projects.core)

        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.material)
        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
        implementation(compose.components.resources)

//                implementation(libs.circuit.foundation)
//                implementation(libs.circuit.overlay)
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(libs.kotlin.test)
      }
    }
  }
}
