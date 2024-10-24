// Copyright 2023, Drew Heavner and the Deckbox project contributors
// SPDX-License-Identifier: Apache-2.0

package app.deckbox.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompileCommon

@OptIn(ExperimentalKotlinGradlePluginApi::class)
class KotlinMultiplatformConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    with(pluginManager) {
      apply("org.jetbrains.kotlin.multiplatform")
    }

    extensions.configure<KotlinMultiplatformExtension> {
      applyDefaultHierarchyTemplate()

      jvm()
      if (pluginManager.hasPlugin("com.android.library")) {
        androidTarget()
      }

      listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
      ).forEach { target ->
        target.binaries.framework {
          baseName = path.substring(1).replace(':', '-')
        }
      }

      targets.withType<KotlinNativeTarget>().configureEach {
        binaries.all {
          // Add linker flag for SQLite. See:
          // https://github.com/touchlab/SQLiter/issues/77
          linkerOpts("-lsqlite3")
        }

        compilations.configureEach {
          compilerOptions.configure {
            // Try out preview custom allocator in K/N 1.9
            // https://kotlinlang.org/docs/whatsnew19.html#preview-of-custom-memory-allocator
            freeCompilerArgs.add("-Xallocator=custom")

            // https://kotlinlang.org/docs/whatsnew19.html#compiler-option-for-c-interop-implicit-integer-conversions
            freeCompilerArgs.add("-XXLanguage:+ImplicitSignedToUnsignedIntegerConversion")

            // Enable debug symbols:
            // https://kotlinlang.org/docs/native-ios-symbolication.html
            freeCompilerArgs.add("-Xadd-light-debug=enable")

            // Various opt-ins
            freeCompilerArgs.addAll(
              "-opt-in=kotlinx.cinterop.ExperimentalForeignApi",
              "-opt-in=kotlinx.cinterop.BetaInteropApi",
            )
          }
        }
      }

      targets.configureEach {
        compilations.configureEach {
          compilerOptions.configure {
            freeCompilerArgs.add("-Xexpect-actual-classes")
          }
        }
      }

      metadata {
        compilations.configureEach {
          if (name == KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME) {
            compileTaskProvider.configure {
              // We replace the default library names with something more unique (the project path).
              // This allows us to avoid the annoying issue of `duplicate library name: foo_commonMain`
              // https://youtrack.jetbrains.com/issue/KT-57914
              val projectPath = this@with.path.substring(1).replace(":", "_")
              this as KotlinCompileCommon
              moduleName.set("${projectPath}_commonMain")
            }
          }
        }
      }

      configureSpotless()
      configureKotlin()
    }
  }
}

fun Project.addKspDependencyForAllTargets(dependencyNotation: Any) =
  addKspDependencyForAllTargets("", dependencyNotation)

fun Project.addKspTestDependencyForAllTargets(dependencyNotation: Any) =
  addKspDependencyForAllTargets("Test", dependencyNotation)

fun Project.addKspDependencyForCommon(dependencyNotation: Any) {
  dependencies {
    add("kspCommonMainMetadata", dependencyNotation)
  }

  tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().all {
    if (name != "kspCommonMainKotlinMetadata") {
      dependsOn("kspCommonMainKotlinMetadata")
    }
  }

  extensions.configure<KotlinMultiplatformExtension> {
    sourceSets["commonMain"].apply {
      kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
    }
  }
}

private fun Project.addKspDependencyForAllTargets(
  configurationNameSuffix: String,
  dependencyNotation: Any,
) {
  val kmpExtension = extensions.getByType<KotlinMultiplatformExtension>()
  dependencies {
    kmpExtension.targets
      .asSequence()
      .filter { target ->
        // Don't add KSP for common target, only final platforms
        target.platformType != KotlinPlatformType.common
      }
      .forEach { target ->
        add(
          "ksp${target.targetName.capitalized()}$configurationNameSuffix",
          dependencyNotation,
        )
      }
  }
}
