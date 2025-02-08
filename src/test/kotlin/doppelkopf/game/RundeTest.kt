package doppelkopf.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class RundeTest {

    @Test
    fun testKarteLegen() {
        val r = Runde(Position.OBEN, Spielmodus.NORMAL)
        // Nicht dran
        assertFails { r.karteGelegt(Karte.KA_K, Position.RECHTS) }
        // Wenn gelegt, ist nächster dran
        r.karteGelegt(Karte.KA_K, Position.OBEN)
        r.karteGelegt(Karte.KA_A, Position.RECHTS)
        // Man kann nicht erneut legen
        assertFails { r.karteGelegt(Karte.KR_D, Position.RECHTS) }
        r.karteGelegt(Karte.PI_D, Position.UNTEN)
        r.karteGelegt(Karte.KR_B, Position.LINKS)
        // Wenn stich zuende, muss ausgewertet werden
        assertFails { r.karteGelegt(Karte.KA_K, Position.OBEN) }
        // Nach dem AUswerten geht es wieder und der gewinner fängt an
        r.stichGewinnerErmitteln()
        r.karteGelegt(Karte.HE_A, Position.UNTEN)
    }

    @Test
    fun testGewinnerbestimmung1() {
        val r = Runde(Position.OBEN, Spielmodus.NORMAL)
        r.karteGelegt(Karte.KA_K, Position.OBEN)
        r.karteGelegt(Karte.KA_A, Position.RECHTS)
        r.karteGelegt(Karte.PI_D, Position.UNTEN)
        r.karteGelegt(Karte.KR_B, Position.LINKS)
        assertEquals(Position.UNTEN, r.stichGewinnerErmitteln())
    }

    @Test
    fun testGewinnerbestimmung2() {
        val r = Runde(Position.OBEN, Spielmodus.NORMAL)
        r.karteGelegt(Karte.HE_A, Position.OBEN)
        r.karteGelegt(Karte.HE_9, Position.RECHTS)
        r.karteGelegt(Karte.HE_K, Position.UNTEN)
        r.karteGelegt(Karte.HE_9, Position.LINKS)
        assertEquals(Position.OBEN, r.stichGewinnerErmitteln())
    }

    @Test
    fun testGewinnerbestimmung3() {
        val r = Runde(Position.OBEN, Spielmodus.NORMAL)
        r.karteGelegt(Karte.KR_10, Position.OBEN)
        r.karteGelegt(Karte.KR_9, Position.RECHTS)
        r.karteGelegt(Karte.KR_A, Position.UNTEN)
        r.karteGelegt(Karte.PI_B, Position.LINKS)
        assertEquals(Position.LINKS, r.stichGewinnerErmitteln())
    }

    @Test
    fun testGewinnerbestimmung4() {
        val r = Runde(Position.OBEN, Spielmodus.NORMAL)
        r.karteGelegt(Karte.KR_10, Position.OBEN)
        r.karteGelegt(Karte.HE_9, Position.RECHTS)
        r.karteGelegt(Karte.KR_A, Position.UNTEN)
        r.karteGelegt(Karte.HE_K, Position.LINKS)
        assertEquals(Position.UNTEN, r.stichGewinnerErmitteln())
    }

    @Test
    fun testPunkteZahelen() {
        val r = Runde(Position.OBEN, Spielmodus.NORMAL)
        r.karteGelegt(Karte.KR_10, Position.OBEN)
        r.karteGelegt(Karte.KR_9, Position.RECHTS)
        r.karteGelegt(Karte.KR_A, Position.UNTEN)
        r.karteGelegt(Karte.PI_B, Position.LINKS)
        r.stichGewinnerErmitteln() // Links -> 23 Punkte
        r.karteGelegt(Karte.HE_10, Position.LINKS)
        r.karteGelegt(Karte.KA_A, Position.OBEN)
        r.karteGelegt(Karte.KR_D, Position.RECHTS)
        r.karteGelegt(Karte.PI_B, Position.UNTEN)
        r.stichGewinnerErmitteln() // Links -> 26 Punkte
        r.karteGelegt(Karte.HE_A, Position.LINKS)
        r.karteGelegt(Karte.HE_K, Position.OBEN)
        r.karteGelegt(Karte.KR_B, Position.RECHTS)
        r.karteGelegt(Karte.HE_9, Position.UNTEN)
        r.stichGewinnerErmitteln() // Rechts -> 17 Punkte

        assertEquals(0, r.punkteZahelen(Position.OBEN))
        assertEquals(0, r.punkteZahelen(Position.UNTEN))
        assertEquals(17, r.punkteZahelen(Position.RECHTS))
        assertEquals(49, r.punkteZahelen(Position.LINKS))
    }
}