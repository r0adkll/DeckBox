plugins {
    alias(libs.plugins.composeMultiplatform)
    id("app.deckbox.android.library")
    id("app.deckbox.multiplatform")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core)

//                implementation(libs.circuit.foundation)
//                implementation(libs.circuit.overlay)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}
