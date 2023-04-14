package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import river.exertion.kcop.simulation.view.displayViewMenus.params.ActionParam
import river.exertion.kcop.simulation.view.displayViewMenus.params.MenuNavParams
import river.exertion.kcop.system.view.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.Switchboard
import river.exertion.kcop.system.messaging.messages.AMHLoadMessage
import river.exertion.kcop.system.messaging.messages.AMHSaveMessage
import river.exertion.kcop.system.messaging.messages.MenuNavMessage
import kotlin.system.exitProcess

class MainMenu(override var screenWidth: Float, override var screenHeight: Float) : DisplayViewMenu {

    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()

    override val backgroundColor = ColorPalette.of("blue")

    override fun menuPane(bitmapFont: BitmapFont) = null

    override val breadcrumbEntries = mapOf<String, String>()

    override val navs = mutableListOf(
        ActionParam("Profile >", {
            MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.ReloadMenuProfiles))
            MessageChannel.INTRA_MENU_BRIDGE.send(null, MenuNavMessage(MenuNavParams(ProfileMenu.tag)))
        }),
        ActionParam("Settings >", {
            MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.UpdateSelectedProfileFromComponents))
            MessageChannel.INTRA_MENU_BRIDGE.send(null, MenuNavMessage(MenuNavParams(ProfileSettingsMenu.tag)))
        }),
        ActionParam("Narrative >", {
            MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.ReloadMenuNarratives))
            MessageChannel.INTRA_MENU_BRIDGE.send(null, MenuNavMessage(MenuNavParams(NarrativeMenu.tag)))
        }),
        ActionParam("Save Progress >", {
            MessageChannel.AMH_SAVE_BRIDGE.send(null, AMHSaveMessage(AMHSaveMessage.AMHSaveMessageType.PrepSaveProgress))
            MessageChannel.INTRA_MENU_BRIDGE.send(null, MenuNavMessage(MenuNavParams(SaveProgressMenu.tag)))
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

    override fun tag() = tag
    override fun label() = label

    companion object {
        const val tag = "mainMenu"
        const val label = "Main"
    }
}