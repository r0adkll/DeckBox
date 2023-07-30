package com.r0adkll.kotlininject.merge.generators

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.r0adkll.kotlininject.merge.GeneratedContributionCache
import com.r0adkll.kotlininject.merge.HINT_SUBCOMPONENT_PACKAGE
import com.r0adkll.kotlininject.merge.annotations.ContributesSubcomponent
import com.r0adkll.kotlininject.merge.util.findAnnotation
import com.r0adkll.kotlininject.merge.util.getParentScope
import com.r0adkll.kotlininject.merge.util.toClassName
import com.squareup.kotlinpoet.ClassName

class ContributesSubcomponentGenerator(
  contributionCache: GeneratedContributionCache,
) : HintGenerator(HINT_SUBCOMPONENT_PACKAGE, contributionCache) {

  override fun getScope(element: KSClassDeclaration): ClassName {
    return element.findAnnotation(ContributesSubcomponent::class)
      ?.getParentScope()
      ?.toClassName()
      ?: throw IllegalArgumentException("Unable to find parentScope for ${element.simpleName}")
  }
}
