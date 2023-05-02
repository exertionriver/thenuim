package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import river.exertion.kcop.assets.KcopSkin
import river.exertion.kcop.simulation.view.displayViewMenus.params.ActionParam
import river.exertion.kcop.system.messaging.MessageChannelEnum
import river.exertion.kcop.system.messaging.Switchboard
import river.exertion.kcop.system.messaging.messages.AMHLoadMessage
import river.exertion.kcop.system.messaging.messages.AMHSaveMessage
import river.exertion.kcop.view.messaging.MenuViewMessage
import river.exertion.kcop.base.view.SdcHandler
import river.exertion.kcop.view.ColorPalette
import kotlin.system.exitProcess

class MainMenu(override var screenWidth: Float, override var screenHeight: Float) : Telegraph, DisplayViewMenu {

    init {
        MessageChannelEnum.SDC_BRIDGE.enableReceive(this)
        MessageChannelEnum.KCOP_SKIN_BRIDGE.enableReceive(this)
    }

    override lateinit var sdcHandler : SdcHandler
    override lateinit var kcopSkin: KcopSkin

    override val backgroundColor = ColorPalette.of("blue")

    override fun menuPane() = null

    override val breadcrumbEntries = mapOf<String, String>()

    override val navs = mutableListOf(
        ActionParam("Profile >", {
            MessageChannelEnum.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.ReloadMenuProfiles))
            MessageChannelEnum.DISPLAY_VIEW_MENU_BRIDGE.send(null, MenuViewMessage(ProfileMenu.tag))
        }),
        ActionParam("Settings >", {
            MessageChannelEnum.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.UpdateSelectedProfileFromComponents))
            MessageChannelEnum.DISPLAY_VIEW_MENU_BRIDGE.send(null, MenuViewMessage(ProfileSettingsMenu.tag))
        }),
        ActionParam("Narrative >", {
            MessageChannelEnum.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.ReloadMenuNarratives))
            MessageChannelEnum.DISPLAY_VIEW_MENU_BRIDGE.send(null, MenuViewMessage(NarrativeMenu.tag))
        }),
        ActionParam("Save Progress >", {
            MessageChannelEnum.AMH_SAVE_BRIDGE.send(null, AMHSaveMessage(AMHSaveMessage.AMHSaveMessageType.PrepSaveProgress))
            MessageChannelEnum.DISPLAY_VIEW_MENU_BRIDGE.send(null, MenuViewMessage(SaveProgressMenu.tag))
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
                (MessageChannelEnum.SDC_BRIDGE.isType(msg.message) ) -> {
                    sdcHandler = MessageChannelEnum.SDC_BRIDGE.receiveMessage(msg.extraInfo)
                    return true
                }
                (MessageChannelEnum.KCOP_SKIN_BRIDGE.isType(msg.message) ) -> {
                    kcopSkin = MessageChannelEnum.KCOP_SKIN_BRIDGE.receiveMessage(msg.extraInfo)
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