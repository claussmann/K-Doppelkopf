package doppelkopf.game

class Runde (var startPos: Position = Position.OBEN, val modus: Spielmodus){
    private var links: Karte? = null
    private var linksStiche: ArrayList<Karte> = arrayListOf()
    private var oben: Karte? = null
    private var obenStiche: ArrayList<Karte> = arrayListOf()
    private var rechts: Karte? = null
    private var rechtsStiche: ArrayList<Karte> = arrayListOf()
    private var unten: Karte? = null
    private var untenStiche: ArrayList<Karte> = arrayListOf()
    private var amZug: Position = startPos
    private var stichNummer: Int = 1

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
        return links != null && oben != null && rechts != null && unten != null
    }

    /**
     * Prüft, ob die Runde vollständig ist (alle Karten gelegt, bzw. 12 Stiche).
     */
    fun rundeKomplett(): Boolean {
        return stichNummer > 12
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
        when (pos) {
            Position.LINKS -> if (links == null) links = k else throw IllegalerZugException("Spieler hat schon gelegt")
            Position.OBEN -> if (oben == null) oben = k else throw IllegalerZugException("Spieler hat schon gelegt")
            Position.RECHTS -> if (rechts == null) rechts = k else throw IllegalerZugException("Spieler hat schon gelegt")
            Position.UNTEN -> if (unten == null) unten = k else throw IllegalerZugException("Spieler hat schon gelegt")
        }
        amZug = pos.next()
    }

    /**
     * Ermittelt den Gewinner des aktuellen Stichs und bereitet alles für den nächsten Stich vor.
     */
    fun stichGewinnerErmitteln(): Position {
        if (!stichKomplett()) throw IllegalerZugException("Noch nicht alle haben gelegt")
        var bestPos = startPos
        var bestCard = gelegtVon(bestPos)!!
        val aufspiel = bestCard
        var currentPos = bestPos
        var currentCard = bestCard
        for (i in 1..3) {
            currentPos = currentPos.next()
            currentCard = gelegtVon(currentPos)!!
            if (currentCard.sticht(bestCard, stichNummer == 12, false, aufspiel, modus)){
                bestPos = currentPos
                bestCard = currentCard
            }
        }
        val stich = arrayOf(links!!, oben!!, rechts!!, unten!!)
        when (bestPos) {
            Position.LINKS -> linksStiche.addAll(stich)
            Position.OBEN -> obenStiche.addAll(stich)
            Position.RECHTS -> rechtsStiche.addAll(stich)
            Position.UNTEN -> untenStiche.addAll(stich)
        }
        startPos = bestPos
        amZug = startPos
        links = null
        rechts = null
        unten = null
        oben = null
        stichNummer++
        return bestPos
    }

    fun gelegtVon(pos: Position): Karte? {
        return when(pos) {
            Position.UNTEN -> unten
            Position.LINKS -> links
            Position.OBEN -> oben
            Position.RECHTS -> rechts
        }
    }
}