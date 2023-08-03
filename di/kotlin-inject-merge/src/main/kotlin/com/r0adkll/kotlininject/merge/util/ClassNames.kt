package com.r0adkll.kotlininject.merge.util

import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import com.r0adkll.kotlininject.merge.annotations.ContributesTo
import com.r0adkll.kotlininject.merge.annotations.MergeComponent
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName

object ClassNames {

  val ContributesBinding = ContributesBinding::class.asClassName()
  val ContributesTo = ContributesTo::class.asClassName()
  val MergeComponent = MergeComponent::class.asClassName()

  val Composable = ClassName("androidx.compose.runtime", "Composable")
  val Modifier = ClassName("androidx.compose.ui", "Modifier")

  object Circuit {
    val Ui = ClassName("com.slack.circuit.runtime.ui", "Ui")
    val UiFactory = ClassName("com.slack.circuit.runtime.ui", "Ui", "Factory")
    val Presenter = ClassName("com.slack.circuit.runtime.presenter", "Presenter")
    val PresenterFactory = ClassName("com.slack.circuit.runtime.presenter", "Presenter", "Factory")
    val Context = ClassName("com.slack.circuit.runtime", "CircuitContext")
    val Screen = ClassName("com.slack.circuit.runtime", "Screen")
    val Navigator = ClassName("com.slack.circuit.runtime", "Navigator")
  }
}
