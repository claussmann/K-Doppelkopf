package doppelkopf.game

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class KarteTest {

    @Test
    fun testStichtNormalspiel() {
        // Trumpf sticht Fehl
        assertTrue(Karte.KA_9.sticht(Karte.HE_9, false, Spielmodus.NORMAL))
        assertFalse(Karte.HE_K.sticht(Karte.KA_K, false, Spielmodus.NORMAL))
        // Höchster Trumpf
        assertTrue(Karte.PI_D.sticht(Karte.KA_A, false, Spielmodus.NORMAL))
        assertFalse(Karte.PI_B.sticht(Karte.KR_B, false, Spielmodus.NORMAL))
        // Höchster Fehl
        assertTrue(Karte.KR_A.sticht(Karte.KR_10, false, Spielmodus.NORMAL))
        // Herz 10 sticht alles
        assertTrue(Karte.HE_10.sticht(Karte.KR_D, false, Spielmodus.NORMAL))
        assertFalse(Karte.HE_B.sticht(Karte.HE_10, false, Spielmodus.NORMAL))
        // Herz 10 zweite sticht erste außer letzte Runde
        assertTrue(Karte.HE_10.sticht(Karte.HE_10, false, Spielmodus.NORMAL))
        assertFalse(Karte.HE_10.sticht(Karte.HE_10, true, Spielmodus.NORMAL))
        // Gleiche Karte kann nicht gestochen werden
        assertFalse(Karte.KA_10.sticht(Karte.KA_10, false, Spielmodus.NORMAL))
    }

    @Test
    fun testStichtBubensolo() {
        assertTrue(Karte.KR_B.sticht(Karte.HE_9, false, Spielmodus.SOLO_BUBE))
        assertFalse(Karte.PI_D.sticht(Karte.HE_B, false, Spielmodus.SOLO_BUBE))
        assertTrue(Karte.KA_B.sticht(Karte.HE_10, false, Spielmodus.SOLO_BUBE))
        assertTrue(Karte.KR_B.sticht(Karte.HE_B, false, Spielmodus.SOLO_BUBE))
        assertTrue(Karte.KR_A.sticht(Karte.KR_10, false, Spielmodus.SOLO_BUBE))
    }

    @Test
    fun testStichtDamensolo() {
        assertTrue(Karte.KR_D.sticht(Karte.HE_9, false, Spielmodus.SOLO_DAME))
        assertFalse(Karte.PI_B.sticht(Karte.HE_D, false, Spielmodus.SOLO_DAME))
        assertTrue(Karte.KA_D.sticht(Karte.HE_10, false, Spielmodus.SOLO_DAME))
        assertTrue(Karte.KR_D.sticht(Karte.HE_D, false, Spielmodus.SOLO_DAME))
        assertTrue(Karte.KR_A.sticht(Karte.KR_10, false, Spielmodus.SOLO_DAME))
    }

    @Test
    fun testStichtReinesFarbsolo() {
        assertTrue(Karte.KR_9.sticht(Karte.HE_A, false, Spielmodus.SOLO_REINES_KREUZ))
        assertTrue(Karte.KR_10.sticht(Karte.KR_9, false, Spielmodus.SOLO_REINES_KREUZ))
        assertTrue(Karte.KR_A.sticht(Karte.KR_D, false, Spielmodus.SOLO_REINES_KREUZ))
        assertTrue(Karte.HE_K.sticht(Karte.HE_D, false, Spielmodus.SOLO_REINES_KREUZ))
    }

    @Test
    fun testStichtFleischlos() {
        assertTrue(Karte.HE_K.sticht(Karte.HE_9, false, Spielmodus.FLEISCHLOSER))
        assertFalse(Karte.KR_10.sticht(Karte.HE_9, false, Spielmodus.FLEISCHLOSER))
        assertFalse(Karte.KR_B.sticht(Karte.PI_A, false, Spielmodus.FLEISCHLOSER))
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