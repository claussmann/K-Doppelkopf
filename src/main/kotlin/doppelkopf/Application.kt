package doppelkopf

import doppelkopf.service.DoppelkopfService
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.util.*
import kotlin.collections.ArrayList

val service = DoppelkopfService()
private val clients = Collections.synchronizedList<WebSocketServerSession>(ArrayList())


fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}

fun addClient(client: WebSocketServerSession) {
    clients.add(client)
}

suspend fun notifyClients() {
    for (client in clients) {
        client.send("update")
    }
}