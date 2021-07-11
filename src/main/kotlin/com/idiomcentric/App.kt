package com.idiomcentric

import com.google.inject.Guice
import io.ktor.application.Application
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.util.KtorExperimentalAPI
import mu.KLogger
import mu.KotlinLogging
import kotlin.time.ExperimentalTime

val logger: KLogger = KotlinLogging.logger {}

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
@ExperimentalTime
val defaultMain: Application.() -> Unit = fun Application.() {
    main {
        Guice.createInjector(KtorTemplateBindings(it))
    }
}

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
@ExperimentalTime
fun main() {
    embeddedServer(
        CIO,
        port = 8000,
        configure = {},
        module = defaultMain
    ).start(wait = true)
}
