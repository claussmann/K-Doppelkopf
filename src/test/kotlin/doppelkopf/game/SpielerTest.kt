package doppelkopf.game

import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SpielerTest {

    @Test
    fun testHasKarte() {
        val spieler = Spieler("Peter", Position.LINKS)
        spieler.neueHand(arrayListOf(Karte.HE_K, Karte.HE_10))
        assertTrue(spieler.hasKarte(Karte.HE_10))
        assertFalse(spieler.hasKarte(Karte.PI_K))
    }

    @Test
    fun testLegeKarte() {
        val spieler = Spieler("Peter", Position.LINKS)
        spieler.neueHand(arrayListOf(Karte.HE_K, Karte.HE_10))
        spieler.legeKarte(Karte.HE_10)
        assertFalse(spieler.hasKarte(Karte.HE_10))
        assertFails { spieler.legeKarte(Karte.HE_10) }
        assertFails { spieler.legeKarte(Karte.PI_K) }
    }

    @Test
    fun testNeueHand() {
        val spieler = Spieler("Peter", Position.LINKS)
        spieler.neueHand(arrayListOf(Karte.HE_K, Karte.HE_10))
        assertFails { spieler.neueHand(arrayListOf(Karte.KA_B)) }
    }

    @Test
    fun testKorrektBedient() {
        val spieler = Spieler("Peter", Position.LINKS)
        spieler.neueHand(arrayListOf(Karte.HE_10, Karte.PI_D, Karte.KA_A, Karte.KR_A, Karte.PI_10, Karte.PI_9))
        assertTrue(spieler.korrektBedient(Karte.KA_9, Karte.PI_D, Spielmodus.NORMAL))
        assertFalse(spieler.korrektBedient(Karte.PI_10, Karte.PI_D, Spielmodus.NORMAL))
        assertTrue(spieler.korrektBedient(Karte.KR_K, Karte.KR_A, Spielmodus.NORMAL))
    }
}