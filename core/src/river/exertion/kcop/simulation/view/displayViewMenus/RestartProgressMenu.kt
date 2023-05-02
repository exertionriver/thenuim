package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.assets.KcopSkin
import river.exertion.kcop.simulation.view.displayViewMenus.params.ActionParam
import river.exertion.kcop.system.messaging.MessageChannelEnum
import river.exertion.kcop.system.messaging.Switchboard
import river.exertion.kcop.system.messaging.messages.AMHSaveMessage
import river.exertion.kcop.view.messaging.MenuViewMessage
import river.exertion.kcop.system.messaging.messages.MenuDataMessage
import river.exertion.kcop.base.view.SdcHandler
import river.exertion.kcop.view.ColorPalette

class RestartProgressMenu(override var screenWidth: Float, override var screenHeight: Float) : Telegraph, DisplayViewMenu {

    init {
        MessageChannelEnum.INTER_MENU_BRIDGE.enableReceive(this)

        MessageChannelEnum.SDC_BRIDGE.enableReceive(this)
        MessageChannelEnum.KCOP_SKIN_BRIDGE.enableReceive(this)
    }

    override lateinit var sdcHandler : SdcHandler
    override lateinit var kcopSkin: KcopSkin

    override val backgroundColor = ColorPalette.of("olive")

    var progressAssetsInfo: List<String>? = null

    override fun menuPane() = Table().apply {

        progressAssetsInfo!!.forEach { profileEntry ->
            this.add(Label(profileEntry, skin()
                    //Label.LabelStyle(bitmapFont, backgroundColor.label().color())
                    ).apply {
                this.wrap = true
            }).colspan(2).growX()
            this.row()
        }

        this.top()
    }

    override val breadcrumbEntries = mapOf(
        NarrativeMenu.tag to NarrativeMenu.label,
        MainMenu.tag to MainMenu.label
    )

    override val navs = mutableListOf<ActionParam>()

    override val actions = mutableListOf(
        ActionParam("Restart", {
            Switchboard.closeMenu()
            MessageChannelEnum.AMH_SAVE_BRIDGE.send(null, AMHSaveMessage(AMHSaveMessage.AMHSaveMessageType.RestartProgress))
        }, "Narrative Restarted!"),
        ActionParam("Cancel", {
            MessageChannelEnum.DISPLAY_VIEW_MENU_BRIDGE.send(null, MenuViewMessage(breadcrumbEntries.keys.toList()[0]) )
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
                (MessageChannelEnum.INTER_MENU_BRIDGE.isType(msg.message)) -> {
                    val menuDataMessage: MenuDataMessage = MessageChannelEnum.INTER_MENU_BRIDGE.receiveMessage(msg.extraInfo)

                    if ( menuDataMessage.narrativeMenuDataParams != null ) {

                        if (menuDataMessage.narrativeMenuDataParams!!.selectedNarrativeAssetInfo != null) {
                            progressAssetsInfo = menuDataMessage.narrativeMenuDataParams!!.selectedNarrativeAssetInfo
                        }
                        return true
                    }
                }
            }
        }
        return false
    }

    override fun tag() = tag
    override fun label() = label

    companion object {
        const val tag = "restartProgressMenu"
        const val label = "Restart Progress"
    }

}