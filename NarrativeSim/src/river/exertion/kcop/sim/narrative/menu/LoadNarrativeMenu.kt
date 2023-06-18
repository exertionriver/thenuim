package river.exertion.kcop.sim.narrative.menu

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.profile.asset.ProfileAsset
import river.exertion.kcop.sim.narrative.NarrativeKlop.NarrativeBridge
import river.exertion.kcop.sim.narrative.NarrativeKlop.NarrativeMenuBackgroundColor
import river.exertion.kcop.sim.narrative.NarrativeKlop.NarrativeMenuText
import river.exertion.kcop.sim.narrative.asset.NarrativeAsset
import river.exertion.kcop.sim.narrative.asset.NarrativeStateAsset
import river.exertion.kcop.sim.narrative.asset.NarrativeStateAssets
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

object LoadNarrativeMenu : DisplayViewMenu {

    override val tag = "loadNarrativeMenu"
    override val label = "Load"

    override val backgroundColor = NarrativeMenuBackgroundColor

    override var menuPane = {
        Table().apply {

        if (NarrativeAsset.selectedNarrativeAsset.assetInfo().isNotEmpty()) {
            NarrativeAsset.selectedNarrativeAsset.assetInfo().forEach { profileEntry ->
                this.add(Label(profileEntry, KcopSkin.labelStyle(FontSize.SMALL, NarrativeMenuText))
                        .apply {
                    this.wrap = true
                }).growX().left()
                this.row()
            }
//        this.debug()
            this@LoadNarrativeMenu.assignableActions.firstOrNull { it.label == "Yes" }?.apply { this.log = "Narrative Loaded : ${NarrativeAsset.selectedNarrativeAsset.assetName()}" }
        } else {
            this.add(Label("no narrative info found", KcopSkin.skin)
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
            MenuView.closeMenu()
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