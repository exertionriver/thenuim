package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import ktx.actors.onChange
import ktx.collections.toGdxArray
import river.exertion.kcop.assets.KcopSkin
import river.exertion.kcop.simulation.view.FontPackage
import river.exertion.kcop.simulation.view.displayViewMenus.params.ActionParam
import river.exertion.kcop.simulation.view.displayViewMenus.params.MenuNavParams
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.AMHLoadMessage
import river.exertion.kcop.system.messaging.messages.DisplayViewMenuMessage
import river.exertion.kcop.system.messaging.messages.MenuDataMessage
import river.exertion.kcop.system.messaging.messages.MenuNavMessage
import river.exertion.kcop.system.view.SdcHandler

class ProfileMenu(override var screenWidth: Float, override var screenHeight: Float) : Telegraph, DisplayViewMenu {

    init {
        MessageChannel.INTER_MENU_BRIDGE.enableReceive(this)
        MessageChannel.INTRA_MENU_BRIDGE.enableReceive(this)

        MessageChannel.SDC_BRIDGE.enableReceive(this)
        MessageChannel.KCOP_SKIN_BRIDGE.enableReceive(this)
    }

    override lateinit var sdcHandler : SdcHandler
    override lateinit var kcopSkin: KcopSkin

    override val backgroundColor = ColorPalette.of("green")

    var profileAssetTitles: List<String>? = null
    var selectedProfileAssetTitle : String? = null

    override fun menuPane() : Table {

        val listCtrl = com.badlogic.gdx.scenes.scene2d.ui.List<String>(skin())
                //ListStyle().apply {
//            this.font = bitmapFont
//            this.selection = TextureRegionDrawable(TextureRegion(Texture("images/kobold64.png")))
 //       })

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
            MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.SetSelectedProfileFromAsset, selectedProfileAssetTitle))
            MessageChannel.INTRA_MENU_BRIDGE.send(null, MenuNavMessage(MenuNavParams(selectedProfileAssetTitle)))
            MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(LoadProfileMenu.tag))

        }),
/*  No longer used, Save progress instead
ActionParam("Save >", {
            MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.SetSelectedProfileFromAsset, selectedProfileAssetTitle))
            MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.UpdateSelectedProfileFromComponents))
            MessageChannel.INTRA_MENU_BRIDGE.send(null, MenuNavMessage(MenuNavParams(SaveProfileMenu.tag, selectedProfileAssetTitle)))
        }),*/
        ActionParam("New >", {
            MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(NewProfileMenu.tag))
        })
    )

    override val actions = mutableListOf<ActionParam>()

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannel.SDC_BRIDGE.isType(msg.message) ) -> {
                    sdcHandler = MessageChannel.SDC_BRIDGE.receiveMessage(msg.extraInfo)
                    return true
                }
                (MessageChannel.KCOP_SKIN_BRIDGE.isType(msg.message) ) -> {
                    kcopSkin = MessageChannel.KCOP_SKIN_BRIDGE.receiveMessage(msg.extraInfo)
                    return true
                }
                (MessageChannel.INTER_MENU_BRIDGE.isType(msg.message)) -> {
                    val menuDataMessage: MenuDataMessage = MessageChannel.INTER_MENU_BRIDGE.receiveMessage(msg.extraInfo)

                    if ( menuDataMessage.profileMenuDataParams != null ) {
                        if (menuDataMessage.profileMenuDataParams!!.profileAssetTitles != null) {
                            profileAssetTitles = menuDataMessage.profileMenuDataParams!!.profileAssetTitles
                        }
                        if (menuDataMessage.profileMenuDataParams!!.selectedProfileAssetTitle != null) {
                            selectedProfileAssetTitle = menuDataMessage.profileMenuDataParams!!.selectedProfileAssetTitle
                        }
                    } else {
                        profileAssetTitles = null
                        selectedProfileAssetTitle = null
                    }
                    return true
                }
                (MessageChannel.INTRA_MENU_BRIDGE.isType(msg.message)) -> {
                    val menuNavMessage: MenuNavMessage = MessageChannel.INTRA_MENU_BRIDGE.receiveMessage(msg.extraInfo)

                    //between menus
                    selectedProfileAssetTitle = if (menuNavMessage.menuNavParams != null) {
                        menuNavMessage.menuNavParams!!.selectedAssetTitle
                    } else {
                        null
                    }

                    return true
                }
            }
        }
        return false
    }

    override fun tag() = tag
    override fun label() = label

    companion object {
        const val tag = "profileMenu"
        const val label = "Profile"
    }
}