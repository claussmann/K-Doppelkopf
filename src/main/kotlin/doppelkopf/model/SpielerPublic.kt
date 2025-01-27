package doppelkopf.model

import doppelkopf.game.Spieler

class SpielerPublic (spieler: Spieler){
    val name = spieler.name
    val position = spieler.pos
    val punkte = spieler.punkte
    val vorbehalt = spieler.vorbehalt
}