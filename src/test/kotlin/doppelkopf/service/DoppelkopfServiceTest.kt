package doppelkopf.service

import doppelkopf.game.Spielmodus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails


class DoppelkopfServiceTest {

    @Test
    fun testCreatePlayer() {
        val service = DoppelkopfService()
        val secretSpieler = service.syncJoin("Peter")
        val publicSpieler = service.getPublicSpielerInfo(secretSpieler.position)

        assertEquals("Peter", publicSpieler.name)
        assertEquals(secretSpieler.position, publicSpieler.position)

        val s = service.getPrivateSpielerInfo(secretSpieler.sessionToken)
        assertEquals(secretSpieler.name, s.name)
    }

    @Test
    fun testCreateAllPlayersAndStartGame() {
        val service = DoppelkopfService()
        val s1 = service.syncJoin("Peter")
        val s2 = service.syncJoin("Reiner")
        val s3 = service.syncJoin("Heidi")
        val s4 = service.syncJoin("Gustav")

        assertFails { service.syncJoin("Herrman") }
        assertEquals(12, service.getPrivateSpielerInfo(s1.sessionToken).hand.size)
    }

    @Test
    fun testsyncVorbehaltAnsagen() {
        val service = DoppelkopfService()
        val s1 = service.syncJoin("Peter")
        val s2 = service.syncJoin("Reiner")
        val s3 = service.syncJoin("Heidi")
        val s4 = service.syncJoin("Gustav")

        var currentTurnPlayer = service.getCurrentTurnPlayer()
        var privateCurrentTurn = listOf(s1, s2, s3, s4).first { it.position == currentTurnPlayer.position }
        service.syncVorbehaltAnsagen(privateCurrentTurn.sessionToken, Spielmodus.NORMAL)

        currentTurnPlayer = service.getCurrentTurnPlayer()
        privateCurrentTurn = listOf(s1, s2, s3, s4).first { it.position == currentTurnPlayer.position }
        service.syncVorbehaltAnsagen(privateCurrentTurn.sessionToken, Spielmodus.NORMAL)

        currentTurnPlayer = service.getCurrentTurnPlayer()
        privateCurrentTurn = listOf(s1, s2, s3, s4).first { it.position == currentTurnPlayer.position }
        service.syncVorbehaltAnsagen(privateCurrentTurn.sessionToken, Spielmodus.NORMAL)

        currentTurnPlayer = service.getCurrentTurnPlayer()
        privateCurrentTurn = listOf(s1, s2, s3, s4).first { it.position == currentTurnPlayer.position }
        service.syncVorbehaltAnsagen(privateCurrentTurn.sessionToken, Spielmodus.NORMAL)
    }

    @Test
    fun testsyncKarteLegen() {
        val service = DoppelkopfService()
        val s1 = service.syncJoin("Peter")
        val s2 = service.syncJoin("Reiner")
        val s3 = service.syncJoin("Heidi")
        val s4 = service.syncJoin("Gustav")

        var currentTurnPlayer = service.getCurrentTurnPlayer()
        var privateCurrentTurn = listOf(s1, s2, s3, s4).first { it.position == currentTurnPlayer.position }
        service.syncVorbehaltAnsagen(privateCurrentTurn.sessionToken, Spielmodus.NORMAL)
        currentTurnPlayer = service.getCurrentTurnPlayer()
        privateCurrentTurn = listOf(s1, s2, s3, s4).first { it.position == currentTurnPlayer.position }
        service.syncVorbehaltAnsagen(privateCurrentTurn.sessionToken, Spielmodus.NORMAL)
        currentTurnPlayer = service.getCurrentTurnPlayer()
        privateCurrentTurn = listOf(s1, s2, s3, s4).first { it.position == currentTurnPlayer.position }
        service.syncVorbehaltAnsagen(privateCurrentTurn.sessionToken, Spielmodus.NORMAL)
        currentTurnPlayer = service.getCurrentTurnPlayer()
        privateCurrentTurn = listOf(s1, s2, s3, s4).first { it.position == currentTurnPlayer.position }
        service.syncVorbehaltAnsagen(privateCurrentTurn.sessionToken, Spielmodus.NORMAL)

        currentTurnPlayer = service.getCurrentTurnPlayer()
        privateCurrentTurn = listOf(s1, s2, s3, s4).first { it.position == currentTurnPlayer.position }
        val playerWithCards = service.getPrivateSpielerInfo(privateCurrentTurn.sessionToken)
        service.syncKarteLegen(playerWithCards.sessionToken, playerWithCards.hand.first())
        assertEquals(11, service.getPrivateSpielerInfo(privateCurrentTurn.sessionToken).hand.size)
    }
}