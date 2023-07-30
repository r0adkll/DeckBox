package com.r0adkll.kotlininject.merge.validator

import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.visitor.KSValidateVisitor
import com.r0adkll.kotlininject.merge.util.hasAnnotation
import me.tatarka.inject.annotations.Inject

object MergeComponentValidator : ClassValidator {

  override fun validate(element: KSClassDeclaration): Boolean {
    return element.isAbstract()
  }
}
