package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette

class ProfileMenu(override var screenWidth: Float, override var screenHeight: Float) : DisplayViewMenu {

    override val tag = "profileMenu"
    override val label = "Profile"
    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()

    override val backgroundColor = ColorPalette.of("green")

    override var menuTable = Table().apply { this.debug() }

    override fun getMenu(batch : Batch, bitmapFont: BitmapFont) : Table {

        return genericLayout(batch, bitmapFont)
    }

}