package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette

class LoadProfileMenu(override var screenWidth: Float, override var screenHeight: Float) : DisplayViewMenu {

    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()

    override val backgroundColor = ColorPalette.of("teal")
    override var menuTable = Table().apply { this.debug() }

    override val breadcrumbEntries = mapOf(
        ProfileMenu.tag to ProfileMenu.label,
        MainMenu.tag to MainMenu.label
    )

    override fun getMenu(batch : Batch, bitmapFont: BitmapFont) : Table {
        return genericLayout(batch, bitmapFont)
    }

    override fun tag() = tag
    override fun label() = label

    companion object {
        const val tag = "loadProfileMenu"
        const val label = "Load"
    }
}