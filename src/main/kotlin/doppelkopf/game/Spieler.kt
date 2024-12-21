package doppelkopf.game

class Spieler(val name: String, val pos: Position) {
    var hand = ArrayList<Karte>()
    val hasSchwein: Boolean = false;

    fun hasKarte(k: Karte): Boolean {
        return hand.contains(k);
    }

    fun legeKarte(k: Karte) {
        if (hand.contains(k)) hand.remove(k) else throw IllegalerZugException("Karte $k nicht in Hand.")
    }

    fun neueHand(hand: ArrayList<Karte>) {
        if (this.hand.isNotEmpty()) throw IllegalerZugException("Hand ist noch nicht leer.") else this.hand = hand
    }
}