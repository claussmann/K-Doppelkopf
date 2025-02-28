package doppelkopf.service

import doppelkopf.game.*
import doppelkopf.model.SpielerPrivate
import doppelkopf.model.SpielerPublic
import doppelkopf.model.UpdateResponse
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class DoppelkopfService {
    private val lobby = HashMap<String, Spieler>()
    private var game: DoppelkopfSpiel? = null

    // To send notifications to the players
    private var clients = Collections.synchronizedList<WebSocketServerSession>(ArrayList())

    // We will lock every action which CHANGES the state but not reading operations. We can accept rare cases where a
    // client reads a faulty state because the client will update periodically anyway. However, the update functions
    // in DoppelkopfSpiel are not thread-safe, so we must protect them.
    private val mutex = Mutex()

    suspend fun join(spielername: String): SpielerPrivate {
        val ret = syncJoin(spielername)
        abonnentenBenachrichtigen()
        return ret
    }

    internal fun syncJoin(spielername: String): SpielerPrivate {
        var sessionToken = UUID.randomUUID().toString()
        while (sessionToken in lobby) {
            sessionToken = UUID.randomUUID().toString()
        }
        val spieler = Spieler(spielername, randomPosition())
        lobby[sessionToken] = spieler
        if (isFull()) game = DoppelkopfSpiel(lobby.values.toTypedArray())
        return SpielerPrivate(spieler, sessionToken)
    }

    fun getPublicSpielerInfo(pos: Position): SpielerPublic {
        for (s in lobby.values) {
            if (s.pos == pos) return SpielerPublic(s)
        }
        throw SpielerNichtGefundenException("Spieler an dieser Position existiert nicht.")
    }

    fun getPrivateSpielerInfo(sessionToken: String): SpielerPrivate {
        if (sessionToken.length > 50) throw FehlerhaftesTokenException("Token nicht korrekt.")
        val s = lobby[sessionToken] ?: throw FehlerhaftesTokenException("Token nicht korrekt.")
        return SpielerPrivate(s, sessionToken)
    }

    fun getTableUpdate(sessionToken: String): UpdateResponse {
        val spielerSelf = getPrivateSpielerInfo(sessionToken)
        val spielerListe = lobby.values.map { SpielerPublic(it) }
        return UpdateResponse(
            spielerSelf,
            game?.werIstDran() ?: Position.OBEN,
            spielerListe,
            game?.aktuellerSpielmodus(),
            game?.aktuellerStich(),
            game?.letzterStich()
        )
    }

    fun getCurrentTurnPlayer(): SpielerPublic {
        val pos = getSpiel().werIstDran()
        return getPublicSpielerInfo(pos)
    }

    suspend fun vorbehaltAnsagen(sessionToken: String, vorbehalt: Spielmodus) {
        mutex.withLock {
            syncVorbehaltAnsagen(sessionToken, vorbehalt)
        }
        abonnentenBenachrichtigen()
    }

    internal fun syncVorbehaltAnsagen(sessionToken: String, vorbehalt: Spielmodus) {
        val s = getPrivateSpielerInfo(sessionToken)
        getSpiel().vorbehaltAnsagen(vorbehalt, s.position)
    }

    suspend fun karteLegen(sessionToken: String, karte: Karte) {
        mutex.withLock {
            syncKarteLegen(sessionToken, karte)
        }
        abonnentenBenachrichtigen()
    }

    internal fun syncKarteLegen(sessionToken: String, karte: Karte) {
        val s = getPrivateSpielerInfo(sessionToken)
        getSpiel().karteLegen(karte, s.position)
    }

    fun websocketAbonnieren(client: WebSocketServerSession) {
        clients.add(client)
    }

    fun websocketDeabonnieren(client: WebSocketServerSession) {
        clients.remove(client)
    }

    private suspend fun abonnentenBenachrichtigen() {
        for (client in clients) {
            client.send("update")
        }
    }

    private fun getSpiel(): DoppelkopfSpiel {
        return game ?: throw IllegalerZugException("Spiel noch nicht bereit.")
    }

    private fun isFull(): Boolean {
        return lobby.size >= 4
    }

    private fun randomPosition(): Position {
        if (isFull()) {
            throw IllegalerZugException("Spiel ist bereits voll.")
        }
        var positions = listOf(Position.OBEN, Position.LINKS, Position.RECHTS, Position.UNTEN)
        for (s in lobby.values) {
            positions = positions.filter { s.pos != it }
        }
        if (positions.isEmpty()) {
            throw IllegalerZugException("Spiel ist bereits voll.")
        }
        return positions.random()
    }
}