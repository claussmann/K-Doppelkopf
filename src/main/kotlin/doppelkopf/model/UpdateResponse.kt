package doppelkopf.model

import doppelkopf.game.Position

class UpdateResponse(
    val playerself: SpielerPrivate,
    val currentTurn: Position,
    val links: SpielerPublic?,
    val oben: SpielerPublic?,
    val rechts: SpielerPublic?,
    val unten: SpielerPublic?
)