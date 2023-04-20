package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.assets.KcopSkin
import river.exertion.kcop.simulation.view.FontPackage
import river.exertion.kcop.simulation.view.displayViewMenus.params.ActionParam
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.Switchboard
import river.exertion.kcop.system.messaging.messages.DisplayViewMenuMessage
import river.exertion.kcop.system.messaging.messages.MenuDataMessage
import river.exertion.kcop.system.view.SdcHandler

class LoadNarrativeMenu(override var screenWidth: Float, override var screenHeight: Float) : Telegraph, DisplayViewMenu {

    init {
        MessageChannel.INTER_MENU_BRIDGE.enableReceive(this)

        MessageChannel.SDC_BRIDGE.enableReceive(this)
        MessageChannel.KCOP_SKIN_BRIDGE.enableReceive(this)
    }

    override lateinit var sdcHandler : SdcHandler
    override lateinit var kcopSkin: KcopSkin

    override val backgroundColor = ColorPalette.of("teal")

    var selectedNarrativeAssetInfo: List<String>? = null
    var selectedNarrativeAssetName : String? = null

    fun selectedNarrativeAssetName() = selectedNarrativeAssetName ?: throw Exception("LoadNarrativeMenu requires valid selectNarrativeAssetTitle")

    override fun menuPane() = Table().apply {
        if (selectedNarrativeAssetInfo != null) {
            selectedNarrativeAssetInfo!!.forEach { profileEntry ->
                this.add(Label(profileEntry, skin())
                        //LabelStyle(bitmapFont, backgroundColor.label().color()))
                        .apply {
                    this.wrap = true
                }).growX().left()
                this.row()
            }
//        this.debug()
            this@LoadNarrativeMenu.actions.firstOrNull { it.label == "Yes" }?.apply { this.log = "Narrative Loaded : ${selectedNarrativeAssetName()}" }
        } else {
            this.add(Label("no narrative info found", skin())
                    //LabelStyle(bitmapFont, backgroundColor.label().color()))
            ).growX().left()
            this@LoadNarrativeMenu.actions.firstOrNull { it.label == "Yes" }?.apply { this.label = "Error"; this.action = {} }
        }
        this.top()
    }

    override val breadcrumbEntries = mapOf(
        NarrativeMenu.tag to NarrativeMenu.label,
        MainMenu.tag to MainMenu.label
    )

    override val navs = mutableListOf<ActionParam>()

    override val actions = mutableListOf(
        ActionParam("Yes", {
            Switchboard.closeMenu()
            Switchboard.loadSelectedNarrative()
        }, "Narrative Loaded!"),
        //go back a menu
        ActionParam("No", {
            MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(breadcrumbEntries.keys.toList()[0]) )
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

                (MessageChannel.INTER_MENU_BRIDGE.isType(msg.message)) -> {
                    val menuDataMessage: MenuDataMessage = MessageChannel.INTER_MENU_BRIDGE.receiveMessage(msg.extraInfo)

                    if ( menuDataMessage.narrativeMenuDataParams != null ) {

                        if (menuDataMessage.narrativeMenuDataParams!!.selectedNarrativeAssetInfo != null) {
                            selectedNarrativeAssetInfo = menuDataMessage.narrativeMenuDataParams!!.selectedNarrativeAssetInfo
                        }
                        if (menuDataMessage.narrativeMenuDataParams!!.selectedNarrativeAssetName != null) {
                            selectedNarrativeAssetName = menuDataMessage.narrativeMenuDataParams!!.selectedNarrativeAssetName
                        }
                    } else {
                        selectedNarrativeAssetInfo = null
                        selectedNarrativeAssetName = null
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
        const val tag = "loadNarrativeMenu"
        const val label = "Load"
    }
}