package river.exertion.kcop.sim.menu

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.messaging.Switchboard
import river.exertion.kcop.sim.menu.messaging.MenuDataMessage
import river.exertion.kcop.sim.menu.messaging.MenuDataMessage.Companion.MenuDataBridge
import river.exertion.kcop.sim.menu.params.ActionParam
import river.exertion.kcop.view.ColorPalette
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.KcopSkin.Companion.KcopSkinBridge
import river.exertion.kcop.view.SdcHandler
import river.exertion.kcop.view.SdcHandler.Companion.SDCBridge
import river.exertion.kcop.view.layout.MenuView
import river.exertion.kcop.view.messaging.MenuViewMessage

class LoadProfileMenu(override var screenWidth: Float, override var screenHeight: Float) : Telegraph, DisplayViewMenu {

    init {
        MessageChannelHandler.enableReceive(MenuDataBridge, this)

        MessageChannelHandler.enableReceive(SDCBridge, this)
        MessageChannelHandler.enableReceive(KcopSkinBridge, this)

    }

    override lateinit var sdcHandler : SdcHandler
    override lateinit var kcopSkin: KcopSkin

    override val backgroundColor = ColorPalette.of("teal")

    var selectedProfileAssetInfo: List<String>? = null
    var selectedProfileAssetName : String? = null

    fun selectedProfileAssetName() = selectedProfileAssetName ?: throw Exception("LoadProfileMenu requires valid selectProfileAssetName")

    override fun menuPane() = Table().apply {

        if (selectedProfileAssetInfo != null) {
            selectedProfileAssetInfo!!.forEach { profileEntry ->
                this.add(Label(profileEntry, skin())
                        //LabelStyle(bitmapFont, backgroundColor.label().color()))
                        .apply {
                    this.wrap = true
                }).growX().left()
                this.row()
            }
//        this.debug()
            this@LoadProfileMenu.actions.firstOrNull { it.label == "Yes" }?.apply { this.log = "Profile Loaded : ${selectedProfileAssetName()}" }
        } else {
            this.add(Label("no profile info found", skin())
//                    LabelStyle(bitmapFont, backgroundColor.label().color()))
            ).growX().left()
            this@LoadProfileMenu.actions.firstOrNull { it.label == "Yes" }?.apply { this.label = "Error"; this.action = {} }
        }
        this.top()
    }

    override val breadcrumbEntries = mapOf(
        ProfileMenu.tag to ProfileMenu.label,
        MainMenu.tag to MainMenu.label
    )

    override val navs = mutableListOf<ActionParam>()

    override val actions = mutableListOf(
        ActionParam("Yes", {
            MenuView.closeMenu()
//            Switchboard.loadSelectedProfile()
        }, "Profile Loaded!"),
        //go back a menu
        ActionParam("No", {
            MessageChannelHandler.send(MenuViewMessage.MenuViewBridge, MenuViewMessage(breadcrumbEntries.keys.toList()[0]) )
        })
    )

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

                        if (menuDataMessage.profileMenuDataParams!!.selectedProfileAssetInfo != null) {
                            selectedProfileAssetInfo = menuDataMessage.profileMenuDataParams!!.selectedProfileAssetInfo
                        }
                        if (menuDataMessage.profileMenuDataParams!!.selectedProfileAssetName != null) {
                            selectedProfileAssetName = menuDataMessage.profileMenuDataParams!!.selectedProfileAssetName
                        }
                    } else {
                        selectedProfileAssetInfo = null
                        selectedProfileAssetName = null
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
        const val tag = "loadProfileMenu"
        const val label = "Load"
    }
}