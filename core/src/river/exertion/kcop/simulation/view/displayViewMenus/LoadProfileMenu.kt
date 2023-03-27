package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.assets.NarrativeAsset
import river.exertion.kcop.assets.ProfileAsset
import river.exertion.kcop.simulation.view.displayViewMenus.params.ActionParam
import river.exertion.kcop.simulation.view.displayViewMenus.params.ProfileReqMenu
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.messaging.Switchboard
import river.exertion.kcop.system.view.ShapeDrawerConfig

class LoadProfileMenu(override var screenWidth: Float, override var screenHeight: Float) : DisplayViewMenu,
    ProfileReqMenu {

    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()

    override val backgroundColor = ColorPalette.of("teal")

    override var profileAssets = listOf<ProfileAsset>()
    override var narrativeAssets = listOf<NarrativeAsset>()

    override var selectedProfileAsset : ProfileAsset? = null
    override var selectedNarrativeAsset : NarrativeAsset? = null

    override fun menuPane(bitmapFont: BitmapFont) = Table().apply {
        this.add(Label(selectedProfileAsset?.profileAssetTitle(), LabelStyle(bitmapFont, backgroundColor.label().color())).apply {
            this.wrap = true
        }).growX()
        this.row()
        val profileAssetInfo = selectedProfileAsset?.profileAssetInfo()
        profileAssetInfo?.forEach { profileEntry ->
            this.add(Label(profileEntry, LabelStyle(bitmapFont, backgroundColor.label().color())).apply {
                this.wrap = true
            }).growX().left()
            this.row()
        }
        this.top()
//        this.debug()
        this@LoadProfileMenu.replaceErrorAction()
    }

    override val breadcrumbEntries = mapOf(
        ProfileMenu.tag to ProfileMenu.label,
        MainMenu.tag to MainMenu.label
    )

    override val navs = mutableListOf<ActionParam>()

    fun replaceErrorAction() {
        if (selectedProfileAsset == null) {
            actions.firstOrNull { it.label == "Yes" }?.apply { this.label = "Error"; this.action = {} }
        }
    }

    override val actions = mutableListOf(
        ActionParam("Yes", {
            Switchboard.closeMenu()
            Switchboard.loadProfile(selectedProfileAsset!!, selectedNarrativeAsset)
        }, "Profile Loaded : ${selectedProfileAsset?.profile?.name}"),
        ActionParam("No", {})
    )

    override fun tag() = tag
    override fun label() = label

    companion object {
        const val tag = "loadProfileMenu"
        const val label = "Load"
    }
}