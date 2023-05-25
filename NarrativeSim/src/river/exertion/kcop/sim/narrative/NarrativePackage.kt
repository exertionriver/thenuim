package river.exertion.kcop.sim.narrative

import com.badlogic.gdx.scenes.scene2d.Actor
import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.AssetManagerHandler.lfhr
import river.exertion.kcop.ecs.system.SystemHandler
import river.exertion.kcop.messaging.Id
import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.plugin.IDisplayViewLayoutHandler
import river.exertion.kcop.plugin.IImmersionPackage
import river.exertion.kcop.profile.menu.SaveProgressMenu
import river.exertion.kcop.profile.menu.SaveProgressMenu.SaveLabel
import river.exertion.kcop.profile.settings.PSShowTimer
import river.exertion.kcop.profile.settings.ProfileSettingOption
import river.exertion.kcop.sim.narrative.asset.NarrativeAsset
import river.exertion.kcop.sim.narrative.asset.NarrativeAsset.Companion.isNarrativeLoaded
import river.exertion.kcop.sim.narrative.asset.NarrativeAssetLoader
import river.exertion.kcop.sim.narrative.asset.NarrativeStateAsset
import river.exertion.kcop.sim.narrative.asset.NarrativeStateAssetLoader
import river.exertion.kcop.sim.narrative.menu.LoadNarrativeMenu
import river.exertion.kcop.sim.narrative.menu.NarrativeMenu
import river.exertion.kcop.sim.narrative.menu.RestartProgressMenu
import river.exertion.kcop.sim.narrative.messaging.NarrativeComponentMessage
import river.exertion.kcop.sim.narrative.system.NarrativeTextSystem
import river.exertion.kcop.sim.narrative.view.DVLayout
import river.exertion.kcop.sim.narrative.view.DVLayoutHandler
import river.exertion.kcop.sim.narrative.view.asset.DisplayViewLayoutAsset
import river.exertion.kcop.sim.narrative.view.asset.DisplayViewLayoutAssetLoader
import river.exertion.kcop.sim.narrative.view.asset.DisplayViewLayoutAssets
import river.exertion.kcop.view.layout.AudioView
import river.exertion.kcop.view.layout.DisplayView
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.menu.MenuActionParam

object NarrativePackage : IImmersionPackage {

    override var id = Id.randomId()
    override var name = this::class.simpleName.toString()

    override fun loadChannels() {
        MessageChannelHandler.addChannel(MessageChannel(NarrativeBridge, NarrativeComponentMessage::class))
    }

    override fun loadAssets() {
        AssetManagerHandler.assets.setLoader(NarrativeAsset::class.java, NarrativeAssetLoader(lfhr))
        AssetManagerHandler.assets.setLoader(NarrativeStateAsset::class.java, NarrativeStateAssetLoader(lfhr))
        AssetManagerHandler.assets.setLoader(DisplayViewLayoutAsset::class.java, DisplayViewLayoutAssetLoader(lfhr))

        DisplayViewLayoutAssets.reload()
    }

    fun dvLayoutByTag(dvTag : String) = (DisplayViewLayoutAssets.byName(dvTag) as DisplayViewLayoutAsset).DVLayout ?: DVLayout.dvLayout()

    override fun loadSystems() {
        SystemHandler.pooledEngine.addSystem(NarrativeTextSystem())

        PSShowTimer.options.add(ProfileSettingOption("showImmersion","Immersion") {
            if (isNarrativeLoaded()) {
                MessageChannelHandler.send(NarrativeBridge, NarrativeComponentMessage(NarrativeComponentMessage.NarrativeMessageType.ReplaceCumlTimer))
            } else {
                PSShowTimer.options.firstOrNull { it.optionValue == PSShowTimer.ShowProfile }?.optionAction?.let { it() }
            }
        } )

        PSShowTimer.options.add(ProfileSettingOption("showImmersionBlock","Immersion Block") {
            if (isNarrativeLoaded()) {
                MessageChannelHandler.send(NarrativeBridge, NarrativeComponentMessage(NarrativeComponentMessage.NarrativeMessageType.ReplaceBlockCumlTimer))
            } else {
                PSShowTimer.options.firstOrNull { it.optionValue == PSShowTimer.ShowProfile }?.optionAction?.let { it() }
            }
        })
    }

    override fun loadMenus() {
        DisplayViewMenuHandler.addMenu(LoadNarrativeMenu)
        DisplayViewMenuHandler.addMenu(NarrativeMenu)
        DisplayViewMenuHandler.addMenu(RestartProgressMenu)

        MainMenu.assignableNavs.add(
            MenuActionParam("Narrative >", {
                DisplayViewMenuHandler.currentMenuTag = NarrativeMenu.tag
        }) )

        val saveProgressAction = SaveProgressMenu.actions.firstOrNull { it.label == SaveLabel }!!.action

        SaveProgressMenu.actions.firstOrNull { it.label == SaveLabel }!!.action = {
            saveProgressAction()
            NarrativeStateAsset.currentNarrativeStateAsset.save()
        }
    }

    override fun displayViewLayoutHandler() = DVLayoutHandler

    override fun inputProcessor() = NarrativeInputProcessor

    override fun dispose() {

    }

    const val NarrativeBridge = "NarrativeBridge"
    const val NoNarrativeLoaded = "No Narrative Loaded"
}