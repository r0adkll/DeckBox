import app.deckbox.convention.addKspDependencyForCommon

plugins {
  alias(libs.plugins.composeMultiplatform)
  id("app.deckbox.android.library")
  id("app.deckbox.multiplatform")
  alias(libs.plugins.ksp)
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(projects.core)
        api(projects.common.screens)
        api(projects.common.resources.strings)
        api(projects.common.settings)

        api(libs.circuit.foundation)
        api(libs.circuit.overlay)
        api(libs.compose.material3.windowsizeclass)
        api(libs.insetsx)
        api(libs.imageloader)

        api(compose.foundation)
        api(compose.material)
        api(compose.material3)
        api(compose.materialIconsExtended)
        api(compose.animation)
        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
        api(compose.components.resources)

        api(libs.paging.compose)
      }
    }

    val jvmCommon by creating {
      dependsOn(commonMain)

      dependencies {
      }
    }

    val jvmMain by getting {
      dependsOn(jvmCommon)
    }

    val androidMain by getting {
      dependsOn(jvmCommon)

      dependencies {
        implementation(libs.androidx.activity.compose)
        api(libs.androidx.paging.runtime)
        api(libs.androidx.paging.compose)
      }
    }
  }
}

android {
  sourceSets {
    named("main") {
      resources.srcDir("src/commonMain/resources")
    }
  }
}

addKspDependencyForCommon(projects.di.kotlinInjectMerge)
