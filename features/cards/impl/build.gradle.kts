import app.deckbox.convention.addKspDependencyForCommon

plugins {
  id("app.deckbox.android.library")
  id("app.deckbox.multiplatform")
  alias(libs.plugins.ksp)
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(projects.core)
        implementation(projects.data.db)
        implementation(projects.data.network.public)

        api(projects.features.cards.public)

        implementation(libs.store)
        implementation(libs.kotlinx.atomicfu)
      }
    }
  }
}

addKspDependencyForCommon(projects.di.kotlinInjectMerge)
