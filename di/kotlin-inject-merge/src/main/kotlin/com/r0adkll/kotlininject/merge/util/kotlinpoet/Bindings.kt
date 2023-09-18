package com.r0adkll.kotlininject.merge.util.kotlinpoet

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.r0adkll.kotlininject.merge.annotations.ContributesMultibinding
import com.r0adkll.kotlininject.merge.util.buildFun
import com.r0adkll.kotlininject.merge.util.buildProperty
import com.r0adkll.kotlininject.merge.util.ksp.findBindingTypeFor
import com.r0adkll.kotlininject.merge.util.ksp.findMapKey
import com.r0adkll.kotlininject.merge.util.ksp.hasAnnotation
import com.r0adkll.kotlininject.merge.util.ksp.pairTypeOf
import com.r0adkll.kotlininject.merge.util.toClassName
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.addOriginatingKSFile
import com.squareup.kotlinpoet.ksp.toClassName
import kotlin.reflect.KClass
import me.tatarka.inject.annotations.Inject
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

fun TypeSpec.Builder.addBinding(
  boundClass: KSClassDeclaration,
  bindingAnnotationClass: KClass<*>,
): TypeSpec.Builder {
  val isMultibinding = bindingAnnotationClass == ContributesMultibinding::class
  val mapKey = if (isMultibinding) {
    boundClass.findMapKey()
  } else {
    null
  }

  val boundType = boundClass.findBindingTypeFor(bindingAnnotationClass)
  val isObject = boundClass.classKind == ClassKind.OBJECT
  val isBindable = !isObject && boundClass.hasAnnotation(Inject::class)

  if (mapKey != null) {
    addMappingProvidesFunction(
      boundClass = boundClass,
      boundType = boundType,
      mapKey = mapKey,
      isBindable = isBindable,
    )
  } else if (isBindable) {
    addBindingReceiverProperty(
      boundClass = boundClass,
      boundType = boundType.toClassName(),
      additionalAnnotations = listIf(isMultibinding, IntoSet::class),
    )
  } else {
    addProvidesFunction(
      boundClass = boundClass,
      returnType = boundType.toClassName(),
      additionalAnnotations = listIf(isMultibinding, IntoSet::class),
    )
  }

  return this
}

private fun TypeSpec.Builder.addBindingReceiverProperty(
  boundClass: KSClassDeclaration,
  boundType: ClassName,
  additionalAnnotations: List<KClass<*>> = emptyList(),
) {
  addProperty(
    PropertySpec.buildProperty(
      name = "bind",
      type = boundType,
    ) {
      if (boundClass.containingFile != null) {
        addOriginatingKSFile(boundClass.containingFile!!)
      }

      receiver(boundClass.toClassName())

      getter(
        FunSpec.getterBuilder()
          .addAnnotation(Provides::class)
          .apply {
            additionalAnnotations.forEach {
              addAnnotation(it)
            }
          }
          .addStatement("return this")
          .build(),
      )
    },
  )
}

private fun TypeSpec.Builder.addProvidesFunction(
  boundClass: KSClassDeclaration,
  returnType: TypeName,
  additionalAnnotations: List<KClass<*>> = emptyList(),
) {
  addFunction(
    FunSpec.buildFun("provide${boundClass.simpleName.asString()}") {
      returns(returnType)

      addAnnotation(Provides::class)
      additionalAnnotations.forEach { annotation ->
        addAnnotation(annotation)
      }

      if (boundClass.classKind == ClassKind.OBJECT) {
        addStatement("return %T", boundClass.toClassName())
      } else {
        addStatement("return %T()", boundClass.toClassName())
      }
    },
  )
}

private fun TypeSpec.Builder.addMappingProvidesFunction(
  boundClass: KSClassDeclaration,
  boundType: KSDeclaration,
  mapKey: Any,
  isBindable: Boolean,
) {
  addFunction(
    FunSpec.buildFun("provide${boundType.simpleName.asString()}${mapKey}") {
      returns(pairTypeOf(mapKey::class.asTypeName(), boundType.toClassName()))

      addAnnotation(Provides::class)
      addAnnotation(IntoMap::class)

      if (isBindable) {
        addParameter("value", boundClass.toClassName())
        if (mapKey is String) {
          addStatement("return %S to value", mapKey)
        } else {
          addStatement("return %L to value", mapKey)
        }
      } else {
        val valueTemplate = if (boundClass.classKind == ClassKind.OBJECT) "%T" else "%T()"
        if (mapKey is String) {
          addStatement("return %S to $valueTemplate", mapKey, boundClass.toClassName())
        } else {
          addStatement("return %L to $valueTemplate", mapKey, boundClass.toClassName())
        }
      }
    }
  )
}

private fun <T> listIf(predicate: Boolean, vararg items: T): List<T> {
  return if (predicate) {
    listOf(*items)
  } else {
    emptyList()
  }
}
