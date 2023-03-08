package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.assets.ProfileAsset
import river.exertion.kcop.system.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette
import java.awt.Menu

class LoadProfileMenu(override var screenWidth: Float, override var screenHeight: Float) : DisplayViewMenu {

    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()

    override val backgroundColor = ColorPalette.of("teal")

    var profileAsset : ProfileAsset? = null

    override fun menuPane(bitmapFont: BitmapFont) = Table().apply {

        this.add(Label(profileAsset?.assetPath, LabelStyle(bitmapFont, backgroundColor.label().color())))
        this.debug()

    }

    override val breadcrumbEntries = mapOf(
        ProfileMenu.tag to ProfileMenu.label,
        MainMenu.tag to MainMenu.label
    )

    override val navs = mapOf<String, MenuParams>()

    override val actions = mapOf(
        "Yes" to "Profile Loaded!",
        "No" to "Load Cancelled!"
    )

    override fun tag() = tag
    override fun label() = label

    companion object {
        const val tag = "loadProfileMenu"
        const val label = "Load"
    }
}