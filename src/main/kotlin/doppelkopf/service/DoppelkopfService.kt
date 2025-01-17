package doppelkopf.service

import doppelkopf.game.*
import doppelkopf.model.SpielerPrivate
import doppelkopf.model.SpielerPublic
import java.lang.RuntimeException
import java.util.UUID

class DoppelkopfService {
    private val lobby = HashMap<String, Spieler>()
    private var game: DoppelkopfSpiel? = null

    fun join(spielername: String): SpielerPrivate {
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
        throw IllegalArgumentException("Spieler an dieser Position existiert nicht.")
    }

    fun getPrivateSpielerInfo(sessionToken: String): SpielerPrivate {
        val s = lobby[sessionToken] ?: throw IllegalArgumentException("Token nicht korrekt.")
        return SpielerPrivate(s, sessionToken)
    }

    fun getCurrentTurnPlayer(): SpielerPublic {
        val pos = getSpiel().werIstDran()
        return getPublicSpielerInfo(pos)
    }

    fun vorbehaltAnsagen(sessionToken: String, vorbehalt: Spielmodus) {
        val s = getPrivateSpielerInfo(sessionToken)
        getSpiel().vorbehaltAnsagen(vorbehalt, s.position)
    }

    fun karteLegen(sessionToken: String, karte: Karte) {
        val s = getPrivateSpielerInfo(sessionToken)
        getSpiel().karteLegen(karte, s.position)
    }

    private fun getSpiel(): DoppelkopfSpiel {
        return game ?: throw IllegalerZugException("Spiel noch nicht bereit.")
    }

    private fun isFull(): Boolean {
        return lobby.size >= 4
    }

    private fun randomPosition(): Position {
        if (isFull()) {
            throw RuntimeException("Spiel ist bereits voll.")
        }
        var positions = listOf(Position.OBEN, Position.LINKS, Position.RECHTS, Position.UNTEN)
        for (s in lobby.values) {
            positions = positions.filter { s.pos != it }
        }
        if (positions.isEmpty()) {
            throw RuntimeException("Spiel ist bereits voll.")
        }
        return positions.random()
    }
}