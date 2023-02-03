package river.exertion.kcop.system.text1d

enum class Text1dType {

    SEQUENCE,
    NAVIGATION,
    ;

    fun next() = if (this.ordinal == values().size - 1) values()[0] else values()[this.ordinal + 1]
    fun prev() = if (this.ordinal == 0) values()[values().size - 1] else values()[this.ordinal - 1]
}