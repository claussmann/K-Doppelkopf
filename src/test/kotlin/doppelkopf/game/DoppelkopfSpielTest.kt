package doppelkopf.game

import kotlin.test.*

class DoppelkopfSpielTest {

    @Test
    fun testIllegaleSpieler() {
        val peter = Spieler("Peter", Position.OBEN)
        val jan = Spieler("Jan", Position.RECHTS)
        val fred = Spieler("Fred", Position.UNTEN)
        val ann = Spieler("Ann", Position.LINKS)
        val berta = Spieler("Ann", Position.LINKS)

        // Zu wenige
        assertFailsWith(IllegalArgumentException::class) { DoppelkopfSpiel(arrayOf(peter, jan, fred)) }
        // Gleiche position
        assertFailsWith(IllegalArgumentException::class) { DoppelkopfSpiel(arrayOf(peter, jan, ann, berta)) }
        // Zu viele
        assertFailsWith(IllegalArgumentException::class) { DoppelkopfSpiel(arrayOf(peter, jan, fred, ann, berta)) }
    }

    @Test
    fun testCompleteGame() {
        // Spieler hinzufügen
        val peter = Spieler("Peter", Position.OBEN)
        val jan = Spieler("Jan", Position.RECHTS)
        val fred = Spieler("Fred", Position.UNTEN)
        val ann = Spieler("Ann", Position.LINKS)
        val g = DoppelkopfSpiel(arrayOf(peter, jan, fred, ann))

        // Spielern feste Karten geben
        peter.neueHand(arrayListOf(
            Karte.HE_10, Karte.KR_D, Karte.HE_D, Karte.HE_D, Karte.PI_B, Karte.KA_10,
            Karte.KA_9, Karte.KR_10, Karte.KR_9, Karte.HE_K, Karte.PI_A, Karte.PI_K), force = true)
        jan.neueHand(arrayListOf(
            Karte.PI_D, Karte.KR_B, Karte.PI_B, Karte.KA_A, Karte.KA_9, Karte.PI_A,
            Karte.PI_K, Karte.KR_A, Karte.KR_10, Karte.HE_A, Karte.HE_9, Karte.HE_9), force = true)
        fred.neueHand(arrayListOf(
            Karte.KR_D, Karte.KA_D, Karte.KA_D, Karte.KR_B, Karte.HE_B, Karte.KR_K,
            Karte.KR_K, Karte.KR_9, Karte.PI_10, Karte.PI_9, Karte.HE_A, Karte.HE_K), force = true)
        ann.neueHand(arrayListOf(
            Karte.HE_10, Karte.PI_D, Karte.HE_B, Karte.KA_B, Karte.KA_B, Karte.KA_A,
            Karte.KA_10, Karte.KA_K, Karte.KA_K, Karte.KR_A, Karte.PI_10, Karte.PI_9), force = true)


        // Vorbehalt ansagen
        g.vorbehaltAnsagen(Spielmodus.NORMAL, Position.OBEN)
        g.vorbehaltAnsagen(Spielmodus.NORMAL, Position.RECHTS)
        g.vorbehaltAnsagen(Spielmodus.NORMAL, Position.UNTEN)
        g.vorbehaltAnsagen(Spielmodus.NORMAL, Position.LINKS)
        assertEquals(Spielmodus.NORMAL, g.aktuellerSpielmodus())
        assertEquals(Position.OBEN, g.werIstDran())
        assertEquals(Partei.RE, peter.partei)
        assertEquals(Partei.KONTRA, jan.partei)
        assertEquals(Partei.RE, fred.partei)
        assertEquals(Partei.KONTRA, ann.partei)

        // Runde 1
        g.karteLegen(Karte.PI_A, Position.OBEN)
        g.karteLegen(Karte.PI_K, Position.RECHTS)
        g.karteLegen(Karte.PI_9, Position.UNTEN)
        g.karteLegen(Karte.PI_9, Position.LINKS)
        assertEquals(Position.OBEN, g.werIstDran()) // 15 Punkte

        // Runde 2
        g.karteLegen(Karte.KR_9, Position.OBEN)
        g.karteLegen(Karte.KR_A, Position.RECHTS)
        g.karteLegen(Karte.KR_9, Position.UNTEN)
        g.karteLegen(Karte.KR_A, Position.LINKS)
        assertEquals(Position.RECHTS, g.werIstDran()) // 22 Punkte

        // Runde 3
        g.karteLegen(Karte.HE_A, Position.RECHTS)
        g.karteLegen(Karte.HE_K, Position.UNTEN)
        g.karteLegen(Karte.KA_A, Position.LINKS)
        g.karteLegen(Karte.HE_K, Position.OBEN)
        assertEquals(Position.LINKS, g.werIstDran()) // 30 Punkte

        // Runde 4
        g.karteLegen(Karte.PI_10, Position.LINKS)
        g.karteLegen(Karte.PI_K, Position.OBEN)
        g.karteLegen(Karte.PI_A, Position.RECHTS)
        g.karteLegen(Karte.PI_10, Position.UNTEN)
        assertEquals(Position.RECHTS, g.werIstDran()) // 35 Punkte

        // Runde 5
        g.karteLegen(Karte.KR_10, Position.RECHTS)
        g.karteLegen(Karte.KR_K, Position.UNTEN)
        g.karteLegen(Karte.KA_B, Position.LINKS)
        g.karteLegen(Karte.KR_10, Position.OBEN)
        assertEquals(Position.LINKS, g.werIstDran()) // 26 Punkte

        // Runde 6
        g.karteLegen(Karte.PI_D, Position.LINKS)
        g.karteLegen(Karte.KR_D, Position.OBEN)
        g.karteLegen(Karte.KA_9, Position.RECHTS)
        g.karteLegen(Karte.HE_B, Position.UNTEN)
        assertEquals(Position.OBEN, g.werIstDran()) // 8 Punkte

        // Runde 7
        g.karteLegen(Karte.KA_10, Position.OBEN)
        g.karteLegen(Karte.KA_A, Position.RECHTS)
        g.karteLegen(Karte.KR_D, Position.UNTEN)
        g.karteLegen(Karte.HE_10, Position.LINKS)
        assertEquals(Position.LINKS, g.werIstDran()) // 34 Punkte

        // Runde 8
        g.karteLegen(Karte.KA_10, Position.LINKS)
        g.karteLegen(Karte.HE_10, Position.OBEN)
        g.karteLegen(Karte.PI_B, Position.RECHTS)
        g.karteLegen(Karte.KR_B, Position.UNTEN)
        assertEquals(Position.OBEN, g.werIstDran()) // 24 Punkte

        // Runde 9
        g.karteLegen(Karte.KA_9, Position.OBEN)
        g.karteLegen(Karte.KR_B, Position.RECHTS)
        g.karteLegen(Karte.KA_D, Position.UNTEN)
        g.karteLegen(Karte.KA_B, Position.LINKS)
        assertEquals(Position.UNTEN, g.werIstDran()) // 7 Punkte

        // Runde 10
        g.karteLegen(Karte.HE_A, Position.UNTEN)
        g.karteLegen(Karte.HE_B, Position.LINKS)
        g.karteLegen(Karte.PI_B, Position.OBEN)
        g.karteLegen(Karte.HE_9, Position.RECHTS)
        assertEquals(Position.OBEN, g.werIstDran()) // 15 Punkte

        // Runde 11
        g.karteLegen(Karte.HE_D, Position.OBEN)
        g.karteLegen(Karte.PI_D, Position.RECHTS)
        g.karteLegen(Karte.KA_D, Position.UNTEN)
        g.karteLegen(Karte.KA_K, Position.LINKS)
        assertEquals(Position.RECHTS, g.werIstDran()) // 13 Punkte

        // Runde 12
        g.karteLegen(Karte.HE_9, Position.RECHTS)
        g.karteLegen(Karte.KR_K, Position.UNTEN)
        g.karteLegen(Karte.KA_K, Position.LINKS)
        g.karteLegen(Karte.HE_D, Position.OBEN) // Oben gewinnt 11 Punkte

        // Runde sollte nun ausgewertet sein
        // Re hat 80 Punkte. Gegen die alten. Keine 90.
        val auswertung = g.rundenauswertungen().last()
        assertEquals(80, auswertung.stichpunkteRe)
        assertEquals(-3, auswertung.rundenpunkte)
        assertEquals(Spielmodus.NORMAL, auswertung.spielmodus)
        assertTrue(auswertung.gegenDieAlten)
        assertEquals(-3, peter.punkte)
        assertEquals(3, jan.punkte)
        assertEquals(-3, fred.punkte)
        assertEquals(3, ann.punkte)

        // Es sollte neu gegeben werden
        assertEquals(12, peter.hand.size)
        assertEquals(12, jan.hand.size)
        assertEquals(12, fred.hand.size)
        assertEquals(12, ann.hand.size)
        assertNull(peter.vorbehalt)
        assertEquals(Partei.UNBEKANNT, peter.partei)
    }

    @Test
    fun testAnsageUndParteienKorrektNormalGame() {
        val peter = Spieler("Peter", Position.OBEN)
        val jan = Spieler("Jan", Position.RECHTS)
        val fred = Spieler("Fred", Position.UNTEN)
        val ann = Spieler("Ann", Position.LINKS)
        val g = DoppelkopfSpiel(arrayOf(peter, jan, fred, ann))

        peter.neueHand(arrayListOf(
            Karte.HE_10, Karte.KR_D, Karte.HE_D, Karte.HE_D, Karte.PI_B, Karte.KA_10,
            Karte.KA_9, Karte.KR_10, Karte.KR_9, Karte.HE_K, Karte.PI_A, Karte.PI_K), force = true)
        jan.neueHand(arrayListOf(
            Karte.PI_D, Karte.KR_B, Karte.PI_B, Karte.KA_A, Karte.KA_9, Karte.PI_A,
            Karte.PI_K, Karte.KR_A, Karte.KR_10, Karte.HE_A, Karte.HE_9, Karte.HE_9), force = true)
        fred.neueHand(arrayListOf(
            Karte.KR_D, Karte.KA_D, Karte.KA_D, Karte.KR_B, Karte.HE_B, Karte.KR_K,
            Karte.KR_K, Karte.KR_9, Karte.PI_10, Karte.PI_9, Karte.HE_A, Karte.HE_K), force = true)
        ann.neueHand(arrayListOf(
            Karte.HE_10, Karte.PI_D, Karte.HE_B, Karte.KA_B, Karte.KA_B, Karte.KA_A,
            Karte.KA_10, Karte.KA_K, Karte.KA_K, Karte.KR_A, Karte.PI_10, Karte.PI_9), force = true)

        g.vorbehaltAnsagen(Spielmodus.NORMAL, Position.OBEN)
        g.vorbehaltAnsagen(Spielmodus.NORMAL, Position.RECHTS)
        g.vorbehaltAnsagen(Spielmodus.NORMAL, Position.UNTEN)
        g.vorbehaltAnsagen(Spielmodus.NORMAL, Position.LINKS)

        assertEquals(Spielmodus.NORMAL, g.aktuellerSpielmodus())
        assertEquals(Partei.RE, peter.partei)
        assertEquals(Partei.KONTRA, jan.partei)
        assertEquals(Partei.RE, fred.partei)
        assertEquals(Partei.KONTRA, ann.partei)
    }

    @Test
    fun testAnsageUndParteienKorrektSolo() {
        val peter = Spieler("Peter", Position.OBEN)
        val jan = Spieler("Jan", Position.RECHTS)
        val fred = Spieler("Fred", Position.UNTEN)
        val ann = Spieler("Ann", Position.LINKS)
        val g = DoppelkopfSpiel(arrayOf(peter, jan, fred, ann))

        g.vorbehaltAnsagen(Spielmodus.NORMAL, Position.OBEN)
        g.vorbehaltAnsagen(Spielmodus.SOLO_BUBE, Position.RECHTS)
        g.vorbehaltAnsagen(Spielmodus.NORMAL, Position.UNTEN)
        g.vorbehaltAnsagen(Spielmodus.NORMAL, Position.LINKS)

        assertEquals(Spielmodus.SOLO_BUBE, g.aktuellerSpielmodus())
        assertEquals(Partei.KONTRA, peter.partei)
        assertEquals(Partei.RE, jan.partei)
        assertEquals(Partei.KONTRA, fred.partei)
        assertEquals(Partei.KONTRA, ann.partei)
    }

    @Test
    fun testAnsageUndParteienKorrektHochzeit() {
        val peter = Spieler("Peter", Position.OBEN)
        val jan = Spieler("Jan", Position.RECHTS)
        val fred = Spieler("Fred", Position.UNTEN)
        val ann = Spieler("Ann", Position.LINKS)
        val g = DoppelkopfSpiel(arrayOf(peter, jan, fred, ann))

        peter.neueHand(arrayListOf(
            Karte.HE_10, Karte.KR_D, Karte.KR_D, Karte.HE_D, Karte.PI_B, Karte.KA_10,
            Karte.KA_9, Karte.KR_10, Karte.KR_9, Karte.HE_K, Karte.PI_A, Karte.PI_K), force = true)
        jan.neueHand(arrayListOf(
            Karte.PI_D, Karte.KR_B, Karte.PI_B, Karte.KA_A, Karte.KA_9, Karte.PI_A,
            Karte.PI_K, Karte.KR_A, Karte.KR_10, Karte.HE_A, Karte.HE_9, Karte.HE_9), force = true)
        fred.neueHand(arrayListOf(
            Karte.HE_D, Karte.KA_D, Karte.KA_D, Karte.KR_B, Karte.HE_B, Karte.KR_K,
            Karte.KR_K, Karte.KR_9, Karte.PI_10, Karte.PI_9, Karte.HE_A, Karte.HE_K), force = true)
        ann.neueHand(arrayListOf(
            Karte.HE_10, Karte.PI_D, Karte.HE_B, Karte.KA_B, Karte.KA_B, Karte.KA_A,
            Karte.KA_10, Karte.KA_K, Karte.KA_K, Karte.KR_A, Karte.PI_10, Karte.PI_9), force = true)

        g.vorbehaltAnsagen(Spielmodus.HOCHZEIT, Position.OBEN)
        g.vorbehaltAnsagen(Spielmodus.NORMAL, Position.RECHTS)
        g.vorbehaltAnsagen(Spielmodus.NORMAL, Position.UNTEN)
        g.vorbehaltAnsagen(Spielmodus.NORMAL, Position.LINKS)

        assertEquals(Spielmodus.HOCHZEIT, g.aktuellerSpielmodus())
        assertEquals(Partei.RE, peter.partei)
        assertEquals(Partei.UNBEKANNT, jan.partei)
        assertEquals(Partei.UNBEKANNT, fred.partei)
        assertEquals(Partei.UNBEKANNT, ann.partei)

        // Runde 1 geht an Hochzeit selbst
        g.karteLegen(Karte.PI_A, Position.OBEN)
        g.karteLegen(Karte.PI_K, Position.RECHTS)
        g.karteLegen(Karte.PI_9, Position.UNTEN)
        g.karteLegen(Karte.PI_9, Position.LINKS)
        assertEquals(Partei.RE, peter.partei)
        assertEquals(Partei.UNBEKANNT, jan.partei)
        assertEquals(Partei.UNBEKANNT, fred.partei)
        assertEquals(Partei.UNBEKANNT, ann.partei)

        // Runde 2 wird abgestochen
        g.karteLegen(Karte.KR_10, Position.OBEN)
        g.karteLegen(Karte.KR_A, Position.RECHTS)
        g.karteLegen(Karte.KR_9, Position.UNTEN)
        g.karteLegen(Karte.KR_A, Position.LINKS)
        assertEquals(Partei.RE, peter.partei)
        assertEquals(Partei.RE, jan.partei)
        assertEquals(Partei.KONTRA, fred.partei)
        assertEquals(Partei.KONTRA, ann.partei)
    }

    @Test
    fun testAnsageHochzeitUnmoeglich() {
        val peter = Spieler("Peter", Position.OBEN)
        val jan = Spieler("Jan", Position.RECHTS)
        val fred = Spieler("Fred", Position.UNTEN)
        val ann = Spieler("Ann", Position.LINKS)
        val g = DoppelkopfSpiel(arrayOf(peter, jan, fred, ann))

        peter.neueHand(arrayListOf(
            Karte.HE_10, Karte.KR_D, Karte.PI_D, Karte.HE_D, Karte.PI_B, Karte.KA_10,
            Karte.KA_9, Karte.KR_10, Karte.KR_9, Karte.HE_K, Karte.PI_A, Karte.PI_K), force = true)
        jan.neueHand(arrayListOf(
            Karte.KR_D, Karte.KR_B, Karte.PI_B, Karte.KA_A, Karte.KA_9, Karte.PI_A,
            Karte.PI_K, Karte.KR_A, Karte.KR_10, Karte.HE_A, Karte.HE_9, Karte.HE_9), force = true)
        fred.neueHand(arrayListOf(
            Karte.HE_D, Karte.KA_D, Karte.KA_D, Karte.KR_B, Karte.HE_B, Karte.KR_K,
            Karte.KR_K, Karte.KR_9, Karte.PI_10, Karte.PI_9, Karte.HE_A, Karte.HE_K), force = true)
        ann.neueHand(arrayListOf(
            Karte.HE_10, Karte.PI_D, Karte.HE_B, Karte.KA_B, Karte.KA_B, Karte.KA_A,
            Karte.KA_10, Karte.KA_K, Karte.KA_K, Karte.KR_A, Karte.PI_10, Karte.PI_9), force = true)

        // Spieler hat nur eine Kreuz Dame
        assertFails { g.vorbehaltAnsagen(Spielmodus.HOCHZEIT, Position.OBEN) }
    }
}