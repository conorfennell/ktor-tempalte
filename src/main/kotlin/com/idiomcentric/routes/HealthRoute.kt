package com.idiomcentric.routes

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import java.time.Instant

const val HEALTH_PATH = "/v1/health"
data class Health(val startedAt: Instant)

fun Routing.health() {
    val health = Health(startedAt = Instant.now())
    get(HEALTH_PATH) {
        call.respond(health)
    }
}
