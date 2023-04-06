package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import ktx.actors.onChange
import ktx.collections.toGdxArray
import river.exertion.kcop.simulation.view.displayViewMenus.params.ActionParam
import river.exertion.kcop.simulation.view.displayViewMenus.params.MenuNavParams
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.AMHLoadMessage
import river.exertion.kcop.system.messaging.messages.AMHSaveMessage
import river.exertion.kcop.system.messaging.messages.MenuNavMessage
import river.exertion.kcop.system.messaging.messages.ProfileMessage
import river.exertion.kcop.system.view.ShapeDrawerConfig

class ProfileMenu(override var screenWidth: Float, override var screenHeight: Float) : DisplayViewMenu {

    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()

    override val backgroundColor = ColorPalette.of("green")

    var profileAssetTitles: List<String>? = null
    var selectedProfileAssetTitle : String? = null

    override fun menuPane(bitmapFont: BitmapFont) : Table {

        val listCtrl = com.badlogic.gdx.scenes.scene2d.ui.List<String>(ListStyle().apply {
            this.font = bitmapFont
            this.selection = TextureRegionDrawable(TextureRegion(Texture("images/kobold64.png")))
        })

        if (profileAssetTitles != null) {
            selectedProfileAssetTitle = profileAssetTitles!![0]

            listCtrl.onChange {
                navs.forEach {
                    selectedProfileAssetTitle = profileAssetTitles?.get(this.selectedIndex)
                }
            }

            listCtrl.setItems(profileAssetTitles!!.toGdxArray())
        } else {
            listCtrl.setItems(listOf("no profiles found").toGdxArray() )
        }

        listCtrl.alignment = Align.topLeft

        return Table().apply {
            this.add(listCtrl).growY().top().left()
//            this.add(Table())
//            this.debug()
            this.top()
            this.left()
        }
    }

    override val breadcrumbEntries = mapOf(
        MainMenu.tag to MainMenu.label
    )

    override val navs = mutableListOf(
        ActionParam("Load >", {
            MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.SetSelectedProfileAsset, selectedProfileAssetTitle))
            MessageChannel.INTRA_MENU_BRIDGE.send(null, MenuNavMessage(MenuNavParams(LoadProfileMenu.tag, selectedProfileAssetTitle)))
        }),
        ActionParam("Save >", {
            MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.SetSelectedProfileAsset, selectedProfileAssetTitle))
            MessageChannel.AMH_SAVE_BRIDGE.send(null, AMHSaveMessage(AMHSaveMessage.AMHSaveMessageType.PollSelectedProfileAsset))
            MessageChannel.INTRA_MENU_BRIDGE.send(null, MenuNavMessage(MenuNavParams(SaveProfileMenu.tag, selectedProfileAssetTitle)))
        }),
        ActionParam("New >", {
            MessageChannel.INTRA_MENU_BRIDGE.send(null, MenuNavMessage(MenuNavParams(NewProfileMenu.tag, null)))
        })
    )

    override val actions = mutableListOf<ActionParam>()

    override fun tag() = tag
    override fun label() = label

    companion object {
        const val tag = "profileMenu"
        const val label = "Profile"
    }
}