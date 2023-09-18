package com.r0adkll.kotlininject.merge.annotations

import kotlin.reflect.KClass

/**
 * This is for merging contributed interfaces and bindings to the graph
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
annotation class MergeComponent(
  val scope: KClass<*>,
)
