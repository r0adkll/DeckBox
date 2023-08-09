plugins {
  id("app.deckbox.multiplatform")
  alias(libs.plugins.kotlin.serialization)
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(projects.di.kotlinInjectMergeAnnotations)
        api(libs.kotlinx.coroutines.core)
        api(libs.kotlininject.runtime)
        api(libs.kotlinx.datetime)
        implementation(libs.kotlinx.serialization.json)
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(libs.kotlin.test)
      }
    }
  }
}
