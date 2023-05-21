package river.exertion.kcop.sim.narrative

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Actor
import ktx.assets.getAsset
import ktx.assets.load
import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.AssetManagerHandler.lfhr
import river.exertion.kcop.ecs.system.SystemHandler
import river.exertion.kcop.messaging.Id
import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.plugin.IImmersionPackage
import river.exertion.kcop.profile.Profile
import river.exertion.kcop.sim.narrative.asset.NarrativeAsset
import river.exertion.kcop.sim.narrative.asset.NarrativeAssetLoader
import river.exertion.kcop.sim.narrative.asset.NarrativeStateAsset
import river.exertion.kcop.sim.narrative.asset.NarrativeStateAssetLoader
import river.exertion.kcop.sim.narrative.menu.LoadNarrativeMenu
import river.exertion.kcop.sim.narrative.menu.NarrativeMenu
import river.exertion.kcop.sim.narrative.messaging.NarrativeComponentMessage
import river.exertion.kcop.sim.narrative.settings.PSShowTimer
import river.exertion.kcop.sim.narrative.system.NarrativeTextSystem
import river.exertion.kcop.sim.narrative.view.DVLayout
import river.exertion.kcop.sim.narrative.view.DVLayoutHandler
import river.exertion.kcop.sim.narrative.view.asset.DisplayViewLayoutAsset
import river.exertion.kcop.sim.narrative.view.asset.DisplayViewLayoutAssetLoader
import river.exertion.kcop.sim.narrative.view.asset.DisplayViewLayoutAssetStore
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

        DisplayViewLayoutAssetStore.values().forEach { AssetManagerHandler.assets.load(it) }

        AssetManagerHandler.assets.finishLoading()
    }

    fun dvLayoutByTag(dvTag : String) = (DisplayViewLayoutAssets.byName(dvTag) as DisplayViewLayoutAsset).DVLayout ?: DVLayout.dvLayout()

    override fun loadSystems() {
        SystemHandler.pooledEngine.addSystem(NarrativeTextSystem())

        Profile.availableSettings().add(PSShowTimer)
    }

    override fun loadMenus() {
        DisplayViewMenuHandler.addMenu(LoadNarrativeMenu)
        DisplayViewMenuHandler.addMenu(NarrativeMenu)

        MainMenu.assignableNavs.add(
            MenuActionParam("Narrative >", {
                DisplayViewMenuHandler.currentMenuTag = NarrativeMenu.tag
        }) )
    }

    override fun build() : Actor {
        val dv = DVLayoutHandler.build()
        DisplayView.currentDisplayView = dv
        DisplayView.build()
        return dv
    }

    override fun inputProcessor() = NarrativeInputProcessor

    fun clearContent() {
        AudioView.stopMusic()
        DVLayoutHandler.currentDvLayout.clearContent()
        build()
    }

    override fun dispose() {

    }

    const val NarrativeBridge = "NarrativeBridge"
}

fun AssetManager.load(asset: DisplayViewLayoutAssetStore) = load<DisplayViewLayoutAsset>(asset.path)
operator fun AssetManager.get(asset: DisplayViewLayoutAssetStore) = getAsset<DisplayViewLayoutAsset>(asset.path)

operator fun AssetManager.get(asset: DisplayViewLayoutAsset) = getAsset<DisplayViewLayoutAsset>(asset.assetPath).also {
    if (it.status != null) println ("Asset Status: ${it.status}")
    if (it.statusDetail != null) println ("Status Detail: ${it.statusDetail}")
}