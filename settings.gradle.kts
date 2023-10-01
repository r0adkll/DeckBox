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

    // Prerelease versions of Compose Multiplatform
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }
}

val isCi = providers.environmentVariable("CI").isPresent
buildCache {
  local {
    isEnabled = !isCi
  }
}

rootProject.name = "DeckBox"
include(":androidApp")
include(":desktopApp")
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
  ":features:boosterpacks:public",
  ":features:boosterpacks:impl",
  ":features:boosterpacks:ui",
)
include(
  ":features:decks:public",
  ":features:decks:public-ui",
  ":features:decks:impl",
  ":features:decks:ui",
)
include(
  ":ui:browse",
  ":ui:filter",
  ":ui:settings",
)
include(
  ":di:kotlin-inject-merge",
  ":di:kotlin-inject-merge-annotations",
)
include(
  ":thirdparty:shimmer",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
