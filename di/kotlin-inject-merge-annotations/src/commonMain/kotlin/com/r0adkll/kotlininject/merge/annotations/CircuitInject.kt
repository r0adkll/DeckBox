package com.r0adkll.kotlininject.merge.annotations

import kotlin.reflect.KClass

/**
 * TODO: Move this to a separate module once we better productionize this processor
 *   and finish plugin APIs
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class CircuitInject(
  val scope: KClass<*>,
  val screen: KClass<*>,
)
