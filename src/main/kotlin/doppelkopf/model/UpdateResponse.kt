package doppelkopf.model

import doppelkopf.game.Karte
import doppelkopf.game.Position
import doppelkopf.game.Spielmodus
import doppelkopf.game.Stich

class UpdateResponse(
    val playerself: SpielerPrivate,
    val currentTurn: Position,
    val spielerListe: List<SpielerPublic>,
    val spielmodus: Spielmodus?,
    val aktuellerStich: Stich?,
    val letzterStich: Stich?
)