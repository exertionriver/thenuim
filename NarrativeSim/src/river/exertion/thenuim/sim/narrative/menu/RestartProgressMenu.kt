package river.exertion.thenuim.sim.narrative.menu

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.thenuim.messaging.MessageChannelHandler
import river.exertion.thenuim.sim.narrative.NarrativeLoPa
import river.exertion.thenuim.sim.narrative.NarrativeLoPa.NarrativeMenuBackgroundColor
import river.exertion.thenuim.sim.narrative.NarrativeLoPa.NarrativeMenuText
import river.exertion.thenuim.sim.narrative.NarrativeLoPa.NoNarrativeLoaded
import river.exertion.thenuim.sim.narrative.asset.NarrativeAsset
import river.exertion.thenuim.sim.narrative.asset.NarrativeStateAsset
import river.exertion.thenuim.sim.narrative.component.NarrativeComponent
import river.exertion.thenuim.sim.narrative.messaging.NarrativeComponentMessage
import river.exertion.thenuim.sim.narrative.structure.NarrativeState
import river.exertion.thenuim.view.TnmSkin
import river.exertion.thenuim.view.TnmFont
import river.exertion.thenuim.view.layout.ButtonView
import river.exertion.thenuim.view.menu.DisplayViewMenu
import river.exertion.thenuim.view.menu.DisplayViewMenuHandler
import river.exertion.thenuim.view.menu.MainMenu
import river.exertion.thenuim.view.menu.MenuActionParam

object RestartProgressMenu : DisplayViewMenu {

    override val tag = "restartProgressMenu"
    override val label = "Restart Progress"

    override val backgroundColor = NarrativeMenuBackgroundColor

    override var menuPane = {
        Table().apply {

            if ( NarrativeAsset.isNarrativeLoaded() ) {

                this.add(Label("Restarting:", TnmSkin.labelStyle(TnmFont.MEDIUM, NarrativeMenuText)))

                this.row()

                NarrativeAsset.currentNarrativeAsset.assetInfo().forEach { assetInfo ->
                    this.add(Label(assetInfo, TnmSkin.labelStyle(TnmFont.SMALL, NarrativeMenuText)).apply {
                        this.wrap = true
                    }).colspan(2).growX()
                    this.row()
                }

                this.add(Label("Are you sure?", TnmSkin.labelStyle(TnmFont.MEDIUM, NarrativeMenuText)))
                this@RestartProgressMenu.assignableActions.firstOrNull { it.label == "Restart" }?.enabled = true
            } else {
                this.add(Label(NoNarrativeLoaded, TnmSkin.labelStyle(TnmFont.SMALL, NarrativeMenuText)))
                this@RestartProgressMenu.assignableActions.firstOrNull { it.label == "Restart" }?.enabled = false
            }

            this.top().left()
        }
    }

    override val breadcrumbEntries = mapOf(
//        NarrativeMenu.tag to NarrativeMenu.label,
        MainMenu.tag to MainMenu.label
    )

    override val assignableNavs = mutableListOf<MenuActionParam>()

    override val assignableActions = mutableListOf(
        MenuActionParam("Restart", {
            ButtonView.closeMenu()

            MessageChannelHandler.send(NarrativeLoPa.NarrativeBridge, NarrativeComponentMessage(NarrativeComponentMessage.NarrativeMessageType.Inactivate))
            NarrativeStateAsset.currentNarrativeStateAsset.narrativeState = NarrativeState()
            NarrativeComponent.ecsInit()

        }, "Narrative Restarted!"),
        MenuActionParam("Cancel", {
            DisplayViewMenuHandler.currentMenuTag = breadcrumbEntries.keys.toList()[0]
        })
    )

}