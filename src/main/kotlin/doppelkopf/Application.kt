package doppelkopf

import doppelkopf.service.DoppelkopfService
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

val service = DoppelkopfService()



fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}