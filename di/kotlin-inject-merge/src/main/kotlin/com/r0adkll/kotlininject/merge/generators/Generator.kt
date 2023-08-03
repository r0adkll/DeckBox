package com.r0adkll.kotlininject.merge.generators

import com.google.devtools.ksp.symbol.KSDeclaration
import com.r0adkll.kotlininject.merge.MergeContext
import com.squareup.kotlinpoet.FileSpec
import kotlin.reflect.KClass

interface Generator {
  val annotation: KClass<*>

  fun generate(
    context: MergeContext,
    element: KSDeclaration,
  ): GeneratedSpec

  fun generate(context: MergeContext): List<GeneratedSpec>
}

infix fun FileSpec.isAggregating(isAggregating: Boolean): GeneratedSpec = GeneratedSpec(this, isAggregating)

data class GeneratedSpec(
  val fileSpec: FileSpec,
  val isAggregating: Boolean,
)
