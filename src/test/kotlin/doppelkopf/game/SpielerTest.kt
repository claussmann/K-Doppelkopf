package doppelkopf.game

import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SpielerTest {

    @Test
    fun testHasKarte() {
        val spieler = Spieler("Peter", Position.LINKS)
        spieler.neueHand(arrayListOf(Karte.HERZ_K, Karte.HERZ_10))
        assertTrue(spieler.hasKarte(Karte.HERZ_10))
        assertFalse(spieler.hasKarte(Karte.PIK_K))
    }

    @Test
    fun testLegeKarte() {
        val spieler = Spieler("Peter", Position.LINKS)
        spieler.neueHand(arrayListOf(Karte.HERZ_K, Karte.HERZ_10))
        spieler.legeKarte(Karte.HERZ_10)
        assertFalse(spieler.hasKarte(Karte.HERZ_10))
        assertFails { spieler.legeKarte(Karte.HERZ_10) }
        assertFails { spieler.legeKarte(Karte.PIK_K) }
    }

    @Test
    fun testNeueHand() {
        val spieler = Spieler("Peter", Position.LINKS)
        spieler.neueHand(arrayListOf(Karte.HERZ_K, Karte.HERZ_10))
        assertFails { spieler.neueHand(arrayListOf(Karte.KARO_B)) }
    }
}