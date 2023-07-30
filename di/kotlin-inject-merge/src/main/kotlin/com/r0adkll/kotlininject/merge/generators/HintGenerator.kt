package com.r0adkll.kotlininject.merge.generators

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.r0adkll.kotlininject.merge.GeneratedContributionCache
import com.r0adkll.kotlininject.merge.HINT_BINDING_PACKAGE
import com.r0adkll.kotlininject.merge.HINT_CONTRIBUTES_PACKAGE
import com.r0adkll.kotlininject.merge.REFERENCE_SUFFIX
import com.r0adkll.kotlininject.merge.SCOPE_SUFFIX
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import com.r0adkll.kotlininject.merge.annotations.ContributesTo
import com.r0adkll.kotlininject.merge.util.buildFile
import com.r0adkll.kotlininject.merge.util.findAnnotation
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
  private val contributionCache: GeneratedContributionCache,
) : Generator {

  abstract fun getScope(element: KSClassDeclaration): ClassName

  override fun generate(element: KSClassDeclaration): GeneratedSpec {
    val fileName = element.simpleName.asString()
    val className = element.toClassName()
    val propertyName = element.qualifiedName!!.asString()
      .replace(".", "_")

    val scope = getScope(element)

    // Cache this for the session
    contributionCache.add(element)

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
