package doppelkopf.game

enum class Karte {
    KA_9 {
        override fun punkte() = 0;
        override fun farbe() = Farbe.KARO;
        override fun bild() = Bild.NEUN
    },
    KA_10 {
        override fun punkte() = 10;
        override fun farbe() = Farbe.KARO;
        override fun bild() = Bild.ZEHN
    },
    KA_B {
        override fun punkte() = 2;
        override fun farbe() = Farbe.KARO;
        override fun bild() = Bild.BUBE
    },
    KA_D {
        override fun punkte() = 3;
        override fun farbe() = Farbe.KARO;
        override fun bild() = Bild.DAME
    },
    KA_K {
        override fun punkte() = 4;
        override fun farbe() = Farbe.KARO;
        override fun bild() = Bild.KOENIG
    },
    KA_A {
        override fun punkte() = 11;
        override fun farbe() = Farbe.KARO;
        override fun bild() = Bild.ASS
    },

    HE_9 {
        override fun punkte() = 0;
        override fun farbe() = Farbe.HERZ;
        override fun bild() = Bild.NEUN
    },
    HE_10 {
        override fun punkte() = 10;
        override fun farbe() = Farbe.HERZ;
        override fun bild() = Bild.ZEHN
    },
    HE_B {
        override fun punkte() = 2;
        override fun farbe() = Farbe.HERZ;
        override fun bild() = Bild.BUBE
    },
    HE_D {
        override fun punkte() = 3;
        override fun farbe() = Farbe.HERZ;
        override fun bild() = Bild.DAME
    },
    HE_K {
        override fun punkte() = 4;
        override fun farbe() = Farbe.HERZ;
        override fun bild() = Bild.KOENIG
    },
    HE_A {
        override fun punkte() = 11;
        override fun farbe() = Farbe.HERZ;
        override fun bild() = Bild.ASS
    },

    PI_9 {
        override fun punkte() = 0;
        override fun farbe() = Farbe.PIK;
        override fun bild() = Bild.NEUN
    },
    PI_10 {
        override fun punkte() = 10;
        override fun farbe() = Farbe.PIK;
        override fun bild() = Bild.ZEHN
    },
    PI_B {
        override fun punkte() = 2;
        override fun farbe() = Farbe.PIK;
        override fun bild() = Bild.BUBE
    },
    PI_D {
        override fun punkte() = 3;
        override fun farbe() = Farbe.PIK;
        override fun bild() = Bild.DAME
    },
    PI_K {
        override fun punkte() = 4;
        override fun farbe() = Farbe.PIK;
        override fun bild() = Bild.KOENIG
    },
    PI_A {
        override fun punkte() = 11;
        override fun farbe() = Farbe.PIK;
        override fun bild() = Bild.ASS
    },

    KR_9 {
        override fun punkte() = 0;
        override fun farbe() = Farbe.KREUZ;
        override fun bild() = Bild.NEUN
    },
    KR_10 {
        override fun punkte() = 10;
        override fun farbe() = Farbe.KREUZ;
        override fun bild() = Bild.ZEHN
    },
    KR_B {
        override fun punkte() = 2;
        override fun farbe() = Farbe.KREUZ;
        override fun bild() = Bild.BUBE
    },
    KR_D {
        override fun punkte() = 3;
        override fun farbe() = Farbe.KREUZ;
        override fun bild() = Bild.DAME
    },
    KR_K {
        override fun punkte() = 4;
        override fun farbe() = Farbe.KREUZ;
        override fun bild() = Bild.KOENIG
    },
    KR_A {
        override fun punkte() = 11;
        override fun farbe() = Farbe.KREUZ;
        override fun bild() = Bild.ASS
    };

    abstract fun punkte(): Int
    abstract fun farbe(): Farbe
    abstract fun bild(): Bild

    fun istTrumpf(mod: Spielmodus): Boolean {
        return when (mod) {
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
            Spielmodus.FLEISCHLOSER -> false
        }
    }

    fun sticht(other: Karte, letzteRunde: Boolean, mod: Spielmodus): Boolean {
        // TODO: Schwein
        // Herz 10 Regelung
        if (mod in listOf(
                Spielmodus.NORMAL, Spielmodus.HOCHZEIT, Spielmodus.SOLO, Spielmodus.ARMUT,
                Spielmodus.SOLO_KARO, Spielmodus.SOLO_HERZ, Spielmodus.SOLO_KREUZ, Spielmodus.SOLO_PIK
            )
        ) {
            if (this == other && this == HE_10) {
                return !letzteRunde
            } else if (this == HE_10) {
                return true
            } else if (other == HE_10) {
                return false
            }
        }
        return stichtOhneSonderregel(other, mod)
    }

    private fun stichtOhneSonderregel(other: Karte, mod: Spielmodus): Boolean {
        if (this.istTrumpf(mod) && !other.istTrumpf(mod)) { // Trumpf sticht Fehl
            return true
        }
        if (!this.istTrumpf(mod) && other.istTrumpf(mod)) { // Fehl wird von Trumpf gestochen
            return false
        }
        if (this.istTrumpf(mod) && other.istTrumpf(mod)) { // Beide Trumpf; finde höheren
            if (this.bild() != other.bild()) {
                val reihenfolge = when(mod) {
                    Spielmodus.NORMAL, Spielmodus.HOCHZEIT, Spielmodus.ARMUT, Spielmodus.SOLO_KARO,
                    Spielmodus.SOLO_HERZ, Spielmodus.SOLO_PIK, Spielmodus.SOLO_KREUZ,
                    Spielmodus.SOLO  -> listOf(Bild.DAME, Bild.BUBE, Bild.ASS, Bild.ZEHN, Bild.KOENIG, Bild.NEUN)

                    else -> listOf(Bild.ASS, Bild.ZEHN, Bild.KOENIG, Bild.DAME, Bild.BUBE, Bild.NEUN)
                }
                return reihenfolge.indexOf(this.bild()) < reihenfolge.indexOf(other.bild())
            } else {
                if (this.farbe() != other.farbe()) {
                    val reihenfolge = listOf(Farbe.KREUZ, Farbe.PIK, Farbe.HERZ, Farbe.KARO)
                    return reihenfolge.indexOf(this.farbe()) < reihenfolge.indexOf(other.farbe())
                }
                return false
            }
        }

        // Beide Fehl; finde höheren
        if (this.farbe() != other.farbe()) { // Nicht dieselbe Farbe; nicht vergleichbar bei Fehl
            return false
        }
        val reihenfolge = listOf(Bild.ASS, Bild.ZEHN, Bild.KOENIG, Bild.DAME, Bild.BUBE, Bild.NEUN)
        return reihenfolge.indexOf(this.bild()) < reihenfolge.indexOf(other.bild())
    }
}