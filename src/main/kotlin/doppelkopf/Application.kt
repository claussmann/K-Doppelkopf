package doppelkopf

import doppelkopf.service.DoppelkopfService
import io.ktor.server.application.*

val service = DoppelkopfService()

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}
