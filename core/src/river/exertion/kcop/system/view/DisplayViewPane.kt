package river.exertion.kcop.system.view

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import river.exertion.kcop.system.colorPalette.ColorPalette

enum class DisplayViewPane {

    CENTER {
        override fun width(screenWidth: Float) = ViewType.fourthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fourthHeight(screenHeight)
        override fun defaultColor() = firstDefaultColor
    },
    BOTTOM_LEFT {
        override fun width(screenWidth: Float) = ViewType.thirdWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.thirdHeight(screenHeight)
        override fun defaultColor() = firstDefaultColor.comp()
    },
    TOP_LEFT {
        override fun width(screenWidth: Float) = ViewType.thirdWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.thirdHeight(screenHeight)
        override fun defaultColor() = firstDefaultColor.triad().first
    },
    BOTTOM_RIGHT {
        override fun width(screenWidth: Float) = ViewType.thirdWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.thirdHeight(screenHeight)
        override fun defaultColor() = firstDefaultColor.triad().second
    },
    TOP_RIGHT_CORNER {
        override fun width(screenWidth: Float) = ViewType.fourthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fourthHeight(screenHeight)
        override fun defaultColor() = firstDefaultColor.comp().triad().first
    },
    LEFT_SIDE {
        override fun width(screenWidth: Float) = ViewType.fourthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fourthHeight(screenHeight)
        override fun defaultColor() = firstDefaultColor.comp().triad().second
    },
    BOTTOM {
        override fun width(screenWidth: Float) = ViewType.fourthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fourthHeight(screenHeight)
        override fun defaultColor() = secondDefaultColor
    },
    TOP {
        override fun width(screenWidth: Float) = ViewType.fourthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fourthHeight(screenHeight)
        override fun defaultColor() = secondDefaultColor.comp()
    },
    RIGHT_SIDE {
        override fun width(screenWidth: Float) = ViewType.fourthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fourthHeight(screenHeight)
        override fun defaultColor() = secondDefaultColor.triad().first
    },
    LEFT_BOX {
        override fun width(screenWidth: Float) = ViewType.fifthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fifthHeight(screenHeight)
        override fun defaultColor() = secondDefaultColor.triad().second
    },
    BOTTOM_BOX {
        override fun width(screenWidth: Float) = ViewType.fifthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fifthHeight(screenHeight)
        override fun defaultColor() = secondDefaultColor.comp().triad().second
    },
    TOP_BOX {
        override fun width(screenWidth: Float) = ViewType.fifthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fifthHeight(screenHeight)
        override fun defaultColor() = secondDefaultColor.comp().triad().second
    },
    RIGHT_BOX {
        override fun width(screenWidth: Float) = ViewType.fifthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fifthHeight(screenHeight)
        override fun defaultColor() = firstDefaultColor
    },
    RIGHT_UPPER_BOX {
        override fun width(screenWidth: Float) = ViewType.fifthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fifthHeight(screenHeight)
        override fun defaultColor() = firstDefaultColor.comp()
    },
    UPPER_RIGHT_BOX {
        override fun width(screenWidth: Float) = ViewType.fifthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fifthHeight(screenHeight)
        override fun defaultColor() = firstDefaultColor.triad().first
    },
    VOID_BOX {
        override fun width(screenWidth: Float) = ViewType.fifthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fifthHeight(screenHeight)
        override fun defaultColor() = firstDefaultColor.triad().second
    },
    LEFT_TWO {
        override fun width(screenWidth: Float) = ViewType.sixthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.sixthHeight(screenHeight)
        override fun defaultColor() = firstDefaultColor.comp().triad().first
    },
    BOTTOM_TWO {
        override fun width(screenWidth: Float) = ViewType.sixthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.sixthHeight(screenHeight)
        override fun defaultColor() = firstDefaultColor.comp().triad().second
    },
    TOP_TWO {
        override fun width(screenWidth: Float) = ViewType.sixthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.sixthHeight(screenHeight)
        override fun defaultColor() = secondDefaultColor
    },
    RIGHT_TWO {
        override fun width(screenWidth: Float) = ViewType.sixthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.sixthHeight(screenHeight)
        override fun defaultColor() = secondDefaultColor.comp()
    },
    RIGHT_UPPER_TWO {
        override fun width(screenWidth: Float) = ViewType.sixthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.sixthHeight(screenHeight)
        override fun defaultColor() = secondDefaultColor.triad().first
    },
    UPPER_RIGHT_TWO {
        override fun width(screenWidth: Float) = ViewType.sixthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.sixthHeight(screenHeight)
        override fun defaultColor() = secondDefaultColor.triad().second
    },
    LEFT_UNIT_FIRST {
        override fun width(screenWidth: Float) = ViewType.seventhWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.seventhHeight(screenHeight)
        override fun defaultColor() = secondDefaultColor.comp().triad().first
    },
    BOTTOM_UNIT_FIRST {
        override fun width(screenWidth: Float) = ViewType.seventhWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.seventhHeight(screenHeight)
        override fun defaultColor() = secondDefaultColor.comp().triad().second
    },
    TOP_UNIT_FIRST {
        override fun width(screenWidth: Float) = ViewType.seventhWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.seventhHeight(screenHeight)
        override fun defaultColor() = firstDefaultColor
    },
    RIGHT_UNIT_FIRST {
        override fun width(screenWidth: Float) = ViewType.seventhWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.seventhHeight(screenHeight)
        override fun defaultColor() = firstDefaultColor.comp()
    },
    RIGHT_UPPER_UNIT_FIRST {
        override fun width(screenWidth: Float) = ViewType.seventhWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.seventhHeight(screenHeight)
        override fun defaultColor() = firstDefaultColor.triad().first
    },
    UPPER_RIGHT_UNIT_FIRST {
        override fun width(screenWidth: Float) = ViewType.seventhWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.seventhHeight(screenHeight)
        override fun defaultColor() = firstDefaultColor.triad().second
    },
    LEFT_UNIT_SECOND {
        override fun width(screenWidth: Float) = ViewType.seventhWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.seventhHeight(screenHeight)
        override fun defaultColor() = firstDefaultColor.comp().triad().second
    },
    BOTTOM_UNIT_SECOND {
        override fun width(screenWidth: Float) = ViewType.seventhWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.seventhHeight(screenHeight)
        override fun defaultColor() = firstDefaultColor.comp().triad().second
    },
    TOP_UNIT_SECOND {
        override fun width(screenWidth: Float) = ViewType.seventhWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.seventhHeight(screenHeight)
        override fun defaultColor() = secondDefaultColor
    },
    RIGHT_UNIT_SECOND {
        override fun width(screenWidth: Float) = ViewType.seventhWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.seventhHeight(screenHeight)
        override fun defaultColor() = secondDefaultColor.comp()
    },
    RIGHT_UPPER_UNIT_SECOND {
        override fun width(screenWidth: Float) = ViewType.seventhWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.seventhHeight(screenHeight)
        override fun defaultColor() = secondDefaultColor.triad().first
    },
    UPPER_RIGHT_UNIT_SECOND {
        override fun width(screenWidth: Float) = ViewType.seventhWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.seventhHeight(screenHeight)
        override fun defaultColor() = secondDefaultColor.triad().second
    },
    ;
//    abstract fun paneRect(screenWidth : Float, screenHeight : Float) : Rectangle
//    abstract fun paneCenter(screenWidth : Float, screenHeight : Float) : Vector2
    abstract fun width(screenWidth : Float) : Float
    abstract fun height(screenHeight : Float) : Float
//    abstract fun maxWidth() : Int //referring to ViewType.widths idx
//    abstract fun maxHeight() : Int //referring to ViewType.widths idx
    abstract fun defaultColor() : ColorPalette

    val firstDefaultColor = ColorPalette.Color204
    val secondDefaultColor = ColorPalette.Color536


}