package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.ui.Table
import ktx.actors.onChange
import ktx.collections.toGdxArray
import river.exertion.kcop.assets.KcopSkin
import river.exertion.kcop.simulation.view.displayViewMenus.params.ActionParam
import river.exertion.kcop.simulation.view.displayViewMenus.params.MenuNavParams
import river.exertion.kcop.system.messaging.MessageChannelEnum
import river.exertion.kcop.system.messaging.messages.AMHLoadMessage
import river.exertion.kcop.view.messaging.MenuViewMessage
import river.exertion.kcop.system.messaging.messages.MenuDataMessage
import river.exertion.kcop.system.messaging.messages.MenuNavMessage
import river.exertion.kcop.base.view.SdcHandler
import river.exertion.kcop.view.ColorPalette

class NarrativeMenu(override var screenWidth: Float, override var screenHeight: Float) : Telegraph, DisplayViewMenu {

    init {
        MessageChannelEnum.INTER_MENU_BRIDGE.enableReceive(this)
        MessageChannelEnum.INTRA_MENU_BRIDGE.enableReceive(this)

        MessageChannelEnum.SDC_BRIDGE.enableReceive(this)
        MessageChannelEnum.KCOP_SKIN_BRIDGE.enableReceive(this)
    }

    override lateinit var sdcHandler : SdcHandler
    override lateinit var kcopSkin: KcopSkin

    override val backgroundColor = ColorPalette.of("green")

    var narrativeAssetTitles: List<String>? = null
    var selectedNarrativeAssetTitle : String? = null

    override fun menuPane() : Table {

        val listCtrl = com.badlogic.gdx.scenes.scene2d.ui.List<String>(skin())
//                ListStyle().apply {
//            this.font = bitmapFont
//            this.selection = TextureRegionDrawable(TextureRegion(Texture("images/kobold64.png")))
//        })

        if (narrativeAssetTitles != null) {
            selectedNarrativeAssetTitle = narrativeAssetTitles!![0]

            listCtrl.onChange {
                navs.forEach {
                    selectedNarrativeAssetTitle = narrativeAssetTitles?.get(this.selectedIndex)
                }
            }

            listCtrl.setItems(narrativeAssetTitles!!.toGdxArray())
        } else {
            listCtrl.setItems(listOf("no profiles found").toGdxArray() )
        }

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
            MessageChannelEnum.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.SetSelectedNarrativeFromAsset, selectedNarrativeAssetTitle))
            MessageChannelEnum.INTRA_MENU_BRIDGE.send(null, MenuNavMessage(MenuNavParams(selectedNarrativeAssetTitle)))
            MessageChannelEnum.DISPLAY_VIEW_MENU_BRIDGE.send(null, MenuViewMessage(LoadNarrativeMenu.tag))
        }),
        ActionParam("Restart >", {
            MessageChannelEnum.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.SetSelectedNarrativeFromAsset, selectedNarrativeAssetTitle))
            MessageChannelEnum.INTRA_MENU_BRIDGE.send(null, MenuNavMessage(MenuNavParams(selectedNarrativeAssetTitle)))
            MessageChannelEnum.DISPLAY_VIEW_MENU_BRIDGE.send(null, MenuViewMessage(RestartProgressMenu.tag))
        }),
    )

    override val actions = mutableListOf<ActionParam>()

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
                            if (menuDataMessage.narrativeMenuDataParams!!.narrativeAssetTitles != null) {
                                narrativeAssetTitles = menuDataMessage.narrativeMenuDataParams!!.narrativeAssetTitles
                            }
                            if (menuDataMessage.narrativeMenuDataParams!!.selectedNarrativeAssetTitle != null) {
                                selectedNarrativeAssetTitle = menuDataMessage.narrativeMenuDataParams!!.selectedNarrativeAssetTitle
                            }
                    } else {
                        narrativeAssetTitles = null
                        selectedNarrativeAssetTitle = null
                    }
                    return true
                }
                (MessageChannelEnum.INTRA_MENU_BRIDGE.isType(msg.message)) -> {
                    val menuNavMessage: MenuNavMessage = MessageChannelEnum.INTRA_MENU_BRIDGE.receiveMessage(msg.extraInfo)

                    //between menus
                    selectedNarrativeAssetTitle = if (menuNavMessage.menuNavParams != null) {
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
        const val tag = "narrativeMenu"
        const val label = "Narrative"
    }
}