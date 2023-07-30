package com.r0adkll.kotlininject.merge.validator

import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.r0adkll.kotlininject.merge.util.hasAnnotation
import me.tatarka.inject.annotations.Inject

object ContributesBindingValidator : ClassValidator {

  override fun validate(element: KSClassDeclaration): Boolean {
    // Check that the class is wired for kotlin-inject
    if (!element.hasAnnotation(Inject::class)) return false
    // Class MUST be public
    if (!element.isPublic()) return false
    return false
  }
}
