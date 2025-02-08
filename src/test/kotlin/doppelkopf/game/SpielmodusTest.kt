package doppelkopf.game

import kotlin.test.*

class SpielmodusTest {

    @Test
    fun testIsSolo() {
        assertTrue(Spielmodus.SOLO_BUBE.isSolo())
        assertFalse(Spielmodus.NORMAL.isSolo())
    }

    @Test
    fun testVorrang() {
        assertTrue(Spielmodus.ARMUT.vorrangVor(Spielmodus.HOCHZEIT))
        assertTrue(Spielmodus.ARMUT.vorrangVor(Spielmodus.NORMAL))
        assertTrue(Spielmodus.SOLO_DAME.vorrangVor(Spielmodus.HOCHZEIT))
        assertTrue(Spielmodus.SOLO_PIK.vorrangVor(Spielmodus.NORMAL))
    }
}