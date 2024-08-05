package river.exertion.thenuim.sim.narrative.menu

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.thenuim.messaging.MessageChannelHandler
import river.exertion.thenuim.profile.asset.ProfileAsset
import river.exertion.thenuim.sim.narrative.NarrativeLoPa.NarrativeBridge
import river.exertion.thenuim.sim.narrative.NarrativeLoPa.NarrativeMenuBackgroundColor
import river.exertion.thenuim.sim.narrative.NarrativeLoPa.NarrativeMenuText
import river.exertion.thenuim.sim.narrative.asset.NarrativeAsset
import river.exertion.thenuim.sim.narrative.asset.NarrativeStateAsset
import river.exertion.thenuim.sim.narrative.asset.NarrativeStateAssets
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

object LoadNarrativeMenu : DisplayViewMenu {

    override val tag = "loadNarrativeMenu"
    override val label = "Load"

    override val backgroundColor = NarrativeMenuBackgroundColor

    override var menuPane = {
        Table().apply {

        if (NarrativeAsset.selectedNarrativeAsset.assetInfo().isNotEmpty()) {
            NarrativeAsset.selectedNarrativeAsset.assetInfo().forEach { profileEntry ->
                this.add(Label(profileEntry, TnmSkin.labelStyle(TnmFont.SMALL, NarrativeMenuText))
                        .apply {
                    this.wrap = true
                }).growX().left()
                this.row()
            }
            this@LoadNarrativeMenu.assignableActions.firstOrNull { it.label == "Yes" }?.apply { this.log = "Narrative Loaded : ${NarrativeAsset.selectedNarrativeAsset.assetName()}" }
        } else {
            this.add(Label("no narrative info found", TnmSkin.skin)
                    //LabelStyle(bitmapFont, backgroundColor.label().color()))
            ).growX().left()
            this@LoadNarrativeMenu.assignableActions.firstOrNull { it.label == "Yes" }?.apply { this.label = "Error"; this.action = {} }
        }
        this.top()
    } }

    override val breadcrumbEntries = mapOf(
        NarrativeMenu.tag to NarrativeMenu.label,
        MainMenu.tag to MainMenu.label
    )

    override val assignableNavs = mutableListOf<MenuActionParam>()

    override val assignableActions = mutableListOf(
        MenuActionParam("Yes", {
            ButtonView.closeMenu()
            MessageChannelHandler.send(NarrativeBridge, NarrativeComponentMessage(NarrativeComponentMessage.NarrativeMessageType.Inactivate))
            NarrativeAsset.currentNarrativeAsset = NarrativeAsset.selectedNarrativeAsset
            NarrativeStateAsset.currentNarrativeStateAsset = NarrativeStateAssets.reload().firstOrNull { it.assetId() == NarrativeState.genId(ProfileAsset.currentProfileAsset.assetId(), NarrativeAsset.currentNarrativeAsset.assetId() ) } ?: NarrativeStateAsset()

            NarrativeComponent.ecsInit()
        }, "Narrative Loaded!"),
        //go back a menu
        MenuActionParam("No", {
            DisplayViewMenuHandler.currentMenuTag = breadcrumbEntries.keys.toList()[0]
        })
    )

}