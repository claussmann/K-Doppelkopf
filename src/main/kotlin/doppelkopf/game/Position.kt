package doppelkopf.game

enum class Position {
    LINKS {
        override fun next() = OBEN},
    OBEN {
        override fun next() = RECHTS},
    RECHTS {
        override fun next() = UNTEN},
    UNTEN {
        override fun next() = LINKS};

    abstract fun next(): Position
}