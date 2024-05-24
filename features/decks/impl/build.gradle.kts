import app.deckbox.convention.addKspDependencyForAllTargets
import app.deckbox.convention.addKspDependencyForCommon

plugins {
  id("app.deckbox.android.library")
  id("app.deckbox.multiplatform")
  id("app.deckbox.compose")
  alias(libs.plugins.ksp)
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(projects.core)
        implementation(projects.common.settings)
        implementation(projects.data.db)

        api(projects.features.decks.public)
        api(projects.features.cards.public)
        api(projects.features.expansions.public)

        implementation(compose.runtime)
        implementation(compose.ui)
        implementation(libs.imageloader)
        implementation(libs.store)
        implementation(libs.kotlinx.atomicfu)
      }
    }
  }
}

addKspDependencyForCommon(projects.di.kotlinInjectMerge)
