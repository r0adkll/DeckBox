package com.r0adkll.kotlininject.merge

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import com.r0adkll.kotlininject.merge.annotations.ContributesTo
import com.r0adkll.kotlininject.merge.annotations.MergeComponent
import com.r0adkll.kotlininject.merge.annotations.ContributesSubcomponent
import com.r0adkll.kotlininject.merge.generators.ContributesBindingGenerator
import com.r0adkll.kotlininject.merge.generators.ContributesToGenerator
import com.r0adkll.kotlininject.merge.generators.Generator
import com.r0adkll.kotlininject.merge.generators.MergeComponentGenerator
import com.r0adkll.kotlininject.merge.generators.ContributesSubcomponentGenerator
import com.r0adkll.kotlininject.merge.util.getSymbolsWithClassAnnotation
import com.r0adkll.kotlininject.merge.util.hasAnnotation
import com.r0adkll.kotlininject.merge.validator.ContributesBindingValidator
import com.r0adkll.kotlininject.merge.validator.ContributesToValidator
import com.r0adkll.kotlininject.merge.validator.MergeComponentValidator
import com.squareup.kotlinpoet.ksp.writeTo

class MergeSymbolProcessor(
  private val codeGenerator: CodeGenerator,
  private val logger: KSPLogger,
) : SymbolProcessor {

  private var deferred: MutableList<KSClassDeclaration> = mutableListOf()

  override fun process(resolver: Resolver): List<KSAnnotated> {
    val classScanner = ClassScanner(resolver, logger)
    val contributionCache = GeneratedContributionCache()

    val previousDiffered = deferred
    deferred = mutableListOf()

    val contributesBinding = resolver.getSymbolsWithClassAnnotation(ContributesBinding::class)
    val contributesTo = resolver.getSymbolsWithClassAnnotation(ContributesTo::class)
    val contributesSubcomponent = resolver.getSymbolsWithClassAnnotation(ContributesSubcomponent::class)
    val mergeComponent = resolver.getSymbolsWithClassAnnotation(MergeComponent::class)

    for (element in previousDiffered + contributesBinding + contributesTo + contributesSubcomponent + mergeComponent) {
      logger.info("Processing Element", element)
      if (element.validate()) {
        process(element, classScanner, contributionCache)
      } else {
        deferred.add(element)
      }
    }

    return deferred
  }

  private fun validate(element: KSClassDeclaration): Boolean {
    return when {
      element.hasAnnotation(ContributesBinding::class) -> ContributesBindingValidator.validate(element)
      element.hasAnnotation(ContributesTo::class) -> ContributesToValidator.validate(element)
      element.hasAnnotation(MergeComponent::class) -> MergeComponentValidator.validate(element)
      else -> false
    }
  }

  private fun process(
    element: KSClassDeclaration,
    classScanner: ClassScanner,
    contributionCache: GeneratedContributionCache,
  ) {
    val generator: Generator = when {
      element.hasAnnotation(ContributesBinding::class) -> ContributesBindingGenerator(contributionCache)
      element.hasAnnotation(ContributesTo::class) -> ContributesToGenerator(contributionCache)
      element.hasAnnotation(ContributesSubcomponent::class) -> ContributesSubcomponentGenerator(contributionCache)
      element.hasAnnotation(MergeComponent::class) -> MergeComponentGenerator(classScanner, contributionCache, logger)
      else -> throw IllegalArgumentException("Unable to process: $element")
    }

    try {
      val generatedSpec = generator.generate(element)
      generatedSpec.fileSpec.writeTo(codeGenerator, aggregating = generatedSpec.isAggregating)
    } catch (e: Exception) {
//      provider.error(e.message.orEmpty(), e.element)
      logger.error("Unable to generate code", element)
      logger.exception(e)
      // Continue so we can see all errors
    }
  }
}

class MergeSymbolProcessorProvider : SymbolProcessorProvider {
  override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
    println("Creating MergeSymbolProcessor")
    return MergeSymbolProcessor(
      codeGenerator = environment.codeGenerator,
      logger = environment.logger,
    )
  }
}
