package doppelkopf

import doppelkopf.game.Position
import doppelkopf.model.JoinRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        staticResources("/", "static") // Serve index.html

        post("/join") {
            call.respond(service.join(call.receive<JoinRequest>().spielername))
        }
        get("/player/links") {
            try {
                call.respond(service.getPublicSpielerInfo(Position.LINKS))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.NotFound)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
        get("/player/rechts") {
            try {
                call.respond(service.getPublicSpielerInfo(Position.RECHTS))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.NotFound)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
        get("/player/oben") {
            try {
                call.respond(service.getPublicSpielerInfo(Position.OBEN))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.NotFound)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
        get("/player/unten") {
            try {
                call.respond(service.getPublicSpielerInfo(Position.UNTEN))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.NotFound)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
        get("/player/private/{sessionToken}") {
            val token = call.parameters["sessionToken"] ?: call.respond(HttpStatusCode.BadRequest)
            try {
                call.respond(service.getPrivateSpielerInfo(token.toString()))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.Unauthorized)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}
