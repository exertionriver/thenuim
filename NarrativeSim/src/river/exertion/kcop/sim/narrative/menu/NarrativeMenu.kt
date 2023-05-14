package river.exertion.kcop.sim.narrative.menu

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.ui.Table
import ktx.actors.onChange
import ktx.collections.toGdxArray
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.profile.menu.RestartProgressMenu
import river.exertion.kcop.sim.narrative.NarrativePackage
import river.exertion.kcop.sim.narrative.NarrativePackage.NarrativeMenuDataBridge
import river.exertion.kcop.sim.narrative.asset.NarrativeAsset
import river.exertion.kcop.sim.narrative.messaging.NarrativeMenuDataMessage
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.ViewPackage
import river.exertion.kcop.view.ViewPackage.MenuNavBridge
import river.exertion.kcop.view.ViewPackage.MenuViewBridge
import river.exertion.kcop.view.menu.DisplayViewMenu
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.messaging.DisplayViewMessage
import river.exertion.kcop.view.messaging.MenuNavMessage
import river.exertion.kcop.view.messaging.MenuViewMessage
import river.exertion.kcop.view.messaging.menuParams.ActionParam
import river.exertion.kcop.view.messaging.menuParams.MenuNavParams

object NarrativeMenu : DisplayViewMenu {

    override val tag = "narrativeMenu"
    override val label = "Narrative"

    override val backgroundColor = ColorPalette.of("green")


    override fun menuPane() : Table {

        val listCtrl = com.badlogic.gdx.scenes.scene2d.ui.List<String>(KcopSkin.skin)
        val narrativeAssetsMap = NarrativePackage.immersionAssets.reload().associateBy { it.assetTitle() }

        if (narrativeAssetsMap.isNotEmpty()) {
            NarrativePackage.selectedImmersionAsset = narrativeAssetsMap.entries.first().value as NarrativeAsset

            listCtrl.onChange {
                NarrativePackage.selectedImmersionAsset = narrativeAssetsMap.values.toList()[this.selectedIndex] as NarrativeAsset
            }

            listCtrl.setItems(narrativeAssetsMap.keys.toGdxArray())
        } else {
            listCtrl.setItems(listOf("no narrative assets found").toGdxArray() )
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

    override val assignableNavs = mutableListOf(
            ActionParam("Load >", {
                DisplayViewMenuHandler.currentMenuTag = LoadNarrativeMenu.tag
                MessageChannelHandler.send(ViewPackage.DisplayViewBridge, DisplayViewMessage(DisplayViewMessage.DisplayViewMessageType.Rebuild) )
            }),
            ActionParam("Restart >", {
                DisplayViewMenuHandler.currentMenuTag = RestartProgressMenu.tag
                MessageChannelHandler.send(ViewPackage.DisplayViewBridge, DisplayViewMessage(DisplayViewMessage.DisplayViewMessageType.Rebuild) )
            })
    )

    override val actions = mutableListOf<ActionParam>()
}