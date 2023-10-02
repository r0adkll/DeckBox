package com.r0adkll.kotlininject.merge.generators

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.validate
import com.r0adkll.kotlininject.merge.MergeContext
import com.r0adkll.kotlininject.merge.REFERENCE_SUFFIX
import com.r0adkll.kotlininject.merge.SCOPE_SUFFIX
import com.r0adkll.kotlininject.merge.util.buildFile
import com.r0adkll.kotlininject.merge.util.ksp.getSymbolsWithClassAnnotation
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName
import kotlin.reflect.KClass

abstract class HintGenerator(
  private val hintPackageName: String,
) : Generator {

  abstract fun getScope(element: KSClassDeclaration): ClassName

  override fun generate(
    context: MergeContext,
    element: KSDeclaration,
  ): GeneratedSpec {
    require(element is KSClassDeclaration) { "${annotation.simpleName} requires KSClassDeclarations only" }
    return process(context, element)
  }

  override fun generate(context: MergeContext): List<GeneratedSpec> {
    return context.resolver.getSymbolsWithClassAnnotation(annotation)
      .mapNotNull { element ->
        if (element.validate()) {
          process(context, element)
        } else {
          context.defer(element, annotation)
          null
        }
      }
      .toList()
  }

  private fun process(
    context: MergeContext,
    element: KSClassDeclaration,
  ): GeneratedSpec {
    val fileName = element.simpleName.asString()
    val className = element.toClassName()
    val propertyName = element.qualifiedName!!.asString()
      .replace(".", "_")

    val scope = getScope(element)

    // Cache this for the session
    context.contributionCache.add(element)

    return FileSpec.buildFile(hintPackageName, fileName) {
      // Reference Hint
      addProperty(
        PropertySpec
          .builder(
            name = propertyName + REFERENCE_SUFFIX,
            type = KClass::class.asClassName().parameterizedBy(className),
          )
          .initializer("%T::class", className)
          .addModifiers(KModifier.PUBLIC)
          .build(),
      )

      // Scope Hint
      addProperty(
        PropertySpec
          .builder(
            name = propertyName + SCOPE_SUFFIX,
            type = KClass::class.asClassName().parameterizedBy(scope),
          )
          .initializer("%T::class", scope)
          .addModifiers(KModifier.PUBLIC)
          .build(),
      )
    } isAggregating false
  }
}
