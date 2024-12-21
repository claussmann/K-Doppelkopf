package doppelkopf.game

class Game(val spieler: Array<Spieler>) {
    val karten = Kartenstapel()
    var currentRunde: Runde? = null
    var linksVorbehalt: Spielmodus? = null
    var obenVorbehalt: Spielmodus? = null
    var rechtsVorbehalt: Spielmodus? = null
    var untenVorbehalt: Spielmodus? = null
    val geber: Position = Position.OBEN

    init {
        if (spieler.size != 4) throw IllegalArgumentException("Es werden genau 4 Spieler erwartet.")
        neuGeben()
    }

    fun neuGeben() {
        for (s in spieler) {
            s.neueHand(karten.zieheKarten())
            s.partei = Partei.UNBEKANNT
        }
        linksVorbehalt = null
        rechtsVorbehalt = null
        obenVorbehalt = null
        untenVorbehalt = null
    }

    fun karteLegen(karte: Karte, pos: Position) {
        val s = spieler.first { it.pos == pos }
        val r = currentRunde ?: throw IllegalerZugException("Es kann noch keine Karte gelegt werden.")
        if (s.hasKarte(karte)) {
            r.karteGelegt(karte, pos)
            s.legeKarte(karte)
        }
        if (r.stichKomplett()) r.stichGewinnerErmitteln()
        if (r.rundeKomplett()) rundeAuswerten()
    }

    fun vorbehaltAnsagen(vorbehalt: Spielmodus, pos: Position) {
        if (currentRunde != null) throw IllegalerZugException("Vorbehalte können gerade nicht angesagt werden.")
        when (pos) {
            Position.LINKS -> if (linksVorbehalt == null) linksVorbehalt =
                vorbehalt else throw IllegalerZugException("Bereits Vorbehalt angesagt..")

            Position.RECHTS -> if (rechtsVorbehalt == null) rechtsVorbehalt =
                vorbehalt else throw IllegalerZugException("Bereits Vorbehalt angesagt..")

            Position.OBEN -> if (obenVorbehalt == null) obenVorbehalt =
                vorbehalt else throw IllegalerZugException("Bereits Vorbehalt angesagt..")

            Position.UNTEN -> if (untenVorbehalt == null) untenVorbehalt =
                vorbehalt else throw IllegalerZugException("Bereits Vorbehalt angesagt..")
        }
        if (linksVorbehalt != null && rechtsVorbehalt != null && untenVorbehalt != null && obenVorbehalt != null) {
            var bestPos = geber.next()
            var bestVorbehalt = vorbehaltVon(bestPos)!!
            var currentPos = bestPos
            var currentVorbehalt = bestVorbehalt
            for (i in 1..3) {
                currentPos = currentPos.next()
                currentVorbehalt = vorbehaltVon(currentPos)!!
                if (currentVorbehalt.vorrangVor(bestVorbehalt)){
                    bestPos = currentPos
                    bestVorbehalt = currentVorbehalt
                }
            }
            val starter = if (bestVorbehalt.isPflichtsolo()) bestPos else geber.next()
            currentRunde = Runde(starter, bestVorbehalt)
        }
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

    private fun vorbehaltVon(pos: Position): Spielmodus? {
        return when (pos) {
            Position.LINKS -> linksVorbehalt
            Position.RECHTS -> rechtsVorbehalt
            Position.OBEN -> obenVorbehalt
            Position.UNTEN -> untenVorbehalt
        }
    }
}