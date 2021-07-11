package com.idiomcentric.routes


import com.idiomcentric.services.ExampleService
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Location
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.locations.get
import java.util.UUID

const val EXAMPLE_PATH = "/v1/example"

fun Routing.example(exampleService: ExampleService) {

    @Location("$EXAMPLE_PATH/{exampleId}")
    data class ExampleById(val exampleId: String)

    get<ExampleById> { path ->
        when(val example = exampleService.retrieve(UUID.fromString(path.exampleId))) {
            null -> call.respond(HttpStatusCode.NotFound)
            else -> call.respond(example)
        }
    }
}
