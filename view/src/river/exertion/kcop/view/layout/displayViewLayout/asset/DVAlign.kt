package river.exertion.kcop.view.layout.displayViewLayout.asset

import com.badlogic.gdx.utils.Align

enum class DVAlign {

    CENTER { override fun tag() = "center"; override fun align() = Align.center},
    TOP { override fun tag() = "top"; override fun align() = Align.top},
    BOTTOM { override fun tag() = "bottom"; override fun align() = Align.bottom},
    LEFT { override fun tag() = "left"; override fun align() = Align.left},
    RIGHT { override fun tag() = "right"; override fun align() = Align.right},
    TOP_LEFT { override fun tag() = "topLeft"; override fun align() = Align.topLeft},
    TOP_RIGHT { override fun tag() = "topRight"; override fun align() = Align.topRight},
    BOTTOM_LEFT { override fun tag() = "bottomLeft"; override fun align() = Align.bottomLeft},
    BOTTOM_RIGHT { override fun tag() = "bottomRight"; override fun align() = Align.bottomRight}
    ;

    abstract fun tag() : String
    abstract fun align() : Int

    companion object {
        fun byTag(tag: String? = null) = entries.firstOrNull { tag == it.tag() }?.align() ?: TOP_LEFT.align()
    }
}