package com.r0adkll.kotlininject.merge.util.ksp

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSType
import kotlin.reflect.KClass

fun KSClassDeclaration.findBindingTypeFor(
  bindingAnnotation: KClass<*>,
): KSDeclaration {

  // 1) Check if there is an explicit type defined by the binding annotation
  val annotation = findAnnotation(bindingAnnotation)
    ?: error("Unable to find annotation, ${bindingAnnotation.simpleName}, on class ${simpleName.asString()}")

  val boundTypeArgument = annotation.argumentAt(BOUND_TYPE_NAME, BOUND_TYPE_POSITIONAL_INDEX)
  val defaultTypeArgument = annotation.defaultArgumentAt(BOUND_TYPE_NAME, BOUND_TYPE_POSITIONAL_INDEX)

  return if (boundTypeArgument != null && boundTypeArgument.value != defaultTypeArgument?.value) {
    (boundTypeArgument.value as KSType).declaration
  } else {
    val superTypeCount = superTypes.count()
    if (superTypeCount == 0) error("Bound implementation must have a single supertype, " +
      "or specify a 'boundType' if extending more than one supertype.")
    if (superTypeCount > 1) error("Bound implementation is extending more than one supertype. " +
      "Please specify an explicit 'boundType'.")

    superTypes.first().findActualType()
  }
}

private const val BOUND_TYPE_NAME = "boundType"
private const val BOUND_TYPE_POSITIONAL_INDEX = 1
