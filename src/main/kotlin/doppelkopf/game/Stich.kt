package doppelkopf.game

class Stich(val stichnummer: Int, val starter: Position, val spielmodus: Spielmodus) {
    var links: Karte? = null
    var oben: Karte? = null
    var rechts: Karte? = null
    var unten: Karte? = null
    var gewinner: Position? = null

    fun aufspiel(): Karte? {
        return gelegtVon(starter)
    }

    fun istKomplett(): Boolean {
        return links != null && oben != null && rechts != null && unten != null
    }

    fun karteHinzu(karte: Karte, pos: Position) {
        when (pos) {
            Position.LINKS -> if (links == null) links = karte else throw IllegalerZugException("Hat schon gelegt")
            Position.OBEN -> if (oben == null) oben = karte else throw IllegalerZugException("Hat schon gelegt")
            Position.RECHTS -> if (rechts == null) rechts = karte else throw IllegalerZugException("Hat schon gelegt")
            Position.UNTEN -> if (unten == null) unten = karte else throw IllegalerZugException("Hat schon gelegt")
        }
    }

    fun punkte(): Int {
        return (links?.punkte() ?: 0) + (rechts?.punkte() ?: 0) + (oben?.punkte() ?: 0) + (unten?.punkte() ?: 0)
    }

    fun gewinnerErmitteln(): Position {
        if (!istKomplett()) throw IllegalerZugException("Noch nicht alle haben gelegt")
        var bestPos = starter
        var bestCard = gelegtVon(bestPos)!!
        val aufspiel = bestCard
        var currentPos = bestPos
        var currentCard: Karte
        for (i in 1..3) {
            currentPos = currentPos.next()
            currentCard = gelegtVon(currentPos)!!
            if (currentCard.sticht(bestCard, stichnummer == 12, false, aufspiel, spielmodus)) {
                bestPos = currentPos
                bestCard = currentCard
            }
        }
        gewinner = bestPos
        return gewinner ?: throw RuntimeException("Stich konnte nicht ausgewertet werden")
    }

    private fun gelegtVon(pos: Position): Karte? {
        return when (pos) {
            Position.UNTEN -> unten
            Position.LINKS -> links
            Position.OBEN -> oben
            Position.RECHTS -> rechts
        }
    }
}