package doppelkopf.game

class Game(val spieler: Array<Spieler>) {
    val karten = Kartenstapel()

    init {
        if (spieler.size != 4) throw IllegalArgumentException("Spieler must have 4 spielers")
    }

    fun neuGeben() {
        for (s in spieler) {
            s.neueHand(karten.zieheKarten())
        }
    }
}