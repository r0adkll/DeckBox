plugins {
  id("app.deckbox.kotlin.jvm")
  alias(libs.plugins.ktor)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.ksp)
}

group = "app.deckbox"
version = "0.0.1"

application {
  mainClass.set("app.deckbox.ApplicationKt")

  val isDevelopment: Boolean = project.ext.has("development")
  applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

ksp {
  arg("me.tatarka.inject.generateCompanionExtensions", "true")
}

dependencies {
  implementation(libs.server.postgres)
  implementation(libs.server.h2)
  implementation(libs.server.exposed.core)
  implementation(libs.server.exposed.jdbc)
  implementation(libs.server.logback)

  implementation(libs.ktor.server.core)
  implementation(libs.ktor.server.serialization.json)
  implementation(libs.ktor.server.content.negotiation)
  implementation(libs.ktor.server.metrics)
  implementation(libs.ktor.server.call.logging)
  implementation(libs.ktor.server.default.headers)
  implementation(libs.ktor.server.cors)
  implementation(libs.ktor.server.compression)
  implementation(libs.ktor.server.caching.headers)
  implementation(libs.ktor.server.resources)
  implementation(libs.ktor.server.netty)

  implementation(projects.di.kotlinInjectMergeAnnotations)
  implementation(libs.kotlininject.runtime)
  ksp(libs.kotlininject.ksp)
  ksp(projects.di.kotlinInjectMerge)

  testImplementation(libs.ktor.server.tests)
  testImplementation(libs.server.kotlin.junit)
}
