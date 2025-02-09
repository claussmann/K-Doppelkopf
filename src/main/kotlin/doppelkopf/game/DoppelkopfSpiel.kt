package doppelkopf.game

class DoppelkopfSpiel(val spieler: Array<Spieler>) {
    val karten = Kartenstapel()
    var currentRunde: Runde? = null
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
        currentRunde = null
        for (s in spieler) {
            s.neueHand(karten.zieheKarten())
            s.partei = Partei.UNBEKANNT
            s.vorbehalt = null
        }
    }

    fun karteLegen(karte: Karte, pos: Position) {
        val s = spieler.first { it.pos == pos }
        val r = currentRunde ?: throw IllegalerZugException("Es kann noch keine Karte gelegt werden.")
        if (!s.hasKarte(karte)) throw IllegalerZugException("Diese Karte hat der Spieler nicht.")
        val aufspiel = r.aktuellerStich.aufspiel()
        if (aufspiel != null) {
            if (!s.korrektBedient(aufspiel, karte, aktuellerSpielmodus()!!)) throw IllegalerZugException("Falsch bedient.")
        }
        r.karteGelegt(karte, pos)
        s.legeKarte(karte)
        if (r.stichKomplett()) {
            r.stichGewinnerErmitteln()
            ermittleParteien() // Bei Hochzeit ergeben sich die Parteien möglicherweise erst jetzt.
        }
        if (r.rundeKomplett()) rundeAuswerten()
    }

    fun vorbehaltAnsagen(vorbehalt: Spielmodus, pos: Position) {
        if (currentRunde != null) throw IllegalerZugException("Vorbehalte können gerade nicht angesagt werden.")
        if (ansager != pos || spielerAnPos(pos).vorbehalt != null) throw IllegalerZugException("Spieler ist nicht dran mit ansagen.")
        spielerAnPos(pos).vorbehalt = vorbehalt
        ansager = ansager.next()
        if (vorbehaltVon(Position.LINKS) != null && vorbehaltVon(Position.RECHTS) != null
            && vorbehaltVon(Position.OBEN) != null && vorbehaltVon(Position.UNTEN) != null) {
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
            if (bestVorbehalt.isSolo()) {
                for (s in spieler) {
                    if (s.pos == bestPos) s.partei = Partei.RE else s.partei = Partei.KONTRA
                }
            } else {
                ermittleParteien()
            }
        }
    }

    fun werIstDran(): Position {
        val r = currentRunde
        return r?.werIstDran() ?: ansager
    }

    fun rundenauswertungen(): ArrayList<Rundenauswertung> {
        return rundenauswertungen
    }

    fun vorbehaltVon(pos: Position): Spielmodus? {
        return spielerAnPos(pos).vorbehalt
    }

    fun aktuellerStich(): Stich? {
        return currentRunde?.aktuellerStich
    }

    fun letzterStich(): Stich? {
        return currentRunde?.letzterStich
    }

    fun aktuellerSpielmodus(): Spielmodus? {
        return currentRunde?.welcherSpielmodus()
    }

    private fun spielerAnPos(pos: Position): Spieler {
        for (s in spieler) {
            if (s.pos == pos) return s
        }
        throw RuntimeException("Spieler an dieser Position existiert nicht.")
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
            Spielmodus.SOLO, Spielmodus.FLEISCHLOSER -> {
                // Das sollte nie erreicht werden.
                RuntimeException("Bei Solos muss die Partei bei Ansage bereits ermittelt worden sein.")
            }

            null -> RuntimeException("Parteien können nicht auf Spielmodus null ermittelt werden.")
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
        neuGeben()
    }
}