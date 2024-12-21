package doppelkopf.game

class Game(val spieler: Array<Spieler>) {
    val tisch: Tisch = Tisch()

    init {
        if (spieler.size != 4) throw IllegalArgumentException("Spieler must have 4 spielers")
    }

    fun neuGeben() {
        tisch.mischen()
        for (s in spieler) {
            s.neueHand(tisch.zieheKarten())
        }
    }
}