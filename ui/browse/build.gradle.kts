import app.deckbox.convention.addKspDependencyForCommon

plugins {
  alias(libs.plugins.composeMultiplatform)
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
        implementation(projects.common.screens)
        implementation(projects.common.compose)

        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.material)
        implementation(compose.material3)
        implementation(compose.materialIconsExtended)
        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
        implementation(compose.components.resources)
        implementation(compose.ui)

        implementation(libs.circuit.foundation)
        implementation(libs.circuit.runtime)
      }
    }
  }
}

addKspDependencyForCommon(projects.di.kotlinInjectMerge)
