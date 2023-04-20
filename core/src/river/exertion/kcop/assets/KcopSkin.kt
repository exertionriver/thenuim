package river.exertion.kcop.assets

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.assets.disposeSafely
import river.exertion.kcop.simulation.view.FontPackage

class KcopSkin(var skin : Skin, var fontPackage: FontPackage) {

    fun labelStyle(fontSize : FontSize, color : Color) = LabelStyle (fontPackage.font(fontSize), color)

    fun dispose() {
        skin.disposeSafely()
        fontPackage.dispose()
    }

}