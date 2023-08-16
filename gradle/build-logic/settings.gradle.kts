dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
  }

  versionCatalogs {
    create("libs") {
      from(files("../libs.versions.toml"))
    }
  }
}

buildCache {
  val isCi = System.getenv().containsKey("CI")
  local {
    isEnabled = !isCi
  }
}

include(":convention")
