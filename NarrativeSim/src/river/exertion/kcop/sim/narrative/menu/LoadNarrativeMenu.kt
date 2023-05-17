package river.exertion.kcop.sim.narrative.menu

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.sim.narrative.NarrativePackage
import river.exertion.kcop.sim.narrative.asset.NarrativeAsset
import river.exertion.kcop.sim.narrative.component.NarrativeComponent
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.layout.DisplayView
import river.exertion.kcop.view.layout.MenuView
import river.exertion.kcop.view.menu.DisplayViewMenu
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.menu.MenuActionParam

object LoadNarrativeMenu : DisplayViewMenu {

    override val tag = "loadNarrativeMenu"
    override val label = "Load"

    override val backgroundColor = ColorPalette.of("teal")

    override fun menuPane() = Table().apply {

        if (NarrativeAsset.selectedNarrativeAsset.assetInfo().isNotEmpty()) {
            NarrativeAsset.selectedNarrativeAsset.assetInfo().forEach { profileEntry ->
                this.add(Label(profileEntry, KcopSkin.skin)
                        //LabelStyle(bitmapFont, backgroundColor.label().color()))
                        .apply {
                    this.wrap = true
                }).growX().left()
                this.row()
            }
//        this.debug()
            this@LoadNarrativeMenu.actions.firstOrNull { it.label == "Yes" }?.apply { this.log = "Narrative Loaded : ${NarrativeAsset.selectedNarrativeAsset.assetName()}" }
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

    override val assignableNavs = mutableListOf<MenuActionParam>()

    override val actions = mutableListOf(
        MenuActionParam("Yes", {
            MenuView.closeMenu()
            NarrativeAsset.currentNarrativeAsset = NarrativeAsset.selectedNarrativeAsset
            NarrativeComponent.ecsInit()
            DisplayView.currentDisplayView = NarrativePackage.build()
        }, "Narrative Loaded!"),
        //go back a menu
        MenuActionParam("No", {
            DisplayViewMenuHandler.currentMenuTag = breadcrumbEntries.keys.toList()[0]
        })
    )

}