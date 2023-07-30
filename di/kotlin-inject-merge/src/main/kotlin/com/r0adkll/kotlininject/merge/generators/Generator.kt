package com.r0adkll.kotlininject.merge.generators

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.FileSpec

interface Generator {

  fun generate(element: KSClassDeclaration): GeneratedSpec
}

infix fun FileSpec.isAggregating(isAggregating: Boolean): GeneratedSpec = GeneratedSpec(this, isAggregating)

data class GeneratedSpec(
  val fileSpec: FileSpec,
  val isAggregating: Boolean,
)
