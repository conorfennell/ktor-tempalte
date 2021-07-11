package com.idiomcentric

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.google.inject.AbstractModule
import com.google.inject.Inject
import com.idiomcentric.routes.health
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.application.call
import io.ktor.features.CallLogging
import io.ktor.features.DataConversion
import io.ktor.features.StatusPages
import io.ktor.features.ContentNegotiation
import io.ktor.features.MissingRequestParameterException
import io.ktor.features.ParameterConversionException
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.locations.Locations
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.request.httpMethod
import io.ktor.request.path
import io.ktor.response.respond
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import org.slf4j.event.Level
import kotlin.time.ExperimentalTime

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
@ExperimentalTime
class KtorTemplateBindings(private val application: Application) : AbstractModule() {
    override fun configure() {
        binder().requireExplicitBindings()
//        bind(ConfigService::class.java).asEagerSingleton()
//        bind(PostgresConnectionProvider::class.java).asEagerSingleton()
//        bind(KtorTemplateService::class.java).asEagerSingleton()
        bind(Application::class.java).toInstance(application)
        bind(MainRouting::class.java).asEagerSingleton()
    }
}

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
@ExperimentalTime
fun Application.main(injectorCreator: (Application) -> Unit) {
    install(Locations)
    install(DataConversion)
    install(CallLogging) {
        level = Level.INFO
    }
    install(StatusPages) {
        exception<Throwable> { cause ->
            logger.error("${call.request.httpMethod.value} ${call.request.path()} : ${cause.message}")

            when (cause) {
                is MissingKotlinParameterException -> call.respond(HttpStatusCode.BadRequest)
                is MissingRequestParameterException -> call.respond(HttpStatusCode.BadRequest)
                is ParameterConversionException -> call.respond(HttpStatusCode.BadRequest)
                is UnrecognizedPropertyException -> call.respond(HttpStatusCode.BadRequest)
                is JsonProcessingException -> call.respond(HttpStatusCode.BadRequest)
                is MismatchedInputException -> call.respond(HttpStatusCode.BadRequest)
                else -> call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }

    install(ContentNegotiation) {
        jackson {
            // similar to ISO_8601
            dateFormat = StdDateFormat().withColonInTimeZone(true)
            registerModule(JavaTimeModule())
        }
    }
    injectorCreator(this)
}

@KtorExperimentalLocationsAPI
@ExperimentalTime
class MainRouting @Inject constructor(
    application: Application,
) {
    init {
        application.routing {
            health()
        }
    }
}
