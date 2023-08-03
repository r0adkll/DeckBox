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
import com.r0adkll.kotlininject.merge.util.getScope
import com.r0adkll.kotlininject.merge.util.toClassName
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName
import kotlin.reflect.KClass

class ContributesToGenerator : HintGenerator(HINT_CONTRIBUTES_PACKAGE) {

  override val annotation: KClass<*>
    get() = ContributesTo::class

  override fun getScope(element: KSClassDeclaration): ClassName {
    return element.findAnnotation(ContributesTo::class)
      ?.getScope()
      ?.toClassName()
      ?: throw IllegalArgumentException("Unable to find scope for ${element.qualifiedName}")
  }
}
