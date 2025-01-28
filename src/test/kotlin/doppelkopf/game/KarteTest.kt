package doppelkopf.game

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class KarteTest {

    @Test
    fun testStichtNormal() {
        // Trumpf sticht Fehl bei Fehl aufspiel
        assertTrue(Karte.KA_9.sticht(Karte.HE_9, false, false, Karte.HE_A, Spielmodus.NORMAL))
        assertFalse(Karte.HE_K.sticht(Karte.KA_K, false, false, Karte.HE_A, Spielmodus.NORMAL))
        // Höchster Trumpf bei Trumpf aufspiel
        assertTrue(Karte.PI_D.sticht(Karte.KA_A, false, false, Karte.HE_B, Spielmodus.NORMAL))
        // Höchster Fehl der aufgespielten Farbe
        assertTrue(Karte.KR_A.sticht(Karte.KR_10, false, false, Karte.KR_9, Spielmodus.NORMAL))
        assertTrue(Karte.KR_K.sticht(Karte.HE_A, false, false, Karte.KR_9, Spielmodus.NORMAL))
        // Schwein sticht alles
        assertTrue(Karte.KA_A.sticht(Karte.HE_10, false, true, Karte.PI_D, Spielmodus.NORMAL))
        // Herz 10 zweite sticht erste außer letzte Runde
        assertTrue(Karte.HE_10.sticht(Karte.HE_10, false, false, Karte.HE_A, Spielmodus.NORMAL))
        assertFalse(Karte.HE_10.sticht(Karte.HE_10, true, false, Karte.HE_A, Spielmodus.NORMAL))
        // Gleiche Karte kann nicht gestochen werden
        assertFalse(Karte.KA_10.sticht(Karte.KA_10, false, false, Karte.HE_A, Spielmodus.NORMAL))
    }

    @Test
    fun testIstTrumpf() {
        assertTrue(Karte.KA_K.istTrumpf(Spielmodus.NORMAL))
        assertTrue(Karte.KR_D.istTrumpf(Spielmodus.NORMAL))
        assertTrue(Karte.HE_10.istTrumpf(Spielmodus.NORMAL))
        assertFalse(Karte.HE_K.istTrumpf(Spielmodus.NORMAL))

        assertTrue(Karte.KR_D.istTrumpf(Spielmodus.SOLO_DAME))
        assertFalse(Karte.KR_B.istTrumpf(Spielmodus.SOLO_DAME))
        assertFalse(Karte.HE_B.istTrumpf(Spielmodus.SOLO_REINES_KREUZ))
        assertTrue(Karte.KR_10.istTrumpf(Spielmodus.SOLO_REINES_KREUZ))
    }
}