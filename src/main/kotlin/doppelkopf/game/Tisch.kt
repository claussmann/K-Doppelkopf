package doppelkopf.game

import kotlin.random.Random

class Tisch {
    val stapel: ArrayList<Karte> = arrayListOf(
        Karte.KARO_9, Karte.KARO_10, Karte.KARO_B, Karte.KARO_D, Karte.KARO_K, Karte.KARO_A,
        Karte.HERZ_9, Karte.HERZ_10, Karte.HERZ_B, Karte.HERZ_D, Karte.HERZ_K, Karte.HERZ_A,
        Karte.PIK_9, Karte.PIK_10, Karte.PIK_B, Karte.PIK_D, Karte.PIK_K, Karte.PIK_A,
        Karte.KREUZ_9, Karte.KREUZ_10, Karte.KREUZ_B, Karte.KREUZ_D, Karte.KREUZ_K, Karte.KREUZ_A,
        // Alle Karten doppelt
        Karte.KARO_9, Karte.KARO_10, Karte.KARO_B, Karte.KARO_D, Karte.KARO_K, Karte.KARO_A,
        Karte.HERZ_9, Karte.HERZ_10, Karte.HERZ_B, Karte.HERZ_D, Karte.HERZ_K, Karte.HERZ_A,
        Karte.PIK_9, Karte.PIK_10, Karte.PIK_B, Karte.PIK_D, Karte.PIK_K, Karte.PIK_A,
        Karte.KREUZ_9, Karte.KREUZ_10, Karte.KREUZ_B, Karte.KREUZ_D, Karte.KREUZ_K, Karte.KREUZ_A
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