package com.r0adkll.kotlininject.merge.util.ksp

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeAlias
import com.google.devtools.ksp.symbol.KSTypeReference
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName
import kotlin.reflect.KClass

fun KSAnnotated.hasAnnotation(kclass: KClass<*>): Boolean {
  return hasAnnotation(kclass.asClassName())
}

fun KSAnnotated.hasAnnotation(className: ClassName): Boolean {
  return hasAnnotation(className.packageName, className.simpleName)
}

fun KSAnnotated.hasAnnotation(packageName: String, simpleName: String): Boolean {
  return annotations.any { it.hasName(packageName, simpleName) }
}

fun KSAnnotated.findAnnotation(kclass: KClass<*>): KSAnnotation? {
  val className = kclass.asClassName()
  return findAnnotation(className.packageName, className.simpleName)
}

fun KSAnnotated.findAnnotation(packageName: String, simpleName: String): KSAnnotation? {
  return annotations.firstOrNull { it.hasName(packageName, simpleName) }
}

fun KSAnnotation.isAnnotation(kclass: KClass<*>): Boolean {
  val className = kclass.asClassName()
  return hasName(className.packageName, className.simpleName)
}

fun KSAnnotation.isAnnotation(packageName: String, simpleName: String): Boolean {
  return hasName(packageName, simpleName)
}

private fun KSAnnotation.hasName(packageName: String, simpleName: String): Boolean {
  // we can skip resolving if the short name doesn't match
  if (shortName.asString() != simpleName) return false
  val declaration = annotationType.resolve().declaration
  return declaration.packageName.asString() == packageName
}

fun KSAnnotation.getScope(): KSDeclaration? {
  return let {
      it.arguments.find { it.name?.asString() == "scope" }
        ?: it.arguments.firstOrNull()
    }
    ?.value
    ?.let { it as? KSType }
    ?.declaration
}

fun KSAnnotation.getScreen(): KSDeclaration? {
  return let {
      it.arguments.find { it.name?.asString() == "screen" }
        ?: it.arguments.getOrNull(1)
    }
    ?.value
    ?.let { it as? KSType }
    ?.declaration
}

fun KSAnnotation.getParentScope(): KSDeclaration? {
  return let {
      it.arguments.find { it.name?.asString() == "parentScope" }
        ?: it.arguments.getOrNull(1)
    }
    ?.value
    ?.let { it as? KSType }
    ?.declaration
}

fun KSTypeAlias.findActualType(): KSClassDeclaration {
  val resolvedType = this.type.resolve().declaration
  return if (resolvedType is KSTypeAlias) {
    resolvedType.findActualType()
  } else {
    resolvedType as KSClassDeclaration
  }
}

fun KSTypeReference.findActualType(): KSClassDeclaration {
  val resolvedType = this.resolve().declaration
  return if (resolvedType is KSTypeAlias) {
    resolvedType.findActualType()
  } else {
    resolvedType as KSClassDeclaration
  }
}

val KSClassDeclaration.isInterface: Boolean
  get() = this.classKind == ClassKind.INTERFACE

fun Resolver.getSymbolsWithClassAnnotation(kclass: KClass<*>): Sequence<KSClassDeclaration> {
  val className = kclass.asClassName()
  return getSymbolsWithClassAnnotation(className.packageName, className.simpleName)
}

fun Resolver.getSymbolsWithFunctionAnnotation(kclass: KClass<*>): Sequence<KSFunctionDeclaration> {
  val className = kclass.asClassName()
  return getSymbolsWithFunctionAnnotation(className.packageName, className.simpleName)
}

fun Resolver.getAllSymbolsWithAnnotation(kclass: KClass<*>): Sequence<KSDeclaration> {
  val className = kclass.asClassName()
  return getAllSymbolsWithAnnotation(className.packageName, className.simpleName)
}

/**
 * A 'fast' version of [Resolver.getSymbolsWithAnnotation]. We only care about class annotations so we can skip a lot
 * of the tree.
 */
fun Resolver.getSymbolsWithClassAnnotation(packageName: String, simpleName: String): Sequence<KSClassDeclaration> {
  suspend fun SequenceScope<KSClassDeclaration>.visit(declarations: Sequence<KSDeclaration>) {
    for (declaration in declarations) {
      if (declaration is KSClassDeclaration) {
        if (declaration.hasAnnotation(packageName, simpleName)) {
          yield(declaration)
        }
        visit(declaration.declarations)
      }
    }
  }
  return sequence {
    for (file in getNewFiles()) {
      visit(file.declarations)
    }
  }
}

/**
 * A 'fast' version of [Resolver.getSymbolsWithAnnotation]. We only care about class annotations so we can skip a lot
 * of the tree.
 */
fun Resolver.getSymbolsWithFunctionAnnotation(packageName: String, simpleName: String): Sequence<KSFunctionDeclaration> {
  suspend fun SequenceScope<KSFunctionDeclaration>.visit(declarations: Sequence<KSDeclaration>) {
    for (declaration in declarations) {
      if (declaration is KSFunctionDeclaration) {
        if (declaration.hasAnnotation(packageName, simpleName)) {
          yield(declaration)
        }
        visit(declaration.declarations)
      }
    }
  }
  return sequence {
    for (file in getNewFiles()) {
      visit(file.declarations)
    }
  }
}

/**
 * A 'fast' version of [Resolver.getSymbolsWithAnnotation]. We only care about class annotations so we can skip a lot
 * of the tree.
 */
fun Resolver.getAllSymbolsWithAnnotation(packageName: String, simpleName: String): Sequence<KSDeclaration> {
  suspend fun SequenceScope<KSDeclaration>.visit(declarations: Sequence<KSDeclaration>) {
    for (declaration in declarations) {
      if (declaration is KSFunctionDeclaration) {
        if (declaration.hasAnnotation(packageName, simpleName)) {
          yield(declaration)
        }
        visit(declaration.declarations)
      } else if (declaration is KSClassDeclaration) {
        if (declaration.hasAnnotation(packageName, simpleName)) {
          yield(declaration)
        }
        visit(declaration.declarations)
      }
    }
  }
  return sequence {
    for (file in getNewFiles()) {
      visit(file.declarations)
    }
  }
}
