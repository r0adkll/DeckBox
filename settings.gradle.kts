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
include(":data:network")
include(":data:db")
include(":common:screens")
include(":common:compose")
include(
  ":ui:browse",
  ":ui:cards",
  ":ui:decks",
  ":ui:expansions"
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
