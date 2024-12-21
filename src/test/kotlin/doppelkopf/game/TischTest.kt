package doppelkopf.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TischTest {
    @Test
    fun testMischen() {
        val tisch = Tisch()
        val before = tisch.stapel.size
        tisch.mischen()
        val after = tisch.stapel.size
        assertEquals(before, after)
    }

    @Test
    fun testZieheKarten() {
        val tisch = Tisch()
        val before = tisch.stapel.size
        val hand = tisch.zieheKarten(10)
        val after = tisch.stapel.size
        assertEquals(before - 10, after)
        for(k in hand) {
            val replicasInHand = hand.filter { t -> t == k }.size
            val replicasInStapel = tisch.stapel.filter { t -> t == k }.size
            assertTrue(replicasInStapel + replicasInHand == 2)
        }
    }
}