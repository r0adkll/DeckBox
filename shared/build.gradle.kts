import app.deckbox.convention.addKspDependencyForAllTargets
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
  alias(libs.plugins.composeMultiplatform)
  id("app.deckbox.android.library")
  id("app.deckbox.multiplatform")
  alias(libs.plugins.ksp)
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
        api(projects.core)
        api(projects.common.screens)
        api(projects.common.compose)

        // Data Modules
        api(projects.data.db)
        api(projects.data.network.impl)

        // Feature Modules
        api(projects.features.expansions.impl)
        api(projects.features.expansions.ui)

        api(projects.features.cards.impl)
        api(projects.features.cards.ui)

        // UI Modules
        api(projects.ui.browse)
        api(projects.ui.decks)

        api(compose.runtime)
        api(compose.foundation)
        api(compose.material)
        api(compose.material3)
        api(compose.materialIconsExtended)
        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
        api(compose.components.resources)
        api(compose.ui)

        api(libs.circuit.foundation)
        api(libs.circuit.overlay)
        api(libs.circuit.runtime)
      }
    }

    val androidMain by getting {
      dependencies {
        api(libs.androidx.activity.activity)
        api(libs.androidx.activity.compose)
      }
    }
  }
}

ksp {
  arg("me.tatarka.inject.generateCompanionExtensions", "true")
}

addKspDependencyForAllTargets(libs.kotlininject.ksp)
addKspDependencyForAllTargets(projects.di.kotlinInjectMerge)
