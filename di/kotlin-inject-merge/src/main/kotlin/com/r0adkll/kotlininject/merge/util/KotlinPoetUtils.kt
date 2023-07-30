package com.r0adkll.kotlininject.merge.util

import com.google.devtools.ksp.symbol.KSDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
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

fun KSDeclaration.toClassName(): ClassName = ClassName(packageName.asString(), simpleName.asString())
