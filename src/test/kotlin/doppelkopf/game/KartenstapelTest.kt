package doppelkopf.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KartenstapelTest {
    @Test
    fun testZieheKarten() {
        val stapel = Kartenstapel()
        val hand1 = stapel.zieheKarten()
        assertEquals(12, hand1.size)
        val hand2 = stapel.zieheKarten()
        assertEquals(12, hand2.size)
        val hand3 = stapel.zieheKarten()
        assertEquals(12, hand3.size)
        val hand4 = stapel.zieheKarten()
        assertEquals(12, hand4.size)
        // TODO: Duplikate checken
    }
}