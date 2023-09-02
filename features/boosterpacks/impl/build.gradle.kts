import app.deckbox.convention.addKspDependencyForCommon

plugins {
  id("app.deckbox.android.library")
  id("app.deckbox.multiplatform")
  alias(libs.plugins.ksp)
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(projects.core)
        implementation(projects.data.db)

        api(projects.features.cards.public)
        api(projects.features.boosterpacks.public)

        implementation(libs.store)
        implementation(libs.kotlinx.atomicfu)
      }
    }
  }
}

addKspDependencyForCommon(projects.di.kotlinInjectMerge)
