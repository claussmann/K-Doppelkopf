package doppelkopf.game

class DoppelkopfSpiel(val spieler: Array<Spieler>) {
    val karten = Kartenstapel()
    var currentRunde: Runde? = null
    var linksVorbehalt: Spielmodus? = null
    var obenVorbehalt: Spielmodus? = null
    var rechtsVorbehalt: Spielmodus? = null
    var untenVorbehalt: Spielmodus? = null
    val geber: Position = Position.LINKS
    var ansager: Position = geber.next()
    val rundenauswertungen = ArrayList<Rundenauswertung>()

    init {
        if (spieler.size != 4) throw IllegalArgumentException("Es werden genau 4 Spieler erwartet.")
        if (spieler.filter { it.pos == Position.OBEN }.size != 1) throw IllegalArgumentException("Jede Position muss belegt sein.")
        if (spieler.filter { it.pos == Position.UNTEN }.size != 1) throw IllegalArgumentException("Jede Position muss belegt sein.")
        if (spieler.filter { it.pos == Position.LINKS }.size != 1) throw IllegalArgumentException("Jede Position muss belegt sein.")
        if (spieler.filter { it.pos == Position.RECHTS }.size != 1) throw IllegalArgumentException("Jede Position muss belegt sein.")
        neuGeben()
    }

    private fun neuGeben() {
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
        if (r.stichKomplett()) {
            r.stichGewinnerErmitteln()
            ermittleParteien() // Bei Hochzeit ergeben sich die Parteien möglicherweise erst jetzt.
        }
        if (r.rundeKomplett()) rundeAuswerten()
    }

    fun vorbehaltAnsagen(vorbehalt: Spielmodus, pos: Position) {
        if (currentRunde != null) throw IllegalerZugException("Vorbehalte können gerade nicht angesagt werden.")
        if (ansager != pos) throw IllegalerZugException("Spieler ist nicht dran mit ansagen.")
        when (pos) {
            Position.LINKS -> linksVorbehalt = vorbehalt
            Position.RECHTS -> rechtsVorbehalt = vorbehalt
            Position.OBEN -> obenVorbehalt = vorbehalt
            Position.UNTEN -> untenVorbehalt = vorbehalt
        }
        ansager = ansager.next()
        if (linksVorbehalt != null && rechtsVorbehalt != null && untenVorbehalt != null && obenVorbehalt != null) {
            var bestPos = geber.next()
            var bestVorbehalt = vorbehaltVon(bestPos)!!
            var currentPos = bestPos
            var currentVorbehalt = bestVorbehalt
            for (i in 1..3) {
                currentPos = currentPos.next()
                currentVorbehalt = vorbehaltVon(currentPos)!!
                if (currentVorbehalt.vorrangVor(bestVorbehalt)) {
                    bestPos = currentPos
                    bestVorbehalt = currentVorbehalt
                }
            }
            val starter = if (bestVorbehalt.isPflichtsolo()) bestPos else geber.next()
            currentRunde = Runde(starter, bestVorbehalt)
            ermittleParteien()
        }
    }

    fun werIstDran(): Position {
        val r = currentRunde
        return r?.werIstDran() ?: ansager
    }

    fun rundenauswertungen(): ArrayList<Rundenauswertung> {
        return rundenauswertungen
    }

    private fun ermittleParteien() {
        for (s in spieler) {
            if (s.partei != Partei.UNBEKANNT) return
        }
        when (currentRunde?.welcherSpielmodus()) {
            Spielmodus.NORMAL -> {
                for (s in spieler) {
                    if (s.hasKarte(Karte.KR_D)) s.partei = Partei.RE else s.partei = Partei.KONTRA
                }
            }

            Spielmodus.HOCHZEIT -> TODO()
            Spielmodus.ARMUT -> TODO()
            Spielmodus.SOLO_REINES_KARO, Spielmodus.SOLO_REINES_HERZ,
            Spielmodus.SOLO_REINES_PIK, Spielmodus.SOLO_REINES_KREUZ,
            Spielmodus.SOLO_KARO, Spielmodus.SOLO_HERZ,
            Spielmodus.SOLO_PIK, Spielmodus.SOLO_KREUZ,
            Spielmodus.SOLO_DAME, Spielmodus.SOLO_BUBE,
            Spielmodus.SOLO -> {
                TODO()
                // Bei Solo muss man sich direkt nach dem vorbehalt ansagen merken, wer das solo spielt.
            }

            null -> TODO()
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
        else if (punkteRe < 121) -1
        else if (punkteRe < 150) 1
        else if (punkteRe < 180) 2
        else if (punkteRe < 210) 3
        else if (punkteRe < 240) 4
        else 5
        // Sonderpunkte, Absagen
        var gegenDieAlten = false
        if (currentRunde?.welcherSpielmodus() == Spielmodus.NORMAL && punkteRe < 121) {
            rundenpunkte -= 1
            gegenDieAlten = true
        } // Gegen d. alten
        // TODO
        for (s in spieler) {
            if (s.partei == Partei.RE) s.punkte += rundenpunkte else s.punkte -= rundenpunkte
        }
        rundenauswertungen.add(
            Rundenauswertung(
                rundenpunkte,
                punkteRe,
                currentRunde!!.welcherSpielmodus(),
                gegenDieAlten
            )
        )
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