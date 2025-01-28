package doppelkopf.game

import kotlin.test.*

class StichTest {

    @Test
    fun testIstKomplett() {
        val a = Stich(2, Position.LINKS, Spielmodus.NORMAL)
        assertFalse(a.istKomplett())
        a.karteHinzu(Karte.KR_K, Position.LINKS)
        a.karteHinzu(Karte.KR_9, Position.OBEN)
        a.karteHinzu(Karte.KR_A, Position.RECHTS)
        assertFalse(a.istKomplett())
        a.karteHinzu(Karte.KR_10, Position.UNTEN)
        assertTrue(a.istKomplett())
    }

    @Test
    fun testKarteHinzu() {
        val a = Stich(2, Position.LINKS, Spielmodus.NORMAL)
        a.karteHinzu(Karte.KR_K, Position.LINKS)
        assertFails { a.karteHinzu(Karte.KR_10, Position.LINKS) } // Schon gelegt
        a.karteHinzu(Karte.KR_9, Position.OBEN)
        a.karteHinzu(Karte.KR_A, Position.RECHTS)
        a.karteHinzu(Karte.KR_10, Position.UNTEN)
    }

    @Test
    fun testPunkte() {
        val a = Stich(2, Position.LINKS, Spielmodus.NORMAL)
        a.karteHinzu(Karte.KR_K, Position.LINKS)
        a.karteHinzu(Karte.KR_9, Position.OBEN)
        a.karteHinzu(Karte.KR_A, Position.RECHTS)
        a.karteHinzu(Karte.KR_10, Position.UNTEN)
        assertEquals(25, a.punkte())
    }

    @Test
    fun testGewinnerErmitteln() {
        val a = Stich(2, Position.LINKS, Spielmodus.NORMAL)
        a.karteHinzu(Karte.KR_K, Position.LINKS)
        a.karteHinzu(Karte.KR_9, Position.OBEN)
        a.karteHinzu(Karte.KR_A, Position.RECHTS)
        a.karteHinzu(Karte.KR_10, Position.UNTEN)
        assertEquals(Position.RECHTS, a.gewinnerErmitteln())
    }
}