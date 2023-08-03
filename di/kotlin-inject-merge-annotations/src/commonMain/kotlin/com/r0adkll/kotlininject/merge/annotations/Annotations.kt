package com.r0adkll.kotlininject.merge.annotations

import kotlin.reflect.KClass

/**
 * This is for contributing concrete instances of implementations to their interfaces/types
 * on the graph
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
annotation class ContributesBinding(
  val scope: KClass<*>,
  val boundType: KClass<*> = Unit::class,
)

/**
 * This is for contributing component interfaces to scopes on the graph
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
annotation class ContributesTo(
  val scope: KClass<*>,
)

/**
 * This is for merging contributed interfaces and bindings to the graph
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
annotation class MergeComponent(
  val scope: KClass<*>,
)

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
annotation class ContributesSubcomponent(
  val scope: KClass<*>,
  val parentScope: KClass<*>,
)

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
annotation class CircuitInject(
  val scope: KClass<*>,
  val screen: KClass<*>,
)
