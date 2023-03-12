package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import ktx.actors.onClick
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.view.DisplayViewMenuMessage

class SaveProfileMenu(override var screenWidth: Float, override var screenHeight: Float) : DisplayViewMenu {

    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()

    override val backgroundColor = ColorPalette.of("olive")

    override fun menuPane(bitmapFont: BitmapFont) = null

    override val breadcrumbEntries = mapOf(
        ProfileMenu.tag to ProfileMenu.label,
        MainMenu.tag to MainMenu.label
    )

    override val navs = mapOf<String, MenuParams>()

    override val actions = mapOf(
        "Yes" to Pair("Profile Saved!") {},
        "No" to Pair("Save Cancelled!") {}
    )

    override fun tag() = tag
    override fun label() = label

    companion object {
        const val tag = "saveProfileMenu"
        const val label = "Save"
    }
}