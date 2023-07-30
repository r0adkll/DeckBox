package com.r0adkll.kotlininject.merge.util

import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import com.r0adkll.kotlininject.merge.annotations.ContributesTo
import com.r0adkll.kotlininject.merge.annotations.MergeComponent
import com.squareup.kotlinpoet.asClassName

object ClassNames {

  val ContributesBinding = ContributesBinding::class.asClassName()
  val ContributesTo = ContributesTo::class.asClassName()
  val MergeComponent = MergeComponent::class.asClassName()
}
