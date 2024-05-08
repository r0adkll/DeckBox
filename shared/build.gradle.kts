import app.deckbox.convention.addKspDependencyForAllTargets
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
  id("app.deckbox.android.library")
  id("app.deckbox.multiplatform")
  id("app.deckbox.compose")
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
    commonMain {
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

        api(projects.features.collection.impl)
        api(projects.features.collection.ui)

        api(projects.features.decks.impl)
        api(projects.features.decks.ui)

        api(projects.features.boosterpacks.impl)
        api(projects.features.boosterpacks.ui)

        api(projects.features.playtest.impl)
        api(projects.features.playtest.ui)

        api(projects.features.tournament.impl)
        api(projects.features.tournament.ui)

        // UI Modules
        api(projects.ui.browse)
        api(projects.ui.filter)
        api(projects.ui.settings)

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
        api(libs.circuitx.gesturenav)
      }
    }

    androidMain {
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

android {
  sourceSets {
    named("main") {
      resources.srcDir("src/commonMain/resources")
    }
  }
}

addKspDependencyForAllTargets(libs.kotlininject.ksp)
addKspDependencyForAllTargets(projects.di.kotlinInjectMerge)
