package com.r0adkll.kotlininject.merge.util.ksp

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSValueArgument

fun KSAnnotation.argumentAt(name: String, index: Int): KSValueArgument? {
  /**
   * This approach is probably flawed if the user mixes both
   * positional and named arguments.
   *
   * TODO: We should put some kind of enforcement on the use of mixed
   *   argument (positional vs. named) so as to avoid the footgun here
   */
  val isPositional = arguments.any { it.name == null }
  return if (isPositional) {
    arguments.getOrNull(index)
  } else {
    arguments.find { arg -> arg.name?.asString() == name }
  }
}

fun KSAnnotation.defaultArgumentAt(name: String, index: Int): KSValueArgument? {
  /**
   * This approach is probably flawed if the user mixes both
   * positional and named arguments.
   *
   * TODO: We should put some kind of enforcement on the use of mixed
   *   argument (positional vs. named) so as to avoid the footgun here
   */
  val isPositional = defaultArguments.any { it.name == null }
  return if (isPositional) {
    defaultArguments.getOrNull(index)
  } else {
    defaultArguments.find { arg -> arg.name?.asString() == name }
  }
}
