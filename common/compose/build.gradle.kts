plugins {
  alias(libs.plugins.composeMultiplatform)
  id("app.deckbox.android.library")
  id("app.deckbox.multiplatform")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(projects.common.screens)

        api(compose.material3)
        api(libs.circuit.foundation)
        api(libs.compose.material3.windowsizeclass)
        api(libs.insetsx)

        implementation(projects.core)
        implementation(compose.foundation)
        implementation(compose.material)
        implementation(compose.materialIconsExtended)
        implementation(compose.animation)
        implementation(libs.paging.compose)

//                implementation(libs.uuid)

      }
    }

    val jvmCommon by creating {
      dependsOn(commonMain)

      dependencies {
//                implementation(projects.thirdparty.composeMaterialDialogs.datetime)
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
