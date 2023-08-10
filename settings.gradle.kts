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
include(
  ":common:screens",
  ":common:compose",
  ":common:resources:strings",
  ":common:settings",
)
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
  ":features:cards:public",
  ":features:cards:impl",
  ":features:cards:ui",
)
include(
  ":features:decks:public",
  ":features:decks:public-ui",
  ":features:decks:impl",
  ":features:decks:ui",
)
include(
  ":ui:browse",
)
include(
  ":di:kotlin-inject-merge",
  ":di:kotlin-inject-merge-annotations",
)
include(
  ":thirdparty:shimmer",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
