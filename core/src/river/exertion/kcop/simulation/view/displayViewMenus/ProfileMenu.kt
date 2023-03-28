package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import ktx.actors.onChange
import ktx.collections.toGdxArray
import river.exertion.kcop.assets.NarrativeAsset
import river.exertion.kcop.assets.ProfileAsset
import river.exertion.kcop.simulation.view.displayViewMenus.params.ActionParam
import river.exertion.kcop.simulation.view.displayViewMenus.params.NavMenuParams
import river.exertion.kcop.simulation.view.displayViewMenus.params.ProfileReqMenu
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.MenuMessage
import river.exertion.kcop.system.profile.Profile
import river.exertion.kcop.system.view.ShapeDrawerConfig

class ProfileMenu(override var screenWidth: Float, override var screenHeight: Float) : DisplayViewMenu, ProfileReqMenu {

    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()

    override val backgroundColor = ColorPalette.of("green")

    override var profileAssets = listOf<ProfileAsset>()
    override var narrativeAssets = listOf<NarrativeAsset>()

    override var selectedProfileAsset : ProfileAsset? = null
    override var selectedNarrativeAsset : NarrativeAsset? = null

    override var currentProfile : Profile? = null

    override fun menuPane(bitmapFont: BitmapFont) : Table {

        val listCtrl = List<String>(ListStyle().apply {
            this.font = bitmapFont
            this.selection = TextureRegionDrawable(TextureRegion(Texture("images/kobold64.png")))
        })

        listCtrl.onChange {
            navs.forEach {
                selectedProfileAsset = profileAssets[this.selectedIndex]
                selectedNarrativeAsset = narrativeAssets.firstOrNull { it.narrative?.id == selectedProfileAsset?.profile?.currentImmersionId }
            }
        }

//        listCtrl.debug()
        listCtrl.alignment = Align.topLeft
        listCtrl.setItems(profileAssets.map { it.profileAssetTitle() }.toGdxArray())

        return Table().apply {
            this.add(listCtrl).growY().top().left()
//            this.add(Table())
            this.debug()
            this.top()
            this.left()
        }
    }

    override val breadcrumbEntries = mapOf(
        MainMenu.tag to MainMenu.label
    )

    override val navs = mutableListOf(
        ActionParam("Load >", { MessageChannel.MENU_BRIDGE.send(null, MenuMessage(NavMenuParams("loadProfileMenu", selectedProfileAsset, selectedNarrativeAsset))) }),
        ActionParam("Save >", { MessageChannel.MENU_BRIDGE.send(null, MenuMessage(NavMenuParams("saveProfileMenu", selectedProfileAsset, selectedNarrativeAsset))) })
    )

    override val actions = mutableListOf<ActionParam>()

    override fun tag() = tag
    override fun label() = label

    companion object {
        const val tag = "profileMenu"
        const val label = "Profile"
    }
}