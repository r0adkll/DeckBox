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

        api(libs.circuit.foundation)
        api(libs.compose.material3.windowsizeclass)
        api(libs.insetsx)
        api(libs.imageloader)

        api(compose.foundation)
        api(compose.material)
        api(compose.material3)
        api(compose.materialIconsExtended)
        api(compose.animation)
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
