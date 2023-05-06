package river.exertion.kcop.sim.narrative.menu

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.narrative.NarrativePackage.Companion.NarrativeMenuDataBridge
import river.exertion.kcop.sim.narrative.messaging.NarrativeMenuDataMessage
import river.exertion.kcop.view.ColorPalette
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.SdcHandler
import river.exertion.kcop.view.ViewPackage.MenuViewBridge
import river.exertion.kcop.view.menu.DisplayViewMenu
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.messaging.MenuViewMessage
import river.exertion.kcop.view.messaging.menuParams.ActionParam
import river.exertion.kcop.view.switchboard.MenuViewSwitchboard

class LoadNarrativeMenu : Telegraph, DisplayViewMenu {

    init {
        MessageChannelHandler.enableReceive(NarrativeMenuDataBridge,this)
    }

    override val backgroundColor = ColorPalette.of("teal")

    var selectedNarrativeAssetInfo: List<String>? = null
    var selectedNarrativeAssetName : String? = null

    fun selectedNarrativeAssetName() = selectedNarrativeAssetName ?: throw Exception("LoadNarrativeMenu requires valid selectNarrativeAssetTitle")

    override fun menuPane() = Table().apply {
        if (selectedNarrativeAssetInfo != null) {
            selectedNarrativeAssetInfo!!.forEach { profileEntry ->
                this.add(Label(profileEntry, KcopSkin.skin)
                        //LabelStyle(bitmapFont, backgroundColor.label().color()))
                        .apply {
                    this.wrap = true
                }).growX().left()
                this.row()
            }
//        this.debug()
            this@LoadNarrativeMenu.actions.firstOrNull { it.label == "Yes" }?.apply { this.log = "Narrative Loaded : ${selectedNarrativeAssetName()}" }
        } else {
            this.add(Label("no narrative info found", KcopSkin.skin)
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
            MenuViewSwitchboard.closeMenu()
//            Switchboard.loadSelectedNarrative()
        }, "Narrative Loaded!"),
        //go back a menu
        ActionParam("No", {
            MessageChannelHandler.send(MenuViewBridge, MenuViewMessage(breadcrumbEntries.keys.toList()[0]) )
        })
    )

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannelHandler.isType(NarrativeMenuDataBridge, msg.message)) -> {
                    val menuDataMessage: NarrativeMenuDataMessage = MessageChannelHandler.receiveMessage(NarrativeMenuDataBridge, msg.extraInfo)

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