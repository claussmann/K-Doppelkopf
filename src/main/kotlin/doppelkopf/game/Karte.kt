package doppelkopf.game

enum class Karte {
    KA_9 {override fun punkte() = 0; override fun farbe() = Farbe.KARO; override fun bild() = Bild.NEUN},
    KA_10 {override fun punkte() = 10; override fun farbe() = Farbe.KARO; override fun bild() = Bild.ZEHN},
    KA_B {override fun punkte() = 2; override fun farbe() = Farbe.KARO; override fun bild() = Bild.BUBE},
    KA_D {override fun punkte() = 3; override fun farbe() = Farbe.KARO; override fun bild() = Bild.DAME},
    KA_K {override fun punkte() = 4; override fun farbe() = Farbe.KARO; override fun bild() = Bild.KOENIG},
    KA_A {override fun punkte() = 11; override fun farbe() = Farbe.KARO; override fun bild() = Bild.ASS},

    HE_9 {override fun punkte() = 0; override fun farbe() = Farbe.HERZ; override fun bild() = Bild.NEUN},
    HE_10 {override fun punkte() = 10; override fun farbe() = Farbe.HERZ; override fun bild() = Bild.ZEHN},
    HE_B {override fun punkte() = 2; override fun farbe() = Farbe.HERZ; override fun bild() = Bild.BUBE},
    HE_D {override fun punkte() = 3; override fun farbe() = Farbe.HERZ; override fun bild() = Bild.DAME},
    HE_K {override fun punkte() = 4; override fun farbe() = Farbe.HERZ; override fun bild() = Bild.KOENIG},
    HE_A {override fun punkte() = 11; override fun farbe() = Farbe.HERZ; override fun bild() = Bild.ASS},

    PI_9 {override fun punkte() = 0; override fun farbe() = Farbe.PIK; override fun bild() = Bild.NEUN},
    PI_10 {override fun punkte() = 10; override fun farbe() = Farbe.PIK; override fun bild() = Bild.ZEHN},
    PI_B {override fun punkte() = 2; override fun farbe() = Farbe.PIK; override fun bild() = Bild.BUBE},
    PI_D {override fun punkte() = 3; override fun farbe() = Farbe.PIK; override fun bild() = Bild.DAME},
    PI_K {override fun punkte() = 4; override fun farbe() = Farbe.PIK; override fun bild() = Bild.KOENIG},
    PI_A {override fun punkte() = 11; override fun farbe() = Farbe.PIK; override fun bild() = Bild.ASS},

    KR_9 {override fun punkte() = 0; override fun farbe() = Farbe.KREUZ; override fun bild() = Bild.NEUN},
    KR_10 {override fun punkte() = 10; override fun farbe() = Farbe.KREUZ; override fun bild() = Bild.ZEHN},
    KR_B {override fun punkte() = 2; override fun farbe() = Farbe.KREUZ; override fun bild() = Bild.BUBE},
    KR_D {override fun punkte() = 3; override fun farbe() = Farbe.KREUZ; override fun bild() = Bild.DAME},
    KR_K {override fun punkte() = 4; override fun farbe() = Farbe.KREUZ; override fun bild() = Bild.KOENIG},
    KR_A {override fun punkte() = 11; override fun farbe() = Farbe.KREUZ; override fun bild() = Bild.ASS};

    abstract fun punkte(): Int
    abstract fun farbe(): Farbe
    abstract fun bild(): Bild

    fun istTrumpf(mod: Spielmodus): Boolean {
        return when(mod) {
            Spielmodus.NORMAL, Spielmodus.SOLO, Spielmodus.ARMUT, Spielmodus.HOCHZEIT, Spielmodus.SOLO_KARO -> {
                this.bild() == Bild.BUBE || this.bild() == Bild.DAME || this.farbe() == Farbe.KARO || this == HE_10
            }
            Spielmodus.SOLO_REINES_PIK -> this.farbe() == Farbe.PIK
            Spielmodus.SOLO_REINES_HERZ -> this.farbe() == Farbe.HERZ
            Spielmodus.SOLO_REINES_KARO -> this.farbe() == Farbe.KARO
            Spielmodus.SOLO_REINES_KREUZ -> this.farbe() == Farbe.KREUZ
            Spielmodus.SOLO_HERZ -> {
                this.bild() == Bild.BUBE || this.bild() == Bild.DAME || this.farbe() == Farbe.HERZ
            }
            Spielmodus.SOLO_PIK -> {
                this.bild() == Bild.BUBE || this.bild() == Bild.DAME || this.farbe() == Farbe.PIK || this == HE_10
            }
            Spielmodus.SOLO_KREUZ -> {
                this.bild() == Bild.BUBE || this.bild() == Bild.DAME || this.farbe() == Farbe.KREUZ || this == HE_10
            }
            Spielmodus.SOLO_DAME -> this.bild() == Bild.DAME
            Spielmodus.SOLO_BUBE -> this.bild() == Bild.BUBE
        }
    }

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