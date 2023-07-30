pluginManagement {
  includeBuild("gradle/build-logic")
  repositories {
    google()
    gradlePluginPortal()
    mavenCentral()
  }
}

dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "DeckBox"
include(":androidApp")
include(":shared")
include(":core")
include(":common:screens")
include(":common:compose")
include(
  ":data:network:public",
  ":data:network:impl",
  ":data:db",
)
include(
  ":features:expansions:public",
  ":features:expansions:impl",
  ":features:expansions:ui",
)
include(
  ":ui:browse",
  ":ui:cards",
  ":ui:decks",
)
include(
  ":di:kotlin-inject-merge",
  ":di:kotlin-inject-merge-annotations",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
