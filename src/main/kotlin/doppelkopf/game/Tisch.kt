package doppelkopf.game

import kotlin.random.Random

class Tisch {
    val stapel: ArrayList<Karte> = arrayListOf(
        Karte.KA_9, Karte.KA_10, Karte.KA_B, Karte.KA_D, Karte.KA_K, Karte.KA_A,
        Karte.HE_9, Karte.HE_10, Karte.HE_B, Karte.HE_D, Karte.HE_K, Karte.HE_A,
        Karte.PI_9, Karte.PI_10, Karte.PI_B, Karte.PI_D, Karte.PI_K, Karte.PI_A,
        Karte.KR_9, Karte.KR_10, Karte.KR_B, Karte.KR_D, Karte.KR_K, Karte.KR_A,
        // Alle Karten doppelt
        Karte.KA_9, Karte.KA_10, Karte.KA_B, Karte.KA_D, Karte.KA_K, Karte.KA_A,
        Karte.HE_9, Karte.HE_10, Karte.HE_B, Karte.HE_D, Karte.HE_K, Karte.HE_A,
        Karte.PI_9, Karte.PI_10, Karte.PI_B, Karte.PI_D, Karte.PI_K, Karte.PI_A,
        Karte.KR_9, Karte.KR_10, Karte.KR_B, Karte.KR_D, Karte.KR_K, Karte.KR_A
    )

    init {
        mischen()
    }

    fun mischen() {
        stapel.shuffle()
    }

    fun zieheKarten(anzahl: Int = 12): ArrayList<Karte> {
        val ret = ArrayList<Karte>()
        if (stapel.size < anzahl) throw IllegalArgumentException("Stapel enthält nicht genug Karten.")
        for (i in 1..anzahl) {
            ret.add(stapel.removeAt(Random.nextInt(0, stapel.size)))
        }
        return ret
    }
}