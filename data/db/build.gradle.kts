import app.deckbox.convention.addKspDependencyForCommon

plugins {
  id("app.deckbox.android.library")
  id("app.deckbox.multiplatform")
  alias(libs.plugins.sqldelight)
  alias(libs.plugins.ksp)
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
  sqldelight {
    databases {
      create("DeckBoxDatabase") {
        packageName.set("app.deckbox")
      }
    }
    linkSqlite.set(true)
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(projects.core)

        api(libs.sqldelight.coroutines)
        api(libs.sqldelight.async)
        api(libs.kotlinx.datetime)
        implementation(libs.sqldelight.primitive)
      }
    }

    val androidMain by getting {
      dependencies {
        implementation(libs.sqldelight.android)
      }
    }

    val iosMain by getting {
      dependencies {
        implementation(libs.sqldelight.native)
      }
    }

    val jvmMain by getting {
      dependencies {
        implementation(libs.sqldelight.sqlite)
      }
    }
  }
}

addKspDependencyForCommon(projects.di.kotlinInjectMerge)
