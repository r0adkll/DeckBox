package com.r0adkll.kotlininject.merge

import com.google.devtools.ksp.symbol.KSDeclaration
import kotlin.reflect.KClass

data class DeferredSymbol(
  val element: KSDeclaration,
  val annotation: KClass<*>,
)
