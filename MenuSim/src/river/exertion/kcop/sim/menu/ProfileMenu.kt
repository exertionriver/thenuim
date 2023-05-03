package river.exertion.kcop.sim.menu

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import ktx.actors.onChange
import ktx.collections.toGdxArray
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.menu.messaging.MenuDataMessage
import river.exertion.kcop.sim.menu.messaging.MenuDataMessage.Companion.MenuDataBridge
import river.exertion.kcop.sim.menu.messaging.MenuNavMessage
import river.exertion.kcop.sim.menu.messaging.MenuNavMessage.Companion.MenuNavBridge
import river.exertion.kcop.sim.menu.params.ActionParam
import river.exertion.kcop.sim.menu.params.MenuNavParams
import river.exertion.kcop.view.ColorPalette
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.KcopSkin.Companion.KcopSkinBridge
import river.exertion.kcop.view.SdcHandler
import river.exertion.kcop.view.SdcHandler.Companion.SDCBridge
import river.exertion.kcop.view.messaging.MenuViewMessage
import river.exertion.kcop.view.messaging.MenuViewMessage.Companion.MenuViewBridge

class ProfileMenu(override var screenWidth: Float, override var screenHeight: Float) : Telegraph, DisplayViewMenu {

    init {
        MessageChannelHandler.enableReceive(MenuDataBridge, this)
        MessageChannelHandler.enableReceive(MenuNavBridge, this)

        MessageChannelHandler.enableReceive(SDCBridge, this)
        MessageChannelHandler.enableReceive(KcopSkinBridge, this)

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
//            MessageChannelEnum.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.SetSelectedProfileFromAsset, selectedProfileAssetTitle))
            MessageChannelHandler.send(MenuNavBridge, MenuNavMessage(MenuNavParams(selectedProfileAssetTitle)))
            MessageChannelHandler.send(MenuViewBridge, MenuViewMessage(LoadProfileMenu.tag))

        }),
/*  No longer used, Save progress instead
ActionParam("Save >", {
            MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.SetSelectedProfileFromAsset, selectedProfileAssetTitle))
            MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.UpdateSelectedProfileFromComponents))
            MessageChannel.INTRA_MENU_BRIDGE.send(null, MenuNavMessage(MenuNavParams(SaveProfileMenu.tag, selectedProfileAssetTitle)))
        }),*/
        ActionParam("New >", {
            MessageChannelHandler.send(MenuViewBridge, MenuViewMessage(NewProfileMenu.tag))
        })
    )

    override val actions = mutableListOf<ActionParam>()

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannelHandler.isType(SDCBridge, msg.message) ) -> {
                    sdcHandler = MessageChannelHandler.receiveMessage(SDCBridge, msg.extraInfo)
                    return true
                }
                (MessageChannelHandler.isType(KcopSkinBridge, msg.message) ) -> {
                    kcopSkin = MessageChannelHandler.receiveMessage(KcopSkinBridge, msg.extraInfo)
                    return true
                }
                (MessageChannelHandler.isType(MenuDataBridge, msg.message)) -> {
                    val menuDataMessage: MenuDataMessage = MessageChannelHandler.receiveMessage(MenuDataBridge, msg.extraInfo)

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
                (MessageChannelHandler.isType(MenuNavBridge, msg.message)) -> {
                    val menuNavMessage: MenuNavMessage = MessageChannelHandler.receiveMessage(MenuNavBridge, msg.extraInfo)

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