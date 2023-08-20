import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  id("app.deckbox.kotlin.jvm")
  id("app.deckbox.compose")
}

dependencies {
  implementation(projects.shared)
  implementation(compose.desktop.currentOs)
}

compose.desktop {
  application {
    mainClass = "app.deckbox.MainKt"

    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "app.deckbox"
      packageVersion = "1.0.0"
    }
  }
}
