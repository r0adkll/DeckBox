package com.r0adkll.kotlininject.merge.annotations

import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ContributesSubcomponent(
  val scope: KClass<*>,
  val parentScope: KClass<*>,
)
