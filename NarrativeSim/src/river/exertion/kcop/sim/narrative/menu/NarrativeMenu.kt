package river.exertion.kcop.sim.narrative.menu

import com.badlogic.gdx.scenes.scene2d.ui.Table
import ktx.actors.onChange
import ktx.collections.toGdxArray
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.profile.menu.RestartProgressMenu
import river.exertion.kcop.sim.narrative.asset.NarrativeAsset
import river.exertion.kcop.sim.narrative.asset.NarrativeAssets
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.menu.DisplayViewMenu
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.menu.MenuActionParam

object NarrativeMenu : DisplayViewMenu {

    override val tag = "narrativeMenu"
    override val label = "Narrative"

    override val backgroundColor = ColorPalette.of("green")


    override fun menuPane() : Table {

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
            MenuActionParam("Load >", {
                DisplayViewMenuHandler.currentMenuTag = LoadNarrativeMenu.tag
            }),
            MenuActionParam("Restart >", {
                DisplayViewMenuHandler.currentMenuTag = RestartProgressMenu.tag
            })
    )

    override val actions = mutableListOf<MenuActionParam>()
}