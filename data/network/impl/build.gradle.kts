import app.deckbox.convention.addKspDependencyForCommon

plugins {
  id("app.deckbox.android.library")
  id("app.deckbox.multiplatform")
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.buildConfig)
  alias(libs.plugins.ksp)
}

buildConfig {
  packageName("app.deckbox.network")

  buildConfigField(
    type = "String",
    name = "POKEMON_TCG_API_KEY",
    value = "\"${properties["POKEMONTCG_API_KEY"]?.toString() ?: ""}\"",
  )
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api(projects.data.network.public)
        implementation(projects.core)

        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.datetime)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.contentnegotiation)
        implementation(libs.ktor.client.logging)
        implementation(libs.ktor.client.serialization.json)
      }
    }

    commonTest {
      dependencies {
        implementation(libs.kotlin.test)
      }
    }

    androidMain {
      dependencies {
        api(libs.okhttp.okhttp)
        implementation(libs.ktor.client.okhttp)
      }
    }

    jvmMain {
      dependencies {
        api(libs.okhttp.okhttp)
        implementation(libs.ktor.client.okhttp)
      }
    }

    iosMain {
      dependencies {
        implementation(libs.ktor.client.darwin)
      }
    }
  }
}

addKspDependencyForCommon(projects.di.kotlinInjectMerge)
