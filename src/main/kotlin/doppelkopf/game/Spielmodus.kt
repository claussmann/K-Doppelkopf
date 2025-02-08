package doppelkopf.game

enum class Spielmodus {
    NORMAL, HOCHZEIT, ARMUT,
    SOLO_REINES_KARO, SOLO_REINES_HERZ, SOLO_REINES_PIK, SOLO_REINES_KREUZ,
    SOLO_KARO, SOLO_HERZ, SOLO_PIK, SOLO_KREUZ,
    SOLO_DAME, SOLO_BUBE, SOLO, FLEISCHLOSER;

    fun vorrangVor(other: Spielmodus): Boolean {
        return false // TODO
    }

    fun isPflichtsolo(): Boolean {
        return when(this) {
            NORMAL, HOCHZEIT, ARMUT -> false
            else -> true
        }
    }
}