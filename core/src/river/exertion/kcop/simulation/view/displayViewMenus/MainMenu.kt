package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import river.exertion.kcop.simulation.view.displayViewMenus.params.ActionParam
import river.exertion.kcop.simulation.view.displayViewMenus.params.MenuNavParams
import river.exertion.kcop.system.view.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.AMHMessage
import river.exertion.kcop.system.messaging.messages.MenuNavMessage
import kotlin.system.exitProcess

class MainMenu(override var screenWidth: Float, override var screenHeight: Float) : DisplayViewMenu {

    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()

    override val backgroundColor = ColorPalette.of("blue")

    override fun menuPane(bitmapFont: BitmapFont) = null

    override val breadcrumbEntries = mapOf<String, String>()

    override val navs = mutableListOf(
        ActionParam("Profile >", {
            MessageChannel.AMH_BRIDGE.send(null, AMHMessage(AMHMessage.AMHMessageType.ReloadMenuProfiles))
            MessageChannel.INTRA_MENU_BRIDGE.send(null, MenuNavMessage(MenuNavParams("profileMenu")))
        }),
        ActionParam("Narrative >", {
            MessageChannel.AMH_BRIDGE.send(null, AMHMessage(AMHMessage.AMHMessageType.ReloadMenuNarratives))
            MessageChannel.INTRA_MENU_BRIDGE.send(null, MenuNavMessage(MenuNavParams("narrativeMenu")))
        })
    )

    override val actions = mutableListOf(
        ActionParam("Exit", {
            Gdx.app.exit()
            exitProcess(0)
        }, "Peace Out")
    )

    override fun tag() = tag
    override fun label() = label

    companion object {
        const val tag = "mainMenu"
        const val label = "Main"
    }
}