package doppelkopf.game

class Spieler(val name: String, val pos: Position) {
    var hand = ArrayList<Karte>()
    val hasSchwein: Boolean = false
    var punkte: Int = 0
    var partei: Partei = Partei.UNBEKANNT
    var vorbehalt: Spielmodus? = null

    fun hasKarte(k: Karte): Boolean {
        return hand.contains(k);
    }

    fun korrektBedient(aufspiel: Karte, willLegen: Karte, modus: Spielmodus): Boolean {
        val farbeAufsp = aufspiel.farbe()
        val trumpfAufsp = aufspiel.istTrumpf(modus)
        if (trumpfAufsp) {
            if (willLegen.istTrumpf(modus)) return true
            for (k in hand) {
                if (k.istTrumpf(modus)) return false
            }
        } else {
            if (willLegen.farbe() == farbeAufsp && !willLegen.istTrumpf(modus)) return true
            for (k in hand) {
                if (k.farbe() == farbeAufsp && !k.istTrumpf(modus)) return false
            }
        }
        return true
    }

    fun legeKarte(k: Karte) {
        if (hand.contains(k)) hand.remove(k) else throw IllegalerZugException("Karte $k nicht in Hand.")
    }

    fun neueHand(hand: ArrayList<Karte>, force: Boolean = false) {
        if (this.hand.isNotEmpty() && !force) throw IllegalerZugException("Hand ist nicht leer.") else this.hand = hand
    }
}