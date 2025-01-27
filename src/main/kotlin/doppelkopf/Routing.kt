package doppelkopf

import doppelkopf.game.IllegalerZugException
import doppelkopf.game.Karte
import doppelkopf.game.Position
import doppelkopf.model.CardPutRequest
import doppelkopf.model.JoinRequest
import doppelkopf.service.FehlerhaftesTokenException
import doppelkopf.service.SpielerNichtGefundenException
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
            try {
                call.respond(service.join(call.receive<JoinRequest>().spielername))
            } catch (e: IllegalerZugException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Unbekannter Fehler")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        post("/putcard") {
            try {
                val body = call.receive<CardPutRequest>()
                call.respond(service.karteLegen(body.token, body.card))
            } catch (e: FehlerhaftesTokenException) {
                call.respond(HttpStatusCode.Unauthorized)
            } catch (e: IllegalerZugException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Unbekannter Fehler")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        get("/player/links") {
            try {
                call.respond(service.getPublicSpielerInfo(Position.LINKS))
            } catch (e: SpielerNichtGefundenException) {
                call.respond(HttpStatusCode.NotFound)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        get("/player/rechts") {
            try {
                call.respond(service.getPublicSpielerInfo(Position.RECHTS))
            } catch (e: SpielerNichtGefundenException) {
                call.respond(HttpStatusCode.NotFound)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        get("/player/oben") {
            try {
                call.respond(service.getPublicSpielerInfo(Position.OBEN))
            } catch (e: SpielerNichtGefundenException) {
                call.respond(HttpStatusCode.NotFound)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        get("/player/unten") {
            try {
                call.respond(service.getPublicSpielerInfo(Position.UNTEN))
            } catch (e: SpielerNichtGefundenException) {
                call.respond(HttpStatusCode.NotFound)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        get("/player/private/{sessionToken}") {
            val token = (call.parameters["sessionToken"] ?: call.respond(HttpStatusCode.BadRequest)).toString()
            if (token.length > 40) call.respond(HttpStatusCode.BadRequest)
            try {
                call.respond(service.getPrivateSpielerInfo(token))
            } catch (e: FehlerhaftesTokenException) {
                call.respond(HttpStatusCode.Unauthorized)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}
