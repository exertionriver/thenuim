package river.exertion.kcop.view

import com.badlogic.gdx.assets.AssetManager
import river.exertion.kcop.ecs.system.SystemHandler
import river.exertion.kcop.messaging.Id
import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.plugin.IPackage
import river.exertion.kcop.view.layout.AudioView
import river.exertion.kcop.view.layout.DisplayView
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.messaging.*
import river.exertion.kcop.view.system.TimeLogSystem

object ViewPackage : IPackage {

    override var id = Id.randomId()

    override var name = this::class.simpleName.toString()

    override fun loadChannels() {
        MessageChannelHandler.addChannel(MessageChannel(KcopBridge, KcopMessage::class))

        MessageChannelHandler.addChannel(MessageChannel(AiHintBridge, AiHintMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(AudioViewBridge, AudioViewMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(DisplayViewBridge, DisplayViewMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(DisplayViewTextBridge, DisplayViewTextMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(DisplayViewTextureBridge, DisplayViewTextureMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(InputViewBridge, InputViewMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(LogViewBridge, LogViewMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(MenuViewBridge, MenuViewMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(PauseViewBridge, PauseViewMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(StatusViewBridge, StatusViewMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(TextViewBridge, TextViewMessage::class))

        MessageChannelHandler.addChannel(MessageChannel(MenuNavBridge, MenuNavMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(ImmersionPauseBridge, String::class))
        MessageChannelHandler.addChannel(MessageChannel(ImmersionKeypressBridge, String::class))
    }

    override fun loadAssets(assetManager: AssetManager) { }

    override fun loadMenus() {
        DisplayViewMenuHandler.addMenu(MainMenu)
        DisplayViewMenuHandler.currentMenuTag = MainMenu.tag
    }

    override fun loadSystems() {
        SystemHandler.pooledEngine.addSystem(TimeLogSystem())
    }

    override fun dispose() {
        SdcHandler.dispose()
        KcopSkin.dispose()
    }

    const val KcopBridge = "KcopBridge"

    const val AiHintBridge = "AiHintBridge"
    const val AudioViewBridge = "AudioViewBridge"
    const val DisplayViewBridge = "DisplayViewBridge"
    const val DisplayViewTextBridge = "DisplayViewTextBridge"
    const val DisplayViewTextureBridge = "DisplayViewTextureBridge"
    const val InputViewBridge = "InputViewBridge"
    const val LogViewBridge = "LogViewBridge"
    const val MenuViewBridge = "MenuViewBridge"
    const val PauseViewBridge = "PauseViewBridge"
    const val StatusViewBridge = "StatusViewBridge"
    const val TextViewBridge = "TextViewBridge"

    const val MenuNavBridge = "MenuNavBridge"
    const val ImmersionPauseBridge = "ImmersionPauseBridge"
    const val ImmersionKeypressBridge = "ImmersionKeypressBridge"
}