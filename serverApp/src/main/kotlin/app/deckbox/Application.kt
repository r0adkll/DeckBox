package app.deckbox

import app.deckbox.di.AppComponent
import app.deckbox.plugins.configureDatabases
import app.deckbox.plugins.configureHTTP
import app.deckbox.plugins.configureMonitoring
import app.deckbox.plugins.configureRouting
import app.deckbox.plugins.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlininject.merge.app.deckbox.di.createMergedAppComponent

lateinit var appComponent: AppComponent

fun main() {
  appComponent = AppComponent.createMergedAppComponent()

  embeddedServer(
    factory = Netty,
    port = 8080,
    host = "0.0.0.0",
    module = Application::module,
  ).start(wait = true)
}

fun Application.module() {
  configureSerialization()
  configureDatabases()
  configureMonitoring()
  configureHTTP()
  configureRouting()
}
