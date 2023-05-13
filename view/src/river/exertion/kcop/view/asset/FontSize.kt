package river.exertion.kcop.view.asset

enum class FontSize {
    TEXT {override fun fontScale() = .28f; override fun fontTag() = "text"},
    SMALL {override fun fontScale() = .41f; override fun fontTag() = "small"},
    MEDIUM {override fun fontScale() = .55f; override fun fontTag() = "medium"},
    LARGE {override fun fontScale() = .68f; override fun fontTag() = "large"}
    ;
    abstract fun fontScale() : Float
    abstract fun fontTag() : String

    companion object {
        const val baseFontSize = 48
        fun byTag(tag : String?) = values().firstOrNull { it.fontTag() == tag } ?: TEXT
    }
}