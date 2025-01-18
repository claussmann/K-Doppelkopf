package doppelkopf.service

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails


class DoppelkopfServiceTest {

    @Test
    fun testCreatePlayer() {
        val service = DoppelkopfService()
        val secretSpieler = service.join("Peter")
        val publicSpieler = service.getPublicSpielerInfo(secretSpieler.position)

        assertEquals("Peter", publicSpieler.name)
        assertEquals(secretSpieler.position, publicSpieler.position)

        val s = service.getPrivateSpielerInfo(secretSpieler.sessionToken)
        assertEquals(secretSpieler.name, s.name)
    }

    @Test
    fun testCreateAllPlayersAndStartGame() {
        val service = DoppelkopfService()
        val s1 = service.join("Peter")
        val s2 = service.join("Reiner")
        val s3 = service.join("Heidi")
        val s4 = service.join("Gustav")

        assertFails { service.join("Herrman") }
        assertEquals(12, service.getPrivateSpielerInfo(s1.sessionToken).hand.size)
    }
}