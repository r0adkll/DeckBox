import app.deckbox.convention.addKspDependencyForCommon

plugins {
  id("app.deckbox.android.library")
  id("app.deckbox.multiplatform")
  id("app.deckbox.compose")
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

        api(projects.thirdparty.shimmer)

        api(libs.circuit.foundation)
        api(libs.circuit.overlay)
        api(libs.compose.material3.windowsizeclass)
        api(libs.insetsx)
        api(libs.imageloader)
        api(libs.materialcolorsutilities)

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
      }
    }
  }
}

addKspDependencyForCommon(projects.di.kotlinInjectMerge)
