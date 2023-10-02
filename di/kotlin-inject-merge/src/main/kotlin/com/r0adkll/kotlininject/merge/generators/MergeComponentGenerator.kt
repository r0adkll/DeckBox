package com.r0adkll.kotlininject.merge.generators

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.validate
import com.r0adkll.kotlininject.merge.MergeContext
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import com.r0adkll.kotlininject.merge.annotations.ContributesMultibinding
import com.r0adkll.kotlininject.merge.annotations.ContributesSubcomponent
import com.r0adkll.kotlininject.merge.annotations.ContributesTo
import com.r0adkll.kotlininject.merge.annotations.MergeComponent
import com.r0adkll.kotlininject.merge.util.addIfNonNull
import com.r0adkll.kotlininject.merge.util.buildClass
import com.r0adkll.kotlininject.merge.util.buildFile
import com.r0adkll.kotlininject.merge.util.kotlinpoet.addBinding
import com.r0adkll.kotlininject.merge.util.ksp.findAnnotation
import com.r0adkll.kotlininject.merge.util.ksp.getScope
import com.r0adkll.kotlininject.merge.util.ksp.getSymbolsWithClassAnnotation
import com.r0adkll.kotlininject.merge.util.ksp.isInterface
import com.r0adkll.kotlininject.merge.util.toClassName
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.addOriginatingKSFile
import com.squareup.kotlinpoet.ksp.toAnnotationSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import kotlin.reflect.KClass
import me.tatarka.inject.annotations.Component

class MergeComponentGenerator : Generator {

  override val annotation: KClass<*>
    get() = MergeComponent::class

  override fun generate(
    context: MergeContext,
    element: KSDeclaration,
  ): GeneratedSpec {
    require(element is KSClassDeclaration) { "${annotation.simpleName} requires a KSClassDeclaration" }
    return process(context, element)
  }

  override fun generate(context: MergeContext): List<GeneratedSpec> {
    return context.resolver
      .getSymbolsWithClassAnnotation(annotation)
      .mapNotNull { element ->
        if (element.validate()) {
          process(context, element)
        } else {
          context.defer(element, annotation)
          null
        }
      }
      .toList()
  }

  private fun process(
    context: MergeContext,
    element: KSClassDeclaration,
  ): GeneratedSpec {
    val packageName = "kotlininject.merge.${element.packageName.asString()}"
    val classSimpleName = "Merged${element.simpleName.asString()}"
    val className = ClassName(packageName, classSimpleName)

    return FileSpec.buildFile(packageName, classSimpleName) {
      // Recursively Generate a component and its contributed subcomponents
      addType(generateComponent(context, packageName, element))

      // Create Companion extension method on original element to create this component
      val constructorParameters = getConstructorParameters(element)
      addFunction(
        FunSpec.builder("create$classSimpleName")
          .receiver(element.toClassName().nestedClass("Companion"))
          .addParameters(constructorParameters)
          .addStatement(
            "return %T.create(${constructorParameters.joinToString { "%L" }})",
            className,
            *constructorParameters.map { it.name }.toTypedArray(),
          )
          .returns(className)
          .build(),
      )
    } isAggregating true
  }


  private fun FileSpec.Builder.generateComponent(
    context: MergeContext,
    packageName: String,
    element: KSClassDeclaration,
    parent: ClassName? = null,
  ): TypeSpec = with(context) {
    val classSimpleName = "Merged${element.simpleName.asString()}"
    val className = ClassName(packageName, classSimpleName)
    val isSubcomponent: Boolean = parent != null

    val annotationKlass = if (isSubcomponent) ContributesSubcomponent::class else MergeComponent::class
    val scope = element.findAnnotation(annotationKlass)
      ?.getScope()
      ?.toClassName()
      ?: throw IllegalArgumentException("Unable to find scope for ${element.simpleName}")

    // Pull the contributed components for the scope
    val subcomponents = classScanner.findContributedClasses(
      annotation = ContributesSubcomponent::class,
      scope = scope,
    ) + contributionCache.getContributedClasses(
      annotation = ContributesSubcomponent::class,
      scope = scope,
    )

    val modules = classScanner.findContributedClasses(
      annotation = ContributesTo::class,
      scope = scope,
    ) + contributionCache.getContributedClasses(
      annotation = ContributesTo::class,
      scope = scope,
    )

    val bindings = classScanner.findContributedClasses(
      annotation = ContributesBinding::class,
      scope = scope,
    ) + contributionCache.getContributedClasses(
      annotation = ContributesBinding::class,
      scope = scope,
    )

    val multiBindings = classScanner.findContributedClasses(
      annotation = ContributesMultibinding::class,
      scope = scope,
    ) + contributionCache.getContributedClasses(
      annotation = ContributesMultibinding::class,
      scope = scope,
    )

    // Build the kotlin poet code
    return TypeSpec.buildClass(classSimpleName) {
      // Add this original file + contributed modules to the metadata
      // for incremental processing
      element.containingFile
        ?.let { addOriginatingKSFile(it) }
      modules
        .mapNotNull { it.containingFile }
        .forEach { addOriginatingKSFile(it) }
      subcomponents
        .mapNotNull { it.containingFile }
        .forEach { addOriginatingKSFile(it) }

      addModifiers(KModifier.ABSTRACT)

      // Mark our generated class as the component to be generated
      addAnnotation(Component::class)

      // Setup the constructor / superclass
      if (element.isInterface) {
        addSuperinterface(element.toClassName())
      } else {
        superclass(element.toClassName())
      }

      val constructorParams = getConstructorParameters(element)

      // If this is a subcomponent, i.e it has a parent,
      // then we need to add it's parent as an @Component parameter, but
      // not add it to the superclass constructor params
      val parentParameter = if (parent != null) {
        // Add the initializer property to the class to properly create the
        // constructor parent component reference.
        addProperty(
          PropertySpec.builder("parent", parent)
            .initializer("parent")
            .build(),
        )

        ParameterSpec.builder("parent", parent)
          .addAnnotation(Component::class)
          .build()
      } else null

      primaryConstructor(
        FunSpec.constructorBuilder()
          .addParameters(constructorParams addIfNonNull parentParameter)
          .build(),
      )

      constructorParams.map { it.name }.forEach {
        addSuperclassConstructorParameter("%L", it)
      }

      // Add all the contributed interfaces
      addSuperinterfaces(
        modules
          .map { it.toClassName() }
          .toList(),
      )

      // Generate all the contributed bindings
      bindings.forEach { binding ->
        addBinding(binding, ContributesBinding::class)
      }

      // Generate all the contributed multi-bindings
      multiBindings.forEach { multiBinding ->
        addBinding(multiBinding, ContributesMultibinding::class)
      }

      // Now iterate through all the subcomponents, and add them
      subcomponents.forEach { subcomponent ->
        val subcomponentClassSimpleName = "Merged${subcomponent.simpleName.asString()}"
        val subcomponentClassName = className.nestedClass(subcomponentClassSimpleName)
        val subcomponentConstructorParams = getConstructorParameters(subcomponent)

        // Generate a creation method for the component
        addFunction(
          FunSpec.builder("create${subcomponent.simpleName.asString().replaceFirstChar { it.uppercaseChar() }}")
            .returns(subcomponentClassName)
            .addParameters(subcomponentConstructorParams)
            .addStatement(
              "return %T.create(${subcomponentConstructorParams.joinToString { "%L" }}${if (subcomponentConstructorParams.isNotEmpty()) ", " else ""}this)",
              subcomponentClassName,
              *subcomponentConstructorParams.map { it.name }.toTypedArray(),
            )
            .build(),
        )

        // Generate the Subcomponent
        addType(
          generateComponent(
            context = context,
            packageName = packageName,
            element = subcomponent,
            parent = className,
          ),
        )
      }

      // Every component should have an empty companion object
      addType(
        TypeSpec.companionObjectBuilder()
          .build(),
      )
    }
  }

  private fun getConstructorParameters(element: KSClassDeclaration): List<ParameterSpec> {
    return element.primaryConstructor?.let { primaryConstructor ->
      primaryConstructor.parameters.map { param ->
        ParameterSpec.builder(
          name = param.name!!.asString(),
          type = param.type.toTypeName(),
        )
          .addAnnotations(
            param.annotations
              .map { annotation -> annotation.toAnnotationSpec() }
              .toList(),
          )
          .build()
      }
    } ?: emptyList()
  }
}
