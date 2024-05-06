package river.exertion.thenuim.sim.narrative

import com.badlogic.gdx.scenes.scene2d.ui.Label
import river.exertion.thenuim.asset.AssetManagerHandler
import river.exertion.thenuim.asset.AssetManagerHandler.lfhr
import river.exertion.thenuim.asset.klop.IAssetKlop
import river.exertion.thenuim.asset.view.ColorPalette
import river.exertion.thenuim.base.Id
import river.exertion.thenuim.base.KcopBase
import river.exertion.thenuim.ecs.EngineHandler
import river.exertion.thenuim.ecs.klop.IECSKlop
import river.exertion.thenuim.messaging.MessageChannel
import river.exertion.thenuim.messaging.MessageChannelHandler
import river.exertion.thenuim.messaging.klop.IMessagingKlop
import river.exertion.thenuim.profile.ProfileKlop
import river.exertion.thenuim.profile.menu.SaveProgressMenu
import river.exertion.thenuim.profile.menu.SaveProgressMenu.SaveLabel
import river.exertion.thenuim.profile.settings.PSShowTimer
import river.exertion.thenuim.profile.settings.ProfileSettingOption
import river.exertion.thenuim.sim.narrative.asset.NarrativeAsset
import river.exertion.thenuim.sim.narrative.asset.NarrativeAsset.Companion.currentNarrativeAsset
import river.exertion.thenuim.sim.narrative.asset.NarrativeAsset.Companion.isNarrativeLoaded
import river.exertion.thenuim.sim.narrative.asset.NarrativeAssetLoader
import river.exertion.thenuim.sim.narrative.asset.NarrativeStateAsset
import river.exertion.thenuim.sim.narrative.asset.NarrativeStateAsset.Companion.currentNarrativeStateAsset
import river.exertion.thenuim.sim.narrative.asset.NarrativeStateAssetLoader
import river.exertion.thenuim.sim.narrative.menu.LoadNarrativeMenu
import river.exertion.thenuim.sim.narrative.menu.NarrativeMenu
import river.exertion.thenuim.sim.narrative.menu.RestartProgressMenu
import river.exertion.thenuim.sim.narrative.messaging.NarrativeComponentMessage
import river.exertion.thenuim.sim.narrative.system.NarrativeTextSystem
import river.exertion.thenuim.view.layout.displayViewLayout.DVLayoutHandler
import river.exertion.thenuim.view.layout.displayViewLayout.asset.DisplayViewLayoutAsset
import river.exertion.thenuim.view.layout.displayViewLayout.asset.DisplayViewLayoutAssetLoader
import river.exertion.thenuim.view.layout.displayViewLayout.asset.DisplayViewLayoutAssets
import river.exertion.thenuim.view.KcopSkin
import river.exertion.thenuim.view.KcopFont
import river.exertion.thenuim.view.klop.IDisplayViewKlop
import river.exertion.thenuim.view.klop.IMenuKlop
import river.exertion.thenuim.view.layout.ButtonView
import river.exertion.thenuim.view.layout.PauseView
import river.exertion.thenuim.view.menu.DisplayViewMenuHandler
import river.exertion.thenuim.view.menu.MainMenu
import river.exertion.thenuim.view.menu.MenuActionParam

object NarrativeKlop : IDisplayViewKlop, IMessagingKlop, IAssetKlop, IECSKlop, IMenuKlop {

    override val id = Id.randomId()
    override val tag = "narrative"
    override val name = KcopBase.appName
    override val version = KcopBase.appVersion

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
        EngineHandler.addSystem(NarrativeTextSystem())

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

    override fun unloadSystems() {
        EngineHandler.removeSystem<NarrativeTextSystem>()
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
        MainMenu.addAssignableNav(
            MenuActionParam("Narrative >", {
                DisplayViewMenuHandler.currentMenuTag = NarrativeMenu.tag
            }), 2 )

        MainMenu.addAssignableNav(
            MenuActionParam("Restart Narrative >", {
                DisplayViewMenuHandler.currentMenuTag = RestartProgressMenu.tag
            }), 3 )
    }

    private fun addNarrativeInfoToMainMenu() {
        val mainMenuPane = MainMenu.menuPane

        MainMenu.menuPane = {
            mainMenuPane().apply {
                this.row() // profile is above this
                this.add(Label("Narrative:", KcopSkin.labelStyle(KcopFont.MEDIUM, NarrativeMenuText))).left()
                this.row()

                if (isNarrativeLoaded() && currentNarrativeAsset.assetInfo().isNotEmpty()) {
                    currentNarrativeAsset.assetInfo().forEach { narrativeEntry ->
                        this.add(
                            Label(narrativeEntry, KcopSkin.labelStyle(KcopFont.SMALL, NarrativeMenuText))
                                .apply {
                                    this.wrap = true
                                }).growX().left()
                        this.row()
                    }
                } else {
                    this.add(
                        Label(NoNarrativeLoaded, KcopSkin.labelStyle(KcopFont.SMALL, NarrativeMenuText))
                    ).growX().left()
                }

                if (currentNarrativeStateAsset.persisted && currentNarrativeStateAsset.assetInfo().isNotEmpty()) {
                    this.row()
                    currentNarrativeStateAsset.assetInfo().forEach { narrativeEntry ->
                        this.add(
                            Label(narrativeEntry, KcopSkin.labelStyle(KcopFont.SMALL, NarrativeMenuText))
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
        val menuOpenButtonAction = ButtonView.openMenu
        val menuCloseButtonAction = ButtonView.closeMenu

        ButtonView.openMenu = {
            menuOpenButtonAction()
            MessageChannelHandler.send(NarrativeBridge, NarrativeComponentMessage(NarrativeComponentMessage.NarrativeMessageType.Pause))
        }

        ButtonView.closeMenu = {
            menuCloseButtonAction()
            MessageChannelHandler.send(NarrativeBridge, NarrativeComponentMessage(NarrativeComponentMessage.NarrativeMessageType.Unpause))
        }
    }

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

        ProfileKlop.dispose()
    }

    const val NarrativeBridge = "NarrativeBridge"
    const val NoNarrativeLoaded = "No Narrative Loaded"

    val NarrativeMenuBackgroundColor = ColorPalette.of("Color010")
    val NarrativeMenuText = ColorPalette.of("Color443")

    val NarrativeMainMenuText = ColorPalette.of("Color343")
}