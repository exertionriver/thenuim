package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.simulation.view.displayViewMenus.params.ActionParam
import river.exertion.kcop.simulation.view.displayViewMenus.params.MenuNavParams
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.Switchboard
import river.exertion.kcop.system.messaging.messages.DisplayViewMenuMessage
import river.exertion.kcop.system.messaging.messages.MenuDataMessage
import river.exertion.kcop.system.messaging.messages.MenuNavMessage
import river.exertion.kcop.system.view.ShapeDrawerConfig

class LoadProfileMenu(override var screenWidth: Float, override var screenHeight: Float) : Telegraph, DisplayViewMenu {

    init {
        MessageChannel.INTER_MENU_BRIDGE.enableReceive(this)
    }

    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()

    override val backgroundColor = ColorPalette.of("teal")

    var selectedProfileAssetInfo: List<String>? = null
    var selectedProfileAssetName : String? = null

    fun selectedProfileAssetName() = selectedProfileAssetName ?: throw Exception("LoadProfileMenu requires valid selectProfileAssetName")

    override fun menuPane(bitmapFont: BitmapFont) = Table().apply {

        if (selectedProfileAssetInfo != null) {
            selectedProfileAssetInfo!!.forEach { profileEntry ->
                this.add(Label(profileEntry, LabelStyle(bitmapFont, backgroundColor.label().color())).apply {
                    this.wrap = true
                }).growX().left()
                this.row()
            }
//        this.debug()
            this@LoadProfileMenu.actions.firstOrNull { it.label == "Yes" }?.apply { this.log = "Profile Loaded : ${selectedProfileAssetName()}" }
        } else {
            this.add(Label("no profile info found", LabelStyle(bitmapFont, backgroundColor.label().color()))
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
            Switchboard.closeMenu()
            Switchboard.loadSelectedProfile()
        }, "Profile Loaded!"),
        //go back a menu
        ActionParam("No", {
            MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(breadcrumbEntries.keys.toList()[0]) )
        })
    )

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannel.INTER_MENU_BRIDGE.isType(msg.message)) -> {
                    val menuDataMessage: MenuDataMessage = MessageChannel.INTER_MENU_BRIDGE.receiveMessage(msg.extraInfo)

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