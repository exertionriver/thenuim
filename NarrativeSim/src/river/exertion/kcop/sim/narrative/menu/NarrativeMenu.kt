package river.exertion.kcop.sim.narrative.menu

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.ui.Table
import ktx.actors.onChange
import ktx.collections.toGdxArray
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.asset.view.KcopSkin
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.narrative.NarrativePackage.Companion.NarrativeMenuDataBridge
import river.exertion.kcop.sim.narrative.messaging.NarrativeMenuDataMessage
import river.exertion.kcop.view.ViewPackage.MenuNavBridge
import river.exertion.kcop.view.ViewPackage.MenuViewBridge
import river.exertion.kcop.view.menu.DisplayViewMenu
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.messaging.MenuNavMessage
import river.exertion.kcop.view.messaging.MenuViewMessage
import river.exertion.kcop.view.messaging.menuParams.ActionParam
import river.exertion.kcop.view.messaging.menuParams.MenuNavParams

object NarrativeMenu : Telegraph, DisplayViewMenu {

    override val tag = "narrativeMenu"
    override val label = "Narrative"

    init {
        MessageChannelHandler.enableReceive(NarrativeMenuDataBridge, this)
        MessageChannelHandler.enableReceive(MenuNavBridge, this)
    }

    override val backgroundColor = ColorPalette.of("green")

    var narrativeAssetTitles: List<String>? = null
    var selectedNarrativeAssetTitle : String? = null

    override fun menuPane() : Table {

        val listCtrl = com.badlogic.gdx.scenes.scene2d.ui.List<String>(KcopSkin.skin)
//                ListStyle().apply {
//            this.font = bitmapFont
//            this.selection = TextureRegionDrawable(TextureRegion(Texture("images/kobold64.png")))
//        })

        if (narrativeAssetTitles != null) {
            selectedNarrativeAssetTitle = narrativeAssetTitles!![0]

            listCtrl.onChange {
                navs().forEach {
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

    override fun navs() = mutableListOf(
        ActionParam("Load >", {
//            MessageChannelEnum.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.SetSelectedNarrativeFromAsset, selectedNarrativeAssetTitle))
            MessageChannelHandler.send(MenuNavBridge, MenuNavMessage(MenuNavParams(selectedNarrativeAssetTitle)))
            MessageChannelHandler.send(MenuViewBridge, MenuViewMessage(LoadNarrativeMenu.tag) )
        }),
        ActionParam("Restart >", {
//            MessageChannelEnum.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.SetSelectedNarrativeFromAsset, selectedNarrativeAssetTitle))
            MessageChannelHandler.send(MenuNavBridge, MenuNavMessage(MenuNavParams(selectedNarrativeAssetTitle)))
 //           MessageChannelHandler.send(MenuViewBridge, MenuViewMessage(RestartProgressMenu.tag) )
        }),
    )

    override val actions = mutableListOf<ActionParam>()

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannelHandler.isType(NarrativeMenuDataBridge, msg.message)) -> {
                    val menuDataMessage: NarrativeMenuDataMessage = MessageChannelHandler.receiveMessage(NarrativeMenuDataBridge, msg.extraInfo)

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
                (MessageChannelHandler.isType(MenuNavBridge, msg.message)) -> {
                    val menuNavMessage: MenuNavMessage = MessageChannelHandler.receiveMessage(MenuNavBridge, msg.extraInfo)

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
}