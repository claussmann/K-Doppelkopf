package doppelkopf

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        staticResources("/", "static") // Serve index.html

        post("/join") {
            call.respond(service.join(call.receive()))
        }
    }
}
