package doppelkopf.service

import doppelkopf.game.DoppelkopfSpiel
import doppelkopf.game.Position
import doppelkopf.game.Spieler
import doppelkopf.model.SpielerPrivate
import doppelkopf.model.SpielerPublic
import java.lang.RuntimeException
import java.util.UUID

class DoppelkopfService {
    val spielers = HashMap<String, Spieler>()
    var game: DoppelkopfSpiel? = null

    fun join(spielername: String): SpielerPrivate {
        var sessionToken = UUID.randomUUID().toString()
        while (sessionToken in spielers) {
            sessionToken = UUID.randomUUID().toString()
        }
        val spieler = Spieler(spielername, randomPosition())
        spielers[sessionToken] = spieler
        if (isFull()) game = DoppelkopfSpiel(spielers.values.toTypedArray())
        return SpielerPrivate(spieler, sessionToken)
    }

    fun getPublicSpielerInfo(pos: Position): SpielerPublic {
        for (s in spielers.values) {
            if (s.pos == pos) return SpielerPublic(s)
        }
        throw IllegalArgumentException("Spieler an dieser Position existiert nicht.")
    }

    private fun isFull(): Boolean {
        return spielers.size >= 4
    }

    private fun randomPosition(): Position {
        if (isFull()) {throw RuntimeException("Spiel ist bereits voll.")}
        var positions = listOf(Position.OBEN, Position.LINKS, Position.RECHTS, Position.UNTEN)
        for (s in spielers.values) {
            positions = positions.filter { s.pos != it }
        }
        if (positions.isEmpty()) {throw RuntimeException("Spiel ist bereits voll.")}
        return positions.random()
    }
}