package com.r0adkll.kotlininject.merge.validator

import com.google.devtools.ksp.symbol.KSClassDeclaration

interface ClassValidator {

  fun validate(element: KSClassDeclaration): Boolean
}
