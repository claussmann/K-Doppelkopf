package doppelkopf.model

import doppelkopf.game.Partei
import doppelkopf.game.Spieler

class SpielerPrivate (spieler: Spieler, token: String) {
    val sessionToken = token
    val name = spieler.name
    val position = spieler.pos
    val hand = spieler.hand
    val hasSchwein = spieler.hasSchwein
    val punkte = spieler.punkte
    val partei = spieler.partei
}