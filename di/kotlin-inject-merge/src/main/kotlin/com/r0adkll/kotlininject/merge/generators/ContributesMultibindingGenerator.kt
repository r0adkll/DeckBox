package com.r0adkll.kotlininject.merge.generators

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.r0adkll.kotlininject.merge.HINT_MULTIBINDING_PACKAGE
import com.r0adkll.kotlininject.merge.annotations.ContributesMultibinding
import com.r0adkll.kotlininject.merge.util.ksp.findAnnotation
import com.r0adkll.kotlininject.merge.util.ksp.getScope
import com.r0adkll.kotlininject.merge.util.toClassName
import com.squareup.kotlinpoet.ClassName
import kotlin.reflect.KClass

class ContributesMultibindingGenerator : HintGenerator(HINT_MULTIBINDING_PACKAGE) {

  override val annotation: KClass<*>
    get() = ContributesMultibinding::class

  override fun getScope(element: KSClassDeclaration): ClassName {
    return element.findAnnotation(ContributesMultibinding::class)
      ?.getScope()
      ?.toClassName()
      ?: throw IllegalArgumentException("Unable to find scope for ${element.qualifiedName}")
  }
}
