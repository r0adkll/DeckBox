import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

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
      allWarningsAsErrors.set(true)

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
}
