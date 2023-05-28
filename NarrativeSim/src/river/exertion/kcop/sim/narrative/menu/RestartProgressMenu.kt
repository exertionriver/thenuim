package river.exertion.kcop.sim.narrative.menu

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.narrative.NarrativePackage
import river.exertion.kcop.sim.narrative.NarrativePackage.NarrativeMenuBackgroundColor
import river.exertion.kcop.sim.narrative.NarrativePackage.NarrativeMenuText
import river.exertion.kcop.sim.narrative.asset.NarrativeAsset
import river.exertion.kcop.sim.narrative.asset.NarrativeStateAsset
import river.exertion.kcop.sim.narrative.component.NarrativeComponent
import river.exertion.kcop.sim.narrative.messaging.NarrativeComponentMessage
import river.exertion.kcop.sim.narrative.structure.NarrativeState
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.asset.FontSize
import river.exertion.kcop.view.layout.MenuView
import river.exertion.kcop.view.menu.DisplayViewMenu
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.menu.MenuActionParam

object RestartProgressMenu : DisplayViewMenu {

    override val tag = "restartProgressMenu"
    override val label = "Restart Progress"

    override val backgroundColor = NarrativeMenuBackgroundColor

    override var menuPane = {
        Table().apply {

            NarrativeStateAsset.currentNarrativeStateAsset.assetInfo().forEach { assetInfo ->
                this.add(Label(assetInfo, KcopSkin.labelStyle(FontSize.SMALL, NarrativeMenuText)).apply {
                    this.wrap = true
                }).colspan(2).growX()
                this.row()
            }

            this.top()
        }
    }

    override val breadcrumbEntries = mapOf(
//        NarrativeMenu.tag to NarrativeMenu.label,
        MainMenu.tag to MainMenu.label
    )

    override val assignableNavs = mutableListOf<MenuActionParam>()

    override val actions = mutableListOf(
        MenuActionParam("Restart", {
            MenuView.closeMenu()

            MessageChannelHandler.send(NarrativePackage.NarrativeBridge, NarrativeComponentMessage(NarrativeComponentMessage.NarrativeMessageType.Inactivate))
            NarrativeStateAsset.currentNarrativeStateAsset.narrativeState = NarrativeState()
            NarrativeComponent.ecsInit()

        }, "Narrative Restarted!"),
        MenuActionParam("Cancel", {
            DisplayViewMenuHandler.currentMenuTag = breadcrumbEntries.keys.toList()[0]
        })
    )

}