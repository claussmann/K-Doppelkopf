package doppelkopf.game

class Runde (val startPos: Position = Position.OBEN, val modus: Spielmodus){
    var letzterStich: Stich? = null
    var aktuellerStich: Stich = Stich(1, startPos, modus)
    private var linksStiche: ArrayList<Stich> = arrayListOf()
    private var obenStiche: ArrayList<Stich> = arrayListOf()
    private var rechtsStiche: ArrayList<Stich> = arrayListOf()
    private var untenStiche: ArrayList<Stich> = arrayListOf()
    private var amZug: Position = startPos

    /**
     * Gibt zurück, welche Position dran ist.
     */
    fun werIstDran(): Position {
        return amZug
    }

    /**
     * Gibt zurück, welcher Spielmodus gespielt wird.
     */
    fun welcherSpielmodus(): Spielmodus {
        return modus
    }

    /**
     * Prüft, ob der Stich vollständig ist (4 Karten liegen).
     */
    fun stichKomplett(): Boolean {
        return aktuellerStich.istKomplett()
    }

    /**
     * Prüft, ob die Runde vollständig ist (alle Karten gelegt, bzw. 12 Stiche).
     */
    fun rundeKomplett(): Boolean {
        return stichnummer() > 12
    }

    /**
     * Gibt die aktuelle Stichnummer aus. Es ist immer die nummer des aktuellen unvollständigen stichs.
     * Das heißt, wenn 4 Karten gelegt wurden ist die Stichnummer schon auf 2 gesprungen.
     */
    fun stichnummer(): Int {
        return aktuellerStich.stichnummer
    }

    /**
     * Zählt die Punkte eines Spielers aus (hier wird noch nicht nach Teams gezählt!).
     */
    fun punkteZahelen(pos: Position): Int {
        return when(pos) {
            Position.OBEN -> obenStiche.sumOf { it.punkte() }
            Position.LINKS -> linksStiche.sumOf { it.punkte() }
            Position.RECHTS -> rechtsStiche.sumOf { it.punkte() }
            Position.UNTEN -> untenStiche.sumOf { it.punkte() }
        }
    }

    /**
     * Legt eine Karte auf den Stapel und merkt sich, welche Spielerposition sie gelegt hat.
     * Diese Funktion prüft auch, ob der Spieler bereits gelegt hat, oder ob er überhaupt dran ist.
     */
    fun karteGelegt(k: Karte, pos: Position) {
        if (rundeKomplett()) throw IllegalerZugException("Diese Runde ist bereits abgeschlossen.")
        if (pos != amZug) throw IllegalerZugException("Spieler ist nicht an der Reihe")
        aktuellerStich.karteHinzu(k, pos)
        amZug = pos.next()
    }

    /**
     * Ermittelt den Gewinner des aktuellen Stichs und bereitet alles für den nächsten Stich vor.
     */
    fun stichGewinnerErmitteln(): Position {
        val bestPos = aktuellerStich.gewinnerErmitteln()
        when (bestPos) {
            Position.LINKS -> linksStiche.add(aktuellerStich)
            Position.OBEN -> obenStiche.add(aktuellerStich)
            Position.RECHTS -> rechtsStiche.add(aktuellerStich)
            Position.UNTEN -> untenStiche.add(aktuellerStich)
        }
        amZug = bestPos
        letzterStich = aktuellerStich
        aktuellerStich = Stich(stichnummer() + 1, amZug, modus)
        return bestPos
    }
}