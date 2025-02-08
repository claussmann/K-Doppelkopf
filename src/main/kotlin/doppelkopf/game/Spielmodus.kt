package doppelkopf.game

enum class Spielmodus {
    NORMAL, HOCHZEIT, ARMUT,
    SOLO_REINES_KARO, SOLO_REINES_HERZ, SOLO_REINES_PIK, SOLO_REINES_KREUZ,
    SOLO_KARO, SOLO_HERZ, SOLO_PIK, SOLO_KREUZ,
    SOLO_DAME, SOLO_BUBE, SOLO, FLEISCHLOSER;

    fun vorrangVor(other: Spielmodus): Boolean {
        if (other.isPflichtsolo()) return false
        if (this.isPflichtsolo()) return true
        if (other.isSolo()) return false
        if (this.isSolo()) return true
        // Jetzt sind beide keine Soli
        val order = listOf(ARMUT, HOCHZEIT, NORMAL)
        return order.indexOf(this) < order.indexOf(other)
    }

    fun isPflichtsolo(): Boolean {
        // TODO
        return false
    }

    fun isSolo(): Boolean {
        return when(this) {
            NORMAL, HOCHZEIT, ARMUT -> false
            else -> true
        }
    }
}