package river.exertion.kcop.sim.narrative.menu

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.narrative.NarrativeKlop
import river.exertion.kcop.sim.narrative.NarrativeKlop.NarrativeMenuBackgroundColor
import river.exertion.kcop.sim.narrative.NarrativeKlop.NarrativeMenuText
import river.exertion.kcop.sim.narrative.NarrativeKlop.NoNarrativeLoaded
import river.exertion.kcop.sim.narrative.asset.NarrativeAsset
import river.exertion.kcop.sim.narrative.asset.NarrativeStateAsset
import river.exertion.kcop.sim.narrative.component.NarrativeComponent
import river.exertion.kcop.sim.narrative.messaging.NarrativeComponentMessage
import river.exertion.kcop.sim.narrative.structure.NarrativeState
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.KcopFont
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

            if ( NarrativeAsset.isNarrativeLoaded() ) {

                this.add(Label("Restarting:", KcopSkin.labelStyle(KcopFont.MEDIUM, NarrativeMenuText)))

                this.row()

                NarrativeAsset.currentNarrativeAsset.assetInfo().forEach { assetInfo ->
                    this.add(Label(assetInfo, KcopSkin.labelStyle(KcopFont.SMALL, NarrativeMenuText)).apply {
                        this.wrap = true
                    }).colspan(2).growX()
                    this.row()
                }

                this.add(Label("Are you sure?", KcopSkin.labelStyle(KcopFont.MEDIUM, NarrativeMenuText)))
                this@RestartProgressMenu.assignableActions.firstOrNull { it.label == "Restart" }?.enabled = true
            } else {
                this.add(Label(NoNarrativeLoaded, KcopSkin.labelStyle(KcopFont.MEDIUM, NarrativeMenuText)))
                this@RestartProgressMenu.assignableActions.firstOrNull { it.label == "Restart" }?.enabled = false
            }

            this.top()
        }
    }

    override val breadcrumbEntries = mapOf(
//        NarrativeMenu.tag to NarrativeMenu.label,
        MainMenu.tag to MainMenu.label
    )

    override val assignableNavs = mutableListOf<MenuActionParam>()

    override val assignableActions = mutableListOf(
        MenuActionParam("Restart", {
            MenuView.closeMenu()

            MessageChannelHandler.send(NarrativeKlop.NarrativeBridge, NarrativeComponentMessage(NarrativeComponentMessage.NarrativeMessageType.Inactivate))
            NarrativeStateAsset.currentNarrativeStateAsset.narrativeState = NarrativeState()
            NarrativeComponent.ecsInit()

        }, "Narrative Restarted!"),
        MenuActionParam("Cancel", {
            DisplayViewMenuHandler.currentMenuTag = breadcrumbEntries.keys.toList()[0]
        })
    )

}