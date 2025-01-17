package doppelkopf.game

enum class Karte {
    KA_9 {override fun punkte() = 0},
    KA_10 {override fun punkte() = 10},
    KA_B {override fun punkte() = 2},
    KA_D {override fun punkte() = 3},
    KA_K {override fun punkte() = 4},
    KA_A {override fun punkte() = 11},

    HE_9 {override fun punkte() = 0},
    HE_10 {override fun punkte() = 10},
    HE_B {override fun punkte() = 2},
    HE_D {override fun punkte() = 3},
    HE_K {override fun punkte() = 4},
    HE_A {override fun punkte() = 11},

    PI_9 {override fun punkte() = 0},
    PI_10 {override fun punkte() = 10},
    PI_B {override fun punkte() = 2},
    PI_D {override fun punkte() = 3},
    PI_K {override fun punkte() = 4},
    PI_A {override fun punkte() = 11},

    KR_9 {override fun punkte() = 0},
    KR_10 {override fun punkte() = 10},
    KR_B {override fun punkte() = 2},
    KR_D {override fun punkte() = 3},
    KR_K {override fun punkte() = 4},
    KR_A {override fun punkte() = 11};

    abstract fun punkte(): Int

    fun sticht(other: Karte, letzteRunde: Boolean, schweinInRunde: Boolean, aufspiel: Karte, mod: Spielmodus): Boolean {
        return when (mod) {
            Spielmodus.NORMAL, Spielmodus.SOLO, Spielmodus.ARMUT, Spielmodus.HOCHZEIT -> stichtNormal(other, letzteRunde, schweinInRunde, aufspiel)
            Spielmodus.SOLO_REINES_PIK -> TODO()
            Spielmodus.SOLO_REINES_HERZ -> TODO()
            Spielmodus.SOLO_REINES_KARO -> TODO()
            Spielmodus.SOLO_REINES_KREUZ -> TODO()
            Spielmodus.SOLO_KARO -> TODO()
            Spielmodus.SOLO_HERZ -> TODO()
            Spielmodus.SOLO_PIK -> TODO()
            Spielmodus.SOLO_KREUZ -> TODO()
            Spielmodus.SOLO_DAME -> TODO()
            Spielmodus.SOLO_BUBE -> TODO()
        }
    }

    private fun stichtNormal(other: Karte, letzteRunde: Boolean, schweinInRunde: Boolean, aufspiel: Karte): Boolean {
        if (this == other && this == HE_10) {
            return !letzteRunde
        }
        if (this == other) {
            return false
        }
        if (schweinInRunde) {
            if (this == KA_A) return true
            if (other == KA_A) return false
        }

        val trumpf = arrayOf(KA_9, KA_K, KA_10, KA_A, KA_B, HE_B, PI_B, KR_B, KA_D, HE_D, PI_D, KR_D, HE_10)
        if (trumpf.contains(this) && !trumpf.contains(other)) return true
        if (!trumpf.contains(this) && trumpf.contains(other)) return false
        if (trumpf.contains(this) && trumpf.contains(other)) return trumpf.indexOf(other) < trumpf.indexOf(this)

        val herz = arrayOf(HE_9, HE_K, HE_A)
        val pik = arrayOf(PI_9, PI_K, PI_10, PI_A)
        val kreuz = arrayOf(KR_9, KR_K, KR_10, KR_A)
        if (herz.contains(aufspiel)) {
            if (herz.contains(this) && !herz.contains(other)) return true
            if (herz.contains(this) && herz.contains(other)) return herz.indexOf(other) < herz.indexOf(this)
            return false
        }
        else if (pik.contains(aufspiel)) {
            if (pik.contains(this) && !pik.contains(other)) return true
            if (pik.contains(this) && pik.contains(other)) return pik.indexOf(other) < pik.indexOf(this)
            return false
        }
        else {
            if (kreuz.contains(this) && !kreuz.contains(other)) return true
            if (kreuz.contains(this) && kreuz.contains(other)) return kreuz.indexOf(other) < kreuz.indexOf(this)
            return false
        }
    }
}