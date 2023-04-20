package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import river.exertion.kcop.assets.KcopSkin
import river.exertion.kcop.simulation.view.FontPackage
import river.exertion.kcop.simulation.view.displayViewMenus.params.ActionParam
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.Switchboard
import river.exertion.kcop.system.messaging.messages.AMHLoadMessage
import river.exertion.kcop.system.messaging.messages.AMHSaveMessage
import river.exertion.kcop.system.messaging.messages.DisplayViewMenuMessage
import river.exertion.kcop.system.view.SdcHandler
import kotlin.system.exitProcess

class MainMenu(override var screenWidth: Float, override var screenHeight: Float) : Telegraph, DisplayViewMenu {

    init {
        MessageChannel.SDC_BRIDGE.enableReceive(this)
        MessageChannel.KCOP_SKIN_BRIDGE.enableReceive(this)
    }

    override lateinit var sdcHandler : SdcHandler
    override lateinit var kcopSkin: KcopSkin

    override val backgroundColor = ColorPalette.of("blue")

    override fun menuPane() = null

    override val breadcrumbEntries = mapOf<String, String>()

    override val navs = mutableListOf(
        ActionParam("Profile >", {
            MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.ReloadMenuProfiles))
            MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(ProfileMenu.tag))
        }),
        ActionParam("Settings >", {
            MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.UpdateSelectedProfileFromComponents))
            MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(ProfileSettingsMenu.tag))
        }),
        ActionParam("Narrative >", {
            MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.ReloadMenuNarratives))
            MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(NarrativeMenu.tag))
        }),
        ActionParam("Save Progress >", {
            MessageChannel.AMH_SAVE_BRIDGE.send(null, AMHSaveMessage(AMHSaveMessage.AMHSaveMessageType.PrepSaveProgress))
            MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(SaveProgressMenu.tag))
        })
    )

    override val actions = mutableListOf(
        ActionParam("Exit kcop", {
            Gdx.app.exit()
            exitProcess(0)
        }, "Peace Out"),
        ActionParam("Close Menu", {
            Switchboard.closeMenu()
        })
    )

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
            }
        }
        return false
    }

    override fun tag() = tag
    override fun label() = label

    companion object {
        const val tag = "mainMenu"
        const val label = "Main"
    }
}