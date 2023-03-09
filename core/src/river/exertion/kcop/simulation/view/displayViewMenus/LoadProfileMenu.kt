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

    fun profileAssetTitle() = profileAsset?.assetPath

    fun profileAssetInfo() : MutableList<String?> {

        val returnList = mutableListOf<String?>()

        if ((profileAsset != null) && (profileAsset!!.profile != null)) {
            returnList.add("name: ${profileAsset!!.profile!!.name}")
            returnList.add("current: ${profileAsset!!.profile!!.currentImmersionId}: ${profileAsset!!.profile!!.currentImmersionIdx?.toString()}")

            if (profileAsset!!.profile!!.statuses.isNotEmpty()) returnList.add("\nstatuses:")

            val listMaxSize = profileAsset!!.profile!!.statuses.size.coerceAtMost(8)

            profileAsset!!.profile!!.statuses.sortedByDescending { it.value }.subList(0, listMaxSize).forEach {
                returnList.add("${it.key}: ${it.value} (${it.cumlImmersionTime})")
            }
            profileAsset!!.profile!!.statuses.sortedByDescending { it.value }.subList(0, listMaxSize).forEach {
                returnList.add("${it.key}: ${it.value} (${it.cumlImmersionTime})")
            }
        }

        if ( returnList.isEmpty() ) returnList.add("no profile info found")

        return returnList
    }

    override fun menuPane(bitmapFont: BitmapFont) = Table().apply {

        this.add(Label(profileAssetTitle(), LabelStyle(bitmapFont, backgroundColor.label().color())).apply {
            this.wrap = true
        }).growX()
        this.row()
        val profileAssetInfo = profileAssetInfo()
        profileAssetInfo.forEach { profileEntry ->
            this.add(Label(profileEntry, LabelStyle(bitmapFont, backgroundColor.label().color())).apply {
                this.wrap = true
            }).growX().left()
            this.row()
        }
        this.top()
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