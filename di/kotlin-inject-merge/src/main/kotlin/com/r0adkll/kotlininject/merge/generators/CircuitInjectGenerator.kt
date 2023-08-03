package com.r0adkll.kotlininject.merge.generators

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.validate
import com.r0adkll.kotlininject.merge.MergeContext
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.r0adkll.kotlininject.merge.annotations.ContributesTo
import com.r0adkll.kotlininject.merge.util.ClassNames
import com.r0adkll.kotlininject.merge.util.MemberNames
import com.r0adkll.kotlininject.merge.util.buildClass
import com.r0adkll.kotlininject.merge.util.buildFile
import com.r0adkll.kotlininject.merge.util.findAnnotation
import com.r0adkll.kotlininject.merge.util.getAllSymbolsWithAnnotation
import com.r0adkll.kotlininject.merge.util.getScope
import com.r0adkll.kotlininject.merge.util.getScreen
import com.r0adkll.kotlininject.merge.util.hasAnnotation
import com.r0adkll.kotlininject.merge.util.toClassName
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeSpec
import kotlin.reflect.KClass
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

class CircuitInjectGenerator : Generator {

  override val annotation: KClass<*>
    get() = CircuitInject::class

  override fun generate(context: MergeContext, element: KSDeclaration): GeneratedSpec {
    return when (element) {
      is KSFunctionDeclaration -> generateUiFactory(context, element)
      is KSClassDeclaration -> generatePresenterFactory(context, element)
      else -> throw IllegalStateException("Unable to process declaration")
    }
  }

  override fun generate(context: MergeContext): List<GeneratedSpec> {
    return context.resolver
      .getAllSymbolsWithAnnotation(CircuitInject::class)
      .mapNotNull { element ->
        if (element.validate()) {
          generate(context, element)
          when (element) {
            is KSFunctionDeclaration -> generateUiFactory(context, element)
            is KSClassDeclaration -> generatePresenterFactory(context, element)
            else -> null
          }
        } else {
          context.defer(element, annotation)
          null
        }
      }
      .toList()
  }

  private fun generateUiFactory(context: MergeContext, element: KSFunctionDeclaration): GeneratedSpec {
    val packageName = element.packageName.asString()
    val classSimpleName = "${element.simpleName.asString()}UiFactory"
    val className = ClassName(packageName, classSimpleName)
    val componentClassName = ClassName(packageName, "${classSimpleName}Component")

    // Verify that this element has @Composable annotation @ first param is state
    // and it
    if (!element.hasAnnotation(ClassNames.Composable)) {
      context.logger.error("Missing @Composable on this function", element)
      throw IllegalStateException("@Circuit inject is only usable on composable functions ir Presenter classes")
    }

    // Get the targeted scope and screen
    val scope = element.findAnnotation(CircuitInject::class)
      ?.getScope()
      ?.toClassName()
      ?: throw IllegalStateException("Unable to find scope to contribute injection to")

    val screen = element.findAnnotation(CircuitInject::class)
      ?.getScreen()
      ?.toClassName()
      ?: throw IllegalStateException("Unable to find screen for injected UI")

    // Get the state, assuming that the first parameter type is the state
    val stateClassName = element.parameters.firstOrNull()
      ?.type?.resolve()?.declaration?.toClassName()
      ?: throw IllegalStateException("@CircuitInject composable functions must have their state as the first parameter")

    element.parameters.getOrNull(1)
      ?.type?.resolve()?.declaration?.toClassName()
      ?: throw IllegalStateException("@CircuitInject requires your composable function to have a Modifier parameter")

    return FileSpec.buildFile(packageName, classSimpleName) {

      addType(
        TypeSpec.interfaceBuilder(componentClassName)
          .addAnnotation(
            AnnotationSpec.builder(ContributesTo::class)
              .addMember("%T::class", scope)
              .build(),
          )
          .addFunction(
            FunSpec.builder("bind${classSimpleName}")
              .addAnnotation(IntoSet::class)
              .addAnnotation(Provides::class)
              .addParameter("factory", className)
              .returns(ClassNames.Circuit.UiFactory)
              .addStatement("return factory")
              .build(),
          )
          .build(),
      )

      addType(
        TypeSpec.classBuilder(className)
          .addAnnotation(Inject::class)
          .addSuperinterface(ClassNames.Circuit.UiFactory)
          .addFunction(
            FunSpec.builder("create")
              .addModifiers(KModifier.OVERRIDE)
              .addParameter("screen", ClassNames.Circuit.Screen)
              .addParameter("context", ClassNames.Circuit.Context)
              .returns(
                ClassNames.Circuit.Ui
                  .parameterizedBy(STAR)
                  .copy(nullable = true),
              )
              .addCode(
                CodeBlock.builder()
                  .beginControlFlow("return when(screen)")
                  .beginControlFlow("is %T ->", screen)
                  .addStatement(
                    "%M<%T> { state, modifier -> %T(state, modifier) }",
                    MemberNames.CircuitUi,
                    stateClassName,
                    element.toClassName(),
                  )
                  .endControlFlow()
                  .addStatement("else -> null")
                  .endControlFlow()
                  .build(),
              )
              .build(),
          )
          .build(),
      )

    } isAggregating false
  }

  private fun generatePresenterFactory(context: MergeContext, element: KSClassDeclaration): GeneratedSpec {
    val packageName = element.packageName.asString()
    val classSimpleName = "${element.simpleName.asString()}Factory"
    val className = ClassName(packageName, classSimpleName)
    val componentClassName = ClassName(packageName, "${classSimpleName}Component")

    // Verify that this element has @Composable annotation @ first param is state
    // and it
    if (!element.hasAnnotation(Inject::class)) {
      context.logger.error("Missing @Inject on this class", element)
      throw IllegalStateException("@CircuitInject on presenter classes that are injectable")
    }

    // FIXME: Outdated
    element.primaryConstructor
      ?.parameters
      ?.firstOrNull()
      ?.let { firstParam ->
        require(
          firstParam.type.resolve().declaration.toClassName() == ClassNames.Circuit.Navigator,
        ) {
          "@CircuitInject annotated presenter classes just have an " +
            "@Assisted private val navigator: Navigator as their first param"
        }
      }
      ?: throw IllegalStateException(
        "@CircuitInject annotated presenter classes just have an " +
          "@Assisted private val navigator: Navigator as their first param",
      )

    // Get the targeted scope and screen
    val scope = element.findAnnotation(CircuitInject::class)
      ?.getScope()
      ?.toClassName()
      ?: throw IllegalStateException("Unable to find scope to contribute injection to")

    val screen = element.findAnnotation(CircuitInject::class)
      ?.getScreen()
      ?.toClassName()
      ?: throw IllegalStateException("Unable to find screen for injected UI")

    // Gather all assisted annotated parameters and evaluate providing them
    val allowedAssistedTypes = listOf(
      screen,
      ClassNames.Circuit.Navigator,
      ClassNames.Circuit.Context,
    )

    val assistInjectedParameters = element.primaryConstructor
      ?.parameters
      ?.mapNotNull { parameter ->
        if (parameter.hasAnnotation(Assisted::class)) {
          // Validate that injected type is allowed type
          val parameterTypeClassName = parameter.type.resolve().declaration.toClassName()
          if (parameterTypeClassName in allowedAssistedTypes) {
            ParameterSpec(parameter.name!!.asString(), parameterTypeClassName)
          } else null
        } else null
      }
      ?: emptyList()

    return FileSpec.buildFile(packageName, classSimpleName) {

      addType(
        TypeSpec.interfaceBuilder(componentClassName)
          .addAnnotation(
            AnnotationSpec.builder(ContributesTo::class)
              .addMember("%T::class", scope)
              .build(),
          )
          .addFunction(
            FunSpec.builder("bind${classSimpleName}")
              .addAnnotation(IntoSet::class)
              .addAnnotation(Provides::class)
              .addParameter("factory", className)
              .returns(ClassNames.Circuit.PresenterFactory)
              .addStatement("return factory")
              .build(),
          )
          .build(),
      )

      val factoryLambda = LambdaTypeName.get(
        parameters = assistInjectedParameters,
        returnType = element.toClassName(),
      )

      addType(
        TypeSpec.classBuilder(classSimpleName)
          .addAnnotation(Inject::class)
          .addAnnotation(
            AnnotationSpec.builder(ContributesTo::class)
              .addMember("%T::class", scope)
              .build(),
          )
          .addSuperinterface(ClassNames.Circuit.PresenterFactory)
          .primaryConstructor(
            FunSpec.constructorBuilder()
              .addParameter("presenterFactory", factoryLambda)
              .build(),
          )
          .addProperty(
            PropertySpec.builder("presenterFactory", factoryLambda)
              .initializer("presenterFactory")
              .addModifiers(KModifier.PRIVATE)
              .build(),
          )
          .addFunction(
            FunSpec.builder("create")
              .addModifiers(KModifier.OVERRIDE)
              .addParameter("screen", ClassNames.Circuit.Screen)
              .addParameter("navigator", ClassNames.Circuit.Navigator)
              .addParameter("context", ClassNames.Circuit.Context)
              .returns(
                ClassNames.Circuit.Presenter
                  .parameterizedBy(STAR)
                  .copy(nullable = true),
              )
              .addCode(
                CodeBlock.builder()
                  .beginControlFlow("return when(screen)")
                  .addStatement(
                    "is %T -> presenterFactory(${assistInjectedParameters.joinToString { it.name }})",
                    screen,
                  )
                  .addStatement("else -> null")
                  .endControlFlow()
                  .build(),
              )
              .build(),
          )
          .build(),
      )

    } isAggregating false
  }
}
