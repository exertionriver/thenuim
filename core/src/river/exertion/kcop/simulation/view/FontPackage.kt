package river.exertion.kcop.simulation.view

import com.badlogic.gdx.graphics.g2d.BitmapFont
import ktx.assets.disposeSafely
import river.exertion.kcop.assets.FontSize

data class FontPackage(val text : BitmapFont, val small : BitmapFont, val medium : BitmapFont, val large : BitmapFont) {

    fun font(fontSize: FontSize) : BitmapFont {
        return when (fontSize) {
            FontSize.TEXT -> text
            FontSize.SMALL -> small
            FontSize.MEDIUM -> medium
            FontSize.LARGE -> large
        }
    }

    fun dispose() {
        text.disposeSafely()
        small.disposeSafely()
        medium.disposeSafely()
        large.disposeSafely()
    }
}