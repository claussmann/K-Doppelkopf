package doppelkopf.game

import java.security.SecureRandom
import kotlin.random.Random

class Kartenstapel {
    private val stapel: Array<Karte> = arrayOf(
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
    private var currentCopy: ArrayList<Karte> = arrayListOf()
    private val rand = SecureRandom()

    private fun initialisieren() {
        stapel.shuffle()
        currentCopy = arrayListOf(*stapel)
    }

    /**
     * Zieht 12 Karten zufällig aus dem Stapel und entfernt diese aus dem Stapel.
     */
    fun zieheKarten(): ArrayList<Karte> {
        if (currentCopy.isEmpty()) {
            initialisieren()
        }
        val anzahl = 12
        val ret = ArrayList<Karte>()
        if (currentCopy.size < anzahl) throw IllegalArgumentException("Stapel enthält nicht genug Karten.")
        for (i in 1..anzahl) {
            ret.add(currentCopy.removeAt(rand.nextInt(0, currentCopy.size)))
        }
        return ret
    }
}