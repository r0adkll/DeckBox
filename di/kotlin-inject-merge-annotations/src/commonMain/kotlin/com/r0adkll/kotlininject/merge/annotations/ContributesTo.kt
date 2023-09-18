package com.r0adkll.kotlininject.merge.annotations

import kotlin.reflect.KClass

/**
 * This is for contributing component interfaces to scopes on the graph
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ContributesTo(
  val scope: KClass<*>,
  val replaces: Array<KClass<*>> = [],
)
