package river.exertion.thenuim.sim.narrative.menu

import com.badlogic.gdx.scenes.scene2d.ui.Table
import ktx.actors.onChange
import ktx.collections.toGdxArray
import river.exertion.thenuim.sim.narrative.NarrativeKlop.NarrativeMenuBackgroundColor
import river.exertion.thenuim.sim.narrative.asset.NarrativeAsset
import river.exertion.thenuim.sim.narrative.asset.NarrativeAssets
import river.exertion.thenuim.view.KcopSkin
import river.exertion.thenuim.view.menu.DisplayViewMenu
import river.exertion.thenuim.view.menu.DisplayViewMenuHandler
import river.exertion.thenuim.view.menu.MainMenu
import river.exertion.thenuim.view.menu.MenuActionParam

object NarrativeMenu : DisplayViewMenu {

    override val tag = "narrativeMenu"
    override val label = "Narrative"

    override val backgroundColor = NarrativeMenuBackgroundColor

    override var menuPane = {

        val listCtrl = com.badlogic.gdx.scenes.scene2d.ui.List<String>(KcopSkin.skin)
        val narrativeAssetsMap = NarrativeAssets.reload().associateBy { it.assetTitle() }

        if (narrativeAssetsMap.isNotEmpty()) {
            NarrativeAsset.selectedNarrativeAsset = narrativeAssetsMap.entries.first().value

            listCtrl.onChange {
                NarrativeAsset.selectedNarrativeAsset = narrativeAssetsMap.values.toList()[this.selectedIndex]
            }

            listCtrl.setItems(narrativeAssetsMap.keys.toGdxArray())
        } else {
            listCtrl.setItems(listOf("no narrative assets found").toGdxArray() )
        }

        Table().apply {

            this.add(listCtrl).growY().top().left()
            this.top()
            this.left()
        }
    }

    override val breadcrumbEntries = mapOf(
        MainMenu.tag to MainMenu.label
    )

    override val assignableNavs = mutableListOf(
            MenuActionParam("Load >", {
                DisplayViewMenuHandler.currentMenuTag = LoadNarrativeMenu.tag
            })
    )

    override val assignableActions = mutableListOf<MenuActionParam>()
}