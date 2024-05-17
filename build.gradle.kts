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

buildscript {
  dependencies {
    // Yuck. Need to force kotlinpoet:1.16.0 as that is what buildconfig uses.
    // CMP 1.6.0-x uses kotlinpoet:1.14.x. Gradle seems to force 1.14.x which then breaks
    // buildconfig.
    classpath("com.squareup:kotlinpoet:1.16.0")
  }
}

allprojects {
  val projectName = this.path
  tasks.withType<KotlinCompilationTask<*>>().configureEach {
    compilerOptions {
      // Treat all Kotlin warnings as errors
      allWarningsAsErrors.set(true)

      if (project.hasProperty("deckbox.enableComposeCompilerReports") && projectName != ":common:compose") {
        freeCompilerArgs.addAll(
          "-P",
          "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
            project.layout.buildDirectory.asFile.get().absolutePath + "/compose_metrics",
        )
        freeCompilerArgs.addAll(
          "-P",
          "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
            project.layout.buildDirectory.asFile.get().absolutePath + "/compose_metrics",
        )
      }
    }
  }
}

tasks.register<Copy>("bootstrap") {
  from(file("scripts/pre-push"))
  into(file(".git/hooks"))
  fileMode = 777
}
