package doppelkopf.game

class Spieler(val name: String, val pos: Position) {
    var hand = ArrayList<Karte>()
    val hasSchwein: Boolean = false;
    var punkte: Int = 0;
    var partei: Partei = Partei.UNBEKANNT

    fun hasKarte(k: Karte): Boolean {
        return hand.contains(k);
    }

    fun legeKarte(k: Karte) {
        if (hand.contains(k)) hand.remove(k) else throw IllegalerZugException("Karte $k nicht in Hand.")
    }

    fun neueHand(hand: ArrayList<Karte>, force: Boolean = false) {
        if (this.hand.isNotEmpty() && !force) throw IllegalerZugException("Hand ist nicht leer.") else this.hand = hand
    }
}