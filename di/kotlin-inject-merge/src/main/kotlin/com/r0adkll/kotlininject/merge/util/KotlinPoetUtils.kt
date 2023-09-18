package com.r0adkll.kotlininject.merge.util

import com.google.devtools.ksp.symbol.KSDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

fun FileSpec.Companion.buildFile(
  packageName: String,
  fileName: String,
  builder: FileSpec.Builder.() -> Unit,
): FileSpec = builder(packageName, fileName)
  .apply(builder)
  .build()

fun TypeSpec.Companion.buildClass(
  name: String,
  builder: TypeSpec.Builder.() -> Unit,
): TypeSpec = classBuilder(name)
  .apply(builder)
  .build()

fun PropertySpec.Companion.buildProperty(
  name: String,
  type: ClassName,
  builder: PropertySpec.Builder.() -> Unit
): PropertySpec = builder(name, type)
  .apply(builder)
  .build()

fun FunSpec.Companion.buildFun(
  name: String,
  builder: FunSpec.Builder.() -> Unit,
): FunSpec = builder(name)
  .apply(builder)
  .build()

fun KSDeclaration.toClassName(): ClassName = ClassName(packageName.asString(), simpleName.asString())
