package river.exertion.kcop.sim.narrative

import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.scenes.scene2d.ui.Label
import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.AssetManagerHandler.lfhr
import river.exertion.kcop.base.Id
import river.exertion.kcop.asset.klop.IAssetKlop
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.ecs.klop.IECSKlop
import river.exertion.kcop.ecs.system.SystemHandler
import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.messaging.klop.IMessagingKlop
import river.exertion.kcop.profile.menu.SaveProgressMenu
import river.exertion.kcop.profile.menu.SaveProgressMenu.SaveLabel
import river.exertion.kcop.profile.settings.PSShowTimer
import river.exertion.kcop.profile.settings.ProfileSettingOption
import river.exertion.kcop.sim.narrative.asset.NarrativeAsset
import river.exertion.kcop.sim.narrative.asset.NarrativeAsset.Companion.currentNarrativeAsset
import river.exertion.kcop.sim.narrative.asset.NarrativeAsset.Companion.isNarrativeLoaded
import river.exertion.kcop.sim.narrative.asset.NarrativeAssetLoader
import river.exertion.kcop.sim.narrative.asset.NarrativeStateAsset
import river.exertion.kcop.sim.narrative.asset.NarrativeStateAsset.Companion.currentNarrativeStateAsset
import river.exertion.kcop.sim.narrative.asset.NarrativeStateAssetLoader
import river.exertion.kcop.sim.narrative.menu.LoadNarrativeMenu
import river.exertion.kcop.sim.narrative.menu.NarrativeMenu
import river.exertion.kcop.sim.narrative.menu.RestartProgressMenu
import river.exertion.kcop.sim.narrative.messaging.NarrativeComponentMessage
import river.exertion.kcop.sim.narrative.system.NarrativeTextSystem
import river.exertion.kcop.sim.narrative.view.DVLayoutHandler
import river.exertion.kcop.sim.narrative.view.asset.DisplayViewLayoutAsset
import river.exertion.kcop.sim.narrative.view.asset.DisplayViewLayoutAssetLoader
import river.exertion.kcop.sim.narrative.view.asset.DisplayViewLayoutAssets
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.asset.FontSize
import river.exertion.kcop.view.klop.IDisplayViewKlop
import river.exertion.kcop.view.klop.IMenuKlop
import river.exertion.kcop.view.layout.MenuView
import river.exertion.kcop.view.layout.PauseView
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.menu.MenuActionParam

object NarrativeKlop : IDisplayViewKlop, IMessagingKlop, IAssetKlop, IECSKlop, IMenuKlop {

    override var id = Id.randomId()
    override var name = this::class.simpleName.toString()

    override fun load() {
        loadChannels()
        loadAssets()
        loadSystems()
        loadMenus()
    }

    override fun unload() { }

    override fun loadChannels() {
        MessageChannelHandler.addChannel(MessageChannel(NarrativeBridge, NarrativeComponentMessage::class))
    }

    override fun loadAssets() {
        AssetManagerHandler.assets.setLoader(NarrativeAsset::class.java, NarrativeAssetLoader(lfhr))
        AssetManagerHandler.assets.setLoader(NarrativeStateAsset::class.java, NarrativeStateAssetLoader(lfhr))
        AssetManagerHandler.assets.setLoader(DisplayViewLayoutAsset::class.java, DisplayViewLayoutAssetLoader(lfhr))

        DisplayViewLayoutAssets.reload()
    }

    fun dvLayoutByTag(dvTag : String) = (DisplayViewLayoutAssets.byName(dvTag) as DisplayViewLayoutAsset).dvLayout

    override fun loadSystems() {
        SystemHandler.pooledEngine.addSystem(NarrativeTextSystem())

        val profileAction = PSShowTimer.options[0].optionAction

        PSShowTimer.options[0].optionAction = {
            profileAction()
            MessageChannelHandler.send(NarrativeBridge, NarrativeComponentMessage(NarrativeComponentMessage.NarrativeMessageType.RemoveBlockCumlTimer))
        }

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

        PauseView.toggleImmersionPause = {
            val messageType = if (PauseView.isChecked) NarrativeComponentMessage.NarrativeMessageType.Pause else NarrativeComponentMessage.NarrativeMessageType.Unpause
            MessageChannelHandler.send(NarrativeBridge, NarrativeComponentMessage(messageType) )
        }

    }

    override fun loadMenus() {
        DisplayViewMenuHandler.addMenu(LoadNarrativeMenu)
        DisplayViewMenuHandler.addMenu(NarrativeMenu)
        DisplayViewMenuHandler.addMenu(RestartProgressMenu)

        addNarrativeNavsToMainMenu()
        addNarrativeInfoToMainMenu()
        addNarrativeStateSaveToSaveProgress()
        addNarrativePauseToMenuAction()
    }

    private fun addNarrativeNavsToMainMenu() {
        MainMenu.assignableNavs.add(
            MenuActionParam("Narrative >", {
                DisplayViewMenuHandler.currentMenuTag = NarrativeMenu.tag
            }) )
        MainMenu.assignableNavs.add(
            MenuActionParam("Restart Narrative >", {
                DisplayViewMenuHandler.currentMenuTag = RestartProgressMenu.tag
            }) )
    }

    private fun addNarrativeInfoToMainMenu() {
        val mainMenuPane = MainMenu.menuPane

        MainMenu.menuPane = {
            mainMenuPane().apply {
                this.row() // profile is above this
                this.add(Label("Narrative:", KcopSkin.labelStyle(FontSize.MEDIUM, NarrativeMenuText))).left()
                this.row()

                if (isNarrativeLoaded() && currentNarrativeAsset.assetInfo().isNotEmpty()) {
                    currentNarrativeAsset.assetInfo().forEach { narrativeEntry ->
                        this.add(
                            Label(narrativeEntry, KcopSkin.labelStyle(FontSize.SMALL, NarrativeMenuText))
                                .apply {
                                    this.wrap = true
                                }).growX().left()
                        this.row()
                    }
                } else {
                    this.add(
                        Label(NoNarrativeLoaded, KcopSkin.labelStyle(FontSize.SMALL, NarrativeMenuText))
                    ).growX().left()
                }

                if (currentNarrativeStateAsset.persisted && currentNarrativeStateAsset.assetInfo().isNotEmpty()) {
                    this.row()
                    currentNarrativeStateAsset.assetInfo().forEach { narrativeEntry ->
                        this.add(
                            Label(narrativeEntry, KcopSkin.labelStyle(FontSize.SMALL, NarrativeMenuText))
                                .apply {
                                    this.wrap = true
                                }).growX().left()
                        this.row()
                    }
                }

                this.top()
            }
        }
    }

    private fun addNarrativeStateSaveToSaveProgress() {
        val saveProgressAction = SaveProgressMenu.assignableActions.firstOrNull { it.label == SaveLabel }!!.action

        SaveProgressMenu.assignableActions.firstOrNull { it.label == SaveLabel }!!.action = {
            saveProgressAction()
            currentNarrativeStateAsset.save()
        }
    }

    private fun addNarrativePauseToMenuAction() {
        val menuOpenButtonAction = MenuView.openMenu
        val menuCloseButtonAction = MenuView.closeMenu

        MenuView.openMenu = {
            menuOpenButtonAction()
            MessageChannelHandler.send(NarrativeBridge, NarrativeComponentMessage(NarrativeComponentMessage.NarrativeMessageType.Pause))
        }

        MenuView.closeMenu = {
            menuCloseButtonAction()
            MessageChannelHandler.send(NarrativeBridge, NarrativeComponentMessage(NarrativeComponentMessage.NarrativeMessageType.Unpause))
        }
    }

    override var inputMultiplexer: InputMultiplexer? = null

    override fun displayViewLayoutHandler() = DVLayoutHandler

    override fun inputProcessor() = NarrativeInputProcessor

    override fun showView() {
        MessageChannelHandler.send(NarrativeBridge, NarrativeComponentMessage(NarrativeComponentMessage.NarrativeMessageType.Unpause))
        MessageChannelHandler.send(NarrativeBridge, NarrativeComponentMessage(NarrativeComponentMessage.NarrativeMessageType.Refresh))
        super.showView()
    }

    override fun hideView() {
        MessageChannelHandler.send(NarrativeBridge, NarrativeComponentMessage(NarrativeComponentMessage.NarrativeMessageType.Pause))
        super.hideView()
    }

    override fun dispose() {

    }

    const val NarrativeBridge = "NarrativeBridge"
    const val NoNarrativeLoaded = "No Narrative Loaded"

    val NarrativeMenuBackgroundColor = ColorPalette.of("Color010")
    val NarrativeMenuText = ColorPalette.of("Color443")

    val NarrativeMainMenuText = ColorPalette.of("Color343")
}