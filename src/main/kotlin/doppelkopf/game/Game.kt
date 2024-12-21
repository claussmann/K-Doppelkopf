package doppelkopf.game

class Game(val spieler: Array<Spieler>) {
    val karten = Kartenstapel()
    var currentRunde: Runde? = null

    init {
        if (spieler.size != 4) throw IllegalArgumentException("Es werden genau 4 Spieler erwartet.")
        neuGeben()
    }

    fun neuGeben() {
        for (s in spieler) {
            s.neueHand(karten.zieheKarten())
        }
    }

    fun karteLegen(karte: Karte, pos: Position) {
        val s = spieler.first { it.pos == pos }
        val r = currentRunde ?: throw IllegalerZugException("Es kann noch keine Karte gelegt werden.")
        if (s.hasKarte(karte)) {
            r.karteGelegt(karte, pos)
        }
        if (r.stichKomplett()) r.stichGewinnerErmitteln()
        if (r.rundeKomplett()) rundeAuswerten()
    }

    fun ansagen(pos: Position) {
        TODO()
        // Neue Runde anlegen
    }

    private fun rundeAuswerten() {
        var punkteRe = 0
        for (s in spieler) {
            if (s.partei == Partei.RE) punkteRe += currentRunde!!.punkteZahelen(s.pos)
        }
        var rundenpunkte = if (punkteRe == 0) -5
        else if (punkteRe < 30) -4
        else if (punkteRe < 60) -3
        else if (punkteRe < 90) -2
        else if (punkteRe < 120) -1
        else if (punkteRe < 150) 1
        else if (punkteRe < 180) 2
        else if (punkteRe < 210) 3
        else if (punkteRe < 240) 4
        else 5
        // TODO: Sonderpunkte, Absagen
        for (s in spieler) {
            if (s.partei == Partei.RE) s.punkte += rundenpunkte else s.punkte -= rundenpunkte
        }
        currentRunde = null
    }
}