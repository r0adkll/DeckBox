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
    commonMain {
      dependencies {
        implementation(projects.core)

        api(libs.sqldelight.coroutines)
        api(libs.sqldelight.async)
        api(libs.kotlinx.datetime)
        implementation(libs.sqldelight.primitive)
      }
    }

    androidMain {
      dependencies {
        implementation(libs.sqldelight.android)
      }
    }

    iosMain {
      dependencies {
        implementation(libs.sqldelight.native)
      }
    }

    jvmMain {
      dependencies {
        implementation(libs.sqldelight.sqlite)
      }
    }
  }
}

addKspDependencyForCommon(projects.di.kotlinInjectMerge)
