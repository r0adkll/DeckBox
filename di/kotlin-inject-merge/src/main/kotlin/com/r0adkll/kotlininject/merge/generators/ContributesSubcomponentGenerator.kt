package com.r0adkll.kotlininject.merge.generators

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.r0adkll.kotlininject.merge.HINT_SUBCOMPONENT_PACKAGE
import com.r0adkll.kotlininject.merge.annotations.ContributesSubcomponent
import com.r0adkll.kotlininject.merge.util.ksp.findAnnotation
import com.r0adkll.kotlininject.merge.util.ksp.getParentScope
import com.r0adkll.kotlininject.merge.util.toClassName
import com.squareup.kotlinpoet.ClassName
import kotlin.reflect.KClass

class ContributesSubcomponentGenerator : HintGenerator(HINT_SUBCOMPONENT_PACKAGE) {

  override val annotation: KClass<*>
    get() = ContributesSubcomponent::class

  override fun getScope(element: KSClassDeclaration): ClassName {
    return element.findAnnotation(ContributesSubcomponent::class)
      ?.getParentScope()
      ?.toClassName()
      ?: throw IllegalArgumentException("Unable to find parentScope for ${element.simpleName}")
  }
}
