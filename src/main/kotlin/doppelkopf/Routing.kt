package doppelkopf

import doppelkopf.game.IllegalerZugException
import doppelkopf.game.Karte
import doppelkopf.game.Position
import doppelkopf.model.CardPutRequest
import doppelkopf.model.JoinRequest
import doppelkopf.model.UpdateRequest
import doppelkopf.model.VorbehaltRequest
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

        post("/putvorbehalt") {
            try {
                val body = call.receive<VorbehaltRequest>()
                call.respond(service.vorbehaltAnsagen(body.token, body.vorbehalt))
            } catch (e: FehlerhaftesTokenException) {
                call.respond(HttpStatusCode.Unauthorized)
            } catch (e: IllegalerZugException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Unbekannter Fehler")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
                println(e.message)
            }
        }

        post("/update") {
            try {
                val body = call.receive<UpdateRequest>()
                call.respond(service.getTableUpdate(body.token))
            } catch (e: FehlerhaftesTokenException) {
                call.respond(HttpStatusCode.Unauthorized)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}
