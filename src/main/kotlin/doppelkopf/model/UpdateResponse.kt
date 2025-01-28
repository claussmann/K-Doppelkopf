package doppelkopf.model

import doppelkopf.game.Karte
import doppelkopf.game.Position
import doppelkopf.game.Spielmodus

class UpdateResponse(
    val playerself: SpielerPrivate,
    val currentTurn: Position,
    val spielerListe: List<SpielerPublic>,
    val spielmodus: Spielmodus?,
    val gelegtLinks: Karte?,
    val gelegtOben: Karte?,
    val gelegtRechts: Karte?,
    val gelegtUnten: Karte?
)