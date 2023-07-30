plugins {
  kotlin("jvm")
}

dependencies {
  implementation(libs.kotlininject.runtime)
  implementation(libs.kotlin.ksp)
  implementation(libs.kotlinpoet)
  implementation(libs.kotlinpoet.ksp)

  implementation(projects.di.kotlinInjectMergeAnnotations)
}
