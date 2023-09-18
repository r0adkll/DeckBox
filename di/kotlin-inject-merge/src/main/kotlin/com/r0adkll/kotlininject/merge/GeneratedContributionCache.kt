package com.r0adkll.kotlininject.merge

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import com.r0adkll.kotlininject.merge.annotations.ContributesMultibinding
import com.r0adkll.kotlininject.merge.annotations.ContributesSubcomponent
import com.r0adkll.kotlininject.merge.annotations.ContributesTo
import com.r0adkll.kotlininject.merge.util.ksp.findAnnotation
import com.r0adkll.kotlininject.merge.util.ksp.getParentScope
import com.r0adkll.kotlininject.merge.util.ksp.getScope
import com.r0adkll.kotlininject.merge.util.ksp.hasAnnotation
import com.r0adkll.kotlininject.merge.util.toClassName
import com.squareup.kotlinpoet.ClassName
import kotlin.reflect.KClass

class GeneratedContributionCache {

  private val contributions = mutableListOf<Contribution>()

  fun add(element: KSClassDeclaration) {
    val (scope, annotation) = when {
      element.hasAnnotation(ContributesBinding::class) -> {
        element.findAnnotation(ContributesBinding::class)!!.getScope()!! to ContributesBinding::class
      }
      element.hasAnnotation(ContributesMultibinding::class) -> {
        element.findAnnotation(ContributesMultibinding::class)!!.getScope()!! to ContributesMultibinding::class
      }
      element.hasAnnotation(ContributesTo::class) -> {
        element.findAnnotation(ContributesTo::class)!!.getScope()!! to ContributesTo::class
      }
      element.hasAnnotation(ContributesSubcomponent::class) -> {
        element.findAnnotation(ContributesSubcomponent::class)!!.getParentScope()!! to ContributesSubcomponent::class
      }
      else -> null
    } ?: return

    contributions += Contribution(element, scope.toClassName(), annotation)
  }

  fun getContributedClasses(annotation: KClass<*>, scope: ClassName): List<KSClassDeclaration> {
    return contributions
      .filter { it.scope == scope && it.annotation == annotation }
      .map { it.element }
  }
}

data class Contribution(
  val element: KSClassDeclaration,
  val scope: ClassName,
  val annotation: KClass<*>,
)
