package com.r0adkll.kotlininject.merge

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.validate
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import com.r0adkll.kotlininject.merge.annotations.ContributesTo
import com.r0adkll.kotlininject.merge.annotations.MergeComponent
import com.r0adkll.kotlininject.merge.annotations.ContributesSubcomponent
import com.r0adkll.kotlininject.merge.generators.CircuitInjectGenerator
import com.r0adkll.kotlininject.merge.generators.ContributesBindingGenerator
import com.r0adkll.kotlininject.merge.generators.ContributesToGenerator
import com.r0adkll.kotlininject.merge.generators.Generator
import com.r0adkll.kotlininject.merge.generators.MergeComponentGenerator
import com.r0adkll.kotlininject.merge.generators.ContributesSubcomponentGenerator
import com.r0adkll.kotlininject.merge.generators.GeneratedSpec
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

  private var deferred: MutableList<DeferredSymbol> = mutableListOf()

  override fun process(resolver: Resolver): List<KSAnnotated> {
    val classScanner = ClassScanner(resolver, logger)
    val contributionCache = GeneratedContributionCache()
    val context = MergeContext(
      logger = logger,
      resolver = resolver,
      classScanner = classScanner,
      contributionCache = contributionCache,
    ) { element, annotation ->
      deferred += DeferredSymbol(element, annotation)
    }

    val previousDiffered = deferred
    deferred = mutableListOf()

    val codeGenerators = loadCodeGenerators()

    // Process all of the previously deferred
    for (symbol in previousDiffered) {
      codeGenerators.find { it.annotation == symbol.annotation }
        ?.generate(context, symbol.element)
        ?.let { spec ->
          process(spec)
        }
    }

    // Process each generator consecutively, order is important
    codeGenerators.forEach { generator ->
      generator.generate(context)
        .forEach { generatedSpec ->
          process(generatedSpec)
        }
    }

    return deferred.map { it.element }
  }

  private fun process(generatedSpec: GeneratedSpec) {
    try {
      generatedSpec.fileSpec.writeTo(codeGenerator, aggregating = generatedSpec.isAggregating)
    } catch (e: Exception) {
      logger.exception(e)
    }
  }

  private fun loadCodeGenerators(): List<Generator> {
    return listOf(
      CircuitInjectGenerator(),
      ContributesBindingGenerator(),
      ContributesToGenerator(),
      ContributesSubcomponentGenerator(),
      MergeComponentGenerator(),
    ) // TODO use Java service loader to load custom generators
  }
}

class MergeSymbolProcessorProvider : SymbolProcessorProvider {
  override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
    return MergeSymbolProcessor(
      codeGenerator = environment.codeGenerator,
      logger = environment.logger,
    )
  }
}
