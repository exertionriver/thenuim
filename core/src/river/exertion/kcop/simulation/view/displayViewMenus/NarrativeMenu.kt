package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import ktx.actors.onChange
import ktx.collections.toGdxArray
import river.exertion.kcop.simulation.view.displayViewMenus.params.ActionParam
import river.exertion.kcop.simulation.view.displayViewMenus.params.MenuNavParams
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.AMHLoadMessage
import river.exertion.kcop.system.messaging.messages.DisplayViewMenuMessage
import river.exertion.kcop.system.messaging.messages.MenuDataMessage
import river.exertion.kcop.system.messaging.messages.MenuNavMessage
import river.exertion.kcop.system.view.ShapeDrawerConfig

class NarrativeMenu(override var screenWidth: Float, override var screenHeight: Float) : Telegraph, DisplayViewMenu {

    init {
        MessageChannel.INTER_MENU_BRIDGE.enableReceive(this)
        MessageChannel.INTRA_MENU_BRIDGE.enableReceive(this)
    }

    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()
    override val backgroundColor = ColorPalette.of("green")

    var narrativeAssetTitles: List<String>? = null
    var selectedNarrativeAssetTitle : String? = null

    override fun menuPane(bitmapFont: BitmapFont) : Table {

        val listCtrl = com.badlogic.gdx.scenes.scene2d.ui.List<String>(ListStyle().apply {
            this.font = bitmapFont
            this.selection = TextureRegionDrawable(TextureRegion(Texture("images/kobold64.png")))
        })

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
            MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.SetSelectedNarrativeFromAsset, selectedNarrativeAssetTitle))
            MessageChannel.INTRA_MENU_BRIDGE.send(null, MenuNavMessage(MenuNavParams(selectedNarrativeAssetTitle)))
            MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(LoadNarrativeMenu.tag))
        }),
        ActionParam("Restart >", {
            MessageChannel.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.SetSelectedNarrativeFromAsset, selectedNarrativeAssetTitle))
            MessageChannel.INTRA_MENU_BRIDGE.send(null, MenuNavMessage(MenuNavParams(selectedNarrativeAssetTitle)))
            MessageChannel.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(RestartProgressMenu.tag))
        }),
    )

    override val actions = mutableListOf<ActionParam>()

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannel.INTER_MENU_BRIDGE.isType(msg.message)) -> {
                    val menuDataMessage: MenuDataMessage = MessageChannel.INTER_MENU_BRIDGE.receiveMessage(msg.extraInfo)

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
                (MessageChannel.INTRA_MENU_BRIDGE.isType(msg.message)) -> {
                    val menuNavMessage: MenuNavMessage = MessageChannel.INTRA_MENU_BRIDGE.receiveMessage(msg.extraInfo)

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