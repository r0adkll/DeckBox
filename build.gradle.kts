import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile

plugins {
  id("app.deckbox.root")

  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.android.lint) apply false
  alias(libs.plugins.android.test) apply false
  alias(libs.plugins.cacheFixPlugin) apply false
  alias(libs.plugins.composeMultiplatform) apply false
//    alias(libs.plugins.firebase.crashlytics) apply false
//    alias(libs.plugins.gms.googleServices) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.parcelize) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.spotless) apply false
  alias(libs.plugins.sqldelight) apply false
}

allprojects {
  tasks.withType<KotlinCompilationTask<*>>().configureEach {
    compilerOptions {
      // Treat all Kotlin warnings as errors
//      allWarningsAsErrors.set(true)

      // Hack to make klib manifests have an actually unique name
//      val uniqueName = "${this@allprojects.group}.${this@allprojects.name}"
//      freeCompilerArgs.addAll(
//        "-module-name",
//        uniqueName,
//      )

      if (project.hasProperty("deckbox.enableComposeCompilerReports")) {
        freeCompilerArgs.addAll(
          "-P",
          "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
            project.buildDir.absolutePath + "/compose_metrics",
        )
        freeCompilerArgs.addAll(
          "-P",
          "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
            project.buildDir.absolutePath + "/compose_metrics",
        )
      }
    }
  }

//  pluginManager.withPlugin("kotlin-multiplatform") {
//    val kotlinExtension = project.extensions.getByName("kotlin")
//      as org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
//    val uniqueName = "${project.group}.${project.name}"
//
//    kotlinExtension.targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java) {
//      compilations.configureEach {
//        kotlinOptions.freeCompilerArgs += listOf("-module-name", uniqueName)
//      }
//    }
//  }
}
